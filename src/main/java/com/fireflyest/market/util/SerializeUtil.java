package com.fireflyest.market.util;

import com.cryptomorin.xseries.XEnchantment;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class SerializeUtil {

    private SerializeUtil(){
    }

    private static Method deserialize = null;
    private static final Map<String, ItemMeta> metaStorage = new HashMap<>();
    private static final Map<String, ItemStack> stackStorage = new HashMap<>();

    /**
     * 析构堆
     * @param itemStack 物品
     * @return 堆析构后的文本
     */
    public static String serializeItemStack(ItemStack itemStack) {
        Gson stack = new Gson();
        ItemStack item = itemStack.clone();
        item.setItemMeta(null);
        return stack.toJson(item.serialize());
    }

    /**
     * 析构元
     * @param itemStack 物品
     * @return 元析构后的文本
     */
    public static String serializeItemMeta(ItemStack itemStack){
        Gson meta = new Gson();
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta == null ? "" : meta.toJson(itemMeta.serialize());
    }

    /**
     * 解析物品
     * @param itemStack 堆
     * @param itemMeta 元
     * @return 物品
     */
    public static ItemStack deserialize(String itemStack, String itemMeta) {
        Gson gson = new Gson();
        Map<String, Object> itemMap;
        Map<String, Object> metaMap;
        Type type = new TypeToken<Map<String, Object>>() {}.getType();

        ItemStack item;
        itemMap = gson.fromJson(itemStack, type);

        // 先从缓存查是否解析过
        if (stackStorage.containsKey(itemStack)){
            item = stackStorage.get(itemStack).clone();
            item.setItemMeta(null);
        }else {
            item = ItemStack.deserialize(itemMap);

            // 缓存
            stackStorage.put(itemStack, item);
        }

        // 判断是否有解析过这个元
        if(!"".equals(itemMeta)){
            ItemMeta meta;
            if (metaStorage.containsKey(itemMeta) && metaStorage.get(itemMeta) != null){
                meta = metaStorage.get(itemMeta).clone();
            }else {
                metaMap = gson.fromJson(itemMeta, type);
                meta = deserializeItemMeta(metaMap);

                // 缓存
                metaStorage.put(itemMeta, meta);
            }
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * 解析元
     * @param map 键值
     * @return 元
     */
    public static ItemMeta deserializeItemMeta(Map<String, Object> map) {
        if (deserialize == null) {
            String version = Bukkit.getVersion();
            String v = version.substring(version.indexOf(".")+1, version.lastIndexOf("."));
            Class<?> clazz = null;
            int r = 1;
            while (clazz == null && r < 9){
                try {
                    clazz = Class.forName(
                            String.format("org.bukkit.craftbukkit.v1_%s_R%d.inventory.CraftMetaItem$SerializableMeta", v, r));
                } catch (ClassNotFoundException ignore) {}
                r ++;
            }
            if (clazz == null){
                Bukkit.getLogger().warning("inventory.CraftMetaItem$SerializableMeta not found!");
                return null;
            }
            try {
                deserialize = clazz.getMethod("deserialize", Map.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        ItemMeta meta;
        String metaType = String.valueOf(map.get("meta-type"));

        // 耐久装备
        double damage = 0;
        if (map.containsKey("Damage")){
            damage = (double)map.get("Damage");
            map.remove("Damage");
        }

        // 有附魔
        String enchants = null;
        if (map.containsKey("enchants")){
            enchants = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
            map.remove("enchants");
        }
        
        double repairCost = 0.0;
        if (map.containsKey("repair-cost")){
            repairCost = (Double) map.get("repair-cost");
            map.remove("repair-cost"); // 经验修补附带
        }

        // 皮革甲
        boolean isLeather = false;
        double red = 0, green = 0, blue = 0;
        if ("LEATHER_ARMOR".equals(metaType) && map.containsKey("color")){
            isLeather = true;
            LinkedTreeMap<? ,?> treeMap = ((LinkedTreeMap<? ,?>) map.get("color"));
            red = (Double)(treeMap.get("red"));
            green = (Double)(treeMap.get("green"));
            blue = (Double)(treeMap.get("blue"));
            if (red < 0) red += 256;
            if (green < 0) green += 256;
            if (blue < 0) blue += 256;
            map.remove("color");
        }

        // 如果是附魔书
        String storageEnchants = null;
        if ("ENCHANTED".equals(metaType)){
            storageEnchants =
                    map.get("stored-enchants")
                            .toString()
                            .replace("{", "")
                            .replace("}", "")
                            .replace("\"", "");
            map.remove("enchants");
        }

        // 烟花核
        List<LinkedTreeMap<? ,?>> effectTreeMapList = new ArrayList<>();
        boolean fireworkEffect = false;
        if ("FIREWORK_EFFECT".equals(metaType)){
            fireworkEffect = true;
            effectTreeMapList.add((LinkedTreeMap<? ,?>) map.get("firework-effect"));
            map.remove("type");
            map.remove("firework-effect");
        }

        boolean hasPatterns = false;
        List<Pattern> patternList = new ArrayList<>();
        if ("BANNER".equals(metaType) && map.containsKey("patterns")){
            hasPatterns = true;

            List<?>patternMapList = (List<?>) map.get("patterns");
            for (Object o : patternMapList) {
                LinkedTreeMap<?,?> treeMap = (LinkedTreeMap<?, ?>)o;
                String color = treeMap.get("color").toString();
                String pattern = treeMap.get("pattern").toString();
                Pattern p = new Pattern(DyeColor.valueOf(color), PatternType.valueOf(pattern));
                patternList.add(p);
            }
            map.remove("patterns");
        }

        // 烟花
        boolean firework = false;
        double power = 1;
        if ("FIREWORK".equals(metaType)){
            firework = true;
            ArrayList<?> effectList = ((ArrayList<?>) map.get("firework-effects"));
            for (Object o : effectList) {
                effectTreeMapList.add((LinkedTreeMap<? ,?>)o);
            }
            power = (Double) map.get("power");
            map.remove("power");
            map.remove("firework-effects");
        }

        double mapId = -1;
        if ("MAP".equals(metaType)){
            mapId = (Double) map.get("map-id");
            map.remove("map-id");
        }

        meta = invokeMeta(map);
        if (meta == null) {
            return null;
        }
        if (isLeather) ((LeatherArmorMeta)meta).setColor(Color.fromRGB((int)red, (int)green, (int)blue));
        if (damage != 0) ((Damageable)meta).setDamage((int)damage);
        if (enchants != null) addEnchantments(meta, enchants.split(","));
        if (storageEnchants != null) addStorageEnchantments(meta, storageEnchants.split(","));
        if (fireworkEffect) {
            addFireworkEffects(meta, effectTreeMapList, false);
        }
        if (firework){
            addFireworkEffects(meta, effectTreeMapList, true);
            ((FireworkMeta)meta).setPower((int)power);
        }
        if (mapId != -1) //noinspection deprecation
            ((MapMeta)meta).setMapId((int)mapId);
        if (repairCost != 0){
            ((Repairable)meta).setRepairCost(((int) repairCost));
        }
        if (hasPatterns) ((BannerMeta)meta).setPatterns(patternList);

        return meta;
    }

    /**
     * 利用反射执行解析
     * @param map 键值
     * @return 元
     */
    private static ItemMeta invokeMeta(Map<String, Object> map){
        // 开始解析
        try{
            return  (ItemMeta) deserialize.invoke(null, map);
        }catch (InvocationTargetException | IllegalAccessException e){
            Bukkit.getLogger().severe(String.format("解析物品数据时出错：%s", map.toString()));
            e.printStackTrace();
        }
        return null;
    }

    private static void addFireworkEffects(ItemMeta meta, List<LinkedTreeMap<? ,?>> effectTreeMapList, boolean isFirework){
        List<FireworkEffect> effects = new ArrayList<>();
        for (LinkedTreeMap<?, ?> linkedTreeMap : effectTreeMapList) {
            boolean fireworkFlicker, fireworkTrail;
            double fadeRed = 0, fadeGreen = 0, fadeBlue = 0; // 淡化颜色
            double red = 0, green = 0, blue = 0;

            fireworkFlicker = (Boolean)(linkedTreeMap.get("flicker"));
            fireworkTrail = (Boolean)(linkedTreeMap.get("trail"));
            ArrayList<?> colorList = (ArrayList<?>)linkedTreeMap.get("colors");
            ArrayList<?> fadeColorList = (ArrayList<?>)linkedTreeMap.get("colors");
            LinkedTreeMap<?,?> colorsMap = null, fadeColorsMap = null;
            if (!colorList.isEmpty()){
                colorsMap = (LinkedTreeMap<?,?>) (colorList.get(0));
            }
            if (!fadeColorList.isEmpty()){
                fadeColorsMap = ((LinkedTreeMap<?,?>) (fadeColorList).get(0));
            }
            if (colorsMap != null) {
                red = (Double)(colorsMap.get("red"));
                green = (Double)(colorsMap.get("green"));
                blue = (Double)(colorsMap.get("blue"));
                if (red < 0) red += 256;
                if (green < 0) green += 256;
                if (blue < 0) blue += 256;
            }
            if (fadeColorsMap != null) {
                fadeRed = (Double)(fadeColorsMap.get("red"));
                fadeGreen = (Double)(fadeColorsMap.get("green"));
                fadeBlue = (Double)(fadeColorsMap.get("blue"));
                if (fadeRed < 0) fadeRed += 256;
                if (fadeGreen < 0) fadeGreen += 256;
                if (fadeBlue < 0) fadeBlue += 256;
            }

            FireworkEffect fe = FireworkEffect.builder()
                    .flicker(fireworkFlicker)
                    .trail(fireworkTrail)
                    .withColor(Color.fromRGB((int)red, (int)green, (int)blue))
                    .withFade(Color.fromRGB((int)fadeRed, (int)fadeGreen, (int)fadeBlue))
                    .build();
            effects.add(fe);
        }
        if (isFirework){
            ((FireworkMeta)meta).addEffects(effects);
        }else if (effects.size() != 0){
            ((FireworkEffectMeta)meta).setEffect(effects.get(0));
        }
    }

    private static void addEnchantments(ItemMeta meta, String[] enchantments){
        for (String s : enchantments) {
            String[] split = s.split("=");
            if (split.length > 0) {
                Optional<XEnchantment> xEnchantment = XEnchantment.matchXEnchantment(split[0]);
                Enchantment enchantment = null;
                if (xEnchantment.isPresent()){
                    enchantment = xEnchantment.get().parseEnchantment();
                }
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) meta.addEnchant(enchantment, level, true);
            }
        }
    }

    private static void addStorageEnchantments(ItemMeta meta, String[] enchantments){
        for (String s : enchantments) {
            String[] split = s.split("=");
            if (split.length > 0) {
                Optional<XEnchantment> xEnchantment = XEnchantment.matchXEnchantment(split[0]);
                Enchantment enchantment = null;
                if (xEnchantment.isPresent()){
                    enchantment = xEnchantment.get().parseEnchantment();
                }
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) ((EnchantmentStorageMeta)meta).addStoredEnchant(enchantment, level, true);
            }
        }
    }

}

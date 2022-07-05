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

    private static Method deserialize;
    private static final Map<String, ItemMeta> metaStorage = new HashMap<>();
    private static final Map<String, ItemStack> stackStorage = new HashMap<>();

    static {
        String version = Bukkit.getVersion();
        String v = version.substring(version.indexOf(".")+1, version.lastIndexOf("."));
        Class<?> clazz = null;
        int r = 1;
        while (clazz == null && r < 9){
            try {
                String versionPacket = String.format("v1_%s_R%d", v, r);
                clazz = Class.forName(
                        String.format("org.bukkit.craftbukkit.%s.inventory.CraftMetaItem$SerializableMeta", versionPacket));
            } catch (ClassNotFoundException ignore) {}
            r ++;
        }
        if (clazz != null) {
            try {
                deserialize = clazz.getMethod("deserialize", Map.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

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
        if (deserialize == null) return null;

        ItemMeta meta;
        String metaType = String.valueOf(map.get("meta-type"));

        // 耐久装备
        double damage = 0.0, repairCost = 0.0;
        // 附魔装备 附魔书
        String enchants = null;
        // 皮革甲颜色
        boolean isLeather = false;
        double red = 0, green = 0, blue = 0;
        // 附魔书
        String storageEnchants = null;
        // 烟花核
        boolean fireworkEffect = false;
        List<LinkedTreeMap<? ,?>> effectTreeMapList = new ArrayList<>();
        // 烟花
        boolean firework = false;
        double power = -1;
        // 旗帜图案
        boolean hasPatterns = false;
        List<Pattern> patternList = new ArrayList<>();
        // 地图
        double mapId = -1;

        // 先处理部分meta数据
        switch (metaType){
            case "LEATHER_ARMOR" : {
                if ( !map.containsKey("color") ) break;
                isLeather = true;
                LinkedTreeMap<? ,?> treeMap = ((LinkedTreeMap<? ,?>) map.get("color"));
                red = (Double)(treeMap.get("red"));
                green = (Double)(treeMap.get("green"));
                blue = (Double)(treeMap.get("blue"));
                if (red < 0) red += 256;
                if (green < 0) green += 256;
                if (blue < 0) blue += 256;
                map.remove("color");
                break;
            }
            case "ENCHANTED" : {
                if ( !map.containsKey("stored-enchants") ) break;
                storageEnchants =
                        map.get("stored-enchants")
                                .toString()
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "");
                map.remove("stored-enchants");
                break;
            }
            case "FIREWORK_EFFECT" : {
                if ( map.containsKey("firework-effect") ){
                    fireworkEffect = true;
                    effectTreeMapList.add((LinkedTreeMap<? ,?>) map.get("firework-effect"));
                    map.remove("firework-effect");
                }
                // TODO type数据读取
                map.remove("type");
                break;
            }
            case "FIREWORK" : {
                if (map.containsKey("firework-effects")){
                    firework = true;
                    ArrayList<?> effectList = ((ArrayList<?>) map.get("firework-effects"));
                    if (effectList != null) {
                        for (Object o : effectList) effectTreeMapList.add((LinkedTreeMap<? ,?>)o);
                    }
                    map.remove("firework-effects");
                }
                if (map.containsKey("power")){
                    power = (Double) map.get("power");
                    map.remove("power");
                }
                break;
            }
            case "BANNER" : {
                if ( !map.containsKey("patterns") ) break;
                hasPatterns = true;
                List<?>patternMapList = (List<?>) map.get("patterns");
                if (patternMapList != null) {
                    for (Object o : patternMapList) {
                        LinkedTreeMap<?,?> treeMap = (LinkedTreeMap<?, ?>)o;
                        String color = treeMap.get("color").toString();
                        String pattern = treeMap.get("pattern").toString();
                        Pattern p = new Pattern(DyeColor.valueOf(color), PatternType.valueOf(pattern));
                        patternList.add(p);
                    }
                }
                map.remove("patterns");
                break;
            }
            case "MAP" : {
                if ( !map.containsKey("map-id") ) break;
                mapId = (Double) map.get("map-id");
                map.remove("map-id");
                break;
            }
            default : {}
        }

        // 遍历其他nbt
        Iterator<Map.Entry<String, Object>> mapIterator = map.entrySet().iterator();
        while (mapIterator.hasNext()){
            Map.Entry<String, Object> entry = mapIterator.next();
            switch (entry.getKey()){
                case "custom-model-data" : {
                    // 针对某插件修复
                    String value = map.get("custom-model-data").toString();
                    if (! value.contains(".")) break;
                    // 转为整数类型
                    map.put("custom-model-data", ((int) ConvertUtils.parseDouble(value)));
                    break;
                }
                case "Damage" :{
                    damage = (double)map.get("Damage");
                    mapIterator.remove();
                    break;
                }
                case "enchants" :{
                    enchants = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
                    mapIterator.remove();
                    break;
                }
                case "repair-cost" :{
                    repairCost = (double) map.get("repair-cost");
                    mapIterator.remove();
                    break;
                }
                default : {}
            }
        }

        meta = invokeMeta(map);
        if (meta == null) return null;

        if (isLeather) ((LeatherArmorMeta)meta).setColor(Color.fromRGB((int)red, (int)green, (int)blue));
        if (damage != 0) ((Damageable)meta).setDamage((int)damage);
        if (enchants != null) addEnchantments(meta, enchants.split(","));
        if (storageEnchants != null) addStorageEnchantments(meta, storageEnchants.split(","));
        if (fireworkEffect) {
            addFireworkEffects(meta, effectTreeMapList, false);
        }
        if (firework){
            addFireworkEffects(meta, effectTreeMapList, true);
        }
        if(power != -1){
            ((FireworkMeta)meta).setPower((int)power);
        }
        if (mapId != -1)
            //noinspection deprecation
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
        // 解析
        try{
            return  (ItemMeta) deserialize.invoke(null, map);
        }catch (InvocationTargetException | IllegalAccessException e){
            Bukkit.getLogger().severe(String.format("[GlobalMarket]解析物品数据时出错：%s", map));
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
                    enchantment = xEnchantment.get().getEnchant();
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
                    enchantment = xEnchantment.get().getEnchant();
                }
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) ((EnchantmentStorageMeta)meta).addStoredEnchant(enchantment, level, true);
            }
        }
    }

}

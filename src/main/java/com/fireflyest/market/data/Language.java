package com.fireflyest.market.data;

import io.fireflyest.emberlib.config.annotation.Comments;
import io.fireflyest.emberlib.config.annotation.Entry;
import io.fireflyest.emberlib.config.annotation.Yaml;
import io.fireflyest.emberlib.data.Box;

/**
 * 语言文件
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Yaml("lang/zh-CN.yml")
public final class Language {

    private Language() {
        // EMPTY
    }

    @Entry
    public static final Box<String> COMMAND_PLAYER = new Box<>("该指令只能由玩家使用!");
    @Entry
    public static final Box<String> COMMAND_ARGUMENT = new Box<>("指令参数类型错误或者参数缺失");
    @Entry
    public static final Box<String> COMMAND_PERMISSION = new Box<>("您没有这样做的权限 §3%perm%");

    @Entry
    public static final Box<String> CONFIG_RELOADING = new Box<>("配置重载中...");
    @Entry
    public static final Box<String> CONFIG_RELOADED = new Box<>("配置重载完成!");

    @Entry
    public static final Box<String> VERSION_NEW = new Box<>("检测到新的版本\n§e§n%url%");
    @Entry
    public static final Box<String> VERSION_CHECK = new Box<>("当前最新版本§3%version%\n§e§n%url%");


    @Entry
    @Comments("货币符号")
    public static final Box<String> SYMBOL_COIN = new Box<>("§6ɞ");
    @Entry
    public static final Box<String> SYMBOL_POINT = new Box<>("§dɗ");

    @Entry
    public static final Box<String> MAIL_REMIND = new Box<>("您有邮件未签收");
    @Entry
    public static final Box<String> MAIL_MAXIMUM = new Box<>("邮箱数量已上限");
    @Entry
    public static final Box<String> MAIL_SEND = new Box<>("邮件已发送");
    @Entry
    public static final Box<String> MAIL_RECEIVE = new Box<>("您接收到新的邮件 ");

    @Entry
    public static final Box<String> BID_REMIND = new Box<>("玩家 §3%p% §f对拍卖商品叫价 §3%m% ");
    @Entry
    public static final Box<String> BID_SUCCEED = new Box<>("成功参与竞拍");
    @Entry
    public static final Box<String> BID_CONFIRM = new Box<>("拍卖商品确认第 §3%n%§f 次 ");

    @Entry
    public static final Box<String> ERROR_TRANSACTION = new Box<>("你不能购买你自己出售的物品");
    @Entry
    public static final Box<String> ERROR_TYPE = new Box<>("物品的出售类型不符合");
    @Entry
    public static final Box<String> ERROR_DATA = new Box<>("暂无数据");
    @Entry
    public static final Box<String> ERROR_SIGN = new Box<>("无法签收邮件");
    @Entry
    public static final Box<String> ERROR_CANCEL = new Box<>("你不是该商品的主人");
    @Entry
    public static final Box<String> ERROR_DISCOUNT = new Box<>("折扣范围应该在1~9");
    @Entry
    public static final Box<String> ERROR_REPRICE = new Box<>("超出定价范围");
    @Entry
    public static final Box<String> ERROR_AMOUNT = new Box<>("该货币类型不允许零散购买");
    @Entry
    public static final Box<String> ERROR_CURRENCY = new Box<>("该商品还未设置货币");
    @Entry
    public static final Box<String> ERROR_STAR = new Box<>("请过段时间再赞吧");
    @Entry
    public static final Box<String> ERROR_CREATE = new Box<>("黑名单用户无法创建交易");

    @Entry
    public static final Box<String> SUCCEED_SIGN = new Box<>("你签收了一个物品");
    @Entry
    public static final Box<String> SUCCEED_AFFAIR = new Box<>("交易收获 §3%m%");
    @Entry
    public static final Box<String> SUCCEED_CANCEL = new Box<>("交易已取消");
    @Entry
    public static final Box<String> SUCCEED_TRANSACTION = new Box<>("交易成功");
    @Entry
    public static final Box<String> SUCCEED_STAR = new Box<>("已为店铺点赞");
    @Entry
    public static final Box<String> SUCCEED_STORE = new Box<>("已为店铺更名");

    @Entry
    public static final Box<String> TRANSACTION_BROADCAST = new Box<>("玩家§3%p%§f在全球市场上架 ");
    @Entry
    public static final Box<String> TRANSACTION_EDIT = new Box<>("交易类型修改成功");
    @Entry
    public static final Box<String> TRANSACTION_FAIL = new Box<>("玩家%t%无法承担交易所需费用");
    @Entry
    public static final Box<String> TRANSACTION_NUM = new Box<>("超过商品剩余数量");
    @Entry
    public static final Box<String> TRANSACTION_CREATE = new Box<>("成功创建新的交易");
    @Entry
    public static final Box<String> TRANSACTION_CONTRABAND = new Box<>("无法交易违禁品");
    @Entry
    public static final Box<String> TRANSACTION_REPRICE = new Box<>("交易价格已更改为 %price%");
    @Entry
    public static final Box<String> TRANSACTION_SIZE = new Box<>("玩家 §3%t% §f的额外上架空间为 §3%num%");
    @Entry
    public static final Box<String> TRANSACTION_MAXIMUM = new Box<>("交易数量已上限");

    @Entry
    public static final Box<String> AUCTION_FLOW = new Box<>("你的拍卖商品流拍了");
    @Entry
    public static final Box<String> AUCTION_FINISH = new Box<>("拍卖结束，请到收件箱领取收益");
    
    @Entry
    public static final Box<String> MERCHANT_BLACK = new Box<>("已更改玩家店铺封禁状态");

    @Entry
    @Comments("界面显示文本，重载配置不会马上更新")
    public static final Box<String> GUI_MAIL_BUTTON = new Box<>("打开邮箱");
    @Entry
    public static final Box<String> GUI_MAIL_HOVER = new Box<>("点击打开邮箱界面");
    @Entry
    public static final Box<String> GUI_MAIL_FROM_CANCEL = new Box<>("交易取消");
    @Entry
    public static final Box<String> GUI_MAIL_FROM_AUCTION = new Box<>("拍卖行");
    @Entry
    public static final Box<String> GUI_MAIL_FROM_REWARD = new Box<>("交易收获");
    @Entry
    public static final Box<String> GUI_MAIL_FROM_TRANSACTION = new Box<>("市场交易");
    @Entry
    public static final Box<String> GUI_MAIL_FROM_ORDER = new Box<>("市场收购");
    @Entry
    public static final Box<String> GUI_MAIL_STAGNATE = new Box<>("还有 %amount% 件物品滞留");

    @Entry
    public static final Box<String> GUI_RECORD = new Box<>("§e§l交易记录");
    @Entry
    public static final Box<String> GUI_ITEM = new Box<>("§3§l交易物品§f: ");
    @Entry
    public static final Box<String> GUI_REWARD = new Box<>("§3§l收获§f: %s%s");
    @Entry
    public static final Box<String> GUI_BUYER = new Box<>("§3§l买家§7: §f%s");
    @Entry
    public static final Box<String> GUI_SELLER = new Box<>("§3§l卖家§7: §f%s");
    @Entry
    public static final Box<String> GUI_PRICE_START = new Box<>("§3§l起拍价§7: §f%s%s");
    @Entry
    public static final Box<String> GUI_PRICE_PRESENT = new Box<>("§3§l现价§7: §f%s%s");
    @Entry
    public static final Box<String> GUI_PRICE_ORIGINAL = new Box<>("§3§l原价§7: §f§m%s%s");
    @Entry
    public static final Box<String> GUI_PRICE_NORMAL = new Box<>("§3§l价格§7: §f%s%s");
    @Entry
    public static final Box<String> GUI_PRICE_ORDER = new Box<>("§3§l收购价§7: §f%s%s");
    @Entry
    public static final Box<String> GUI_HEAT = new Box<>("§3§l热度§7: §f%s");
    @Entry
    public static final Box<String> GUI_CONFIRM = new Box<>("§7第§3%s§7次确认");
    @Entry
    public static final Box<String> GUI_BIDERS = new Box<>("§3§l参与者§7: §f%s");
    @Entry
    public static final Box<String> GUI_DEADLINE = new Box<>("§3§l剩余时间§7: §f%s");
    @Entry
    public static final Box<String> GUI_CREDIT = new Box<>("§3§l信誉度§7: §f%s");
    @Entry
    public static final Box<String> GUI_AMOUNT = new Box<>("§3§l成交量§7: §f%s");
    @Entry
    public static final Box<String> GUI_SELLING = new Box<>("§3§l在售§7: §f%s");
    @Entry
    public static final Box<String> GUI_VISIT = new Box<>("§3§l浏览量§7: §f%s");
    @Entry
    public static final Box<String> GUI_STAR = new Box<>("§3§l点赞§7: §f%s");
    @Entry
    public static final Box<String> GUI_OWNER = new Box<>("§3§l店主§7: §f%s");
    @Entry
    public static final Box<String> GUI_CURRENCY = new Box<>("§7将货币物品拖拽至此处");
    @Entry
    public static final Box<String> GUI_STORE = new Box<>("§a§l%p%的小店");

    @Entry
    public static final Box<String> TAG_PREPARE = new Box<>("§f[§8预售§f]");
    @Entry
    public static final Box<String> TAG_AUCTION = new Box<>("§f[§e拍卖§f]");
    @Entry
    public static final Box<String> TAG_RETAIL = new Box<>("§f[§7零售§f]");
    @Entry
    public static final Box<String> TAG_ORDER = new Box<>("§f[§9收购§f]");
    @Entry
    public static final Box<String> TAG_ADMIN = new Box<>("§f[§c官方§f]");
    @Entry
    public static final Box<String> TAG_NEW = new Box<>("§f[§b新§f]");
    @Entry
    public static final Box<String> TAG_DEADLINE = new Box<>("§f[§4即将下架§f]");
    @Entry
    public static final Box<String> TAG_HEAT = new Box<>("§f[§6🔥§f]");

    @Entry
    public static final Box<String> TITLE_MAIN = new Box<>("§0市场 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_SEARCH = new Box<>("§0搜索§n%search%§r §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_RETAIL = new Box<>("§0零售 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_AUCTION = new Box<>("§0拍卖行 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_ORDER = new Box<>("§0收购 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_POINT = new Box<>("§0点券交易 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_COIN = new Box<>("§0金币交易 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_ITEM = new Box<>("§0物品交易 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_ADMIN = new Box<>("§0官方市场 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_HOME = new Box<>("§0主菜单");
    @Entry
    public static final Box<String> TITLE_MANAGE = new Box<>("§0管理界面");
    @Entry
    public static final Box<String> TITLE_STORE = new Box<>("§0玩家商店 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_MINE = new Box<>("§0个人页面");
    @Entry
    public static final Box<String> TITLE_MAIL = new Box<>("§0邮箱页面");
    @Entry
    public static final Box<String> TITLE_EDIT = new Box<>("§0%item% §7ID§8%page%");
    @Entry
    public static final Box<String> TITLE_VISIT = new Box<>("§0%p%的商店 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_AFFAIR = new Box<>("§0%item% §7ID§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_1 = new Box<>("§0食物 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_2 = new Box<>("§0物品 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_3 = new Box<>("§0方块 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_4 = new Box<>("§0可燃物与木制品 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_5 = new Box<>("§0装备武器 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_6 = new Box<>("§0附魔与药水 §7#§8%page%");
    @Entry
    public static final Box<String> TITLE_CATEGORY_7 = new Box<>("§0其他 §7#§8%page%");


}

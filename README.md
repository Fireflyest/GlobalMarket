# GlobalMarket - Free Trade Market
##### 一个基于游戏箱子界面的玩家交易市场插件
在Minecraft多人游戏中，玩家在进行物品交易时，需要考虑他们在交易过程中的信任问题。
本插件提供一个实时交易平台使玩家能够使用游戏币进行商品交易。

插件基于[Spigot](https://www.spigotmc.org/)核心开发，使用[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)插件发送虚拟物品，将玩家交易商品展示在游戏容器中。
玩家可在容器中点击物品进行对商品的查看、购买等操作。
插件支持在1.13.x-1.18.x的服务端版本中运行。

![市场图片](https://attachment.mcbbs.net/data/myattachment/forum/202007/31/182901yqwqs5dz50qwdxw6.png.thumb.jpg)

## 目录
* [安装插件](#安装插件)
* 功能和使用方法
  * 出售物品
  * 打折或修改价格
  * 使用数据库存储
  * 商品分类
  * 牌子交互
* 自定义配置
* 权限与指令
* 维护人员

## 安装插件
在安装插件前，先确保已经添加前置插件Vault、ProtocolLib和Gui以正常加载插件。
将插件下载后放置于plugins文件夹，并重载服务器。
## 功能和使用方法
### 出售物品
手持要交易的物品，输入指令`/market sell 价格 数量`便可将指定数量的手持物品出售。
如果不输入数量，则手持物品全部出售。打开市场后可以看到你所出售的商品，并展示卖家和价格。

![商品展示图](https://attachment.mcbbs.net/data/myattachment/forum/202007/31/175812urwsuvuwutstzdqt.png.thumb.jpg)

点击商品进入交易菜单，可以查看更多的商品信息或者进行其他商品操作。

![交易界面](https://attachment.mcbbs.net/data/myattachment/forum/202010/19/124911mdnz42u20voy4idu.png.thumb.jpg)

商品主人点击交易界面内的漏斗或者在市场浏览界面shift+鼠标左键可以下架商品。
如果商品被他人购买，你将会收到一封交易记录，点击收件后获得交易所得。
同时，购买者的将会收到所购买的物品。在聊天框中点击便可打开邮箱。

![邮箱提示](https://attachment.mcbbs.net/data/myattachment/forum/202007/31/180225nq88dslnd8y3hlhh.png.thumb.jpg)

### 打折或修改价格
在交易界面查看所出售商品的ID，并在聊天框中输入指令`/market discount ID 1`可以将商品打一折。
当然你可以在1~9中选择任意一个折数

![商品打折](https://attachment.mcbbs.net/data/myattachment/forum/202007/31/182625malxl2u27rpu9zpz.png.thumb.jpg)

如果你想直接修改价格，可以输入指令`/market reprice ID 0.1`也可以直接修改价格

### 使用数据库存储
如果想多个服务器使用同一个市场数据，只需要让他们连接到同一个数据库即可。
在插件的config.yml中修改Sql为true并填写其他配置信息，使插件能够连接数据库。
```
#SQL参数(高一点的版本需要在url加上'useSSL=false')
Sql: false
Url: 'jdbc:mysql://localhost:3306/mc_market?useSSL=false&serverTimezone=UTC'
User: 'root'
Password: '123456'
```
其中数据库需要自己创建，数据库中的表不需要自己创建。

### 商品分类
上架后的商品会按照不同的类型分类，输入指令`/market classify`可以进入分类菜单。

![分类菜单](https://attachment.mcbbs.net/data/myattachment/forum/202008/01/122459l77ossso9ncm24bf.png.thumb.jpg)

### 牌子交互
在牌子的第一行输入`market`然后确认，第一行将自动变为插件名称，右键牌子可以打开市场。
手持物品蹲着右键牌子，可以快捷上架商品。

如果在创建牌子的时候第一行输入`market`第二行输入`mail`并保存，右键牌子可以打开邮箱

![牌子交互](https://attachment.mcbbs.net/data/myattachment/forum/202008/29/112613bgdpi3ttif3w9pd2.png.thumb.jpg)

## 自定义配置
config.yml
```
#SQL参数(高一点的版本需要在url加上'useSSL=false')
Sql: false
Url: 'jdbc:mysql://localhost:3306/mc_market?useSSL=false&serverTimezone=UTC'
User: 'root'
Password: '123456'

#物品几天后自动下架，请输入正整数，输入-1为关闭限制
LimitTime: 7

#启用分类
ItemClassify: true

#上架限制
LimitLore: false
LimitLoreList: 私有,vip,绑定

#数量限制
LimitAmount: true
LimitAmountNum: 30
LimitAmountNumVip: 50

#上架最大金额(默认99999999)
MaxPrice: 99999999

#上架提示
SellBroadcast: true

#邮箱限制
LimitMail: true
LimitMailNum: 50

# 换页按钮是否分离
PageButtonSplit: false

# 上架税，防止玩家随意高价上架物品，不退还
Tax: true
# 起征点
TaxThreshold: 100000
# 税率，百分比0~1
TaxRate: 0.002

# 零散购买，开启后玩家可以只买一部分
BuyPartial: true
```

## 权限与指令
其中p表玩家，a表数量，m表价格，()表示可选，[]表示必填

前面带*的权限是op默认拥有，普通玩家没有的

| 指令                          | 功能           | 权限                 |
|-----------------------------|--------------|--------------------|
| /market admin [id]          | 将商品设置为无限     | * market.admin     |
| /market statistic (p)       | 统计服务器或玩家交易数据 | * market.statistic |
| /market mine                | 打开个人商店       |                    |
| /market mail                | 打开邮箱         |                    |
| /market data (id)           | 查看个人或商品数据    |                    |
| /market sign                | 一次性签收所有邮件    | market.sign        |
| /market other [p]           | 打开他人的商店      | market.other       |
| /market quick               | 快捷上架         |                    |
| /market sell [m] (a)        | 出售商品         | market.sell        |
| /market auction [m] (a)     | 拍卖商品         | market.auction     |
| /market discount [id] [1~9] | 商品打折         | market.discount    |
| /market reprice [id] [m]    | 商品重新定价       | market.reprice     |
| /market send [p] (a)        | 邮寄物品         | market.send        |
| /market desc [id] [desc]    | 给商品添加描述      | market.desc        |
| /market search [something]  | 搜索商品         | market.search      |

## 维护人员
[@Fireflyest](https://github.com/Fireflyest) QQ: 746969484

## 使用情况
![bstats](https://bstats.org/signatures/bukkit/GlobalMarket.svg)
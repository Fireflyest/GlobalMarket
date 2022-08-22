![banner](https://attachment.mcbbs.net/data/myattachment/forum/202208/01/095315xb0ciuzks5cgb0i5.png)
# GlobalMarket - Free Trade Market
![GitHub all releases](https://img.shields.io/github/downloads/Fireflyest/GlobalMarket/total?style=flat-square)
![GitHub release (latest by date)](https://img.shields.io/github/downloads/Fireflyest/GlobalMarket/latest/total?style=flat-square)
![Spiget Downloads](https://img.shields.io/spiget/downloads/103933?label=spigot%20downloads)
![Spiget Rating](https://img.shields.io/spiget/rating/103933?style=flat-square)
##### 一个基于游戏箱子界面的玩家交易市场插件
在Minecraft多人游戏中，玩家在进行物品交易时，需要考虑他们在交易过程中的信任问题。
为了保证玩家交易的安全性，本插件提供一个实时交易平台使玩家能够使用游戏币或点券进行商品交易。

插件基于[Spigot](https://www.spigotmc.org/)核心开发，使用ProtocolLib插件发送虚拟物品，将玩家交易商品展示在游戏容器中。
玩家可在容器中点击物品进行对商品的查看、购买等操作。
插件支持在1.13.x-1.18.x的服务端版本中运行。

## 目录
* [安装插件](#安装插件)
* [功能和使用方法](#功能和使用方法)
  * 使用数据库存储
  * 牌子交互
  * 自定义按钮
* [配置文件](#自定义配置)
* [权限与指令](#权限与指令)
* 维护人员
* 使用情况

## 安装插件
1. 确保你的服务器有一个使用前置插件[Vault](https://www.spigotmc.org/resources/vault.34315/)的经济插件
2. 添加前置插件[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)用于显示虚拟物品按钮
3. 添加前置插件[CraftGUI](https://github.com/Fireflyest/CraftGUI)用于展示容器界面
4. 可选前置[PlayerPoints](https://www.spigotmc.org/resources/playerpoints.80745/)用于使用点券进行交易
5. 下载本插件后将其放置于服务器的plugins文件夹内，并重载服务器。

## 功能和使用方法
### 使用数据库存储
如果想多个服务器使用同一个市场数据，只需要让他们连接到同一个数据库即可。
在插件的config.yml中修改Sql为true并填写其他配置信息，使插件能够连接数据库。
```
#SQL参数(高一点的版本需要在url加上'useSSL=false')
Sql: false
Url: 'jdbc:mysql://localhost:3306/{DATABASE}?useSSL=false&serverTimezone=UTC'
User: 'root'
Password: '123456'
```
其中数据库需要自己创建，数据库中的表不需要自己创建。

### 牌子交互
在牌子的第一行输入`market`然后确认，第一行将自动变为插件名称，右键牌子可以打开市场。
手持物品蹲着右键牌子，可以快捷上架商品。

如果在创建牌子的时候第一行输入`market`第二行输入`mail`并保存，右键牌子可以打开邮箱

### 自定义按钮
输入`/mka`打开管理员菜单，找到你要替换的按钮，将新的按钮放上去

## 自定义配置
config.yml
```
#SQL参数(高一点的版本需要在url加上'useSSL=false')
Sql: false
Url: 'jdbc:mysql://localhost:3306/{DATABASE}?useSSL=false&serverTimezone=UTC'
User: 'root'
Password: '123456'

# 点券交易
Point: false

#物品几天后自动下架，请输入正整数，输入-1为关闭限制
LimitTime: 7

#上架限制
LimitLore: false
LimitLoreList: 私有,vip,绑定

#数量限制
LimitAmount: true
LimitAmountNum: 35
LimitAmountNumVip: 56

#上架最大金额(默认99999999)
MaxPrice: 99999999

#上架提示
SellBroadcast: true

#邮箱限制
LimitMail: true
LimitMailNum: 50

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


| 指令                      | 功能        | 权限               | 默认   |
|-------------------------|-----------|------------------|------|
| /mk admin [id]          | 将商品设置为无限  | market.admin     | op   |
| /mk statistic (p)       | 统计数据      | market.statistic | op   |
| /mk mine                | 打开个人页面    |                  |      |
| /mk mail                | 打开邮箱      |                  |      |
| /mk data (id)           | 查看个人或商品数据 |                  |      |
| /mk sign                | 一次性签收所有邮件 | market.sign      | true |
| /mk other [p]           | 打开他人的商店   | market.other     | true |
| /mk quick               | 快捷上架      | market.quick     | true |
| /mk sell [m] (a)        | 出售商品      | market.sell      | true |
| /mk auction [m] (a)     | 拍卖商品      | market.auction   | true |
| /mk discount [id] [1~9] | 商品打折      | market.discount  | true |
| /mk reprice [id] [m]    | 商品重新定价    | market.reprice   | true |
| /mk send [p] (a)        | 邮寄物品      | market.send      | true |
| /mk desc [id] [desc]    | 给商品添加描述   | market.desc      | true |
| /mk search [something]  | 搜索商品      | market.search    | true |
| /mka                    | 按钮编辑      | market.admin     | op   |
| /mka check              | 查看最新版本    | market.admin     | op   |
| /mka reload             | 重载配置      | market.admin     | op   |
|                         | 更多上架空间    | market.vip       | op   |

## 维护人员
[@Fireflyest](https://github.com/Fireflyest) QQ: 746969484

## 使用情况
![bstats](https://bstats.org/signatures/bukkit/GlobalMarket.svg)

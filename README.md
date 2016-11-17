## eSDK\_SSO\_SDK\_Java  ##
华为eSDK SSO单点登录，提供了用户只需要一次登录就可以访问所有相互信任的应用系统的功能，实现现代企业集成的多个应用之间的相互交互，满足业务之间的融合。

华为eSDKSSO系统**eSDK\_SSO\_SDK\_Java**提供Java本地化接口，为您提供单点登录业务的开放能力。

## 版本更新 ##
eSDK SSO最新版本v2.1.00

## 开发环境 ##

- 操作系统： Windows7专业版
- JDK 1.8或以上版本
- Eclipse for Java EE：Eclipse 4.4.2或以上版本

## 文件指引 ##

- src文件夹：eSDK\_SSO\_SDK\_Java源码
- sample文件夹：eSDK SSO SDK的代码样例
- doc：eSDK SSO SDK的接口参考、开发指南
- third-party:eSDK SSO SDK中使用的第三方库


## 入门指导 ##
编译SOAP工程：

- 把src/Servlet目录下的两个工程导入eclipse之后，修正其Java Build Path中引用jar包的路径。
- 把src/webForRun目录下的一个工程导入eclipse之后，修正其Java Build Path中引用jar包的路径。
- 编译运行：编译工作空间中的这三个工程，将esdksso添加到在eclipse上配置的Server中，并在该Server的Launch configuration中添加这三个工程及其引用的类库，Classpath中的顺序先工程后jar包，保存配置并运行Server。
- 详细的开发指南请参考doc中的开发指南

编译Sample Code：

- 将这些工程导入eclipse，直接编译运行即可。

###单点登录###
体验华为eSDK SSO服务的能力，以下步骤演示如何实现单点登录。

    Step1：填充配置文件sso_common_ext_conf.properties
    auth_ips=10.45.17.19
    sso_auth_processor=com.huawei.esdk.sso.demo.DemoImpl

    Step2：添加实现用户鉴权接口实现类（即Step1中的sso_auth_processor）的jar包（详细可参照doc中的开发指南），并添加到Server的Launch configuration中，保存配置并运行Server。


## 获取帮助 ##

在开发过程中，您有任何问题均可以至[DevCenter](https://devcenter.huawei.com)中提单跟踪。也可以在[华为开发者社区](http://bbs.csdn.net/forums/hwucdeveloper)中查找或提问。另外，华为技术支持热线电话：400-822-9999（转二次开发）
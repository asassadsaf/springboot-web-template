# 程序部署目录结构
root_path<br/>
&emsp;--scripts &emsp;&emsp;&emsp;——脚本目录，包含启动脚本停止脚本等 <br/>
&emsp;--config &emsp;&emsp;&emsp;——配置目录 <br/>
&emsp;&emsp;--policy &emsp;&emsp;——策略配置目录<br/>
&emsp;&emsp;--shell &emsp;&emsp;&emsp;——shell脚本<br/>
&emsp;&emsp;--template &emsp;——安装时填充的模板文件<br/>
&emsp;--logs &emsp;&emsp;&emsp;&emsp;——日志目录<br/>
&emsp;&emsp;--sys &emsp;&emsp;&emsp;——系统日志目录<br/>
&emsp;&emsp;--operation &emsp;——操作日志目录<br/>
&emsp;--springboot-web-template-1.0.0.jar ——jar包<br/>

# 功能
## 一、程序部署使用相对路径的目录结构

## 二、包装HttpServletRequest对象获取或设置请求参数和请求头

## 三、文件监听器+缓存实现读取文件内容

## 四、OpenFeign日志配置

## 五、OpenFeign调用透传请求头

## 六、OpenFeign调用添加签名信息

## 七、OpenFeign调用支持HTTPS

## 八、OpenFeign调用示例

## 九、本地缓存Caffeine定义并使用Cache

## 十、Jackson序列化/反序列化首字母大写属性

## 十一、使用单例模式实现SecureRandom对象生成随机数

## 十二、类属性存在默认初值时使用Lombok-@Builder的注意事项

## 十三、读写yml文件

## 十四、基于BouncyCastle生成JKS文件公钥证书和公钥指纹

## 十五、自定义异常类以及全局异常处理器

## 十六、基于AOP和全局异常处理器实现记录操作日志

## 十七、基于Spring监听器实现读取外部配置文件

## 十八、在非IOC管理对象或静态方法中获取Bean方式

## 十九、在linux下执行命令、脚本工具类

## 二十、服务配置ssl以及限制密码套件

## 二十一、Spring提供的读取classpath下文件方式

## 二十二、手动注册Bean到IOC容器的两种方式
### 在监听器中通过GenericApplicationContext注册
### 实现BeanDefinitionRegistryPostProcessor接口注册

## 二十三、匿名内部类是否在循环中创建对类名的影响

## HttpServletRequestWrapper获取@PathVariable标注的参数

## springboot启动时执行sql脚本初始化数据库

## MybatisPlus自定义Id生成器

## MybatisPlus自定义字段填充处理器

## MybatisPlus自定义DatabaseIdProvider，用于手动处理多种数据库类型

## MybatisPlus分页插件

## MybatisPlus类型转换处理器，自定义DateTypeHandler用于统一日期格式

## MybatisPlus工具类，通过方法引用获取该字段名

## 三分量合成密钥加解密工具类

## Caffeine缓存指定过期时间和删除监听器

## Validator校验框架使用组序列规定校验顺序

## 通过上下文对象选取接口的实现类

## SpringBoot开启并使用@Scheduled定时任务
1ca57efa
## 在Runnable接口实现类中使用Spring声明式事务，Bean的原型模式
1ca57efa
## SpringBoot手动提交回滚事务
1ca57efa
## 定时任务实现在条件成立的情况下自己取消
1ca57efa
## 手动使用Druid连接池管理获取数据库连接并进行jdbc操作
1ca57efa
## Druid连接池中连接的关闭机制以及连接池的关闭机制
1ca57efa
## DruidDataSource加入以及逐出缓存的实际，使用双重检查锁机制避免重复加入缓存并减少性能损耗
1ca57efa
## IOUtils不关闭流使用注意
a8a632b5
## 加权随机算法实现选择概率问题
7e7d2c1e
## 使用SpringSecurity实现认证和鉴权
a1422edd 63a22d5b
## 接口调用次数统计功能
b4c232e7
## 本地缓存集成到Spring Cache
24e967d0
## Spring MVC拦截器抛出异常的场景
586edff8
## 手动设置Http响应头的时机以及HttpServletResponse类的isCommitted方法
f027cd59
## 引入Redis及其配置，执行lua脚本
77431423 c757b964
## SpringBoot定时任务和线程池配置
9e775a60 80b29fc6
## 子类使用父类属性是注入属性示例
3eb20474
## Socket使用tls双向认证示例，TLS版本和jdk版本关系
f2126259  3d31a5bb
## 将SpringBoot项目打成胖包直接引入到其他项目中
c757b964
## 获取类的泛型类型、Fastjson反序列化带泛型的类的对象和TypeReference实现
3d31a5bb  ab4319e7

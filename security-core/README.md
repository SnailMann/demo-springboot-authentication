# Security-Core

####Security配置类

SecurityProoperties:

- BrowserProperties
- ValidateCodeProperties
- OAuth2Properties
- SocialProperties

为什么写在Core,因为不管是app还是browser都会用到


#### 图形验证码基本参数配置

- 请求级配置      
(覆盖下面)配置值在调用接口时传递

- =>应用级配置
(覆盖下面)配置值写在security-demo

- =>默认配置
配置值写在security-core
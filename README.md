# demo-springboot-authentication
authentication demo

```
<module>security-core</module>    <!--核心业务逻辑-->
<module>security-demo</module>    <!--浏览器安全特定代码-->
<module>security-app</module>     <!--app相关特定代码-->
<module>security-browser</module> <!--样例程序-->
```

#### 多端登录分别处理

#### 根据不同客户端分别返回html或是Json数据

#### 登录认证

#### 拦截器，过滤器

#### 图形验证码功能

- 根据随机数生成图片
- 将随机数存到Session中
- 再将生产的图片写到接口中


#### 记住我原理


#### 短信验证码登录

- 开发短信验证码接口
- 校验短信验证码并登录
- 重构代码



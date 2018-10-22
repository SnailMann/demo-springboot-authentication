# Spring Security


#### Spring Security原理

实际原理就是过滤器链，完全通过了过滤才能到达RestFul API

1. Username Password Authentication Filter
2. Basic Authentication Filter
3. ...
4. Exception Translation Filter //主要是用来捕获后面的Interceptor所捕获的异常
5. FilterSecurity Interceptor //SpringSecurity最后的守门员,可以做一些权限控制


(4)前面的过滤器都是可以通过配置来决定是否开启的，后面的过滤器是必然会存在

<br>

#### 自定义用户凭证（即登录验证，不使用默认的方式）

实现UserDetailService,返回UserDetail.比如校验的逻辑也是SpringSecurity来实现的
即获取了账户信息，然后我们将从数据库获取的密码拿到，交给SpringSecurity去验证就可以了
不需要我们自己去验证的

<br>

#### 密码加解密

crypto.password包下的PasswordEncoder类

<br>

#### 个性化用户认证流程

```
 @Override
     protected void configure(HttpSecurity http) throws Exception {
         //任何请求都需要表单认证
         http.formLogin()
                 .loginPage("/login.html")    //自动登录页面，没有则使用默认的
                 .loginProcessingUrl("/authentication/form")   //自定义，对应form表达的url请求
                 .and()
                 .authorizeRequests()
                 .antMatchers("/login.html").permitAll()  //当访问/login.html是，放行，避免造成死循环
                 .anyRequest()
                 .authenticated()
                 .and()
                 .csrf().disable();
     }
```
<br>

#### RequestCache记住用户上次的请求

```java
   /**
     *   For RequestCache
     *   在跳转到/authentication/require之前，SpringSecurity会将当前的请求缓存到HttpSessionRequestCache中
     *   比如说某个用户想查看某篇文章（/article），但是该文章需要用户登录才能查看,所以会自动跳转/authentication/require
     *   但是在跳转之前，Security会记录/article这个请求到Cache中，当我们通过/authentication/require完成登录，就可以从Cache
     *   取得上次要跳转的请求继续执行，在如下场景就是登录完毕后，自动跳转到该文章浏览
     */
```

<br>

#### 自定义登录成功和失败的处理

在form表单提交的同步请求方式里，如果登录成功，SpringSecurity就会默认跳转到你上次跳转的页面
但是在现在前后端分离开发的情况下，几乎请求都是异步的ajax请求

- 自定义登录成功处理
AuthenticationSuccessHandler接口是核心
securityconfig中http.successHandler(myAuthenticationSuccessHandler) 显示声明我们自己的登录成功处理逻辑


- 自定义登录失败处理
AuthenticationFailHandler接口是核心

- 可以做到根据配置来自动选择处理方式（耦合式跳转，分离式JSON）
要使用根据配置来决定，就不能使用AuthenticationSuccessHandler了，而要使用默认的Handler
SavedRequestAwareAuthenticationSuccessHandler
SimpleUrlAuthenticationFailureHandler


#### 认证处理流程

UsernamePasswordAuthenticationFilter

=>
AuthenticationManager

=>
AuthenticationProvider

=>
UserDetailsService

=>
UserDetails

=>
Authentication(已认证)

#### 认证结果如何在多个请求之间共享
简单来说就是在session里共享

Authentication(已认证)

=>
SecurityContext                        　　　　　　　　　　　//将Authentication封装在内部

=>
SecurityContextHolder                  　　　　　　　　//实际就是ThreadLocal,线程级变量，毕竟每一个请求都是一个线程在处理

=>
SecurityContextPersistenceFilter

//这是Filter链前面的Fliter，进来的时候检查Session，有SecurityContext就放进线程。出去的时候检查线程，有就放进Session

#### 获取认证用户信息


#### 记住我

- 记住我原理

First: 

Browser 认证请求=> UsernamePasswordAuthenticationFilter 认证成功=> RemberMeService(TokenRepository)

1. 将Token写入数据库=> DB
2. 将Token写入浏览器Cookie=> 浏览器
 
After:

Browser => RememberMeAuthenticationFilter 读取Cookie中的Token=> RemberMeService(TokenRepository)
 查找Token=>DB 拿到Token认证后获取用户信息=> UserDetailsService
 
 不同浏览器同一用户在DB会有多个token
 
- 记住我功能具体实现

    1. PersistentTokenRepository
    2. JdbcTokenRepositoryImpl
    3. UserDetailService

- 记住我功能SpringSecurity源码解析



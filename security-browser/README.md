# Spring Security

- Spring Security原理

实际原理就是过滤器链，完全通过了过滤才能到达RestFul API

1. Username Password Authentication Filter
2. Basic Authentication Filter
3. ...
4. Exception Translation Filter //主要是用来捕获后面的Interceptor所捕获的异常
5. FilterSecurity Interceptor //SpringSecurity最后的守门员,可以做一些权限控制


(4)前面的过滤器都是可以通过配置来决定是否开启的，后面的过滤器是必然会存在


- 自定义用户凭证（即登录验证，不使用默认的方式）

实现UserDetailService,返回UserDetail.比如校验的逻辑也是SpringSecurity来实现的
即获取了账户信息，然后我们将从数据库获取的密码拿到，交给SpringSecurity去验证就可以了
不需要我们自己去验证的



- 密码加解密

crypto.password包下的PasswordEncoder类


- 个性化用户认证流程

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


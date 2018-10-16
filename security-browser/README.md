# Spring Security

- Spring Security原理

实际原理就是过滤器链，完全通过了过滤才能到达RestFul API

1. Username Password Authentication Filter
2. Basic Authentication Filter
3. ...
4. Exception Translation Filter //主要是用来捕获后面的Interceptor所捕获的异常
5. FilterSecurity Interceptor //SpringSecurity最后的守门员,可以做一些权限控制


(4)前面的过滤器都是可以通过配置来决定是否开启的，后面的过滤器是必然会存在

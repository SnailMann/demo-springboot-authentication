# demo-springboot-demo

----

### Description:

测试demo-样例程序


### Remark:

- 我们可以在url后面加一些正则表达式，例如只允许输入的id是数字
```
@GetMapping("/user/{id:\\d+}")              //：\\d+只允许id是数字
```

- 什么时候我们会想用@JsonView注解？@JsonView的使用步骤？
````
什么时候用？

当我们想在返回的信息中，有部分信息不返回，只返回想要返回的信息，可以使用@JsonView

使用步骤：

1. 使用接口来声明多个视图

 public interface UserSimpleView{};
 public interface UserDetailView extends UserSimpleView{}
 
2. 在值对象的get方法上指定视图

 @JsonView(UserSimpleView.class)  //为什么在属性定义上，而不在get方法上
 private String username;         //因为我们用了lombok的@Data注解
 @JsonView(UserDetailView.class)
 private String password; 

3. 在Controller方法上指定要返回的视图

 @GetMapping("/user")
 @JsonView(User.UserSimpleView.class)
 public List<User> query(@RequestParam String username) {...}
 
 @GetMapping("/user/{id:\\d+}")                   
 @JsonView(User.UserDetailView.class)
 public User getInfo(@PathVariable String id){...}

````

- Post请求，Controller上没有对应的@RequestBody去解析body会怎么样
```
 //如果没有@RequestBody,则具体的数据是不会被成功解析的，即user是没有数据的
 public User create(User user){log.info(user); }
 
 //correct
 public User create(@RequestBody  User user){log.info(user); }
```


- SpringMVC Date时间类型解析问题
```
通常网上的答案就是加一些注解去格式化成某个格式，比如说yyyy-mm-dd hh:mm:ss等
但是在前后端分离的情况下，这个数据有可能是app获取，也可能browser去获取数据，
他们需要进行展示的时间格式不一定是一样的。所以如果在传递方就固定好格式，或者
实现格式判断返回，其实都是很麻烦的，所以我们可以统一只传递没有格式的时间格式，
比如时间戳。也就是说，跨服务的时间传递，我们以没有格式的形式传递，具体需要什
么形式以需要方自己格式化
```

- @Valid注解和BindingResult验证请求参数的合法性并处理校验结果

```
    //entity中写入一些注解，比如@NotBlank的意思就是password属性不允许为空
    @NotBlank
    private String password;
    
    //Controller方法中需要对要验证的属性用@Vaild注解修饰
     public User create(@Valid @RequestBody User user,BindingResult errors){
        if (errors.hasErrors()){
            errors.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
        }
         ...
     }
    //如果没有使用BindingResult，验证失败的话是会错误的，如果使用了的话，则200状态码并进入到方法内部
```

- RESTful API的错误处理机制（SpringBoot默认的错误处理机制与自定义异常处理）

```
//SpringBoot的默认错误处理机制
1.  当请求出现错误，SpringBoot后台会自定判断请求是否由浏览器发起还是别的客户端发起，如果是浏览器发起的请求，返回的
    错误是一个html，但如果是安卓端或其他客户端发起的请求，返回的就不是html而是一段json格式的错误信息数据
    由SpringBoot的BasicErrorController类来处理这种错误，主要看Request Headers中是否含有text/html来区别处理
2.  如果在Controller请求中抛出异常，SpringBoot默认会将异常的信息返回出去，报500服务端错误
3.  我们可以通过@ControllerAdvice，@ExceptionHandler来实现全局异常处理
```


- RESTful API的拦截
```
1. 过滤器（Filter）
javax.servlet.filter包下的filter的doFilter方法实现，我们可以@Component去启动Filter
2. 拦截器（interceptor）
interceptor是spring框架提供的拦截方式，集成度更好，但只能拦截controller的请求
3. 切片（Aspect）
aop两个重点，一个是切入点(poincut,那些方法切入，什么时候执行)，一个是增强（advice,方法）

Filter可以拦截任何东西，是servlet提供的东西，但是无法拿到原始http请求响应和方法信息。
拦截器可以拿到原始http请求响应和方法信息，但无法拿到所拦截方法的参数（在prehandle阶段），因为参数传入到handle是在postHandle阶段执行的，
所以这是interceptor的一个缺陷。而aspect则没有这个问题，面向切面是可以拿到的

如果filter和intercepter和aspect都存在的情况下，
在pre阶段，filter是最先被触发的，然后interceptor,然后aspect
filter也是最后结束的，关系就是包饺子，filter是最外层，intercptor是中层，aspect是内层

外层                                 内层
filter>interceptor>controllerAdvice>aspect>controller
filter最先执行，aspect最后执行，如果有异常，也是aspect最先获取到，filter最后获取到，总之结构是类似栈，先进后出

```

- 如何将第三方的Filter注册到Spring容器中，以启动第三方拦截器
```
    /**
     * 通过configuration的方式来讲Filter注册到Spring容器中
     * 作用就是某些第三方filter无法使用@Component注解，springboot也没有提供web.xml的方式去注册
     * 所以就必须通过configuration的方式来注册
     * @return
     */
    @Bean
    public FilterRegistrationBean getThirdPartyFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ThirdPartFilter());
        List<String> urls = Stream.of("/*").collect(Collectors.toList());
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
```
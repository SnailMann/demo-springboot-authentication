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

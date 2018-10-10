# demo-springboot-demo

----

### Description:

测试demo-样例程序


### Remark:

- 我们可以在url后面加一些正则表达式，例如只允许输入的id是数字
```
@GetMapping("/user/{id:\\d+}")                   //：\\d+只允许id是数字
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




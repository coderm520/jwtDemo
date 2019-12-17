# jwtDemo
jwtDemo in springboot

手动实现一个JWT(JSON WEB TOKEN)的生成和校验过程

1.启动项目后调用登录接口：login/do
2.将登录返回的token带入接口参数：test/getUserName
3.如果希望将token放入header中，可以加入 @Login注解，会自动校验token合法性，token不合法则进行url跳转登录


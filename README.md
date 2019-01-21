# spring-boot-starter-jcaptcha


### 说明

 > 基于 JCaptcha 验证码项目实现的验证码 Spring Boot Starter 实现

1. SimpleImageCaptchaServlet 自动注册
2. https://jcaptcha.atlassian.net/wiki/spaces/general/pages/1212438/Simple+Servlet+Integration+documentation
3.使用 SimpleImageCaptchaServlet.validateResponse(req, "");进行验证码校验


### Maven

``` xml
<dependency>
	<groupId>${project.groupId}</groupId>
	<artifactId>spring-boot-starter-jcaptcha</artifactId>
	<version>1.0.1.RELEASE</version>
</dependency>
```

### Sample ：  CaptchaResolver Extends

[https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-jcaptcha](https://github.com/vindell/spring-boot-starter-samples/tree/master/spring-boot-sample-jcaptcha "spring-boot-sample-jcaptcha")

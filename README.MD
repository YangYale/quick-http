Quick-Http 快速调用rest接口
=================================================
### quickstart
#### 依赖
```xml
<dependency>
    <groupId>com.ipet</groupId>
    <artifactId>quick-http</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 调用
```java
//包扫描
@HttpComponentScan("com.ipet.test.rest.http")
@Configuration
@SpringBootApplication
public class ServiceExecutor {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ConfigurableApplicationContext configurableApplicationContext =  new SpringApplication(ServiceExecutor.class).run(args);
        new CountDownLatch(1).await();
    }
}

//http interface声明
@HttpComponent
public interface HttpTestInterface {

    //http request 声明
    @HttpRequest(value = "http://www.qq.com",header = @Header(key = "Content-Type",value = "text/html"))
    String visitQQ();
}
```
#### 特性
##### 适配6种http请求 GET,POST,HEAD,OPTIONS,PUT,DELETE，默认情况下使用GET，通过@HttpRequest注解中的method指定
```java
@HttpComponent
public interface HttpTestInterface {
    @HttpRequest("https://www.baidu.com")
    String visitBaidu();

    @HttpRequest(value = "https://www.baidu.com",method = HttpMethod.POST)
    String postBaidu();

    @HttpRequest(value = "https://www.baidu.com",method = HttpMethod.PUT)
    String putBaidu();

    @HttpRequest(value = "https://www.baidu.com", method = HttpMethod.DELETE)
    String deleteBaidu();

    @HttpRequest(value = "https://www.baidu.com", method = HttpMethod.HEAD)
    String headBaidu();

    @HttpRequest(value = "https://www.baidu.com", method = HttpMethod.OPTIONS)
    String optionsBaidu();
}
```
##### SSL请求，可以手动指定证书,默认情况下无证书处理,SSL请求根据schema自动识别,通过@HttpRequest注解中的certification指定
```java
@HttpComponent
public interface HttpTestInterface {
    @HttpRequest(value="https://localhost",certification="/home/ssl/key.crt")
    String visitBaidu();
}
```

##### 设置header
```java
@HttpComponent
public interface HttpTestInterface {
    //方式一 直接在@HttpRequest注解中通过@Header或者@Headers注解设置固定header
    @HttpRequest(value = "http://www.qq.com",header = @Header(key = "Content-Type",value = "text/html"))
    String visitQQ();
    
    //方式二 通过入参的方式设置可变header
    @HttpRequest("http://www.qq.com")
    String visitQQ(@Header(key = "Content-Type") String contentType);
}
```

##### 字符集设置 默认为utf-8
```java
@HttpComponent
public interface HttpTestInterface {
    @HttpRequest(value = "http://www.qq.com",charset="GB2312")
    String visitQQ();
}
```

##### 缓冲区大小设置 默认是8KB
```java
@HttpComponent
public interface HttpTestInterface {

    @HttpRequest(url = "http://www.qq.com", bufferSize = 16 * 1024)
    String visitQQ(@Header(key = "Content-Type") String contentType);
}
```

##### request请求前后支持切面操作(前后置操作类的定义需要实现相应的接口PreRequest,AfterRequest)
```java
@HttpComponent
public interface HttpTestInterface {
    @HttpRequest(url = "http://www.qq.com", bufferSize = 16 * 1024, preScript = BeforeRequestScript.class, afterScript = AfterRequestScript.class)
    String visitQQ(@Header(key = "Content-Type") String contentType);
}

//前置操作类BeforeRequestScript
public class BeforeRequestScript implements PreRequest {
    @Override
    public void setRequestMethodMetadata(RequestMethodMetadata requestMethodMetadata) {
        //pass the request method metadata
    }

    @Override
    public void doScript() {
        //do your things...
    }
}


//后置操作类AfterRequestScript
public class AfterRequestScript implements AfterRequest {
    @Override
    public void doScript() {
        //do your things...
    }

    @Override
    public <T> void setResponse(T response) {
        //pass the response object
    }

    @Override
    public void setRequestMethodMetadata(RequestMethodMetadata requestMethodMetadata) {
        //pass the request method metadata
    }
}

```

##### 支持异步，且异步目前通过NIO实现,目前异步实现的时候需要注意,Future<T> 泛型中的T不能为空或者是?，即必须要有正常的类型作为泛型
```java
@HttpComponent
public interface HttpTestInterface {

    //只需要设置返回值为Future类型，自动识别为异步调用
    @HttpRequest("https://www.baidu.com")
    Future<String> asyncVisitBaidu();
}
```
# 一、依赖注入简介
依赖注入DI是控制反转IoC的一种实现方式——当前最常用的，其他的实现方式有工厂模式、服务定位器模式
DI：业务逻辑所需要的对象由IoC容器提供，可以更好地面向接口编程、单元测试更容易（注入的对象可以是一个测试对象），业务逻辑可更关注本身代码

1. 传统：业务逻辑里，new一个接口的具体实现
2. 工厂模式：传入一个参数（可以是String，相当于一个凭据），找到工厂，工厂根据凭据产生对象
3. DI：业务逻辑接受接口作为参数（IoC容器提供具体的对象），直接执行业务逻辑。

依赖注入从jdk5开始遵循JSR-330标准，javax.inject，DI框架都主持这个
# 二、javax.inject
## 2.1@Inject

1. 用在构造器上，只有一个构造器可以使用该注解，因为jre无法确定注入的优先级，也就无法确定到底调用哪个构造函数（参数0个或多个）
2. 方法，一般用在setXxx方法上（参数0个或多个），向构造器注入的是必要依赖项，其他非必要的用setXxx注入
3. 属性，不是final的，最好不要这么做，不利于单元测试。

## 2.2@Qualifier

需要具体实现，用于建立，标识同类的不同对象的标识符
```java
@Documented
@Retention(RUNTIME)
@Qualifier
public @interface MusicGene {
    Genre genre() default GENRE.TRANCE;
    public enum GENRE {CLASSICAL, METAL, ROCK, TRANCE}
}

public class MetalRecordAlbumns {
    @Inject @MusicGenre(GENRE.METAL) Genre genre;
}
```

## 2.3@Named

特殊的`@Qualifier`注解，一般的DI框架都要实现的，借助名字注入对象，和`@Inject`一起使用，既要符合类又要符合名字

## 2.4@Scope

需要具体实现，用于限定对象的重用方式（生命周期），一个类只能有一个`@Scope`

## 2.5@Singleton

通用的`@Scope`实现，单例模式，被注入的对象不会改变

## 2.6接口Provide&lt;T&gt;

可以一次注入获取多个对象
```java
@Inject test(Provide<T> messageProvider) {
    Message mysql = messageProvider.get();
    if(condition) {
        //得到Message的副本
        Message mysqlCopy = messageProvider.get();
    }
}
```

# 三、轻量级的DI框架

Guice
```pom.xml
    <dependency>
        <groupId>com.google.inject</groupId>
        <artifactId>guice</artifactId>
        <version>4.1.0</version>
    </dependency>
```
具体使用见[wzt3309/think-in-java](http://github.com/wzt3309/think-in-java "wzt3309/think-in-java")的`jxl.wzt.u3di`
























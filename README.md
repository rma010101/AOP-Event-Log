# Event Log AOP - Spring Boot Project

## Overview
This is a Spring Boot application demonstrating Aspect-Oriented Programming (AOP) for event logging. AOP allows you to separate cross-cutting concerns like logging, security, and transaction management from your business logic.

## Dependencies

### Core Dependencies
The following dependencies are configured in `pom.xml`:

```xml
<dependencies>
    <!-- Spring Boot Web Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot AOP Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>

    <!-- Spring Boot Test Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### AOP Dependency Details

#### `spring-boot-starter-aop`
This starter dependency includes:
- **Spring AOP**: Core Spring framework AOP functionality
- **AspectJ Weaver**: Provides AspectJ pointcut expressions and weaving capabilities
- **AspectJ Runtime**: Runtime support for AspectJ aspects

## Important AOP Annotations

### Core Aspect Annotations

#### `@Aspect`
- **Purpose**: Marks a class as an aspect containing advice methods
- **Usage**: Place on aspect classes
```java
@Aspect
@Component
public class LoggingAspect {
    // Advice methods here
}
```

#### `@Component`
- **Purpose**: Makes the aspect class a Spring-managed bean
- **Usage**: Required alongside `@Aspect` for Spring to detect the aspect
```java
@Aspect
@Component
public class EventLogAspect {
    // Aspect implementation
}
```

### Advice Annotations

#### `@Before`
- **Purpose**: Executes before the target method
- **Use Case**: Pre-processing, validation, logging method entry
```java
@Before("execution(* com.aop_example.event_log_aop.service.*.*(..))")
public void logMethodEntry(JoinPoint joinPoint) {
    System.out.println("Entering method: " + joinPoint.getSignature().getName());
}
```

#### `@After`
- **Purpose**: Executes after the target method (regardless of outcome)
- **Use Case**: Cleanup, logging method exit
```java
@After("execution(* com.aop_example.event_log_aop.service.*.*(..))")
public void logMethodExit(JoinPoint joinPoint) {
    System.out.println("Exiting method: " + joinPoint.getSignature().getName());
}
```

#### `@AfterReturning`
- **Purpose**: Executes after successful method completion
- **Use Case**: Success logging, result processing
```java
@AfterReturning(pointcut = "execution(* com.aop_example.event_log_aop.service.*.*(..))", 
                returning = "result")
public void logMethodReturn(JoinPoint joinPoint, Object result) {
    System.out.println("Method " + joinPoint.getSignature().getName() + 
                      " returned: " + result);
}
```

#### `@AfterThrowing`
- **Purpose**: Executes when target method throws an exception
- **Use Case**: Error logging, exception handling
```java
@AfterThrowing(pointcut = "execution(* com.aop_example.event_log_aop.service.*.*(..))", 
               throwing = "error")
public void logError(JoinPoint joinPoint, Throwable error) {
    System.out.println("Method " + joinPoint.getSignature().getName() + 
                      " threw exception: " + error.getMessage());
}
```

#### `@Around`
- **Purpose**: Surrounds method execution, most powerful advice
- **Use Case**: Performance monitoring, transaction management
```java
@Around("execution(* com.aop_example.event_log_aop.service.*.*(..))")
public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long endTime = System.currentTimeMillis();
    System.out.println("Method " + joinPoint.getSignature().getName() + 
                      " executed in " + (endTime - startTime) + "ms");
    return result;
}
```

### Pointcut Annotations

#### `@Pointcut`
- **Purpose**: Defines reusable pointcut expressions
- **Usage**: Create named pointcuts for better maintainability
```java
@Pointcut("execution(* com.aop_example.event_log_aop.service.*.*(..))")
public void serviceLayer() {}

@Pointcut("execution(* com.aop_example.event_log_aop.controller.*.*(..))")
public void controllerLayer() {}

@Before("serviceLayer()")
public void logServiceMethods(JoinPoint joinPoint) {
    // Logging logic
}
```

## Common Pointcut Expressions

### Execution Pointcuts
- `execution(* com.aop_example.event_log_aop.service.*.*(..))` - All methods in service package
- `execution(public * *(..))` - All public methods
- `execution(* *.get*(..))` - All getter methods
- `execution(* com.aop_example.event_log_aop.service.UserService.save(..))` - Specific method

### Annotation-based Pointcuts
- `@annotation(org.springframework.web.bind.annotation.GetMapping)` - Methods with @GetMapping
- `@within(org.springframework.stereotype.Service)` - Classes with @Service
- `@target(org.springframework.stereotype.Repository)` - Target objects with @Repository

## Custom Annotations for AOP

You can create custom annotations for more specific pointcuts:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogEvent {
    String value() default "";
}
```

Then use it in pointcuts:
```java
@Before("@annotation(logEvent)")
public void logCustomEvent(JoinPoint joinPoint, LogEvent logEvent) {
    System.out.println("Event: " + logEvent.value());
}
```

## Project Structure for AOP

Recommended package structure:
```
src/main/java/com/aop_example/event_log_aop/
├── EventLogAopApplication.java
├── aspect/
│   ├── LoggingAspect.java
│   └── PerformanceAspect.java
├── controller/
│   └── EventController.java
├── service/
│   └── EventService.java
├── model/
│   └── Event.java
└── annotation/
    └── LogEvent.java
```

## Configuration

### Enable AspectJ Auto Proxy (Optional)
Add to your main application class if needed:
```java
@SpringBootApplication
@EnableAspectJAutoProxy
public class EventLogAopApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventLogAopApplication.class, args);
    }
}
```

### Application Properties
Configure AOP settings in `application.properties`:
```properties
# Enable AOP (enabled by default with spring-boot-starter-aop)
spring.aop.auto=true

# Use CGLIB proxies instead of JDK dynamic proxies
spring.aop.proxy-target-class=true
```

## Getting Started

1. **Clone/Download** the project
2. **Install Dependencies**: Run `mvn clean install`
3. **Create Aspects**: Add aspect classes in the `aspect` package
4. **Add Services**: Create service classes to demonstrate AOP
5. **Run Application**: Execute `mvn spring-boot:run`

## Best Practices

1. **Keep Aspects Simple**: Focus on single concerns
2. **Use Appropriate Advice**: Choose the right advice type for your use case
3. **Performance Consideration**: Be mindful of @Around advice performance impact
4. **Exception Handling**: Properly handle exceptions in @Around advice
5. **Logging Levels**: Use appropriate logging levels for different environments
6. **Pointcut Reusability**: Define reusable pointcuts with @Pointcut

## Common Use Cases

- **Logging**: Method entry/exit, parameter values, return values
- **Security**: Authentication, authorization checks
- **Transaction Management**: Automatic transaction handling
- **Performance Monitoring**: Execution time measurement
- **Caching**: Automatic cache management
- **Validation**: Input validation before method execution
- **Auditing**: Track user actions and data changes

---

*This README provides a comprehensive guide to the AOP dependencies and annotations used in this Spring Boot project. For more advanced AOP features, refer to the Spring Framework and AspectJ documentation.*

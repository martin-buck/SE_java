import java.lang.annotation.*;

// @interface is part of creating a custom annotation - in this case 'Test'
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
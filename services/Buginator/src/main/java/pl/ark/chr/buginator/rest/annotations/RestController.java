package pl.ark.chr.buginator.rest.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * Created by Arek on 2016-12-19.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestController {

    @AliasFor(annotation = RequestMapping.class, attribute = "name") String name() default "";

    @AliasFor(annotation = RequestMapping.class, attribute = "path") String[] value() default {};

    @AliasFor(annotation = RequestMapping.class, attribute = "path") String[] path() default {};

    @AliasFor(annotation = org.springframework.web.bind.annotation.RestController.class, attribute = "value") String restControllerValue() default "";
}

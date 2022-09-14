package tech.oorjaa.reporting.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;

@ExtendWith(MockitoExtension.class)
public class FooControllerTest {

    @InjectMocks
    private FooController fooController;

    @Test
    public void fooControllerIsAvailableWithRoute(){
        Annotation restControllerAnnotation = FooController.class.getAnnotation(RestController.class);
        var requestRoute = FooController.class.getAnnotation(RestController.class).value();
        Assertions.assertNotNull(restControllerAnnotation);
        Assertions.assertEquals("/foo", requestRoute);
    }
}

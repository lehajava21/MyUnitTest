package handlers;

import annotations.Expected;

import java.lang.annotation.Annotation;

public class ExpectedHandler {
    public static boolean check(Annotation annotation, Exception exception){
        return ((Expected)annotation).exceptionType().isInstance(exception.getCause());
    }
}

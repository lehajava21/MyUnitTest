package handlers;

import annotations.Test;

import java.lang.annotation.Annotation;

public class TestHandler {
    public static boolean check(Annotation annotation){
        return ((Test)annotation).isEnabled();
    }
}

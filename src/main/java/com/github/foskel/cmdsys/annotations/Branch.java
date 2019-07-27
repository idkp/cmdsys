package com.github.foskel.cmdsys.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Branch {
    String id();

    String[] handles();

    String handleAliasDelimiter() default ";";
}

package com.taobao.bird.common.utils.compile;

public interface JavaSourceCompiler {

    @SuppressWarnings("rawtypes")
    Class compile(String sourceString);

    @SuppressWarnings("rawtypes")
    Class compile(JavaSource javaSource);

}

package com.facedamon.orm.generator.common;

public enum GeneratorConstant {
    SERVER("server"),
    SRC_PATH("srcPath"),
    BEAN_PACKAGE("beanPackage");

    private String value;

    GeneratorConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

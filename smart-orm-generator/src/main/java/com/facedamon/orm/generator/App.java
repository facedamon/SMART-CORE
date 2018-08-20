package com.facedamon.orm.generator;

import com.facedamon.orm.generator.common.DBType;
import com.facedamon.orm.generator.core.TableContext;

public class App {
    public static void main(String[] args) {
        TableContext.init(DBType.MYSQL);
    }
}

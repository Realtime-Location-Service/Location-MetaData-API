package com.rls.lms.converters;

import java.util.Map;

public class DSLToSQLConverter {
    public static String getSQLQueryFromDSL(Map<String, Object> queryDSL) {
        StringBuilder sql = new StringBuilder();

        queryDSL.entrySet().forEach((entry) -> {

        });

        return sql.toString();
    }
}

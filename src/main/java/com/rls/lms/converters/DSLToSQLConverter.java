package com.rls.lms.converters;

import com.rls.lms.exceptions.UnsupportedSearchParameter;

import java.util.Map;

public class DSLToSQLConverter {
    public static String getSQLQueryFromDSL(Map<String, Object> queryDSL) {
        StringBuilder sql = new StringBuilder();

        queryDSL.forEach((key, value) -> {
            switch (key) {
                case "match":
                    break;
                case "except":
                    break;
                case "range":
                    break;
                default:
                    throw new UnsupportedSearchParameter("'" + key + "' search is not supported");
            }
        });

        return sql.toString();
    }
}

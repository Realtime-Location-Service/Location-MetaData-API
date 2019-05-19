package com.rls.lms.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rls.lms.exceptions.JSONProcessingException;
import com.rls.lms.exceptions.UnsupportedSearchParameter;

import java.util.List;
import java.util.Map;

public class DSLToSQLConverter {
    public static String getSQLQueryFromDSL(Map<String, Object> queryDSL, String tableName, String jsonColumnName) {
        StringBuilder sql = new StringBuilder();

        final boolean[] multiTermFlag = {false};
        queryDSL.forEach((key, value) -> {
            switch (key) {
                case "match":
                    if (multiTermFlag[0]) {
                        sql.append(" AND ");
                    }
                    //noinspection unchecked
                    buildMatchQuery(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    multiTermFlag[0] = true;
                    break;
                case "except":
                    if (multiTermFlag[0]) {
                        sql.append(" AND ");
                    }
                    //noinspection unchecked
                    buildExceptQuery(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    multiTermFlag[0] = true;
                    break;
                case "range":
                    if (multiTermFlag[0]) {
                        sql.append(" AND ");
                    }
                    // TODO
                    multiTermFlag[0] = true;
                    break;
                default:
                    throw new UnsupportedSearchParameter("'" + key + "' search is not supported");
            }
        });

        return sql.toString();
    }

    private static void buildMatchQuery(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) -> {
            try {
                buildJSONContainsPhrase(sql, tableName+"."+jsonColumnName, value, key);
                sql.append("=1");
            } catch (JsonProcessingException e) {
                throw new JSONProcessingException(e.getMessage());
            }
        });
    }

    private static void buildExceptQuery(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) -> {
            try {
                buildJSONContainsPhrase(sql, tableName+"."+jsonColumnName, value, key);
                sql.append("=0");
            } catch (JsonProcessingException e) {
                throw new JSONProcessingException(e.getMessage());
            }
        });
    }

    private static void buildJSONContainsPhrase(StringBuilder sql, String target, Object candidate, String path) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        sql.append("JSON_CONTAINS(").append(target).append(", '");
        if (candidate instanceof List) {
            List list = (List) candidate;
            if (list.size()==0) {
                throw new JSONProcessingException(path + " list contains no item.");
            } else if (list.size()>1) {
                sql.append(objectMapper.writeValueAsString(list));
            } else {
                sql.append(objectMapper.writeValueAsString(list.get(0)));
            }
        } else {
            sql.append(objectMapper.writeValueAsString(candidate));
        }
        sql.append("', '$.").append(path).append("')");
    }
}

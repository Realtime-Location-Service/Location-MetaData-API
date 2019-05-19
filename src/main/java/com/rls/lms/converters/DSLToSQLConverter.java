package com.rls.lms.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rls.lms.exceptions.InvalidPayloadException;
import com.rls.lms.exceptions.JSONProcessingException;
import com.rls.lms.exceptions.UnsupportedSearchParameter;

import java.util.List;
import java.util.Map;

public class DSLToSQLConverter {
    public static String getSQLQueryFromDSL(Map<String, Object> queryDSL, String tableName, String jsonColumnName) {
        StringBuilder sql = new StringBuilder();

        queryDSL.forEach((key, value) -> {
            switch (key.toLowerCase()) {
                case "match":
                    //noinspection unchecked
                    buildMatchQuery(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    break;
                case "except":
                    //noinspection unchecked
                    buildExceptQuery(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    break;
                case "range":
                    //noinspection unchecked
                    buildRangeQuery(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    break;
                default:
                    throw new UnsupportedSearchParameter("'" + key + "' search is not supported");
            }
        });

        return sql.toString();
    }

    private static void buildMatchQuery(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) -> {
            buildJSONContainsPhrase(sql, tableName+"."+jsonColumnName, value, "$."+key, true);
        });
    }

    private static void buildExceptQuery(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) -> {
            buildJSONContainsPhrase(sql, tableName+"."+jsonColumnName, value, "$."+key, false);
        });
    }

    private static void buildRangeQuery(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) -> {
            buildJSONContainsPhrase(sql, tableName+"."+jsonColumnName, value, "$."+key, false);
            sql.append("=0");
        });
    }

    private static void buildJSONContainsPhrase(StringBuilder sql, String target, Object candidate,
                                                String path, boolean match, boolean appendOperator)
            throws JSONProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (appendOperator) sql.append(" AND ");
        try {
            sql.append("JSON_CONTAINS(").append(target).append(", '")
                    .append(objectMapper.writeValueAsString(candidate))
                    .append("', '").append(path).append("')");
        } catch (JsonProcessingException e) {
            throw new JSONProcessingException(e.getMessage());
        }
        if (match) sql.append("=1");
        else sql.append("=0");
    }

    private static void buildJSONContainsPhrase(StringBuilder sql, String target, Object candidate,
                                                String path, boolean match) throws InvalidPayloadException, JSONProcessingException {
        if (candidate instanceof List) {
            List list = (List) candidate;
            if (list.size() == 0) {
                throw new JSONProcessingException(path + " list contains no item.");
            } else {
//                sql.append(objectMapper.writeValueAsString(list.get(0)));
                buildJSONContainsPhrase(sql, target, candidate, path, match, true);
            }
        } else if(candidate instanceof Map) {
            for(Object entry : ((Map) candidate).entrySet()) {
                if (!match) {
                    buildOperatorJSONContainsPhrase(sql, target, ((Map.Entry)entry).getValue(),
                            path, false, translateOperator((String) ((Map.Entry)entry).getKey(), false));
                } else {
                    String operator = (String) ((Map.Entry)entry).getKey();
                    if (operator.equalsIgnoreCase("ANY")) {
                        buildOperatorJSONContainsPhrase(sql, target, ((Map.Entry)entry).getValue(),
                                path, true, translateOperator("ANY", true));
                    } else if (operator.equalsIgnoreCase("ALL")) {
                        buildJSONContainsPhrase(sql, target, ((Map.Entry)entry).getValue(), path, true, true);
                    } else {
                        throw new InvalidPayloadException("Operator '"+((Map.Entry)entry).getKey()+"' is unknown!");
                    }
                }
            }
        } else {
            buildJSONContainsPhrase(sql, target, candidate, path, match, true);
        }
    }

    private static String translateOperator(String operator, boolean match) {
        if (operator.equalsIgnoreCase("ANY") && !match) return " AND ";
        if (operator.equalsIgnoreCase("ALL") && !match) return " OR ";
        if (operator.equalsIgnoreCase("ANY") && match) return " OR ";
        if (operator.equalsIgnoreCase("ALL") && match) return " AND ";
        throw new InvalidPayloadException("Operator '"+operator+"' is unknown!");
    }

    private static void buildOperatorJSONContainsPhrase(StringBuilder sql, String target, Object candidate,
                                                          String path, boolean match, String operator) throws JSONProcessingException {
        if (candidate instanceof List) {
            List list = (List) candidate;
            if (list.size() == 0) {
                throw new JSONProcessingException(path + " list contains no item.");
            } else {
                boolean appendOperator = false;
                for (Object item : list) {
                    if (!appendOperator) {
                        sql.append(" AND (");
                    } else {
                        sql.append(operator);
                    }
                    buildJSONContainsPhrase(sql, target, item, path, match, false);
                    appendOperator = true;
                }
                sql.append(")");
            }
        } else {
            buildJSONContainsPhrase(sql, target, candidate, path, match, true);
        }
    }
}

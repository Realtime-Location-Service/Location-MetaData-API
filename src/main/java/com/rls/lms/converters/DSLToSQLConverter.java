package com.rls.lms.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rls.lms.exceptions.InvalidPayloadException;
import com.rls.lms.exceptions.JSONProcessingException;
import com.rls.lms.exceptions.UnsupportedSearchParameter;

import java.util.List;
import java.util.Map;

public class DSLToSQLConverter {
    public static String getSQLQueryClauseFromDSL(Map<String, Object> queryDSL, String tableName, String jsonColumnName) {
        StringBuilder sql = new StringBuilder();

        queryDSL.forEach((key, value) -> {
            switch (key.toLowerCase()) {
                case "match":
                    //noinspection unchecked
                    buildMatchQueryClause(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    break;
                case "except":
                    //noinspection unchecked
                    buildExceptQueryClause(sql, (Map<String, Object>) value, tableName, jsonColumnName);
                    break;
                case "range":
                    //noinspection unchecked
                    buildRangeQueryClause(sql, (Map<String, Object>) value, jsonColumnName);
                    break;
                default:
                    throw new UnsupportedSearchParameter("'" + key + "' search is not supported");
            }
        });

        return sql.toString();
    }

    private static void buildMatchQueryClause(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) ->
            buildJSONContainsClause(sql, tableName+"."+jsonColumnName, value, "$."+key, true)
        );
    }

    private static void buildExceptQueryClause(StringBuilder sql, Map<String, Object> payload, String tableName, String jsonColumnName) {
        payload.forEach((key, value) ->
            buildJSONContainsClause(sql, tableName+"."+jsonColumnName, value, "$."+key, false)
        );
    }

    private static void buildRangeQueryClause(StringBuilder sql, Map<String, Object> payload, String jsonColumnName) {
        payload.forEach((key, value) -> {
            //noinspection unchecked
            buildComparisonClause(sql, (Map<String, Object>) value, jsonColumnName, "$."+key);
        });
    }

    private static void buildComparisonClause(StringBuilder sql, Map<String, Object> payload, String jsonColumnName, String path) {
        payload.forEach((key, value) -> {
            sql.append(" AND ").append(jsonColumnName).append("->'").append(path).append("'");
            if (key.equalsIgnoreCase("lt")) {
                sql.append(" < ").append(value);
            } else if (key.equalsIgnoreCase("lte")) {
                sql.append(" <= ").append(value);
            } else if (key.equalsIgnoreCase("gt")) {
                sql.append(" > ").append(value);
            } else if (key.equalsIgnoreCase("gte")) {
                sql.append(" >= ").append(value);
            } else {
                throw new JSONProcessingException("Comparison operator '"+key+"' is not supported.");
            }
        });
    }

    private static void buildJSONContainsClause(StringBuilder sql, String target, Object candidate,
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

    private static void buildJSONContainsClause(StringBuilder sql, String target, Object candidate,
                                                String path, boolean match) throws InvalidPayloadException, JSONProcessingException {
        if (candidate instanceof List) {
            List list = (List) candidate;
            if (list.size() == 0) {
                throw new JSONProcessingException(path + " list contains no item.");
            } else {
//                sql.append(objectMapper.writeValueAsString(list.get(0)));
                buildJSONContainsClause(sql, target, candidate, path, match, true);
            }
        } else if(candidate instanceof Map) {
            for(Object entry : ((Map) candidate).entrySet()) {
                if (!match) {
                    buildOperatorJSONContainsClause(sql, target, ((Map.Entry)entry).getValue(),
                            path, false, translateOperator((String) ((Map.Entry)entry).getKey(), false));
                } else {
                    String operator = (String) ((Map.Entry)entry).getKey();
                    if (operator.equalsIgnoreCase("ANY")) {
                        buildOperatorJSONContainsClause(sql, target, ((Map.Entry)entry).getValue(),
                                path, true, translateOperator("ANY", true));
                    } else if (operator.equalsIgnoreCase("ALL")) {
                        buildJSONContainsClause(sql, target, ((Map.Entry)entry).getValue(), path, true, true);
                    } else {
                        throw new InvalidPayloadException("Operator '"+((Map.Entry)entry).getKey()+"' is unknown!");
                    }
                }
            }
        } else {
            buildJSONContainsClause(sql, target, candidate, path, match, true);
        }
    }

    private static String translateOperator(String operator, boolean match) {
        if (operator.equalsIgnoreCase("ANY") && !match) return " AND ";
        if (operator.equalsIgnoreCase("ALL") && !match) return " OR ";
        if (operator.equalsIgnoreCase("ANY") && match) return " OR ";
        if (operator.equalsIgnoreCase("ALL") && match) return " AND ";
        throw new InvalidPayloadException("Operator '"+operator+"' is unknown!");
    }

    private static void buildOperatorJSONContainsClause(StringBuilder sql, String target, Object candidate,
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
                    buildJSONContainsClause(sql, target, item, path, match, false);
                    appendOperator = true;
                }
                sql.append(")");
            }
        } else {
            buildJSONContainsClause(sql, target, candidate, path, match, true);
        }
    }
}

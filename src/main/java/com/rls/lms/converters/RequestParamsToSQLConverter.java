package com.rls.lms.converters;

import java.util.List;

public class RequestParamsToSQLConverter {
    public static String getSQLQuery(List<String> userIds, String status, int page, int size, String tableName) {
        StringBuilder sql = new StringBuilder();
        final int[] p = {Math.max(0, page)};
        final int[] s = {Math.max(0, size)};

        if (!userIds.isEmpty()) {
            sql.append(" AND ");
            sql.append(tableName).append(".").append("user_id").append(" IN (").append(String.join(",", userIds)).append(")");
        }

        if (!status.isEmpty()) {
            sql.append(" AND ");
            sql.append(tableName).append(".").append("status").append(" IN (").append(status).append(")");
        }

        sql.append(" LIMIT ").append((p[0]) * s[0]).append(", ").append(s[0]);

        return sql.toString();
    }
}

package com.rls.lms.converters;

import com.google.common.base.CaseFormat;
import org.springframework.util.MultiValueMap;

public class RequestParamsToSQLConverter {
    public static String getSQLQuery(MultiValueMap<String, String> requestParams, String tableName) {
        StringBuilder sql = new StringBuilder();
        final int[] page = {1};
        final int[] size = {100};

        requestParams.forEach((key, value) -> {
            if (key.equalsIgnoreCase("page")) page[0] = Integer.parseInt(value.get(0));
            else if (key.equalsIgnoreCase("size")) size[0] = Integer.parseInt(value.get(0));
            else if (key.equalsIgnoreCase("user_ids") || key.equalsIgnoreCase("userIds")) {
                sql.append(" AND ");
                sql.append(tableName).append(".").append("user_id").append(" IN (").append(String.join(",", value)).append(")");
            }
            else {
                String snakeCaseKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
                sql.append(" AND ");
                sql.append(tableName).append(".").append(snakeCaseKey).append(" IN (").append(String.join(",", value)).append(")");
            }
        });
        sql.append(" LIMIT ").append((page[0] - 1) * size[0]).append(", ").append(size[0]);

        return sql.toString();
    }
}

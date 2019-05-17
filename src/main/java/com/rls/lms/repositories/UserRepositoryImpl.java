package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rls.lms.converters.DSLToSQLConverter;
import com.rls.lms.exceptions.InvalidPayloadException;
import com.rls.lms.exceptions.UnsupportedPayloadTypeException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class UserRepositoryImpl implements ExtendedUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void patch(String userId, String domain, Map<String, Object> payload)
            throws JsonProcessingException, InvalidPayloadException, UnsupportedPayloadTypeException {
        StringBuilder query = new StringBuilder("UPDATE user u SET ");
        boolean valid = false;

        for (Map.Entry<String, Object> payloadEntry: payload.entrySet()) {
            switch (payloadEntry.getKey()) {
                case "status":
                    if (valid) query.append(", ");
                    query.append("u.status = '").append(payloadEntry.getValue()).append("'");
                    valid = true;
                    break;
                case "metadata":
                    StringBuilder data = new StringBuilder();
                    Map<String, Object> metadata;

                    if (payloadEntry.getValue() instanceof Map) {
                        //noinspection unchecked
                        metadata = (Map<String, Object>) payloadEntry.getValue();
                    } else throw new InvalidPayloadException("'metadata is not a valid Map object'");

                    for (Map.Entry<String, Object> entry: metadata.entrySet()) {
                        if (entry.getValue() instanceof String || entry.getValue() instanceof Boolean ||
                                entry.getValue() instanceof Integer || entry.getValue() instanceof Float ||
                                entry.getValue() instanceof Double) {
                            String json = new ObjectMapper().writeValueAsString(entry.getValue());
                            data.append(", '$.").append(entry.getKey()).append("', ").append(json);
                        } else {
                            String json = new ObjectMapper().writeValueAsString(entry.getValue());
                            data.append(", '$.").append(entry.getKey()).append("', CAST('").append(json).append("' AS JSON)");
                        }
                    }
                    if (valid) query.append(", ");
                    query.append("u.metadata = JSON_SET(u.metadata").append(data).append(")");
                    valid = true;
                    break;
                default:
                    throw new UnsupportedPayloadTypeException("'" + payloadEntry.getKey() + "' is an invalid payload");
            }
        }
        query.append(" WHERE u.user_id=\"").append(userId).append("\" AND u.domain=\"").append(domain).append("\";");
        em.createNativeQuery(query.toString()).executeUpdate();
    }

    @Override
    public List findByQueryDSL(Map<String, Object> queryDSL, String domain) {
        @SuppressWarnings("SqlResolve")
        String query = "SELECT * FROM user u WHERE "+ DSLToSQLConverter.getSQLQueryFromDSL(queryDSL)+" AND u.domain = \""+domain+"\"";

        return em.createNativeQuery(query).getResultList();
    }
}

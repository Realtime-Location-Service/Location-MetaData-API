package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rls.lms.converters.DSLToSQLConverter;
import com.rls.lms.exceptions.InvalidPayloadException;
import com.rls.lms.exceptions.JSONProcessingException;
import com.rls.lms.exceptions.UnsupportedPayloadTypeException;
import com.rls.lms.models.User;
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
            throws JSONProcessingException, InvalidPayloadException, UnsupportedPayloadTypeException {
        StringBuilder query = new StringBuilder("UPDATE user u SET ");
        final boolean[] valid = {false};

        payload.forEach((payloadKey, payloadValue) -> {
            switch (payloadKey) {
                case "status":
                    if (valid[0]) query.append(", ");
                    query.append("u.status = '").append(payloadValue).append("'");
                    valid[0] = true;
                    break;
                case "metadata":
                    StringBuilder data = new StringBuilder();
                    Map<String, Object> metadata;

                    if (payloadValue instanceof Map) {
                        //noinspection unchecked
                        metadata = (Map<String, Object>) payloadValue;
                    } else throw new InvalidPayloadException("'metadata is not a valid Map object'");

                    metadata.forEach((key, value) -> {
                        try {
                            if (value instanceof String || value instanceof Boolean ||
                                    value instanceof Integer || value instanceof Float ||
                                    value instanceof Double) {
                                String json = new ObjectMapper().writeValueAsString(value);
                                data.append(", '$.").append(key).append("', ").append(json);
                            } else {
                                String json = new ObjectMapper().writeValueAsString(value);

                                data.append(", '$.").append(key).append("', CAST('").append(json).append("' AS JSON)");
                            }
                        } catch (JsonProcessingException e) {
                            throw new JSONProcessingException(e.getMessage());
                        }
                    });

                    if (valid[0]) query.append(", ");
                    query.append("u.metadata = JSON_SET(u.metadata").append(data).append(")");
                    valid[0] = true;
                    break;
                default:
                    throw new UnsupportedPayloadTypeException("'" + payloadKey + "' is an invalid payload");
            }
        });

        query.append(" WHERE u.user_id=\"").append(userId).append("\" AND u.domain=\"").append(domain).append("\";");
        em.createNativeQuery(query.toString()).executeUpdate();
    }

    @Override
    public List findByQueryDSL(Map<String, Object> queryDSL, String domain) {
        @SuppressWarnings("SqlResolve")
        String query = "SELECT * FROM user u WHERE "+
                DSLToSQLConverter.getSQLQueryFromDSL(queryDSL, "u", "metadata")+
                " AND u.domain = \""+domain+"\"";

        return em.createNativeQuery(query, User.class).getResultList();
    }
}

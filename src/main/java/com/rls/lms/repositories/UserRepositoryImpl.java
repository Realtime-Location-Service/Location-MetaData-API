package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

public class UserRepositoryImpl implements ExtendedUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public int patchMetadata(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException {
        StringBuilder data = new StringBuilder();

        for (Map.Entry entry: metadata.entrySet()) {
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
        String query = "UPDATE user u SET u.metadata = JSON_SET(u.metadata"+
                data+") WHERE u.user_id=\""+userId+"\" AND u.domain=\""+domain+"\";";
        return em.createNativeQuery(query).executeUpdate();
    }
}

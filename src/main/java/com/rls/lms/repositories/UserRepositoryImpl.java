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
    public void patchMetadata(String id, String domain, Map<String, Object> metadata) throws JsonProcessingException {
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
        String query = "UPDATE user u SET u.metadata = JSON_SET(u.metadata"+data+") WHERE u.id=\""+id+"\"";
        em.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void updateMeta(String id, Map<String, Object> metadata) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(metadata);
        String query = "UPDATE user u SET u.metadata = CAST(\'"+json+"\' AS JSON) WHERE u.id=\""+id+"\"";
        em.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void updateStatus(String id, String status) {
        String query = "UPDATE user u SET u.status = "+status+" WHERE u.id=\""+id+"\"";
        em.createNativeQuery(query).executeUpdate();
    }
}

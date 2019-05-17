package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

@SuppressWarnings("unused")
public class UserRepositoryImpl implements ExtendedUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void patchMetadata(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException {
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
        @SuppressWarnings({"SqlResolve", "SqlSignature"})
        String query = "UPDATE user u SET u.metadata = JSON_SET(u.metadata"+data+") WHERE u.user_id=\""+
                userId+"\" AND u.domain=\""+domain+"\";";
        em.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void updateMeta(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(metadata);
        @SuppressWarnings("SqlResolve")
        String query = "UPDATE user u SET u.metadata = CAST(\'"+json+"\' AS JSON) WHERE u.user_id=\""+
                userId+"\" AND u.domain=\""+domain+"\";";
        em.createNativeQuery(query).executeUpdate();
    }

    @Override
    @Transactional
    public void updateStatus(String id, String status) {
        @SuppressWarnings("SqlResolve")
        String query = "UPDATE user u SET u.status = "+status+" WHERE u.id=\""+id+"\"";
        em.createNativeQuery(query).executeUpdate();
    }
}

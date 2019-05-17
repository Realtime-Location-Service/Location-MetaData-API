package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface ExtendedUserRepository {
    void patchMetadata(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException;

    void updateMeta(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException;

    void updateStatus(String id, String status);
}

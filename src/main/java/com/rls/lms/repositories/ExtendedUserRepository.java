package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface ExtendedUserRepository {
    int patchMetadata(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException;
}

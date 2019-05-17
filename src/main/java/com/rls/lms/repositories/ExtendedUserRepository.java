package com.rls.lms.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface ExtendedUserRepository {
    void patch(String userId, String domain, Map<String, Object> metadata) throws JsonProcessingException;

    List findByQueryDSL(Map<String, Object> queryDSL, String domain);
}

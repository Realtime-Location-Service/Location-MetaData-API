package com.rls.lms.models;

import com.rls.lms.converters.JSONToMapConverter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity // This tells Hibernate to make a table out of this class
public class User {

    @Id
    private String id;
    @Column(name = "user_id", unique = true)
    private String user_id;
    private String domain;
    @Column(name = "metadata", columnDefinition = "json")
    @Convert(converter = JSONToMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String userId) {
        this.user_id = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
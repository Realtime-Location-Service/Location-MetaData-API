package com.rls.lms.models;

import com.rls.lms.converters.JSONToMapConverter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity // This tells Hibernate to make a table out of this class
@Validated
@SuppressWarnings("unused")
@IdClass(User.IdClass.class)
public class User {

    @Id
    @NotNull
    @Column(name = "user_id", unique = true)
    private String user_id;

    @Id
    @NotNull
    private String domain;

    private String status;

    @Column(name = "metadata", columnDefinition = "json")
    @Convert(converter = JSONToMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    static class IdClass implements Serializable {
        private String domain;
        private String user_id;
    }
}
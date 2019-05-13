package com.rls.lms.models;

import com.rls.lms.converters.JSONToMapConverter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity // This tells Hibernate to make a table out of this class
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(name = "userId")
    private String userId;
    private String company;
    @Column(name = "metadata", columnDefinition = "json")
    @Convert(converter = JSONToMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
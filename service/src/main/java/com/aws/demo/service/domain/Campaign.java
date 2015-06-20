package com.aws.demo.service.domain;

import org.joda.time.DateTime;

public class Campaign {
    private final int id;
    private final String name;
    private final DateTime createdAt;

    public Campaign(int id, String name, DateTime createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }
}

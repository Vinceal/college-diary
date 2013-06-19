package com.collegediary.lessons;

import java.io.Serializable;

/**
 * этот класс содержит информацию о типах занятиях
 * @author Vinceal
 */
public class Lessons implements Serializable{
    
    private String id, type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Lessons(String id, String type) {
        this.id = id;
        this.type = type;
    }
    
}

package com.collegediary.groups.allcolumn;

import java.io.Serializable;

/**
 *
 * @author Vinceal
 */
public class GroupAllColumn implements Serializable{
    
    String idGroup, nameOfGroup, course, active, startDate, endDate;

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getNameOfGroup() {
        return nameOfGroup;
    }

    public void setNameOfGroup(String nameOfGroup) {
        this.nameOfGroup = nameOfGroup;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String Course) {
        this.course = Course;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public GroupAllColumn(String idGroup, String NameOfGroup, String Course, String active, String startDate, String endDate) {
        this.idGroup = idGroup;
        this.nameOfGroup = NameOfGroup;
        this.course = Course;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
    }
        
}

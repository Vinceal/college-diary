package com.collegediary.teachers;

import java.io.Serializable;

/**
 *  поля класса содержат информацию об преподавателях
 * @author Vinceal
 */
public class Teacher implements Serializable{
  private String id, firstName, lastName, patronomic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronomic() {
        return patronomic;
    }

    public void setPatronomic(String patronomic) {
        this.patronomic = patronomic;
    }

    public Teacher(String id, String firstName, String lastName, String patronomic) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronomic = patronomic;
    }
  
}

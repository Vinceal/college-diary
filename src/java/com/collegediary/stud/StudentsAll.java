package com.collegediary.stud;

import java.io.Serializable;

/**
 * в этом классе больше полей содержащих инф о учащихся
 * этот класс необходим для администраторской части сайта
 * @author Vinceal
 */
public class StudentsAll implements Serializable{
    String idStudent, lastName, firstName, patronomic, idGroup, numberRecordBook;

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronomic() {
        return patronomic;
    }

    public void setPatronomic(String patronomic) {
        this.patronomic = patronomic;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getNumberRecordBook() {
        return numberRecordBook;
    }

    public void setNumberRecordBook(String numberRecordBook) {
        this.numberRecordBook = numberRecordBook;
    }

    public StudentsAll(String idStudent, String lastName, String firstName, String patronomic, String idGroup, String numberRecordBook) {
        this.idStudent = idStudent;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronomic = patronomic;
        this.idGroup = idGroup;
        this.numberRecordBook = numberRecordBook;
    }
    
}

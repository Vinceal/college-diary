package com.collegediary.stud;

import java.io.Serializable;

/**
 * поля класса хранят информацию об студенте
 * @author Vinceal
 */
public class Students implements Serializable {

    private String lastName, firstName, patrnm, numRecBook, fio;
    private Integer idStudent;

    public Integer getIdStudent() {
        return idStudent;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public Students(Integer idStudent, String lastName, String firstName, String patrnm, String numRecBook) {
        this.idStudent = idStudent;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patrnm = patrnm;
        this.numRecBook = numRecBook;
        this.fio = lastName + " " + firstName + " " + patrnm;
    }
}

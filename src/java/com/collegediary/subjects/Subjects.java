package com.collegediary.subjects;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *поля класса содержат всю информацию о предметах
 * @author VinceAL
 */
@ManagedBean(name = "subj")
@SessionScoped
public class Subjects implements Serializable {

    private String subjectsX, idSubject;

    

    public Subjects(String subjectsX, String idSubject) {
        this.subjectsX = subjectsX;
        this.idSubject = idSubject;
    }
    
    public String getIdSubject() {
        return idSubject;
    }
    
    public String getSubjects() {
        return subjectsX;
    }

    public void setSubjects(String subjects, String idSubject) {
        this.subjectsX = subjects;
        this.idSubject = idSubject;
    }
}

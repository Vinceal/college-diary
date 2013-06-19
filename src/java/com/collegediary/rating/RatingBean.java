package com.collegediary.rating;

import com.collegediary.main.Connect;
import com.collegediary.main.Main;
import com.collegediary.subjects.SubjectsBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * класс позволяет получить успеваемость
 * @author Vinceal
 */
@ManagedBean(name = "ratingBean")
@SessionScoped
public class RatingBean implements Serializable {

    public ArrayList<Rating> rtg;
    public String idLesson, idSubject, idStudent, lessonType;
    private ArrayList<String> listLessonType;

    public ArrayList<String> getListLessonType() {
        return listLessonType;
    }

    public void setListLessonType(ArrayList<String> listLessonType) {
        this.listLessonType = listLessonType;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public ArrayList<Rating> getRtg() {
        return rtg;
    }

    public void setRtg(ArrayList<Rating> rtg) {
        this.rtg = rtg;
    }

    public RatingBean() {
        setupListLessonsType();
    }

    public void setupListRating() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        SubjectsBean subjectsBean = (SubjectsBean) elContext.getELResolver().getValue(elContext, null, "subjectsBean");
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
            HttpSession session = request.getSession(false);
            idStudent = (String) session.getAttribute("student");
            PreparedStatement query;
            ResultSet result;
            Connect connect = new Connect();
            Connection con = connect.getCon();
            if (subjectsBean.selectedSub == null) {
                query = con.prepareStatement("select Lesson_Type, Rating, "
                        + "coupleDate from main_journal "
                        + "inner join couples "
                        + "inner join lessons "
                        + "where couples_idcouples = idcouples "
                        + "and lessons_idLessons = idLessons "
                        + "and student_idStudent = ? "
                        + "and idLessons = ?;");
                query.setString(1, idStudent);
                query.setString(2, this.idLesson);
                query.executeQuery();
            } else {
                idSubject = subjectsBean.selectedSub.getIdSubject();
                query = con.prepareStatement("select Lesson_Type, Rating, "
                        + "coupleDate from main_journal "
                        + "inner join couples "
                        + "inner join lessons "
                        + "where couples_idcouples = idcouples "
                        + "and lessons_idLessons = idLessons "
                        + "and student_idStudent = ? "
                        + "and subject_idSubject = ? "
                        + "and idLessons = ?;");
                query.setString(1, idStudent);
                query.setString(2, idSubject);
                query.setString(3, this.idLesson);
            }
            String allq = query.toString();
            result = query.executeQuery();
            if (result.next()) {
                rtg = new ArrayList<Rating>();
                result.beforeFirst();
                while (result.next()) {
                    rtg.add(new Rating(result.getString("Lesson_Type"),
                            result.getString("Rating"),
                            result.getString("coupleDate")));
                }
            } else {
                if (rtg!=null) {
                    rtg.clear();
                }
            }
            connect.con.close();
        } catch (SQLException ex) {
            System.err.print("error in setupListRating");
            Logger.getLogger(RatingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void setupListLessonsType() {
        
        Connect connect = new Connect();
        Connection con = connect.getCon();
        try {
            PreparedStatement query = con.prepareStatement("select idLessons, Lesson_Type from lessons;");
            ResultSet result = query.executeQuery();
            listLessonType = new ArrayList<String>();
            if (result.next()) {
                if (result.isFirst()) {
                    this.lessonType = result.getString("Lesson_Type");
                    this.idLesson = result.getString("idLessons");
                    result.beforeFirst();
                }
                while (result.next()) {
                    listLessonType.add(result.getString("Lesson_Type"));
                }
            }
        connect.con.close();    
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setupIdLessonType() {
        
        Connect connect = new Connect();
        Connection con = connect.getCon();
        try {
            PreparedStatement query;
            query = con.prepareStatement("select idLessons from lessons where Lesson_Type =?");
            query.setString(1, this.lessonType);
            ResultSet result = query.executeQuery();
            result.first();
            this.idLesson = result.getString("idLessons");
            connect.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        setupListRating();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.collegediary.lessons;

import com.collegediary.main.Connect;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Vinceal
 */
@ManagedBean(name = "lessnsBean")
@SessionScoped
public class LessonsBean implements Serializable {

    private ArrayList<Lessons> lessn;
    private Lessons selLessn;
    private LessonsDataModel lessnDM;
    public String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Lessons getSelLessn() {
        return selLessn;
    }

    public void setSelLessn(Lessons selLessn) {
        this.selLessn = selLessn;
    }

    public LessonsDataModel getLessnDM() {
        return lessnDM;
    }

    public void setLessnDM(LessonsDataModel lessnDM) {
        this.lessnDM = lessnDM;
    }

    public LessonsBean() {
        listLessn();
    }

    public final void listLessn() {

        Connect connect = new Connect();
        Connection con = connect.getCon();
        try {
            PreparedStatement query = con.prepareStatement("SELECT * FROM lessons");
            ResultSet result = query.executeQuery();
            if (result != null) {
                lessn = new ArrayList<Lessons>();
                while (result.next()) {
                    lessn.add(new Lessons(result.getString("idLessons"),
                            result.getString("Lesson_Type")));
                }
            }
            connect.con.close();
            lessnDM = new LessonsDataModel(lessn);
        } catch (SQLException ex) {
            Logger.getLogger(LessonsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addRow() {

        if (this.type != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("insert into lessons (Lesson_Type) values (?)");
                ps.setString(1, this.type);
                ps.execute();
                this.type = null;
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Выполнено", "Запись успешно добавлена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                connect.con.close();
                this.listLessn();
            } catch (SQLException ex) {
                Logger.getLogger(LessonsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Проверьте правильность введённых данных!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteRow() {

        if (this.selLessn != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("delete from lessons where idLessons = ?");
                ps.setString(1, this.selLessn.getId());
                ps.execute();
                connect.con.close();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Выполнено", "Запись успешно удалена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                this.listLessn();
            } catch (SQLException ex) {
                Logger.getLogger(LessonsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Не возможно удалить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void updateRow() {

        if (this.selLessn != null) {

            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("update lessons"
                        + " set Lesson_Type = ? where idLessons = ?");
                ps.setString(1, this.selLessn.getType());
                ps.setString(2, this.selLessn.getId());
                con.setAutoCommit(false);
                boolean commited = false;
                try {
                    ps.executeUpdate();
                    con.commit();
                    commited = true;
                } finally {
                    if (!commited) {
                        con.rollback();
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Ошибка", "При изменении произошла ошибка!");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Выполнено", "Запись успещно изменена!");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                    con.setAutoCommit(true);
                    connect.con.close();
                }
                this.listLessn();
            } catch (SQLException ex) {
                Logger.getLogger(LessonsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Не возможно изменить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
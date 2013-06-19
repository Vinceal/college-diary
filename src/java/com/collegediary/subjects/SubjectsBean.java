package com.collegediary.subjects;

import com.collegediary.groups.Groups;
import com.collegediary.groups.GroupsBean;
import com.collegediary.main.Connect;
import com.collegediary.main.Main;
import com.collegediary.stud.StudentsBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  класс получает данны из бд
 * функции добавления предмета для преподавательской части
 * @author VinceAL
 */
@ManagedBean(name = "subjectsBean")
@SessionScoped
public final class SubjectsBean implements Serializable {

    public ArrayList<Subjects> sub;
    public Subjects selectedSub;
    private SubDataModel subModel;
    public String subName;

    public ArrayList<Subjects> getSub() {
        return sub;
    }

    public void setSub(ArrayList<Subjects> sub) {
        this.sub = sub;
    }  
    
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public SubDataModel getSubModel() {
        return subModel;
    }

    public void setSubModel(SubDataModel subModel) {
        this.subModel = subModel;
    }

    
    public Subjects getSelectedSub() {
        return selectedSub;
    }

    public void setSelectedSub(Subjects selectedSub) {
        this.selectedSub = selectedSub;
    }

    public SubjectsBean() throws NamingException, SQLException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String idTeacher = (String) session.getAttribute("teacher");
        String idStudent = (String) session.getAttribute("student");
        if (!idTeacher.equalsIgnoreCase("0")){
        ListfromDB();
        }else{
            if (!idStudent.equalsIgnoreCase("0")){
            getAllSubStudents();
            }
        }
        
        
    }

    public final void ListfromDB() throws SQLException {
        if (sub!=null){
            sub.clear();
        }        
        sub = new ArrayList<Subjects>();
        
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("teacher");
        if (!param.equalsIgnoreCase("0")) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query = con.prepareStatement("select idSubject,"
                    + "Subject_Name from subject where endDate> CURDATE()"
                    + "and teacher_idTeacher =? GROUP BY Subject_Name");
            query.setString(1, param);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                sub.add(new Subjects(result.getString("Subject_Name"),
                        result.getString("idSubject")));
            }
            connect.con.close();
        }
        subModel = new SubDataModel(sub);
    }

    public void onRowSelect() throws NamingException, SQLException {

        //через ELContext обращаемся к scope пространству, откуда получаем нужный бин
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        GroupsBean groupsBean = (GroupsBean) elContext.getELResolver().getValue(elContext, null, "groupsBean");
        groupsBean.ListfromDB(this.selectedSub.getSubjects());
        //получаем список студентов первой стоящей в списке группы
        //для этого получаем из grplist первую группу с индексом 0 
        //вытягиваем из неё id 
        Groups grp = (Groups) groupsBean.grplist.get(0);
        String idGroup = grp.getIdGroup();
        //получаем бин со студентами, очищаем лист и вызываем функцию заполнения
        //листа с конкретной групыы студентами этой группы.
        StudentsBean studentsBean = (StudentsBean) elContext.getELResolver().getValue(elContext, null, "studBean");
        studentsBean.stud.clear();
        studentsBean.ListfromDB(idGroup);
    }

    public void createNewSubject() {

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String idTeacher = (String) session.getAttribute("teacher");
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        Main mainBean = (Main) elContext.getELResolver().getValue(elContext, null, "mainBean");
        GroupsBean groupsBeanSec = (GroupsBean) elContext.getELResolver().getValue(elContext, null, "groupsBeanSecond");
        String idGroup = groupsBeanSec.selectedgrp.getIdGroup();
        String firstDate = mainBean.firstDateS;
        String lastDate = mainBean.lastDateS;
        if (subName != null & idTeacher != null & idGroup != null & firstDate != null
                & lastDate != null) {
            
            try {
                Connect connect = new Connect();
                Connection con = connect.getCon();
                PreparedStatement query = null;
                query = con.prepareStatement("insert into subject value (null, ?, ?, ?, ?, ?);");
                query.setString(1, subName);
                query.setString(2, idTeacher);
                query.setString(3, firstDate);
                query.setString(4, lastDate);
                query.setString(5, idGroup);
                query.execute();
                /**
                 * 
                 */
                query = con.prepareStatement("select * from subject "
                        + "where Subject_Name = ? and teacher_idTeacher = ? "
                        + "and startDate = ? and endDate = ? "
                        + "and group_idGroup = ?;");
                query.setString(1, subName);
                query.setString(2, idTeacher);
                query.setString(3, firstDate);
                query.setString(4, lastDate);
                query.setString(5, idGroup);
                Boolean exe = query.execute();
                if (exe) {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно добавлена!");
                    FacesContext.getCurrentInstance().addMessage("Тест", message);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Проверьте выбранные данные!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SubjectsBean.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    public void getAllSubStudents() {
        if(sub!=null){
        sub.clear();
        }
        sub = new ArrayList<Subjects>();
        
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("student"); //получаем из сессии id студента!
        int idGroup;
        if (!param.equalsIgnoreCase("0")) {
            try {
                Connect connect = new Connect();
                Connection con = connect.getCon();
                PreparedStatement query = con.prepareStatement("select group_idGroup from student where idStudent = ?");
                query.setString(1, param);
                ResultSet result = query.executeQuery();
                if (result.next()) {
                    idGroup = result.getInt("group_idGroup");
                    query = con.prepareStatement("select idSubject, Subject_Name from subject where group_idGroup = ? GROUP BY Subject_Name;");
                    query.setInt(1, idGroup);
                    result = query.executeQuery();
                    while (result.next()) {
                        sub.add(new Subjects(result.getString("Subject_Name"),
                                result.getString("idSubject")));
                    }
                } else {
                    System.err.println("Не наден параметр group_idGroup, getAllSubStudents()");
                }
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SubjectsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        subModel = new SubDataModel(sub);
    }
}
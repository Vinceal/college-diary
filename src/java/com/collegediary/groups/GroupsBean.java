package com.collegediary.groups;

import com.collegediary.groups.allcolumn.GroupAllColumn;
import com.collegediary.groups.allcolumn.GroupDataModelac;
import com.collegediary.main.Connect;
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
 *
 * @author VinceAL
 */
@ManagedBean(name = "groupsBean")
@SessionScoped
public class GroupsBean implements Serializable {

    public ArrayList<Groups> grplist; // arrayList с объектами класса Groups
    public ArrayList<GroupAllColumn> grplistallcolm;
    public Groups selectedgrp; //строка которая будет выбираться
    public GroupAllColumn selectedgrp3; //строка которая будет выбираться в админке
    private GroupDataModel grpModel; //модель данных 
    private GroupDataModelac grpModelAllColumn; //модель данных для табл в админке
    public String numbut;
    public ArrayList<String> boolButton;
    public String mask, course, active, strDate, endDate;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getBoolButton() {
        return boolButton;
    }

    public void setBoolButton(ArrayList<String> boolButton) {
        this.boolButton = boolButton;
    }

    public GroupAllColumn getSelectedgrp3() {
        return selectedgrp3;
    }

    public void setSelectedgrp3(GroupAllColumn selectedgrp3) {
        this.selectedgrp3 = selectedgrp3;
    }

    public ArrayList<GroupAllColumn> getGrplistallcolm() {
        return grplistallcolm;
    }

    public void setGrplistallcolm(ArrayList<GroupAllColumn> grplistallcolm) {
        this.grplistallcolm = grplistallcolm;
    }

    public String getNumbut() {
        return numbut;
    }

    public void setNumbut(String numbut) {
        this.numbut = numbut;
    }

    public GroupDataModelac getGrpModelAllColumn() {
        return grpModelAllColumn;
    }

    public void setGrpModelAllColumn(GroupDataModelac grpModelAllColumn) {
        this.grpModelAllColumn = grpModelAllColumn;
    }

    public GroupDataModel getGrpModel() {
        return grpModel;
    }

    //геттер и сеттер строки выбранной строки в таблице
    public Groups getSelectedgrp() {
        return selectedgrp;
    }

    public void setSelectedgrp(Groups selectedgrp) {
        this.selectedgrp = selectedgrp;
    }

    public GroupsBean() throws NamingException, SQLException {

        ListfromDB();
        boolButton = new ArrayList<String>();
        boolButton.add("true");
        boolButton.add("false");
    }

    public final void ListfromDB(String q) throws SQLException, NamingException {
        if (grplist != null) {
            grplist.clear();
        }
        grplist = new ArrayList<Groups>();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("teacher");

        Connect connect = new Connect();
        Connection con = connect.getCon();
        if (!param.equalsIgnoreCase("0")) {
            PreparedStatement query = con.prepareStatement("select idGroup,"
                    + " Name_of_Group, Course from `group`\n"
                    + "INNER JOIN `subject` ON group.idGroup = subject.group_idGroup\n"
                    + "WHERE Subject_Name = '" + q + "' and active = 'true'");
            ResultSet result = query.executeQuery();
            while (result.next()) {
                grplist.add(new Groups(result.getString("Name_of_Group"), result.getString("Course"),
                        result.getString("idGroup")));
            }
        }

        grpModel = new GroupDataModel(grplist);
        connect.con.close();
    }

    public final void ListfromDB() throws SQLException, NamingException {

        if (grplist != null) {
            grplist.clear();
        }
        grplist = new ArrayList<Groups>();

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("teacher");

        Connect connect = new Connect();
        Connection con = connect.getCon();

        if (!param.equalsIgnoreCase("0") && con != null) {
            PreparedStatement query = con.prepareStatement("select idGroup,"
                    + " Name_of_Group, Course FROM `group` where active = 'true'");
            ResultSet result = query.executeQuery();
            while (result.next()) {
                grplist.add(new Groups(result.getString("Name_of_Group"), result.getString("Course"),
                        result.getString("idGroup")));
            }
        } else {
            String stud = (String) session.getAttribute("student");
            if (stud.equalsIgnoreCase("0") && con != null) {
                PreparedStatement query = con.prepareStatement("SELECT * FROM `group`");
                if (this.numbut != null) {
                    if (this.numbut.equalsIgnoreCase("1")) {
                        query = con.prepareStatement("SELECT * FROM `group` where active = 'true'");
                    } else {
                        if (this.numbut.equalsIgnoreCase("2")) {
                            query = con.prepareStatement("SELECT * FROM `group` where active = 'false'");
                        }
                    }
                }
                this.grplistallcolm = new ArrayList<GroupAllColumn>();
                ResultSet result = query.executeQuery();
                while (result.next()) {
                    grplistallcolm.add(new GroupAllColumn(
                            result.getString("idGroup"),
                            result.getString("Name_of_Group"),
                            result.getString("Course"),
                            result.getString("active"),
                            result.getString("startDate"),
                            result.getString("endDate")));
                }
                grpModelAllColumn = new GroupDataModelac(grplistallcolm);
            }
        }

        grpModel = new GroupDataModel(grplist);

        connect.con.close();
    }
    //вызываеться когда преподаватель выбрал группу

    public void onRowSelect() throws NamingException, SQLException {

        //через ELContext обращаемся к scope пространству, откуда получаем нужный бин
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        StudentsBean studentsBean = (StudentsBean) elContext.getELResolver().getValue(elContext, null, "studBean");
        studentsBean.ListfromDB(this.selectedgrp.getIdGroup());

    }
    //вызываеться когда преподаватель во второй вкладке добавляет новый предмет

    public void onSelectOneButton() {
        try {
            this.ListfromDB();
        } catch (SQLException ex) {
            System.err.print("error with SQL exp in onSelectOneButton");
            Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            System.err.print("error with Naming exp in onSelectOneButton");
            Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRow() {
        if (this.selectedgrp3 != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement query = con.prepareStatement("delete from `group` where idGroup = ?");
                query.setString(1, this.selectedgrp3.getIdGroup());
                query.execute();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно удалена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                try {
                    this.ListfromDB();
                } catch (NamingException ex) {
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Внимание!", "Запись удалена с ошибкой!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                    Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                System.err.print("catch the error with delete row in GroupBean " + ex);
            } finally {
                if (connect != null) {
                    try {
                        connect.con.close();
                    } catch (SQLException ex) {
                        System.err.print("Error with closing con deleteRow GroupBean" + ex);
                        Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не возможно удалить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void updateRow() throws SQLException {

        if (this.selectedgrp3 != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();

            PreparedStatement query = con.prepareStatement("update `group` SET"
                    + " Name_of_Group = ?, Course = ?, active = ?, startDate = ?, "
                    + "endDate =? where idGroup = ?;");
            query.setString(1, this.selectedgrp3.getNameOfGroup());
            query.setString(2, this.selectedgrp3.getCourse());
            query.setString(3, this.selectedgrp3.getActive());
            query.setString(4, this.selectedgrp3.getStartDate());
            query.setString(5, this.selectedgrp3.getEndDate());
            query.setString(6, this.selectedgrp3.getIdGroup());
            con.setAutoCommit(false);
            boolean commited = false;
            try {
                query.executeUpdate();
                con.commit();
                commited = true;
            } finally {
                if (!commited) {
                    con.rollback();
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "При изменении произошла ошибка!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успещно изменена!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                con.setAutoCommit(true);
                connect.con.close();
            }

            try {
                this.ListfromDB();
            } catch (NamingException ex) {
                Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не возможно изменить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void addRow() throws SQLException {

        if (this.mask != null && this.course != null
                && this.active != null && this.strDate != null && this.endDate != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query = con.prepareStatement("insert into `group`"
                    + " (Name_of_Group,Course,active,startDate,endDate) values "
                    + "( ?, ?, ?, ?, ?)");
            query.setString(1, this.mask);
            query.setString(2, this.course);
            query.setString(3, this.active);
            query.setString(4, this.strDate);
            query.setString(5, this.endDate);
            query.execute();

            this.mask = null;
            this.course = null;
            this.active = null;
            this.strDate = null;
            this.endDate = null;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно добавлена!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            connect.con.close();
            try {
                this.ListfromDB();
            } catch (NamingException ex) {
                Logger.getLogger(GroupsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Проверьте правильность введённых данных!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }


    }
}

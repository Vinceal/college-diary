package com.collegediary.groups;

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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Vinceal
 */
@ManagedBean(name = "groupsBeanSecond")
@SessionScoped
public class GroupsBeanSecond  implements Serializable{
    
    public Groups selectedgrp;
    public ArrayList<Groups> grplist;
    private GroupDataModel grpModel;

    public Groups getSelectedgrp() {
        return selectedgrp;
    }

    public void setSelectedgrp(Groups selectedgrp) {
        this.selectedgrp = selectedgrp;
    }

    public ArrayList<Groups> getGrplist() {
        return grplist;
    }

    public void setGrplist(ArrayList<Groups> grplist) {
        this.grplist = grplist;
    }

    public GroupDataModel getGrpModel() {
        return grpModel;
    }

    public void setGrpModel(GroupDataModel grpModel) {
        this.grpModel = grpModel;
    }

    
    
    public GroupsBeanSecond() {
        try {
            this.ListfromDB();
        } catch (SQLException ex) {
            Logger.getLogger(GroupsBeanSecond.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(GroupsBeanSecond.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        } 
        grpModel = new GroupDataModel(grplist);
        connect.con.close();
    }
    
    
     public void onRowSelect() throws NamingException, SQLException {
       
        //через ELContext обращаемся к scope пространству, откуда получаем нужный бин
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        StudentsBean studentsBean = (StudentsBean) elContext.getELResolver().getValue(elContext, null, "studBean");
        studentsBean.ListfromDB(this.selectedgrp.getIdGroup());

    }
    
}

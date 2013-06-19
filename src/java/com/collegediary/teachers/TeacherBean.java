package com.collegediary.teachers;

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
 * этот класс позволяет получать список преподавателей из бд
 * добавлять, удалять, изменять информацию о преподавателе, генерировать
 * первоначальный пароль, создавать учётную запись для преподавателя
 * @author Vinceal
 */
@ManagedBean(name = "tchrBean")
@SessionScoped
public class TeacherBean implements Serializable {

    public ArrayList<Teacher> tchr;
    public Teacher selTechr;
    public TeacherDataModel tchrDM;
    public String id, firstName, lastName, patronomic, pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

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

    public Teacher getSelTechr() {
        return selTechr;
    }

    public void setSelTechr(Teacher selTechr) {
        this.selTechr = selTechr;
    }

    public TeacherDataModel getTchrDM() {
        return tchrDM;
    }

    public void setTchrDM(TeacherDataModel tchrDM) {
        this.tchrDM = tchrDM;
    }

    public TeacherBean() throws SQLException {
        listTeacher();
    }

    public final void listTeacher() throws SQLException {

        Connect connect = new Connect();
        Connection con = connect.getCon();
        try {
            PreparedStatement query = con.prepareStatement("SELECT * FROM teacher");
            ResultSet result = query.executeQuery();
            if (result != null) {
                tchr = new ArrayList<Teacher>();
                while (result.next()) {
                    tchr.add(new Teacher(result.getString("idTeacher"),
                            result.getString("Teacher_First_Name"),
                            result.getString("Teacher_Last_Name"),
                            result.getString("Teacher_Patronomic")));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        tchrDM = new TeacherDataModel(tchr);
        connect.con.close();
    }

    public void deleteRow() {

        if (this.selTechr != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("delete from teacher where idTeacher = ?");
                ps.setString(1, this.selTechr.getId());
                ps.execute();
                connect.con.close();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Выполнено", "Запись успешно удалена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                this.listTeacher();
            } catch (SQLException ex) {
                Logger.getLogger(TeacherBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Не возможно удалить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void addRow() {

        if (this.id != null && this.lastName != null && this.firstName != null && this.patronomic != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("insert into teacher"
                        + " (idTeacher, Teacher_Last_Name, Teacher_First_Name, "
                        + "Teacher_Patronomic) VALUES ( ?, ?, ?, ?)");
                ps.setString(1, this.id);
                ps.setString(2, this.lastName);
                ps.setString(3, this.firstName);
                ps.setString(4, this.patronomic);
                ps.execute();
                this.id = null;
                this.lastName = null;
                this.firstName = null;
                this.patronomic = null;
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Выполнено", "Запись успешно добавлена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                connect.con.close();
                this.listTeacher();
            } catch (SQLException ex) {
                Logger.getLogger(TeacherBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Проверьте правильность введённых данных!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void updateRow() {

        if (this.selTechr != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("update teacher"
                        + " set Teacher_Last_Name = ?, Teacher_First_Name = ?, "
                        + "Teacher_Patronomic = ? where idTeacher = ?");
                ps.setString(1, this.selTechr.getLastName());
                ps.setString(2, this.selTechr.getFirstName());
                ps.setString(3, this.selTechr.getPatronomic());
                ps.setString(4, this.selTechr.getId());
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
                this.listTeacher();
            } catch (SQLException ex) {
                Logger.getLogger(TeacherBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ошибка", "Не возможно изменить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void genPass() {

        if (this.selTechr != null) {
            int x;
            this.pass = "";
            for (int i = 0; i < 9; i++) {
                x = (int) Math.round(Math.random() * 10);
                this.pass = this.pass + Integer.toString(x);
            }
        }

    }

    public void createAccTchr() {

        if (this.selTechr != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("select idUsers from users where teacher_idTeacher = ?");
                ps.setString(1, this.selTechr.getId());
                ResultSet result = ps.executeQuery();
                if (!result.next()) {
                    ps = con.prepareStatement("insert into users"
                            + " (Login, Password, teacher_idTeacher, "
                            + "student_idStudent) values (?,?,?,?)");
                    ps.setString(1, this.selTechr.getId());
                    ps.setString(2, this.pass);
                    ps.setString(3, this.selTechr.getId());
                    ps.setString(4, "0");
                    ps.execute();
                    this.pass = "";
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Учётная запись успешно создана!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Такая учётная запись уже есть!");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TeacherBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

package com.collegediary.stud;

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
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * этот класс получает из бд списки учащихся
 * методы для создания учётной записи для учащихся
 * добавление удаление изменение инф о учащемся
 * @author Vinceal
 */
@ManagedBean(name = "studBean")
@SessionScoped
public class StudentsBean implements Serializable {

    public ArrayList<Students> stud;
    public ArrayList<StudentsAll> studAll;
    public Students selectedStud;
    public StudentsAll selectedStudAll;
    private StudDataModel studModel;
    private StudDataModelAll studModelAll;
    public String idStudent, lastName, firstName, patronomic, idGroup, numberRecordBook;
    public String pass;

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

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

    public StudentsAll getSelectedStudAll() {
        return selectedStudAll;
    }

    public void setSelectedStudAll(StudentsAll selectedStudAll) {
        this.selectedStudAll = selectedStudAll;
    }

    public ArrayList<StudentsAll> getStudAll() {
        return studAll;
    }

    public void setStudAll(ArrayList<StudentsAll> studAll) {
        this.studAll = studAll;
    }

    public StudDataModelAll getStudModelAll() {
        return studModelAll;
    }

    public void setStudModelAll(StudDataModelAll studModelAll) {
        this.studModelAll = studModelAll;
    }

    public StudDataModel getStudModel() {
        return studModel;
    }

    public Students getSelectedStud() {
        return selectedStud;
    }

    public void setSelectedStud(Students selectedStud) {
        this.selectedStud = selectedStud;
    }

    public StudentsBean() throws SQLException, NamingException {
        ListfromDB();
        setupListStud();
    }

    public final void ListfromDB() throws SQLException, NamingException {
        if (stud != null) {
            stud.clear();
        }
        stud = new ArrayList<Students>();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("teacher");
        if (!param.equalsIgnoreCase("0")) {

            Connect connect = new Connect();
            Connection con = connect.getCon();

            PreparedStatement query = con.prepareStatement("SELECT idStudent, "
                    + "Last_Name, First_Name, Patronomic,"
                    + " Number_recordBook FROM student");
            ResultSet result = query.executeQuery();
            while (result.next()) {
                stud.add(new Students(result.getInt("idStudent"),
                        result.getString("Last_Name"),
                        result.getString("First_Name"),
                        result.getString("Patronomic"),
                        result.getString("Number_recordBook")));
            }
            connect.con.close();
        }
        studModel = new StudDataModel(stud);
    }

    public final void ListfromDB(String quest) throws SQLException, NamingException {
        if (stud != null) {
            stud.clear();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String param = (String) session.getAttribute("teacher");
        if (!param.equalsIgnoreCase("0")) {
            Connect connect = new Connect();
            Connection con = connect.getCon();

            PreparedStatement query = con.prepareStatement("SELECT idStudent, Last_Name,"
                    + " First_Name, Patronomic,"
                    + " Number_recordBook FROM student "
                    + "where group_idGroup =?");
            query.setString(1, quest);
            ResultSet result = query.executeQuery();
            while (result.next()) {
                stud.add(new Students(result.getInt("idStudent"),
                        result.getString("Last_Name"),
                        result.getString("First_Name"),
                        result.getString("Patronomic"),
                        result.getString("Number_recordBook")));
            }
            connect.con.close();
        }
        studModel = new StudDataModel(stud);
    }

    public void onRowSelect() throws NamingException, SQLException {
        this.setSelectedStud(selectedStud);
    }

    public final void setupListStud() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String student = (String) session.getAttribute("student");
        String teacher = (String) session.getAttribute("teacher");
        if (teacher.equalsIgnoreCase("0") && student.equalsIgnoreCase("0")) {
            try {
                Connect connect = new Connect();
                Connection con = connect.getCon();
                PreparedStatement query = con.prepareStatement("SELECT * FROM student");
                this.studAll = new ArrayList<StudentsAll>();
                ResultSet result = query.executeQuery();
                while (result.next()) {
                    studAll.add(new StudentsAll(
                            result.getString("idStudent"),
                            result.getString("Last_Name"),
                            result.getString("First_Name"),
                            result.getString("Patronomic"),
                            result.getString("group_idGroup"),
                            result.getString("Number_recordBook")));
                }
                connect.con.close();
                studModelAll = new StudDataModelAll(studAll);
            } catch (SQLException ex) {
                Logger.getLogger(StudentsBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void deleteRow() throws NamingException {

        if (this.selectedStudAll != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("delete from student where idStudent = ?");
                ps.setString(1, this.selectedStudAll.getIdStudent());
                ps.execute();
                connect.con.close();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно удалена!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                this.setupListStud();
            } catch (SQLException ex) {
                Logger.getLogger(StudentsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не возможно удалить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void updateRow() {

        if (this.selectedStudAll != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("update student "
                        + "set Last_Name = ? , First_Name = ? , Patronomic = ? , "
                        + "group_idGroup = ? , Number_recordBook = ? where idStudent = ?");
                ps.setString(1, this.selectedStudAll.getLastName());
                ps.setString(2, this.selectedStudAll.getFirstName());
                ps.setString(3, this.selectedStudAll.getPatronomic());
                ps.setString(4, this.selectedStudAll.getIdGroup());
                ps.setString(5, this.selectedStudAll.getNumberRecordBook());
                ps.setString(6, this.selectedStudAll.getIdStudent());
                con.setAutoCommit(false);
                boolean commited = false;
                try {
                    ps.executeUpdate();
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
                this.setupListStud();
            } catch (SQLException ex) {
                Logger.getLogger(StudentsBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не возможно изменить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void addRow() throws SQLException {


        if (this.lastName != null && this.firstName != null && this.patronomic != null
                && this.idGroup != null && this.numberRecordBook != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query = con.prepareStatement("insert into "
                    + "student (Last_Name, First_Name, Patronomic, "
                    + "group_idGroup, Number_recordBook) values (?, ?, ?, ?, ?)");
            query.setString(1, this.lastName);
            query.setString(2, this.firstName);
            query.setString(3, this.patronomic);
            query.setString(4, this.idGroup);
            query.setString(5, this.numberRecordBook);
            query.execute();

            this.lastName = null;
            this.firstName = null;
            this.patronomic = null;
            this.idGroup = null;
            this.numberRecordBook = null;
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно добавлена!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            connect.con.close();
            this.setupListStud();
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Проверьте правильность введённых данных!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void genPass() {

        if (this.selectedStudAll != null) {
            int x;
            this.pass = "";
            for (int i = 0; i < 9; i++) {
                x = (int) Math.round(Math.random() * 10);
                this.pass = this.pass + Integer.toString(x);
            }
        }

    }
    public void createAccStud(){
    
        if (this.selectedStudAll != null) {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("select idUsers from users where student_idStudent = ?");
                ps.setString(1, this.selectedStudAll.idStudent);
                ResultSet result = ps.executeQuery();
                if(!result.next())
                {
                ps = con.prepareStatement("insert into users"
                        + " (Login, Password, teacher_idTeacher, "
                        + "student_idStudent) values (?,?,?,?)");
                ps.setString(1, this.selectedStudAll.numberRecordBook);
                ps.setString(2, this.pass);
                ps.setString(3, "0");
                ps.setString(4, this.selectedStudAll.idStudent);
                ps.execute();
                this.pass = "";
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Учётная запись успешно создана!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                }else
                {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Такая учётная запись уже есть!");
                FacesContext.getCurrentInstance().addMessage(null, message);   
                }
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(StudentsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

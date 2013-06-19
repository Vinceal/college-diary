package com.collegediary.main;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.event.SelectEvent;

/**
 * Этот класс позволяет пройти авторизацию на сервере mysql, проверяет введнные
 * данные авторизации и перенаправляет на нужную страницу
 *
 * @author VinceAL
 */
@ManagedBean(name = "mainBean")
@SessionScoped
public class Main implements Serializable {
    //переменные для календаря selDate, firstDateS, lastDateS
    //logo это то что выводиться в шапке сайта
    public String teacher, lessonType, idLessonType, student, logo, rating, selDate, firstDateS, lastDateS;
    private String userlogin, password; //логин и пароль
    private boolean loggedIn;
    private static final Logger logger = Logger.getLogger("com.collegediary.main");
    private ArrayList<String> listLessonType;
    public Date dateForCalendar, firstDate, lastDate;
    public String login, oldpass, newpass, confirm;
    
    //геттеры и сеттеры
    
    public String getOldpass() {
        return oldpass;
    }

    public void setOldpass(String oldpass) {
        this.oldpass = oldpass;
    }

    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getIdLessonType() {
        return idLessonType;
    }

    public void setIdLessonType(String idLessonType) {
        this.idLessonType = idLessonType;
    }

    public String getFirstDateS() {
        return firstDateS;
    }

    public String getLastDateS() {
        return lastDateS;
    }

    public void handleSelectFristDate(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        firstDate = (Date) event.getObject();
        firstDateS = format.format((Date) event.getObject());
    }

    public void handleSelectLasttDate(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lastDate = (Date) event.getObject();
        lastDateS = format.format((Date) event.getObject());
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public String getSelDate() {
        return selDate;
    }

    public Date getDateForCalendar() {
        return dateForCalendar;
    }

    public String getLogo() {
        return logo;
    }

    public ArrayList<String> getListLessonType() {
        return listLessonType;
    }

    public String getRating() {
        return rating;
    }

    public String getUserlogin() {
        return userlogin;
    }

    public String getPassword() {
        return password;
    }

    //календарь
    public void setDateForCalendar(Date date) {
        this.dateForCalendar = date;
    }

    public void handleDateSelect(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateForCalendar = (Date) event.getObject();
        selDate = format.format((Date) event.getObject());
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setListLessonType(ArrayList<String> listLessonType) {
        this.listLessonType = listLessonType;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setUserlogin(String userlogin) {
        this.userlogin = userlogin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Main() {
    this.rating = "н/б";
    }

    public String login() throws NamingException, IOException {
        try {
            doLogin();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "проблема авторизации", ex);
            return "error";// возвращаем строку, используем навигацию
        }
        if (loggedIn) {
            /**
             * объявляем контекст и помещаем в через него в сессию имя
             * пользователя через контекст получаем текущую сессию обращаемся к
             * сессии и помещаем логин пользователя!
             */
            if (teacher.equalsIgnoreCase("0")) {
                if (student.equalsIgnoreCase("0")) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
                    HttpSession session = request.getSession(true);// открываем новый сеанс сессии
                    session.setAttribute("teacher", teacher);
                    session.setAttribute("student", student);
                    this.userlogin = null;
                    this.password = null;
                    setupListLessonsType();
                    return "admin";
                }
                if (student != null) {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
                    HttpSession session = request.getSession(true);// открываем новый сеанс сессии
                    if (session != null) {
                        session.setAttribute("teacher", teacher);
                        session.setAttribute("student", student);
                        this.userlogin = null;
                        this.password = null;
                        return "user";
                    }
                }
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
                HttpSession session = request.getSession(true);// открываем новый сеанс сессии
                if (session != null) {

                    session.setAttribute("teacher", teacher);
                    session.setAttribute("student", student);
                    setupListLessonsType();
                    this.userlogin = null;
                    this.password = null;
                    return "welcome";
                }
            }
        }

        return "sorry";
    }

    public void doLogin() throws SQLException, NamingException {

        //получили соединение с базой данных и пытаемся 
        //получить логин и пароль введённый пользователем
        Connect connect = new Connect();
        Connection con = connect.getCon();
        PreparedStatement passQuery = con.prepareStatement("select password,"
                + " teacher_idTeacher, student_idStudent from users "
                + "where login=? and password=?");
        passQuery.setString(1, userlogin); //получаем строки логин и пароль с базы данных
        passQuery.setString(2, password);
        ResultSet result = passQuery.executeQuery();
        if (!result.next()) {
            return;
            /*проверяем, есть ли такие записи в базе данных, 
             если они отсутствуют то выполняем возврат из блока*/
        }
        teacher = null;
        /*повторно проверяем введённый пароль и присваеваем переменной
         * значение авторизации. true|false
         */
        String storedPass = result.getString("password");
        loggedIn = password.equals(storedPass.trim());
        /*
         * если данные введённые пользователем присутсвуют - то продол-
         * жаем получаем информацию о пользователе, узнаем, это студент
         * или преподаватель и присваиваем в нужную переменную их id
         */
        teacher = result.getString("teacher_idTeacher");
        student = result.getString("student_idStudent");

        if (!teacher.equalsIgnoreCase("0")) {
            PreparedStatement query = con.prepareStatement("SELECT Teacher_First_Name,"
                    + " Teacher_Last_Name, Teacher_Patronomic FROM "
                    + "teacher where idTeacher=?");
            query.setString(1, teacher);
            ResultSet teacherFIO = query.executeQuery();
            if (teacherFIO.next()) {
                logo = teacherFIO.getString("Teacher_Last_Name");
                logo += " " + teacherFIO.getString("Teacher_First_Name");
                logo += " " + teacherFIO.getString("Teacher_Patronomic");
            }
        } else {
            if (!student.equalsIgnoreCase("0")) {
                PreparedStatement query = con.prepareStatement("select Last_Name, "
                        + "First_Name, Patronomic from student where "
                        + "idStudent = ?");
                query.setString(1, student);
                ResultSet studentFIO = query.executeQuery();
                if (studentFIO.next()) {
                    logo = studentFIO.getString("Last_Name");
                    logo += " " + studentFIO.getString("First_Name");
                    logo += " " + studentFIO.getString("Patronomic");
                }
            }

        }
        connect.con.close();

    }

    public void setupIdLessonType() {
        try {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query;
            query = con.prepareStatement("select idLessons from lessons where Lesson_Type =?");
            query.setString(1, this.lessonType);
            String q = query.toString();
            ResultSet result = query.executeQuery();
            result.first();
            idLessonType = result.getString("idLessons");
            connect.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void setupListLessonsType() {

        try {
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query = con.prepareStatement("select idLessons, Lesson_Type from lessons;");
            ResultSet result = query.executeQuery();
            listLessonType = new ArrayList<String>();
            if (result.next()) {
                if (result.isFirst()) {
                    this.lessonType = result.getString("Lesson_Type");
                    this.idLessonType = result.getString("idLessons");
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

    public String logout() throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);// открываем текущий сеанс сессии
        try {
            if (session != null) {
                session.removeAttribute("student");
                session.removeAttribute("teacher");//очищаем переманные в сессии 
                fc.getExternalContext().invalidateSession();//удаляем активную сессию


            }
        } catch (Exception e) {
            System.out.print("Error in logout()" + e);
        }
        fc.responseComplete();
        return "index.xhtml?faces-redirect=true";//перенаправляем пользователя на главную стр.
    }

    public void changeLogPass() {

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String t = (String) session.getAttribute("teacher");
        String s = (String) session.getAttribute("student");

        if (this.login != null && this.oldpass != null && this.newpass != null && this.confirm != null) {
            if (this.newpass.equals(this.confirm)) {
                Connect connect = new Connect();
                Connection con = connect.getCon();
                try {
                    PreparedStatement ps = null;
                    if (t != null && !t.equalsIgnoreCase("0")) {
                        ps = con.prepareStatement("select idUsers "
                                + "from users where teacher_idTeacher = ? and Password = ?");
                        ps.setString(1, t);
                        ps.setString(2, this.oldpass);
                    }
                    if (s != null && !s.equalsIgnoreCase("0")) {
                        ps = con.prepareStatement("select idUsers "
                                + "from users where student_idStudent = ? and Password = ?");
                        ps.setString(1, s);
                        ps.setString(2, this.oldpass);
                    }
                    
                    if (t.equalsIgnoreCase("0") && s.equalsIgnoreCase("0")){
                         ps = con.prepareStatement("select idUsers "
                                + "from users where teacher_idTeacher = 0 and "
                                 + "student_idStudent = 0 and Password = ?");
                        ps.setString(1, this.oldpass);
                    
                    }
                    ResultSet result = ps.executeQuery();
                    if (result.next()) {
                        result.first();
                        String idUser = result.getString("idUsers");
                        result.beforeFirst();
                        ps = con.prepareStatement("update users "
                                + "set Login = ? , Password = ? where idUsers = ?");
                        ps.setString(1, this.login);
                        ps.setString(2, this.newpass);
                        ps.setString(3, idUser);
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
                                        "Выполнено", "Данные успешно изменены!");
                                FacesContext.getCurrentInstance().addMessage(null, message);
                            }
                            con.setAutoCommit(true);
                            connect.con.close();
                            this.login = null;
                            this.oldpass = null;
                            this.newpass = null;
                            this.confirm = null;
                        }

                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка",
                                "Не правильно введён текущий пароль!");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.login = null;
                this.oldpass = null;
                this.newpass = null;
                this.confirm = null;
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка",
                        "Введённые пароли не совпадают!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }


    }
}
package com.collegediary.main;

import com.collegediary.buffer.BufferBean;
import com.collegediary.groups.GroupsBean;
import com.collegediary.stud.StudentsBean;
import com.collegediary.subjects.SubjectsBean;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * этот класс позволяет добовлять информацию (оценки и т.д) в базу данных
 *
 * @author Vinceal
 */
@ManagedBean(name = "action")
@SessionScoped
public class Actions implements Serializable {

    /**
     * эта функция добавляет данные о новом занятии, если его нету, а если есть
     * то возвращает его id.
     */
    public Integer createNewCouple() {
        Integer idCouple = null;
        String lesson, idLesson, idSubject, date, nameSub;
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        SubjectsBean subjectsBean = (SubjectsBean) elContext.getELResolver().getValue(elContext, null, "subjectsBean");
        Main mainBean = (Main) elContext.getELResolver().getValue(elContext, null, "mainBean");
        idSubject = subjectsBean.selectedSub.getIdSubject();
        nameSub = subjectsBean.selectedSub.getSubjects();
        lesson = mainBean.lessonType;
        date = mainBean.selDate;
        //получаем все необходимые данные из бинов и проверяем их на наличие
        if (date != null & lesson != null & idSubject != null) {
            //получаем соединение с бд
            Connect connect = new Connect();
            Connection con = connect.getCon();
            //получаем из базы данных id типа занятия
            PreparedStatement query;
            try {
                query = con.prepareStatement("select idLessons from lessons where Lesson_Type =?");
                query.setString(1, lesson);
                ResultSet result = query.executeQuery();
                result.first();
                idLesson = result.getString("idLessons"); //id типа занятия!
                result.close();
                query.close();
                //если занятие за ранее было создано, то получаем его id
                query = con.prepareStatement("select idCouples from couples "
                        + "where subject_idSubject =? and lessons_idLessons = ?"
                        + " and coupleDate =? ;");
                query.setString(1, idSubject);
                query.setString(2, idLesson);
                query.setString(3, date);
                result = query.executeQuery();
                // если занятие ранее не было создано, то создаём новое
                if (result.next()) {
                    idCouple = result.getInt("idCouples");
                    return idCouple;
                } else {
                    //sql запрос на добавление и указываем параметры
                    query = con.prepareStatement("insert into couples values (null, ?, ?, ?);");
                    query.setString(1, idSubject);
                    query.setString(2, idLesson);
                    query.setString(3, date);
                    query.execute();
                    //получаем id пары, котора была создана
                    query = con.prepareStatement("select idCouples from couples "
                            + "where subject_idSubject =? and lessons_idLessons = ?"
                            + " and coupleDate =? ;");
                    query.setString(1, idSubject);
                    query.setString(2, idLesson);
                    query.setString(3, date);
                    result = query.executeQuery();
                    result.first();
                    String idc = result.getString("idCouples");
                    idCouple = result.getInt("idCouples");
                    //получаем контекст и проверяем на наличие в сессии атрибота пользователя
                    FacesContext fc = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
                    HttpSession session = request.getSession(false);
                    String idTeacher = (String) session.getAttribute("teacher");
                    //создаём новую записть в таблице buffer
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String curTime = format.format((Date) java.util.Calendar.getInstance().getTime());
                    String operationSTR = "Новая пара - " + nameSub + " " + lesson + " " + date;
                    String delSTR = "delete from couples where idcouples = " + idc;
                    query = con.prepareStatement("insert into buffer values(null, ?, ?, ?, ?);");
                    query.setString(1, operationSTR);
                    query.setString(2, delSTR);
                    query.setString(3, idTeacher);
                    query.setString(4, curTime);
                    query.execute();
                }
                /**
                 *
                 */
            } catch (SQLException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return idCouple;
    }

    /**
     * функция добавления новой оценки выбранному студенту
     */
    public void addNewRating() {
        //получаем текущий контекст и с помощью него обращаемся к другим бинам
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        StudentsBean studBean = (StudentsBean) elContext.getELResolver().getValue(elContext, null, "studBean");
        Main mainBean = (Main) elContext.getELResolver().getValue(elContext, null, "mainBean");
        BufferBean bufferBean = (BufferBean) elContext.getELResolver().getValue(elContext, null, "bufferBean");
        SubjectsBean subjectsBean = (SubjectsBean) elContext.getELResolver().getValue(elContext, null, "subjectsBean");
        GroupsBean groupsBean = (GroupsBean) elContext.getELResolver().getValue(elContext, null, "groupsBean");
        Integer idStud = null;
        //получаем id выбранной записи в таблице
        if (studBean.selectedStud != null) {
            idStud = studBean.selectedStud.getIdStudent();
        }
        String rating = mainBean.rating;
        String nameSub = null;
        //nameSub название предмета, по которому выставляется оценка
        if (subjectsBean.selectedSub != null) {
            nameSub = subjectsBean.selectedSub.getSubjects();
        }
        if (idStud != null && rating != null && mainBean.dateForCalendar != null) {
            Integer idcouple = createNewCouple();
            Connect connect = new Connect();
            Connection con = connect.getCon();
            PreparedStatement query;
            // sql Запрос на добавление данных
            try {
                query = con.prepareStatement("insert into main_journal values (null, ?, ?, ?)");
                query.setInt(1, idStud);
                query.setInt(2, idcouple);
                query.setString(3, rating);
                query.execute();
                //получаем id типа занятия
                query = con.prepareStatement("select Lesson_Type, coupleDate from couples \n"
                        + "inner join lessons\n"
                        + "where lessons_idLessons = idLessons and idcouples = ?");
                query.setInt(1, idcouple);
                ResultSet res = query.executeQuery();
                String lessonType = null, coupleDate = null;
                if (res.next()) {
                    lessonType = res.getString("Lesson_Type");
                    coupleDate = res.getString("coupleDate");
                }
                //получаем все данные о добавленной записи
                query = con.prepareStatement("select * from main_journal"
                        + " where student_idStudent =? and couples_idcouples = ?"
                        + " and Rating = ?");
                query.setInt(1, idStud);
                query.setInt(2, idcouple);
                query.setString(3, rating);
                res = query.executeQuery();
                // если все переменные не равны null, то записываем в таблицу bufferS
                if (res.next() & lessonType != null & coupleDate != null) {
                    String idRecord = res.getString("idRecord");
                    FacesContext fc = FacesContext.getCurrentInstance();
                    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
                    HttpSession session = request.getSession(false);
                    String idTeacher = (String) session.getAttribute("teacher");
                    String fioStud = studBean.selectedStud.getFio();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String curTime = format.format((Date) java.util.Calendar.getInstance().getTime());
                    String operationSTR = "Выставлена оценка " + rating + " - " + fioStud + " " + nameSub + " " + lessonType + " " + coupleDate;
                    String delSTR = "delete from main_journal where idRecord = " + idRecord;
                    query = con.prepareStatement("insert into buffer values(null, ?, ?, ?, ?);");
                    query.setString(1, operationSTR);
                    query.setString(2, delSTR);
                    query.setString(3, idTeacher);
                    query.setString(4, curTime);
                    query.execute();
                    bufferBean.getAllOperation();

                }
                //в случае если запись удачно добавлена, то выводим сообщение
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно добавлена!");
                FacesContext.getCurrentInstance().addMessage("Тест", message);
                connect.con.close();
            } catch (SQLException ex) {
                Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            //если что-то было из данных не выбрано, то выводим что именно
            if (subjectsBean.selectedSub == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не выбран предмет!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (groupsBean.selectedgrp == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не выбрана группа!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (studBean.selectedStud == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не выбран студент!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (mainBean.dateForCalendar == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не выбрана дата!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
            if (mainBean.rating == null) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Не выбрана оценка!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
        
    }
    /*
     * эта функция позволяет удалить запись которая выбрана в таблице
     */
    
    
    public void deleteSelectedRow() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        BufferBean bufferBean = (BufferBean) elContext.getELResolver().getValue(elContext, null, "bufferBean");
        Main mainBean = (Main) elContext.getELResolver().getValue(elContext, null, "mainBean");
        if (bufferBean.selectedOper.operation == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Внимание", "Нельзя удалить не выбранные данные!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        Connect connect = new Connect();
        Connection con = connect.getCon();
        try {
            // получаем sql скрип из бд и с помощью него удаляем данные
            String queryForDelete = bufferBean.selectedOper.sqlscript;
            PreparedStatement query = con.prepareStatement(queryForDelete);
            try {
                query.execute();
            } catch (SQLException ex) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка", "Невозможно удалить!");
                FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Внимание", "Нельзя удалить пару, если по ней стоит хоть одна оценка");
                FacesContext.getCurrentInstance().addMessage(null, message);
                FacesContext.getCurrentInstance().addMessage(null, message2);
                return;
            }
            //удаляем информацию о записи
            int selectedOperation = bufferBean.selectedOper.getIdOperation();
            query = con.prepareStatement("delete from buffer where idBuffer = ?");
            query.setInt(1, selectedOperation);
            query.execute();
            //оповещаем пользовать о проделанных действиях
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Выполнено", "Запись успешно удалена!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            connect.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
        }
        // по окончаю вызываем функцию, с помощью которой созданёться новы arryList
        // с удалёнными данными
        bufferBean.getAllOperation();
    }
}
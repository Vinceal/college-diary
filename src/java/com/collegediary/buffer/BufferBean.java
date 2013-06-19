package com.collegediary.buffer;

import com.collegediary.main.Connect;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * В этом классе мы получаем данные из БД
 * для представления данных полученых из БД мы создаём
 * объект класса DataModel который в xhtml сранице указываем для таблицы
 * @author Vinceal
 */
@ManagedBean(name = "bufferBean")
@SessionScoped
public class BufferBean implements Serializable {
    /*
     *Buffer selectedOper - объект клааса Buffer содержащий запись из БД
     * необходимый для хранения выбранной строки в таблице операции
     * BufferDataModel buffModel модель данных
     * ArrayList<Buffer> listBuffer лист объектов в которых храняться данные
     */
    public ArrayList<Buffer> listBuffer;
    public BufferDataModel buffModel;
    public Buffer selectedOper;

    public ArrayList<Buffer> getListBuffer() {
        return listBuffer;
    }

    public void setListBuffer(ArrayList<Buffer> listBuffer) {
        this.listBuffer = listBuffer;
    }

    public BufferDataModel getBuffModel() {
        return buffModel;
    }

    public void setBuffModel(BufferDataModel buffModel) {
        this.buffModel = buffModel;
    }

    public Buffer getSelectedOper() {
        return selectedOper;
    }

    public void setSelectedOper(Buffer selectedOper) {
        this.selectedOper = selectedOper;
    }
    //конструктор, вызываем фунцию getAllOperation()
    public BufferBean() {
        getAllOperation();
    }

    public final void getAllOperation() {
        //создаём объект класса Connect в котором подключаемся к БД
        Connect connect = new Connect();
        Connection con = connect.getCon();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        String idTeacher = (String) session.getAttribute("teacher");
        try {

            PreparedStatement query = con.prepareStatement("SELECT * FROM buffer where teacher_idTeacher = ?");
            query.setString(1, idTeacher);
            ResultSet res = query.executeQuery();
            listBuffer = new ArrayList<Buffer>();
            while (res.next()) {
                listBuffer.add(new Buffer(res.getInt("idBuffer"),
                        res.getString("operation"), res.getString("sqlscript"), res.getString("datetime")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BufferBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        buffModel = new BufferDataModel(listBuffer);
        try {
            connect.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BufferBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

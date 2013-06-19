package com.collegediary.buffer;

/**
 * Данный класс содержит поля необходимые для таблицы операции
 * поля: операции, sql-скрипт на удаление, время, id операции
 * @author Vinceal
 */
public class Buffer {
    public String operation, sqlscript, time;
    public int idOperation;
    //get-ы и set-ы полей
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSqlscript() {
        return sqlscript;
    }

    public void setSqlscript(String sqlscript) {
        this.sqlscript = sqlscript;
    }

    public int getIdOperation() {
        return idOperation;
    }

    public void setIdOperation(int idOperation) {
        this.idOperation = idOperation;
    }
    /*конструктор в котором получаем все поля, целью являеться
    * создать объект в котором хранилась информация о записи в бд
    */
    Buffer (int id, String oper, String sql, String time){
        this.idOperation = id;
        this.operation = oper;
        this.sqlscript = sql;
        this.time = time;
    }
}

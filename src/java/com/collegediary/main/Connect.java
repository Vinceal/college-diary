package com.collegediary.main;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * класс, позволяет получать соединение с БД
 * @author Vinceal
 */
public final class Connect implements Serializable{
   private DataSource  ds;
   public  Connection con;

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    /* полчаем контекст, из контекста получаем источник данных (ds) 
     * а от испточнка данных получаем соединение
     */
    public  Connect() {
        try {
            /*   try {
                       Context ctx = new InitialContext();
                       ds = (DataSource) ctx.lookup("java:comp/env/jdbc/db0_431");
                       ctx.close();
                       try {*/
                          con = DriverManager.getConnection("jdbc:mysql://localhost/db0_431?autoReconnect=true&useUnicode=true&characterEncoding=utf8", "db_431", "hd2Gb4t#Ks");
                        /*   con =  ds.getConnection(); 
                       } catch (SQLException ex) {
                           Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   } catch (NamingException ex) {
                       Logger.getLogger(Actions.class.getName()).log(Level.SEVERE, null, ex);
                   }*/
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
        
}

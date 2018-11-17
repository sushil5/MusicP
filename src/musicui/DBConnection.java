/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicui;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author SUSHIL
 */
public class DBConnection {
     private static Connection con=null;
    static{
        try {
            System.out.println("creating connection");
            Class.forName("oracle.jdbc.driver.OracleDriver");  

            con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","tiger");
            System.out.println("connection created");
        } catch (Exception ex) {

             ex.printStackTrace();
        }
    }
    public static Connection getConnection(){
        System.out.println("connection returned");
    return con;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicui;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author SUSHIL
 */
public class Login {

    /**
     *
     * @author SUSHIL
     */
    
    public static boolean login(Connection con,String username, String password) {

        try {
            PreparedStatement ps = con.prepareStatement("select * from login where username=? and password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login Success");
                return true;

            } else {
                System.out.println("Something went wrong");
                return false;
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
return false;
    }

}

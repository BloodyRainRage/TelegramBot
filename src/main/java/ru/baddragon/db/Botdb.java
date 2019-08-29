package ru.baddragon.db;

import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.*;
import java.util.Properties;

public class Botdb {

    private Connection con = null;

    public Botdb(Properties properties) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + properties.getProperty("db.dbname") +"?" +
                            "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    properties.getProperty("db.username"), properties.getProperty("db.password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public String registerUser(User user) {
        try {
            String regString = "insert into user ( nUser, strUsername, strFname, strLname) values (?, ?, ?, ?);";
            PreparedStatement st = con.prepareStatement(regString);
            st.setInt(1, user.getId());
            st.setString(2, user.getUserName());
            st.setString(3, user.getFirstName());
            st.setString(4, user.getLastName());
            int row = st.executeUpdate();
            st.close();
            con.close();
            System.out.println("Affected " + row + " rows");
        } catch (SQLException e) {
            return e.toString();
        }
        return "ok";
    }

    public String deleteUser(User user) {
        try {
            String delString = "delete from user where nUser = ? ;";
            PreparedStatement st = con.prepareStatement(delString);
            st.setLong(1, user.getId());
            int row = st.executeUpdate();
            st.close();
            con.close();
            System.out.println("Affected " + row + " rows");
        } catch (SQLException e) {
            return e.toString();
        }
        return "ok";
    }

    public String addCity(String city, User user) {
        try {
            String addString = "update user set strCity= ? where nUser = ? ;";
            PreparedStatement st = con.prepareStatement(addString);
            st.setString(1, city);
            st.setLong(2, user.getId());
            int row = st.executeUpdate();
            st.close();
            con.close();
            System.out.println("Affected " + row + " rows");
        } catch (SQLException e) {
            return e.toString();
        }
        return "ok";
    }

    public boolean isUserExist(User user) {
        ResultSet rs = null;
        try {
            String checkString = "select nUser from user where nUser = ? ";
            PreparedStatement st = con.prepareStatement(checkString);
            st.setLong(1, user.getId());
            rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public String getWeather(User user) {
        ResultSet rs = null;
        try {
            String weatherString = "select strCity from user where nUser = ? ;";
            PreparedStatement st = con.prepareStatement(weatherString);
            st.setLong(1, user.getId());
            rs = st.executeQuery();
            Boolean flag = rs.next();
            if (!flag) {
                return "NF";
            } else if (rs.getString("strCity").equals("null")) {
                return "null";
            } else {
                return rs.getString("strCity");
            }
        } catch (SQLException e) {
            return "SQLE";
        } finally {
            try {
                rs.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.User;
import helper.SecurityHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "user";
    public static UserModel INSTANCE = new UserModel();

    public List<User> getAllUsers() {
        List<User> resultListUsers = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListUsers;
            }
            String sql = "SELECT * FROM `" + NAMETABLE + "`";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setLevel(rs.getString("level"));

                long currentTimeMillis = rs.getLong("create_date") * 1000;
                Date date = new Date(currentTimeMillis);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String dateString = sdf.format(date);
                user.setCreate_date(dateString);

                resultListUsers.add(user);
            }

            return resultListUsers;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListUsers;
    }

    public User getUserByID(int id) {
        User result = new User();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setUsername(rs.getString("username"));
                result.setPassword(rs.getString("password"));
                result.setLevel(rs.getString("level"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int addUser(String username, String password, String level) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            password = SecurityHelper.getMD5Hash(password);
            String sql = "INSERT INTO `" + NAMETABLE + "`"
                    + "(`username`, `password`, `level`) "
                    + "VALUES "
                    + "('" + username + "', '" + password + "','" + level + "')";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editUser(int id, String username, String password, String level) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            password = SecurityHelper.getMD5Hash(password);

            String sql = "UPDATE `" + NAMETABLE + "` SET `username`='" + username + "' , `password`='" + password + "', `level`='" + level + "'"
                    + "WHERE `id`='" + id + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteUser(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            User userByID = getUserByID(id);
            if (userByID.getId() == 0) {
                return ErrorCode.NOT_EXIST.getValue();
            }
            String sql = "DELETE FROM `" + NAMETABLE + "` WHERE `id`='" + id + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            int rs = preparedStatement.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public boolean checkLogin(String username, String password) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return false;
            }
            String sql = "SELECT * FROM `" + NAMETABLE + "` "
                    + "WHERE `username`='" + username + "' "
                    + "AND `password`='" + password + "'";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                if (rs.getString("username") != null && !rs.getString("username").trim().isEmpty()) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return false;
    }

    private static final String SECRET_KEY = "ngandethuong";

    public static String genAuthenCookie(String username) {
        return username + "," + SecurityHelper.getMD5Hash(username + SECRET_KEY);
    }

    public static String getAuthenCookie(String cookie) {
        String[] arrCookie = cookie.split("\\,");
        if (arrCookie.length == 2) {
            String username = arrCookie[0];
            String hash = arrCookie[1];

            if (hash.equals(SecurityHelper.getMD5Hash(username + SECRET_KEY))) {
                return username;
            }
        }

        return "";
    }

}

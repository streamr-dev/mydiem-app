package com.fs.vip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MtjServerDatabaseTools {

    private static String user = "doadmin";
    private static String password = "v4b6a4ugnazrr0l3";
    private static String DatabaseName = "mobile_usage";
    private static String IP = "db-postgresql-ams3-72677-do-user-4841599-0.db.ondigitalocean.com";
    /**
     * 连接字符串
     */
    private static String connectDB = "jdbc:postgresql://" + IP + ":25060/" + DatabaseName ;

    private static Connection conn = null;
    private static Statement stmt = null;

    /**
     * 链接数据库
     *
     * @return
     */
    private static Connection getSQLConnection() {
        Connection con = null;
        try {
            //加载驱动换成这个
            Class.forName("org.postgresql.Driver");
            //连接数据库对象
            con = DriverManager.getConnection(connectDB, user,
                    password);
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return con;
    }

    /**
     * 向服务器数据库插入数据
     * @tabName 要插入的表名
     * @tabTopName 要插入的字段名字符串，例如（name,password,age）
     * @values 与tabTopName 中 字段名一一对应的值。一次插入多跳数据，可以用逗号隔开。例如（"张三"，"zhangsan","24"），（"李四"，"lisi","26"）
     */
    public static int insertIntoData( String values) {
        int i = 0;
        try {
            if (conn == null) {
                conn = getSQLConnection();
                stmt = conn.createStatement();
            }
            if (conn == null || stmt == null) {
                return i;
            }
            String sql = "insert into eth_addr (addr) values('"+values+"');";
            i = stmt.executeUpdate(sql);
            closeConnect();
            LogUtils.e(i+"===");
        } catch (SQLException e) {
            closeConnect();
            e.printStackTrace();
        }
        return i;
    }

        /**
     * 关闭数据库链接
     */
    public static void closeConnect() {
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

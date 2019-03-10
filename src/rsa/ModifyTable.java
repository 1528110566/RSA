package rsa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ModifyTable {

    Connection con;
    String SQL;
    String tableName;
    String datasourceName;
    int i;
    String[][] data;
    public static boolean ifConnect = false;

    public ModifyTable() {
        try {
            Class.forName("com.hxtt.sql.access.AccessDriver");
            con = DriverManager.getConnection("jdbc:Access:///" + RSA.datasourcrName, "", "");
            ifConnect = true;
            System.out.println("数据库连接成功！");
        } catch (Exception ex) {
            //数据库连接失败
            ifConnect = false;
            RSA.ifSentToDataBase = false;
            System.out.println("数据库连接失败！" + ex);
            JOptionPane.showMessageDialog(null, "数据库连接失败，快速加密功能已屏蔽！如果要使用快速加密功能，请先连接数据库！");
        }
    }

    int rangeOfDataBase() {//数据库的长度
        Statement sql;
        ResultSet rs;
        int range = 0;
        try {
            String uri = "jdbc:Access:///" + datasourceName;
            String id = "";
            String password = "";
            con = DriverManager.getConnection(uri, id, password);
            sql = con.createStatement();
            rs = sql.executeQuery(SQL);
            range = 0;
            while (rs.next()) {
                range++;
            }
            con.close();
        } catch (Exception e) {
        }
        return range;
    }

    void setSQL(String s) {//设置SQL语句
        this.SQL = s;
    }

    void setDatasourceName(String s) {//设置数据库名字
        datasourceName = s;
    }

    void setTableName(String s) {//设置数据库表头名
        tableName = s;
    }

    void insertData() {//插入数据
        Statement sql;
        boolean message = false;
        try {
            String uri = "jdbc:Access:///" + datasourceName;
            String id = "";
            String password = "";
            con = DriverManager.getConnection(uri, id, password);
            sql = con.createStatement();
            int x = sql.executeUpdate(SQL);
            if (x != 0) {
                message = true;
            }
            con.close();
        } catch (Exception e) {
            message = false;
        }
    }

    String[][] getDataBase() {//读取数据库的数据
        data = new String[this.rangeOfDataBase()][3];
        Statement sql;
        ResultSet rs;
        try {
            String uri = "jdbc:Access:///" + datasourceName;
            String id = "";
            String password = "";
            con = DriverManager.getConnection(uri, id, password);
            sql = con.createStatement();
            rs = sql.executeQuery(SQL);
            i = 0;
            while (rs.next()) {
                data[i][0] = rs.getString(1);
                data[i][1] = rs.getString(2);
                data[i][2] = rs.getString(3);
                i++;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return data;
    }
}

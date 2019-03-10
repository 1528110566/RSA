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
            System.out.println("���ݿ����ӳɹ���");
        } catch (Exception ex) {
            //���ݿ�����ʧ��
            ifConnect = false;
            RSA.ifSentToDataBase = false;
            System.out.println("���ݿ�����ʧ�ܣ�" + ex);
            JOptionPane.showMessageDialog(null, "���ݿ�����ʧ�ܣ����ټ��ܹ��������Σ����Ҫʹ�ÿ��ټ��ܹ��ܣ������������ݿ⣡");
        }
    }

    int rangeOfDataBase() {//���ݿ�ĳ���
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

    void setSQL(String s) {//����SQL���
        this.SQL = s;
    }

    void setDatasourceName(String s) {//�������ݿ�����
        datasourceName = s;
    }

    void setTableName(String s) {//�������ݿ��ͷ��
        tableName = s;
    }

    void insertData() {//��������
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

    String[][] getDataBase() {//��ȡ���ݿ������
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

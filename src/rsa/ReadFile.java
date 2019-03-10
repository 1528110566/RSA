package rsa;

import java.io.*;
import javax.swing.JOptionPane;

public class ReadFile {

    File fRead;
    StringBuffer string;
    String[] data = new String[100000];//���Ϊ10000���ַ�
    int i = 0;

    ReadFile(File dir, String fileName) {
        fRead = new File(dir, fileName);
    }

    ReadFile(String fileName) {
        fRead = new File(fileName);
    }

    String[] readData() {//�������ļ�
        try {

            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(fRead), "GBK");
                 BufferedReader bufferRead = new BufferedReader(isr)) {
                String str = null;
                string = new StringBuffer();
                while ((str = bufferRead.readLine()) != null) {//ÿ��һ�����ݣ�д��һ����\n������������ʱ����
                    data[i] = str;
                    data[i + 1] = "\\n";
                    i += 2;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "��ȡ���ļ����ַ������ڳ������ܳ��ص����ֵ���뽫�ļ����", "������ʾ", JOptionPane.WARNING_MESSAGE);
        }
        return data;
    }

    String[] readEncryptData() {//�������ļ�
        try {
            try (Reader in = new FileReader(fRead); BufferedReader bufferRead = new BufferedReader(in)) {
                String str = null;
                string = new StringBuffer();
                while ((str = bufferRead.readLine()) != null) {//���Ĳ���Ҫ��ӡ�\n����ÿһ�����Ķ�Ӧһ���ַ�
                    data[i] = str;
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "��ȡ���ļ����ַ������ڳ������ܳ��ص����ֵ���뽫�ļ���֣�", "������ʾ", JOptionPane.WARNING_MESSAGE);
        }
        return data;
    }
}

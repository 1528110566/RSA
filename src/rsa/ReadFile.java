package rsa;

import java.io.*;
import javax.swing.JOptionPane;

public class ReadFile {

    File fRead;
    StringBuffer string;
    String[] data = new String[100000];//最大为10000个字符
    int i = 0;

    ReadFile(File dir, String fileName) {
        fRead = new File(dir, fileName);
    }

    ReadFile(String fileName) {
        fRead = new File(fileName);
    }

    String[] readData() {//读明文文件
        try {

            try (InputStreamReader isr = new InputStreamReader(new FileInputStream(fRead), "GBK");
                 BufferedReader bufferRead = new BufferedReader(isr)) {
                String str = null;
                string = new StringBuffer();
                while ((str = bufferRead.readLine()) != null) {//每读一行数据，写入一个“\n”，用来解密时分行
                    data[i] = str;
                    data[i + 1] = "\\n";
                    i += 2;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "读取的文件总字符数大于程序所能承载的最大值，请将文件拆分", "警告提示", JOptionPane.WARNING_MESSAGE);
        }
        return data;
    }

    String[] readEncryptData() {//读密文文件
        try {
            try (Reader in = new FileReader(fRead); BufferedReader bufferRead = new BufferedReader(in)) {
                String str = null;
                string = new StringBuffer();
                while ((str = bufferRead.readLine()) != null) {//密文不需要添加“\n”，每一行密文对应一个字符
                    data[i] = str;
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "读取的文件总字符数大于程序所能承载的最大值，请将文件拆分！", "警告提示", JOptionPane.WARNING_MESSAGE);
        }
        return data;
    }
}

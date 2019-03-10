package rsa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteFile {

    File fWrite;
    Writer out;
    BufferedWriter bufferWrite;
//������д�����㲻ͬ�����

    WriteFile(File dir, String fileName) {
        fWrite = new File(dir, fileName);
        try {
            out = new FileWriter(fWrite);
            bufferWrite = new BufferedWriter(out);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    WriteFile(String fileName) {
        fWrite = new File(fileName);
        try {
            out = new FileWriter(fWrite);
            bufferWrite = new BufferedWriter(out);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    void write(char data) {
        try {
            bufferWrite.write(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void write(String data) {//�����ж�������\n��ʱ�ķ���
        if (data.equals("\n")) {
            try {
                bufferWrite.newLine();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } else {
            try {
                bufferWrite.write(data);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void writeOver() {//�ر���
        try {
            bufferWrite.close();
            out.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

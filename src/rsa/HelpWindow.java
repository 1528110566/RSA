package rsa;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class HelpWindow extends JFrame {

    JTextArea information;

    HelpWindow() {
        setIconImage(new ImageIcon("image\\ico.jpg").getImage());//Œ™¥∞ÃÂ…Ë÷√Õº±Í
        setTitle("∞Ô÷˙");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        this.setLocation(centerX - 1200 / 2, centerY - 400 / 2);
        setSize(1340,380);
        setVisible(true);
        //setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        information = new JTextArea(15,100);
        readFile();
        information.setEditable(false);
        add(information);
    }

    void readFile() {
        ReadFile read = new ReadFile("∞Ô÷˙.txt");
        String[] data = read.readData();
        for (int i = 0; data[i] != null; i++) {
            if (data[i].equals("\\n")) {
                information.append("\n");
            } else {
                information.append(data[i]);
            }
        }
    }
}

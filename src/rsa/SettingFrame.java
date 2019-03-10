package rsa;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class SettingFrame extends JFrame implements KeyListener, ActionListener {

    JButton saveButton, backtoNormalButton, cancelButton;
    JTextField[] text;
    Box baseBox,
            layoutBox,
            localEncryptBox,
            databaseEncryptBox,
            quickEncryptBox,
            decryptBox,
            chooseBox,
            buttonBox;
    JRadioButton yesChoose, noChoose;
    WriteFile write;
    ButtonGroup group = new ButtonGroup();

    SettingFrame() {
        setTitle("����");
        setVisible(true);
        setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("image\\ico.jpg").getImage());//Ϊ��������ͼ��
        init();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        this.setLocation(centerX - 400 / 2, centerY - 400 / 2);
        setSize(450, 250);
    }

    void init() {
        yesChoose = new JRadioButton("��");
        noChoose = new JRadioButton("��");
        if (RSA.ifSentToDataBase == true) {
            yesChoose.setSelected(true);
            noChoose.setSelected(false);
        } else {
            noChoose.setSelected(true);
            yesChoose.setSelected(false);
        }
        text = new JTextField[8];
        for (int i = 0; i < 8; i++) {//ʵ����
            text[i] = new JTextField();
            text[i].setEditable(false);

        }
        for (int i = 1; i < 8; i += 2) {//��Ӽ�����
            text[i].addKeyListener(this);
            int x = Integer.parseInt(MainFrame.acclerator[i]);
            String s = String.valueOf((char) x);
            text[i].setText(s);
        }
        for (int i = 0; i < 8; i += 2) {//�����ı�
            text[i].setText("Ctrl");
        }
        buttonBox = Box.createHorizontalBox();
        saveButton = new JButton("����");
        saveButton.addActionListener(this);
        backtoNormalButton = new JButton("�ָ�����ʼ");
        backtoNormalButton.addActionListener(this);
        cancelButton = new JButton("ȡ��");
        cancelButton.addActionListener(this);
        buttonBox.add(saveButton);
        buttonBox.add(Box.createHorizontalStrut(12));
        buttonBox.add(backtoNormalButton);
        buttonBox.add(Box.createHorizontalStrut(12));
        buttonBox.add(cancelButton);
        layoutBox = Box.createHorizontalBox();
        layoutBox.add(Box.createHorizontalStrut(220));
        localEncryptBox = Box.createHorizontalBox();
        localEncryptBox.add(new JLabel("�������:"));
        localEncryptBox.add(Box.createHorizontalStrut(20));
        localEncryptBox.add(text[0]);
        localEncryptBox.add(Box.createHorizontalStrut(8));
        localEncryptBox.add(new JLabel("+"));
        localEncryptBox.add(Box.createHorizontalStrut(8));
        localEncryptBox.add(text[1]);
        databaseEncryptBox = Box.createHorizontalBox();
        quickEncryptBox = Box.createHorizontalBox();
        decryptBox = Box.createHorizontalBox();
        decryptBox.add(new JLabel("����:"));
        decryptBox.add(Box.createHorizontalStrut(50));
        decryptBox.add(text[6]);
        decryptBox.add(Box.createHorizontalStrut(8));
        decryptBox.add(new JLabel("+"));
        decryptBox.add(Box.createHorizontalStrut(8));
        decryptBox.add(text[7]);
        chooseBox = Box.createHorizontalBox();
        if (ModifyTable.ifConnect == true) {
            databaseEncryptBox.add(new JLabel("���ټ���:"));
            databaseEncryptBox.add(Box.createHorizontalStrut(20));
            databaseEncryptBox.add(text[2]);
            databaseEncryptBox.add(Box.createHorizontalStrut(8));
            databaseEncryptBox.add(new JLabel("+"));
            databaseEncryptBox.add(Box.createHorizontalStrut(8));
            databaseEncryptBox.add(text[3]);
            quickEncryptBox.add(new JLabel("��ݼ���:"));
            quickEncryptBox.add(Box.createHorizontalStrut(20));
            quickEncryptBox.add(text[4]);
            quickEncryptBox.add(Box.createHorizontalStrut(8));
            quickEncryptBox.add(new JLabel("+"));
            quickEncryptBox.add(Box.createHorizontalStrut(8));
            quickEncryptBox.add(text[5]);
            group.add(yesChoose);//��������ѡ��ť�����ButtonGroup�У�����������ʵ�ֵ�ѡ
            group.add(noChoose);
            chooseBox.add(new JLabel("�Ƿ񽫴�������Կ��д�����ݿ���:"));
            chooseBox.add(Box.createHorizontalStrut(8));
            chooseBox.add(yesChoose);
            chooseBox.add(Box.createHorizontalStrut(8));
            chooseBox.add(noChoose);
        }
        baseBox = Box.createVerticalBox();
        baseBox.add(layoutBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(localEncryptBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(databaseEncryptBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(quickEncryptBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(decryptBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(chooseBox);
        baseBox.add(Box.createVerticalStrut(8));
        baseBox.add(buttonBox);
        add(baseBox);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < 8; i++) {
            if (e.getSource() == text[i]) {
                int keycode = e.getKeyCode();
                if ((keycode >= 65 && keycode <= 90) || (keycode >= 48 && keycode <= 57)) {
                    String s = String.valueOf(e.getKeyChar());
                    text[i].setText(s.toUpperCase());
                }
                validate();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            write = new WriteFile(RSA.accleratorFile);
            for (int i = 0; i < 8; i++) {
                if (text[i].getText().equals("Ctrl")) {//���ı��ļ�ֵд���ļ�
                    MainFrame.acclerator[i] = "2";
                    write.write("2");
                    write.write("\n");
                } else {
                    String s = text[i].getText();
                    char c[] = s.toCharArray();
                    write.write(String.valueOf((int) c[0]));
                    MainFrame.acclerator[i] = String.valueOf((int) c[0]);
                    write.write("\n");
                }
            }
            RSA.ifSentToDataBase = yesChoose.isSelected();
            MainFrame.ifAccleratorChanged = true;
            write.writeOver();
            JOptionPane.showMessageDialog(this, "����ɹ���");
            dispose();
        } else if (e.getSource() == backtoNormalButton) {
            yesChoose.requestFocus();
            RSA.ifSentToDataBase = true;
            write = new WriteFile(RSA.accleratorFile);
            String[] acclerator = {"2", "49", "2", "50", "2", "65", "2", "51"};//����ݼ��ĳ�ʼֵд������У��Է���һ
            for (int i = 0; i < acclerator.length; i++) {
                MainFrame.acclerator[i] = acclerator[i];
                write.write(acclerator[i]);
                write.write("\n");
            }
            JOptionPane.showMessageDialog(this, "�ָ��ɹ���");
            MainFrame.ifAccleratorChanged = true;
            write.writeOver();
            dispose();
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }
}

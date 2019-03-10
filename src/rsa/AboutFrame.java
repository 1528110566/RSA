package rsa;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class AboutFrame extends JFrame implements MouseListener {

    Box box;
    JLabel label1, label2, label3;

    AboutFrame() {
        setTitle("关于");
        setIconImage(new ImageIcon("image\\ico.jpg").getImage());//为窗体设置图标
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        this.setLocation(centerX - 300 / 2, centerY - 300 / 2);
        setSize(450, 250);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setVisible(true);
        label1 = new JLabel("组员：陶郑            学号：16111205090");
        label2 = new JLabel("      鲍忠将                16111205001");
        label3 = new JLabel("      汪晖                  16111205093");
        label1.setFont(new Font("宋体", Font.BOLD, RSA.font));
        label2.setFont(new Font("宋体", Font.BOLD, RSA.font));
        label3.setFont(new Font("宋体", Font.BOLD, RSA.font));
        label1.addMouseListener(this);
        label2.addMouseListener(this);
        label3.addMouseListener(this);
        box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(20));
        box.add(label1);
        box.add(Box.createVerticalStrut(30));
        box.add(label2);
        box.add(Box.createVerticalStrut(30));
        box.add(label3);
        add(box);
    }
//鼠标进入、退出动画
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == label1) {
            label1.setFont(new Font("华文楷体", Font.BOLD, RSA.font+5));
            validate();
        }
        if (e.getSource() == label2) {
            label2.setFont(new Font("华文楷体", Font.BOLD, RSA.font+5));
            validate();
        }
        if (e.getSource() == label3) {
            label3.setFont(new Font("华文楷体", Font.BOLD, RSA.font+5));
            validate();
        }

    }

    public void mouseExited(MouseEvent e) {
        if (e.getSource() == label1 || e.getSource() == label2 || e.getSource() == label3) {
            label1.setFont(new Font("宋体", Font.BOLD, RSA.font));
            label2.setFont(new Font("宋体", Font.BOLD, RSA.font));
            label3.setFont(new Font("宋体", Font.BOLD, RSA.font));
            validate();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

}

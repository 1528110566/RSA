package rsa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class MainFrame extends JFrame implements ActionListener {

    JMenuBar menuBar;//�˵���
    JMenu menu1, menu2;//�˵�
    JFileChooser fileOpenTxtChooser, fileOpenRsaChooser;//��txt�ļ���rsa�ļ��ĶԻ���
    JMenuItem localEncryptItem, //������ܲ˵���
            databaseEncryptItem, //���ټ��ܲ˵���
            decryptItem, //���ܲ˵���
            aboutFrameItem,//���ڴ��ڲ˵���
            settingItem,
            helpItem;//���ò˵���
    public static String stateLabelName, timeLabelName;//�������ı�ǩ���������û���ʾ��Ϣ
    JLabel stateLabel, timeLabel;//״̬��ǩ����ʱ����ǩ
    static JTextArea upText, downText;//���ڴ�����������ĵ��ı���
    File dir, deleteFile;//�ļ�λ�á�һ��ɾ�����ļ�����
    String fileName;//�ļ�����
    Arithmetic ari = new Arithmetic();//ʵ�����㷨��
    Timer timer;//��ʱ����������ʾ��ǩ
    //Timer timerOpen;
    Box textBox, //װ��upText��downText�ĺ���
            smallBox, //װ��timeLabel��stopButton��savePrivateKeyButton�ĺ���
            stateLabel_box, //װ��stateLabel�ĺ���
            baseBox;//����Ĵ����
    static JButton stopButton, savePrivateKeyButton;//ֹͣ��ť�뱣�������Կ��ť
    ModifyTable modify = new ModifyTable();//ʵ�������ݿ������
    public static boolean ifDatabaseEncrypt;//�ж��Ƿ�������ݿ����
    public static boolean ifSavedKey = true;//�ж��û��Ƿ񱣴��˽�����Կ
    String[][] databaseData;//�����ݿ��ȡ��������Ϣ
    public static boolean ifAccleratorChanged = true;//��ݼ��Ƿ�ı�
    public static String[] acclerator;
    //int x1 = 1, y1 = 1;//������ʾ�ĳ�ʼֵ

    MainFrame() {
        ReadFile read = new ReadFile(RSA.accleratorFile);
        acclerator = read.readEncryptData();
        //timerOpen = new Timer(2, this);
        //timerOpen.start();
        //���Ĵ���ķ��
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        init();
        //��������ʾ����Ļ����
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        this.setLocation(centerX - RSA.length / 2, centerY - RSA.width / 2);
        setSize(RSA.length, RSA.width);
        setIconImage(new ImageIcon("image/ico.jpg").getImage());//Ϊ��������ͼ��
        setVisible(true);
        setTitle("����RSA�㷨���ļ����������");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.requestFocus();
        addWindowListener(new WindowAdapter() {//�����࣬�û��˳�ʱ������ʾ
            public void windowClosing(WindowEvent e) {
                String str;
                str = "�Ƿ��˳���";
                if (ari.label_Thread.isAlive()) {
                    str = "��������ִ�У��Ƿ��˳���";
                } else if (!ari.label_Thread.isAlive() && ifSavedKey == false) {
                    str = "������Կ��δ���棬�Ƿ񱣴棿";
                }
                int state = JOptionPane.showConfirmDialog(null, str, "ȷ�϶Ի���", JOptionPane.YES_NO_OPTION);
                if (state == JOptionPane.YES_OPTION && ifSavedKey == true) {//������Կ�Ѿ��������û�������ǡ�
                    System.exit(0);
                } else if (state == JOptionPane.YES_OPTION
                        && ifSavedKey == false
                        && str.equals("������Կ��δ���棬�Ƿ񱣴棿")) {//������Կδ�������û�������ǡ�
                    ari.savePrivateKey(dir);//���������Կ
                    System.exit(0);
                } else if (state == JOptionPane.NO_OPTION
                        && ifSavedKey == false
                        && str.equals("������Կ��δ���棬�Ƿ񱣴棿")) {//������Կδ�������û��������
                    System.exit(0);
                } else if (state == JOptionPane.YES_OPTION
                        && ifSavedKey == false
                        && str.equals("��������ִ�У��Ƿ��˳���")) {//���ܣ����ܣ���������ִ�������û�������ǡ�
                    System.exit(0);
                }
            }
        });
        //���׼���
        upText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                try {
                    if (e.getKeyCode() == Integer.parseInt(acclerator[5])
                            && e.getModifiers() == Integer.parseInt(acclerator[4])
                            && !ari.label_Thread.isAlive()
                            && ModifyTable.ifConnect == true) {
                        easyEncrypt();
                    }
                } catch (Exception ex) {
                    System.out.println(e);
                }

            }
        });
        downText.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Integer.parseInt(acclerator[5])
                        && e.getModifiers() == Integer.parseInt(acclerator[4])
                        && !ari.label_Thread.isAlive()
                        && ModifyTable.ifConnect == true) {
                    easyEncrypt();
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Integer.parseInt(acclerator[5])
                        && e.getModifiers() == Integer.parseInt(acclerator[4])
                        && !ari.label_Thread.isAlive()
                        && ModifyTable.ifConnect == true) {
                    easyEncrypt();
                }
            }
        });
    }

    void init() {
        savePrivateKeyButton = new JButton("���������Կ", new ImageIcon("image\\key.png"));//���ñ��������Կ��ť
        savePrivateKeyButton.setFont(new Font("����", Font.BOLD, RSA.font));//���ð�ť�ϵ�����
        savePrivateKeyButton.setContentAreaFilled(false);//����ť����Ϊ͸��
        savePrivateKeyButton.setVisible(false);//���ر�����Կ��ť
        savePrivateKeyButton.addActionListener(this);//��Ӽ�����
        savePrivateKeyButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {//�������ǩ��������ɫ
                savePrivateKeyButton.setForeground(Color.BLUE);
                validate();
            }

            public void mouseExited(MouseEvent e) {//����뿪��ǩ��������ɫ
                savePrivateKeyButton.setForeground(Color.BLACK);
                validate();
            }
        });
        stopButton = new JButton("ȡ��", new ImageIcon("image\\stop.png"));
        stopButton.setFont(new Font("����", Font.BOLD, RSA.font));//���ð�ť�ϵ�����
        stopButton.setContentAreaFilled(false);
        stopButton.setVisible(false);
        stopButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {//�����밴ť��������ɫ
                stopButton.setForeground(Color.RED);
                validate();
            }

            public void mouseExited(MouseEvent e) {//����뿪��ť��������ɫ
                stopButton.setForeground(Color.BLACK);
                validate();
            }
        });
        stopButton.addActionListener(this);
        timer = new Timer(1, this);
        timer.addActionListener(this);
        timer.start();
        stateLabel = new JLabel();
        stateLabel.setFont(new Font("����", Font.BOLD, RSA.font));
        stateLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (stateLabelName.contains("�Ѿ�����")) {
                    try {
                        //System.out.println(Arithmetic.dir + "\\" + Arithmetic.fileName);
                        Runtime.getRuntime().exec("explorer /select," + Arithmetic.dir + "\\" + Arithmetic.fileName);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {//�������ǩ��������ɫ
                if (stateLabelName.contains("�Ѿ�����")) {
                    stateLabel.setForeground(Color.red);
                    validate();
                }
            }

            public void mouseExited(MouseEvent e) {//����뿪��ǩ��������ɫ
                if (stateLabelName.contains("�Ѿ�����")) {
                    stateLabel.setForeground(Color.BLACK);
                    validate();
                }
            }
        });
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("����", Font.BOLD, RSA.font));
        upText = new JTextArea(20, 30);
        upText.setEditable(false);
        downText = new JTextArea(20, 30);
        downText.setEditable(false);
        textBox = Box.createVerticalBox();
        textBox.add(new JScrollPane(upText));
        textBox.add(Box.createVerticalStrut(12));
        textBox.add(new JScrollPane(downText));
        smallBox = Box.createHorizontalBox();
        smallBox.add(timeLabel);
        smallBox.add(Box.createHorizontalStrut(20));
        smallBox.add(stopButton);
        smallBox.add(Box.createHorizontalStrut(20));
        smallBox.add(savePrivateKeyButton);
        stateLabel_box = Box.createHorizontalBox();
        stateLabel_box.add(stateLabel);
        baseBox = Box.createVerticalBox();
        baseBox.add(smallBox);
        baseBox.add(Box.createVerticalStrut(12));
        baseBox.add(textBox);
        baseBox.add(Box.createVerticalStrut(12));
        baseBox.add(stateLabel_box);
        add(baseBox);//��baseBox���������
        fileOpenTxtChooser = new JFileChooser();
        fileOpenRsaChooser = new JFileChooser();
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("txt�ļ�", "txt");//��txt�ļ����ļ��Ի���
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("rsa�ļ�", "rsa");//��rsa�ļ����ļ��Ի���
        fileOpenTxtChooser.setFileFilter(filter1);
        fileOpenRsaChooser.setFileFilter(filter2);
    }

    int easyEncrypt() {
        upText.setText(null);
        downText.setText(null);
        if (ifSavedKey == false) {
            int state = JOptionPane.showConfirmDialog(this, "�Ƿ��ȱ��������Կ��", "ȷ�϶Ի���", JOptionPane.YES_NO_OPTION);
            if (state == JOptionPane.YES_OPTION) {
                ari.savePrivateKey(dir);
                ifSavedKey = true;
            } else {
                ifSavedKey = true;
            }
        }
        stateLabelName = "";
        timeLabelName = "";
        savePrivateKeyButton.setVisible(false);
        String str = JOptionPane.showInputDialog(null, "������Ҫ���ܵ�����", "����Ի���", JOptionPane.PLAIN_MESSAGE);
        String message;
        while (true) {
            if (str != null && str.equals("")) {
                message = "��������Ҫ���ܵ����ݣ�";
            } else if (str != null && !str.equals("") && str.length() >= 20) {
                message = "��������ݹ��������������߽���д��txt�ļ����ٽ��м���";
            } else if (str == null) {
                return 0;//�˳�����
            } else {
                upText.append("���ģ�\n");
                upText.append(str);
                break;
            }
            JOptionPane.showMessageDialog(this, message);
            str = JOptionPane.showInputDialog(null, "������Ҫ���ܵ�����", "����Ի���", JOptionPane.PLAIN_MESSAGE);
        }
        FileSystemView fsv = FileSystemView.getFileSystemView();//��ȡϵͳ�����·��
        dir = fsv.getHomeDirectory();
        Calendar calendar = Calendar.getInstance();//��ȡϵͳ��ǰʱ�䣬��Ϊ�ļ�������
        int hour = calendar.get(Calendar.HOUR_OF_DAY),
                minute = calendar.get(Calendar.MINUTE),
                second = calendar.get(Calendar.SECOND);
        fileName = String.valueOf(hour) + "_" + String.valueOf(minute) + "_" + String.valueOf(second);
        dir = new File(dir.toString() + "\\" + fileName);//�����ļ����������ļ���
        File file = new File(dir.toString());
        if (!file.mkdir())//�����ļ���
        {
            dir = fsv.getHomeDirectory();//�����ļ�ʧ��
        }
        dir = new File(dir.toString());
        modify.setDatasourceName(RSA.datasourcrName);
        modify.setTableName(RSA.tableName);
        modify.setSQL("SELECT * FROM " + RSA.tableName);
        String data[][] = modify.getDataBase();//��ȡ���ݿ����Ϣ
        int range = modify.rangeOfDataBase();
        Random random = new Random();
        int index = random.nextInt(range);//�����ݿ�ĳ��ȵķ�Χ���漴ȡ��һ����Ϊ���ܵ���Կ��
        BigInteger n = new BigInteger(data[index][0]);
        BigInteger e = new BigInteger(data[index][1]);
        BigInteger d = new BigInteger(data[index][2]);
        ari.setKey(e, n);//������Կ��
        ari.savePrivateKey(dir, fileName + ".key", d, n);
        ari.encrypt(str, dir, fileName);
        stateLabelName = "���ܳɹ����ļ������������Ŷ~~";
        return 0;
    }

    public void fresh() {//�������´���ĸ������
        if (stateLabelName != null && timeLabelName != null) {
            stateLabel.setText(stateLabelName);
            stateLabel.setVisible(true);
            timeLabel.setText(timeLabelName);
            timeLabel.setVisible(true);
        }
        if (ari.getD() == null || ifDatabaseEncrypt == true) {//���ټ��ܲ���Ҫ���������Կ
            savePrivateKeyButton.setVisible(false);
        }
        if (ifAccleratorChanged == true) {
            //����ˢ�¿�ݼ�
            menuBar = new JMenuBar();
            menu1 = new JMenu("�˵�");
            menu2 = new JMenu("����");
            localEncryptItem = new JMenuItem("�������", new ImageIcon("image\\local_encrypt.png"));//���ؼ��ܲ˵���
            localEncryptItem.addActionListener(this);
            databaseEncryptItem = new JMenuItem("���ټ���", new ImageIcon("image\\database_encrypt.png"));//���ݿ���ܲ˵���
            databaseEncryptItem.addActionListener(this);
            decryptItem = new JMenuItem("����", new ImageIcon("image\\decrypt.png"));//���ܲ˵���
            decryptItem.addActionListener(this);
            try {
                localEncryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[1]), Integer.parseInt(acclerator[0])));//���ÿ�ݼ�
                databaseEncryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[3]), Integer.parseInt(acclerator[2])));
                decryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[7]), Integer.parseInt(acclerator[6])));
            } catch (Exception e) {
                System.out.println(e);
            }
            aboutFrameItem = new JMenuItem("����", new ImageIcon("image\\about.png"));
            aboutFrameItem.addActionListener(this);
            settingItem = new JMenuItem("����", new ImageIcon("image\\setting.png"));
            settingItem.addActionListener(this);
            helpItem = new JMenuItem("����", new ImageIcon("image\\help.png"));
            helpItem.addActionListener(this);
            menu1.add(localEncryptItem);
            if (modify.ifConnect == true) {//������ݿ����ӳɹ�����ʾ���ݿ���ܲ˵���
                menu1.add(databaseEncryptItem);
            }
            menu1.addSeparator();//��ӷָ���
            menu1.add(decryptItem);
            //menu.add(loopdecryptItem);//���ڲ��Խ׶Σ��ݲ��ų�
            menu2.add(settingItem);
            menu2.addSeparator();
            menu2.add(aboutFrameItem);
            menu2.addSeparator();
            menu2.add(helpItem);
            menuBar.add(menu1);
            menuBar.add(menu2);
            this.setJMenuBar(menuBar);
            validate();
            ifAccleratorChanged = false;
        }
    }

    public void actionPerformed(ActionEvent ex) {
        if (ex.getSource() == localEncryptItem) {
            while (true) {
                int state = fileOpenTxtChooser.showOpenDialog(this);
                if (state == JFileChooser.APPROVE_OPTION) {
                    if (ari.encrypted == false) {//������ܲ�����δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У������ظ�������");
                    } else if (ari.decrypted == false) {//������ܲ�����δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У����Ժ��ٽ��м��ܲ�����");
                    } else {
                        savePrivateKeyButton.setVisible(false);
                        ifDatabaseEncrypt = false;//�������ݿ����
                        dir = fileOpenTxtChooser.getCurrentDirectory();//��ȡ�ļ�λ�����ļ���
                        fileName = fileOpenTxtChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);//�����ļ�
                        int index = fileName.lastIndexOf(".txt");
                        if (index == fileName.length() - 4) {//�����txt�ļ�����������ʾ��upText��
                            upText.setText(null);
                            upText.append("���ģ�" + "\n");
                            ReadFile read = new ReadFile(dir, fileName);
                            String sourceData[] = read.readData();
                            for (int i = 0; i < sourceData.length; i++) {
                                if (sourceData[i] != null) {
                                    if (sourceData[i].equals("\\n")) {
                                        upText.append("\n");
                                    } else {
                                        upText.append(sourceData[i]);
                                    }
                                }
                            }
                            ari.encrypted = false;//���ܲ�����ʼ���У������ܲ�����״̬����Ϊfalse
                            ari.creatThread();//�����߳�
                            ari.creat_label_Thread();//����label_Thread
                            ari.creatPandQ_Thread.start();//����creatPandQ_Thread
                            stopButton.setVisible(true);//��ʾֹͣ��ť
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "��ѡ��һ��txt�ļ���", "����", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    break;
                }
            }
        } else if (ex.getSource() == databaseEncryptItem) {
            while (true) {
                int state = fileOpenTxtChooser.showOpenDialog(this);
                if (state == JFileChooser.APPROVE_OPTION) {
                    if (ari.encrypted == false) {//���ܲ�����δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У������ظ�������");
                    } else if (ari.decrypted == false) {//���ܲ�����δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У����Ժ��ٽ��м��ܲ�����");
                    } else {
                        if (ifSavedKey == false) {
                            state = JOptionPane.showConfirmDialog(null, "������Կ��δ���棬�Ƿ񱣴棿", "ȷ�϶Ի���", JOptionPane.YES_NO_OPTION);
                            if (state == JOptionPane.YES_OPTION) {//�û�������ǡ�
                                ari.savePrivateKey(dir);//���������Կ
                            }
                        }
                        ifSavedKey = true;
                        dir = fileOpenTxtChooser.getCurrentDirectory();
                        fileName = fileOpenTxtChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);
                        int indexa = fileName.lastIndexOf(".txt");
                        if (indexa == fileName.length() - 4) {
                            ifDatabaseEncrypt = true;
                            RSA.ifSentToDataBase = false;//����ǲ������ݿ���ܲ���������Ҫ����Կ���������ݿ���
                            stopButton.setVisible(true);
                            savePrivateKeyButton.setVisible(false);
                            stateLabelName = "�����ݿ��л�ȡ������Կ��...";
                            modify.setDatasourceName(RSA.datasourcrName);
                            modify.setTableName(RSA.tableName);
                            modify.setSQL("SELECT * FROM " + RSA.tableName);
                            String data[][] = modify.getDataBase();//��ȡ���ݿ����Ϣ
                            int range = modify.rangeOfDataBase();
                            Random random = new Random();
                            int index = random.nextInt(range);//�����ݿ�ĳ��ȵķ�Χ���漴ȡ��һ����Ϊ���ܵ���Կ��
                            BigInteger n = new BigInteger(data[index][0]);
                            BigInteger e = new BigInteger(data[index][1]);
                            ari.setKey(e, n);//������Կ��
                            ari.encrypted = false;//����״̬��Ϊfalse
                            ari.creatThread();
                            ari.creat_label_Thread();
                            ari.encrypt_Thread.start();//ֻ������encrypt_Thread�����������߳�
                            stopButton.setVisible(true);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "��ѡ��һ��txt�ļ���", "����", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    break;
                }
            }
        } else if (ex.getSource() == decryptItem) {
            savePrivateKeyButton.setVisible(false);//���ܲ�������Ҫ������Կ�ԣ���savePrivateKeyButton����
            while (true) {
                int state = fileOpenRsaChooser.showOpenDialog(this);
                if (state == JFileChooser.APPROVE_OPTION) {
                    if (ari.encrypted == false) {//���ܻ�δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У����Ժ��ٽ��н��ܲ�����");
                    } else if (ari.decrypted == false) {//���ܻ�δ���
                        JOptionPane.showMessageDialog(this, "���ܲ������ڽ��У������ظ�������");
                    } else {
                        dir = fileOpenRsaChooser.getCurrentDirectory();
                        fileName = fileOpenRsaChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);
                        int index = fileName.lastIndexOf(".rsa");
                        if (index == fileName.length() - 4) {//�����rsa�ļ�
                            ari.decrypted = false;
                            ari.creatThread();
                            ari.decrypt_Thread.start();
                            stopButton.setVisible(true);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "��ѡ��һ��rsa�ļ���", "����", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    break;
                }
            }
        } else if (ex.getSource() == settingItem) {
            new SettingFrame();
        } else if (ex.getSource() == aboutFrameItem) {
            new AboutFrame();
        } else if (ex.getSource() == helpItem) {
            new HelpWindow();
        } else if (ex.getSource() == stopButton) {//ֹͣ
            ari.terminateThread();//�����߳�
            ari.terminateKey();//������Կ��
            JOptionPane.showMessageDialog(this, "��ֹ�����ɹ���");
            if (ari.encrypted == false || ari.decrypted == true) {//�ж������ǰ��ֹ�Ĳ����Ǽ��ܲ���
                String name[] = fileName.split(".txt");//����ַ�������ȡ�ļ�����ת��encrypt XXXX.rsa
                deleteFile = new File(dir, "encrypt " + name[0] + ".rsa");
                stopButton.setVisible(false);
                if (deleteFile.exists()) {//���Ҫɾ������δ��ɵ��ļ�����
                    deleteFile.delete();
                    System.out.println("ɾ��" + deleteFile.getName() + "�ɹ�");
                    stateLabelName = "�û�ȡ��������δ��ɵļ����ļ���ɾ����";
                } else {
                    System.out.println("�ļ�" + deleteFile + "������");
                    stateLabelName = "�û�ȡ��������";
                }
            } else if (ari.encrypted == true || ari.decrypted == false) {//�������ǰ��ֹ�Ĳ����ǽ��ܲ���
                String name[];
                String name1[];//����ַ�������ȡԭ�ļ�����ת��decrypt XXXX.txt
                if (fileName.contains("encrypt")) {//�����ǰ�ļ�������encrypt
                    name = fileName.split("encrypt");
                    name1 = name[1].split(".rsa");
                    deleteFile = new File(dir, "decrypt " + name1[0] + ".txt");
                    stopButton.setVisible(false);
                    if (deleteFile.exists()) {
                        deleteFile.delete();
                        stateLabelName = "�û�ȡ��������δ��ɵĽ����ļ���ɾ����";
                        System.out.println("ɾ��" + deleteFile.getName() + "�ɹ�");
                    } else {
                        stateLabelName = "�û�ȡ��������";
                        System.out.println("�ļ�" + deleteFile + "������");
                    }
                } else {
                    name = fileName.split(".rsa");
                    deleteFile = new File(dir, "decrypt " + name[0] + ".txt");
                    if (deleteFile.exists()) {
                        deleteFile.delete();
                        stateLabelName = "�û�ȡ��������δ��ɵĽ����ļ���ɾ����";
                        System.out.println("ɾ��" + deleteFile.getName() + "�ɹ�");
                    } else {
                        System.out.println("�ļ�" + deleteFile + "������");
                    }
                }
            }
            System.out.println("\n�û���ֹ������\n");
            ari.decrypted = true;
            ari.encrypted = true;
        } else if (ex.getSource() == savePrivateKeyButton) {
            ifSavedKey = true;//�û��Ѿ�������������Կ��ť
            ari.savePrivateKey(dir);
        } else if (ex.getSource() == timer) {
            this.fresh();
        }
//        else if (ex.getSource() == timerOpen) {
//            //System.out.println("x1=" + x1 + "       x2=" + y1);
//            int x = RSA.length;
//            int y = RSA.width;
//            if (x1 < x) {
//                setLocationRelativeTo(null);//��������ʾ����Ļ����
//                setSize(x1, y1);
//                validate();
//                x1 += 4;
//            }
//            if (y1 < y) {
//                setLocationRelativeTo(null);
//                setSize(x1, y1);
//                validate();
//                y1 += 4;
//            }
//            if (x1 >= x && y1 >= y) {//����չ�����
//                timerOpen.stop();
//            }
//        }
    }
}

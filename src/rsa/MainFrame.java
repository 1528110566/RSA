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

    JMenuBar menuBar;//菜单栏
    JMenu menu1, menu2;//菜单
    JFileChooser fileOpenTxtChooser, fileOpenRsaChooser;//打开txt文件与rsa文件的对话框
    JMenuItem localEncryptItem, //常规加密菜单项
            databaseEncryptItem, //快速加密菜单项
            decryptItem, //解密菜单项
            aboutFrameItem,//关于窗口菜单项
            settingItem,
            helpItem;//设置菜单项
    public static String stateLabelName, timeLabelName;//用来更改标签内容来给用户提示信息
    JLabel stateLabel, timeLabel;//状态标签与总时长标签
    static JTextArea upText, downText;//用于存放明文与密文的文本域
    File dir, deleteFile;//文件位置、一个删除的文件对象
    String fileName;//文件名字
    Arithmetic ari = new Arithmetic();//实例化算法类
    Timer timer;//计时器，更改提示标签
    //Timer timerOpen;
    Box textBox, //装有upText与downText的盒子
            smallBox, //装有timeLabel、stopButton、savePrivateKeyButton的盒子
            stateLabel_box, //装有stateLabel的盒子
            baseBox;//总体的大盒子
    static JButton stopButton, savePrivateKeyButton;//停止按钮与保存解密密钥按钮
    ModifyTable modify = new ModifyTable();//实例化数据库操作类
    public static boolean ifDatabaseEncrypt;//判断是否采用数据库加密
    public static boolean ifSavedKey = true;//判断用户是否保存了解密密钥
    String[][] databaseData;//从数据库读取出来的信息
    public static boolean ifAccleratorChanged = true;//快捷键是否改变
    public static String[] acclerator;
    //int x1 = 1, y1 = 1;//窗口显示的初始值

    MainFrame() {
        ReadFile read = new ReadFile(RSA.accleratorFile);
        acclerator = read.readEncryptData();
        //timerOpen = new Timer(2, this);
        //timerOpen.start();
        //更改窗体的风格
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        init();
        //将窗口显示在屏幕中央
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = screenSize.width / 2;
        int centerY = screenSize.height / 2;
        this.setLocation(centerX - RSA.length / 2, centerY - RSA.width / 2);
        setSize(RSA.length, RSA.width);
        setIconImage(new ImageIcon("image/ico.jpg").getImage());//为窗体设置图标
        setVisible(true);
        setTitle("基于RSA算法的文件加密与解密");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.requestFocus();
        addWindowListener(new WindowAdapter() {//匿名类，用户退出时给出提示
            public void windowClosing(WindowEvent e) {
                String str;
                str = "是否退出？";
                if (ari.label_Thread.isAlive()) {
                    str = "操作正在执行，是否退出？";
                } else if (!ari.label_Thread.isAlive() && ifSavedKey == false) {
                    str = "解密密钥尚未保存，是否保存？";
                }
                int state = JOptionPane.showConfirmDialog(null, str, "确认对话框", JOptionPane.YES_NO_OPTION);
                if (state == JOptionPane.YES_OPTION && ifSavedKey == true) {//解密密钥已经保存且用户点击“是”
                    System.exit(0);
                } else if (state == JOptionPane.YES_OPTION
                        && ifSavedKey == false
                        && str.equals("解密密钥尚未保存，是否保存？")) {//解密密钥未保存且用户点击“是”
                    ari.savePrivateKey(dir);//保存解密密钥
                    System.exit(0);
                } else if (state == JOptionPane.NO_OPTION
                        && ifSavedKey == false
                        && str.equals("解密密钥尚未保存，是否保存？")) {//解密密钥未保存且用户点击“否”
                    System.exit(0);
                } else if (state == JOptionPane.YES_OPTION
                        && ifSavedKey == false
                        && str.equals("操作正在执行，是否退出？")) {//解密（加密）操作正在执行中且用户点击“是”
                    System.exit(0);
                }
            }
        });
        //简易加密
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
        savePrivateKeyButton = new JButton("保存解密密钥", new ImageIcon("image\\key.png"));//设置保存解密密钥按钮
        savePrivateKeyButton.setFont(new Font("楷体", Font.BOLD, RSA.font));//设置按钮上的字体
        savePrivateKeyButton.setContentAreaFilled(false);//将按钮设置为透明
        savePrivateKeyButton.setVisible(false);//隐藏保存密钥按钮
        savePrivateKeyButton.addActionListener(this);//添加监视器
        savePrivateKeyButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {//鼠标进入标签，更改颜色
                savePrivateKeyButton.setForeground(Color.BLUE);
                validate();
            }

            public void mouseExited(MouseEvent e) {//鼠标离开标签，更改颜色
                savePrivateKeyButton.setForeground(Color.BLACK);
                validate();
            }
        });
        stopButton = new JButton("取消", new ImageIcon("image\\stop.png"));
        stopButton.setFont(new Font("楷体", Font.BOLD, RSA.font));//设置按钮上的字体
        stopButton.setContentAreaFilled(false);
        stopButton.setVisible(false);
        stopButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {//鼠标进入按钮，更改颜色
                stopButton.setForeground(Color.RED);
                validate();
            }

            public void mouseExited(MouseEvent e) {//鼠标离开按钮，更改颜色
                stopButton.setForeground(Color.BLACK);
                validate();
            }
        });
        stopButton.addActionListener(this);
        timer = new Timer(1, this);
        timer.addActionListener(this);
        timer.start();
        stateLabel = new JLabel();
        stateLabel.setFont(new Font("楷体", Font.BOLD, RSA.font));
        stateLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (stateLabelName.contains("已经保存")) {
                    try {
                        //System.out.println(Arithmetic.dir + "\\" + Arithmetic.fileName);
                        Runtime.getRuntime().exec("explorer /select," + Arithmetic.dir + "\\" + Arithmetic.fileName);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {//鼠标进入标签，更改颜色
                if (stateLabelName.contains("已经保存")) {
                    stateLabel.setForeground(Color.red);
                    validate();
                }
            }

            public void mouseExited(MouseEvent e) {//鼠标离开标签，更改颜色
                if (stateLabelName.contains("已经保存")) {
                    stateLabel.setForeground(Color.BLACK);
                    validate();
                }
            }
        });
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("楷体", Font.BOLD, RSA.font));
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
        add(baseBox);//将baseBox添加至窗体
        fileOpenTxtChooser = new JFileChooser();
        fileOpenRsaChooser = new JFileChooser();
        FileNameExtensionFilter filter1 = new FileNameExtensionFilter("txt文件", "txt");//打开txt文件的文件对话框
        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("rsa文件", "rsa");//打开rsa文件的文件对话框
        fileOpenTxtChooser.setFileFilter(filter1);
        fileOpenRsaChooser.setFileFilter(filter2);
    }

    int easyEncrypt() {
        upText.setText(null);
        downText.setText(null);
        if (ifSavedKey == false) {
            int state = JOptionPane.showConfirmDialog(this, "是否先保存解密密钥？", "确认对话框", JOptionPane.YES_NO_OPTION);
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
        String str = JOptionPane.showInputDialog(null, "请输入要加密的数据", "输入对话框", JOptionPane.PLAIN_MESSAGE);
        String message;
        while (true) {
            if (str != null && str.equals("")) {
                message = "请输入需要加密的数据！";
            } else if (str != null && !str.equals("") && str.length() >= 20) {
                message = "输入的数据过长，请缩减或者将其写入txt文件后再进行加密";
            } else if (str == null) {
                return 0;//退出函数
            } else {
                upText.append("明文：\n");
                upText.append(str);
                break;
            }
            JOptionPane.showMessageDialog(this, message);
            str = JOptionPane.showInputDialog(null, "请输入要加密的数据", "输入对话框", JOptionPane.PLAIN_MESSAGE);
        }
        FileSystemView fsv = FileSystemView.getFileSystemView();//获取系统桌面的路径
        dir = fsv.getHomeDirectory();
        Calendar calendar = Calendar.getInstance();//获取系统当前时间，作为文件的名字
        int hour = calendar.get(Calendar.HOUR_OF_DAY),
                minute = calendar.get(Calendar.MINUTE),
                second = calendar.get(Calendar.SECOND);
        fileName = String.valueOf(hour) + "_" + String.valueOf(minute) + "_" + String.valueOf(second);
        dir = new File(dir.toString() + "\\" + fileName);//加上文件名来创建文件夹
        File file = new File(dir.toString());
        if (!file.mkdir())//创建文件夹
        {
            dir = fsv.getHomeDirectory();//创建文件失败
        }
        dir = new File(dir.toString());
        modify.setDatasourceName(RSA.datasourcrName);
        modify.setTableName(RSA.tableName);
        modify.setSQL("SELECT * FROM " + RSA.tableName);
        String data[][] = modify.getDataBase();//获取数据库的信息
        int range = modify.rangeOfDataBase();
        Random random = new Random();
        int index = random.nextInt(range);//在数据库的长度的范围内随即取出一行作为加密的密钥对
        BigInteger n = new BigInteger(data[index][0]);
        BigInteger e = new BigInteger(data[index][1]);
        BigInteger d = new BigInteger(data[index][2]);
        ari.setKey(e, n);//设置密钥对
        ari.savePrivateKey(dir, fileName + ".key", d, n);
        ari.encrypt(str, dir, fileName);
        stateLabelName = "加密成功，文件夹在桌面等你哦~~";
        return 0;
    }

    public void fresh() {//用来更新窗体的各个组件
        if (stateLabelName != null && timeLabelName != null) {
            stateLabel.setText(stateLabelName);
            stateLabel.setVisible(true);
            timeLabel.setText(timeLabelName);
            timeLabel.setVisible(true);
        }
        if (ari.getD() == null || ifDatabaseEncrypt == true) {//快速加密不需要保存解密密钥
            savePrivateKeyButton.setVisible(false);
        }
        if (ifAccleratorChanged == true) {
            //用以刷新快捷键
            menuBar = new JMenuBar();
            menu1 = new JMenu("菜单");
            menu2 = new JMenu("其他");
            localEncryptItem = new JMenuItem("常规加密", new ImageIcon("image\\local_encrypt.png"));//本地加密菜单项
            localEncryptItem.addActionListener(this);
            databaseEncryptItem = new JMenuItem("快速加密", new ImageIcon("image\\database_encrypt.png"));//数据库加密菜单项
            databaseEncryptItem.addActionListener(this);
            decryptItem = new JMenuItem("解密", new ImageIcon("image\\decrypt.png"));//解密菜单项
            decryptItem.addActionListener(this);
            try {
                localEncryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[1]), Integer.parseInt(acclerator[0])));//设置快捷键
                databaseEncryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[3]), Integer.parseInt(acclerator[2])));
                decryptItem.setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(acclerator[7]), Integer.parseInt(acclerator[6])));
            } catch (Exception e) {
                System.out.println(e);
            }
            aboutFrameItem = new JMenuItem("关于", new ImageIcon("image\\about.png"));
            aboutFrameItem.addActionListener(this);
            settingItem = new JMenuItem("设置", new ImageIcon("image\\setting.png"));
            settingItem.addActionListener(this);
            helpItem = new JMenuItem("帮助", new ImageIcon("image\\help.png"));
            helpItem.addActionListener(this);
            menu1.add(localEncryptItem);
            if (modify.ifConnect == true) {//如果数据库连接成功，显示数据库加密菜单项
                menu1.add(databaseEncryptItem);
            }
            menu1.addSeparator();//添加分隔符
            menu1.add(decryptItem);
            //menu.add(loopdecryptItem);//处于测试阶段，暂不放出
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
                    if (ari.encrypted == false) {//如果加密操作还未完成
                        JOptionPane.showMessageDialog(this, "加密操作正在进行，请勿重复操作！");
                    } else if (ari.decrypted == false) {//如果解密操作还未完成
                        JOptionPane.showMessageDialog(this, "解密操作正在进行，请稍后再进行加密操作！");
                    } else {
                        savePrivateKeyButton.setVisible(false);
                        ifDatabaseEncrypt = false;//不是数据库加密
                        dir = fileOpenTxtChooser.getCurrentDirectory();//获取文件位置与文件名
                        fileName = fileOpenTxtChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);//设置文件
                        int index = fileName.lastIndexOf(".txt");
                        if (index == fileName.length() - 4) {//如果是txt文件，将明文显示在upText上
                            upText.setText(null);
                            upText.append("明文：" + "\n");
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
                            ari.encrypted = false;//加密操作开始进行，将加密操作的状态设置为false
                            ari.creatThread();//创建线程
                            ari.creat_label_Thread();//创建label_Thread
                            ari.creatPandQ_Thread.start();//启动creatPandQ_Thread
                            stopButton.setVisible(true);//显示停止按钮
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "请选择一个txt文件！", "错误", JOptionPane.ERROR_MESSAGE);
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
                    if (ari.encrypted == false) {//加密操作还未完成
                        JOptionPane.showMessageDialog(this, "加密操作正在进行，请勿重复操作！");
                    } else if (ari.decrypted == false) {//解密操作还未完成
                        JOptionPane.showMessageDialog(this, "解密操作正在进行，请稍后再进行加密操作！");
                    } else {
                        if (ifSavedKey == false) {
                            state = JOptionPane.showConfirmDialog(null, "解密密钥尚未保存，是否保存？", "确认对话框", JOptionPane.YES_NO_OPTION);
                            if (state == JOptionPane.YES_OPTION) {//用户点击“是”
                                ari.savePrivateKey(dir);//保存解密密钥
                            }
                        }
                        ifSavedKey = true;
                        dir = fileOpenTxtChooser.getCurrentDirectory();
                        fileName = fileOpenTxtChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);
                        int indexa = fileName.lastIndexOf(".txt");
                        if (indexa == fileName.length() - 4) {
                            ifDatabaseEncrypt = true;
                            RSA.ifSentToDataBase = false;//如果是采用数据库加密操作，则不需要将密钥对送入数据库中
                            stopButton.setVisible(true);
                            savePrivateKeyButton.setVisible(false);
                            stateLabelName = "从数据库中获取加密密钥中...";
                            modify.setDatasourceName(RSA.datasourcrName);
                            modify.setTableName(RSA.tableName);
                            modify.setSQL("SELECT * FROM " + RSA.tableName);
                            String data[][] = modify.getDataBase();//获取数据库的信息
                            int range = modify.rangeOfDataBase();
                            Random random = new Random();
                            int index = random.nextInt(range);//在数据库的长度的范围内随即取出一行作为加密的密钥对
                            BigInteger n = new BigInteger(data[index][0]);
                            BigInteger e = new BigInteger(data[index][1]);
                            ari.setKey(e, n);//设置密钥对
                            ari.encrypted = false;//加密状态设为false
                            ari.creatThread();
                            ari.creat_label_Thread();
                            ari.encrypt_Thread.start();//只需启动encrypt_Thread，无需其他线程
                            stopButton.setVisible(true);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "请选择一个txt文件！", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    break;
                }
            }
        } else if (ex.getSource() == decryptItem) {
            savePrivateKeyButton.setVisible(false);//解密操作不需要保存密钥对，将savePrivateKeyButton隐藏
            while (true) {
                int state = fileOpenRsaChooser.showOpenDialog(this);
                if (state == JFileChooser.APPROVE_OPTION) {
                    if (ari.encrypted == false) {//加密还未完成
                        JOptionPane.showMessageDialog(this, "加密操作正在进行，请稍后再进行解密操作！");
                    } else if (ari.decrypted == false) {//解密还未完成
                        JOptionPane.showMessageDialog(this, "解密操作正在进行，请勿重复操作！");
                    } else {
                        dir = fileOpenRsaChooser.getCurrentDirectory();
                        fileName = fileOpenRsaChooser.getSelectedFile().getName();
                        ari.setFile(dir, fileName);
                        int index = fileName.lastIndexOf(".rsa");
                        if (index == fileName.length() - 4) {//如果是rsa文件
                            ari.decrypted = false;
                            ari.creatThread();
                            ari.decrypt_Thread.start();
                            stopButton.setVisible(true);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(this, "请选择一个rsa文件！", "错误", JOptionPane.ERROR_MESSAGE);
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
        } else if (ex.getSource() == stopButton) {//停止
            ari.terminateThread();//销毁线程
            ari.terminateKey();//销毁密钥对
            JOptionPane.showMessageDialog(this, "终止操作成功！");
            if (ari.encrypted == false || ari.decrypted == true) {//判断如果当前终止的操作是加密操作
                String name[] = fileName.split(".txt");//拆分字符串，获取文件名，转成encrypt XXXX.rsa
                deleteFile = new File(dir, "encrypt " + name[0] + ".rsa");
                stopButton.setVisible(false);
                if (deleteFile.exists()) {//如果要删除的尚未完成的文件存在
                    deleteFile.delete();
                    System.out.println("删除" + deleteFile.getName() + "成功");
                    stateLabelName = "用户取消操作！未完成的加密文件已删除！";
                } else {
                    System.out.println("文件" + deleteFile + "不存在");
                    stateLabelName = "用户取消操作！";
                }
            } else if (ari.encrypted == true || ari.decrypted == false) {//判如果当前终止的操作是解密操作
                String name[];
                String name1[];//拆分字符串，获取原文件名，转成decrypt XXXX.txt
                if (fileName.contains("encrypt")) {//如果当前文件名包含encrypt
                    name = fileName.split("encrypt");
                    name1 = name[1].split(".rsa");
                    deleteFile = new File(dir, "decrypt " + name1[0] + ".txt");
                    stopButton.setVisible(false);
                    if (deleteFile.exists()) {
                        deleteFile.delete();
                        stateLabelName = "用户取消操作！未完成的解密文件已删除！";
                        System.out.println("删除" + deleteFile.getName() + "成功");
                    } else {
                        stateLabelName = "用户取消操作！";
                        System.out.println("文件" + deleteFile + "不存在");
                    }
                } else {
                    name = fileName.split(".rsa");
                    deleteFile = new File(dir, "decrypt " + name[0] + ".txt");
                    if (deleteFile.exists()) {
                        deleteFile.delete();
                        stateLabelName = "用户取消操作！未完成的解密文件已删除！";
                        System.out.println("删除" + deleteFile.getName() + "成功");
                    } else {
                        System.out.println("文件" + deleteFile + "不存在");
                    }
                }
            }
            System.out.println("\n用户终止操作！\n");
            ari.decrypted = true;
            ari.encrypted = true;
        } else if (ex.getSource() == savePrivateKeyButton) {
            ifSavedKey = true;//用户已经点击保存解密密钥按钮
            ari.savePrivateKey(dir);
        } else if (ex.getSource() == timer) {
            this.fresh();
        }
//        else if (ex.getSource() == timerOpen) {
//            //System.out.println("x1=" + x1 + "       x2=" + y1);
//            int x = RSA.length;
//            int y = RSA.width;
//            if (x1 < x) {
//                setLocationRelativeTo(null);//将窗口显示在屏幕中央
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
//            if (x1 >= x && y1 >= y) {//窗口展开完毕
//                timerOpen.stop();
//            }
//        }
    }
}

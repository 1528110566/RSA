package rsa;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Arithmetic implements Runnable {

    private BigInteger p, q, n, Qn, e, d, data;//p,q,n,Qn,e,d为加密的各个参数，data为数据加密后生成的密文
    Random random;//随机类，用来生成参数p,q
    WriteFile write;
    StringBuffer source;//在解密中，用来存储揭秘出来的数据，之后再进行拆分操作
    Thread creatPandQ_Thread,//创建p,q的线程
            calculateNandQn_Thread,//计算n,Qn的线程
            creatE_Thread,//创建e的线程
            calculateD_Thread,//计算d的线程
            encrypt_Thread,//加密的线程
            decrypt_Thread,//解密的线程
            label_Thread;//改变主窗口label的线程
    boolean encrypted = true;//加密状态设为true（加密完成）
    boolean decrypted = true;//解密状态设为true（解密完成）
    public static String fileName;//文件名称
    public static File dir;//文件位置
    String[][] databaseData;//从数据库读取出来的信息
    JFileChooser fileOpenKeyChooser;//选择key文件（解密密钥）对话框
    String str;//用来判断解密后的数据中是否有“\n”，有的话分行

    Arithmetic() {
        creatPandQ_Thread = new Thread(this);
        creatPandQ_Thread.setName("creatPandQ");
        calculateNandQn_Thread = new Thread(this);
        calculateNandQn_Thread.setName("calculateNandQn");
        creatE_Thread = new Thread(this);
        creatE_Thread.setName("creatE");
        calculateD_Thread = new Thread(this);
        calculateD_Thread.setName("calculateD");
        encrypt_Thread = new Thread(this);
        encrypt_Thread.setName("encrypt");
        decrypt_Thread = new Thread(this);
        decrypt_Thread.setName("decrypt");
        label_Thread = new Thread(this);
        label_Thread.setName("label");
    }

    BigInteger getD() {
        return d;
    }

    void setFile(File dir, String fileName) {
        this.dir = dir;
        this.fileName = fileName;
    }

    void setKey(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    void creatThread() {
        creatPandQ_Thread = new Thread(this);
        creatPandQ_Thread.setName("creatPandQ");
        calculateNandQn_Thread = new Thread(this);
        calculateNandQn_Thread.setName("calculateNandQn");
        creatE_Thread = new Thread(this);
        creatE_Thread.setName("creatE");
        calculateD_Thread = new Thread(this);
        calculateD_Thread.setName("calculateD");
        encrypt_Thread = new Thread(this);
        encrypt_Thread.setName("encrypt");
        decrypt_Thread = new Thread(this);
        decrypt_Thread.setName("decrypt");
    }

    void terminateThread() {
        if (this.creatPandQ_Thread.isAlive()) {
            this.creatPandQ_Thread.stop();
        }
        if (this.calculateNandQn_Thread.isAlive()) {
            this.calculateNandQn_Thread.stop();
        }
        if (this.creatE_Thread.isAlive()) {
            this.creatE_Thread.stop();
        }
        if (this.calculateD_Thread.isAlive()) {
            this.calculateD_Thread.stop();
        }
        if (this.encrypt_Thread.isAlive()) {
            this.encrypt_Thread.stop();
        }
        if (this.decrypt_Thread.isAlive()) {
            this.decrypt_Thread.stop();
        }
        if (this.label_Thread.isAlive()) {
            this.label_Thread.stop();
        }
    }

    void creat_label_Thread() {
        label_Thread = new Thread(this);
        label_Thread.setName("label");
    }

    void changelabel() {
        int i = 0;
        while (this.encrypted == false || this.decrypted == false) {
            try {
                MainFrame.timeLabelName = "总时长：" + i + "秒";
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            if (this.encrypted == false && this.decrypted == true) {//加密中
                if (i > 10 && i > 20) {
                    MainFrame.stateLabelName = "疯狂加密中...";
                } else if (i > 20) {
                    MainFrame.stateLabelName = "加密加不动了...    >.<";
                }
            } else if (this.encrypted == true && this.decrypted == false) {//解密中
                if (i > 10 && i < 20) {
                    MainFrame.stateLabelName = "疯狂解密中...";
                } else if (i > 20) {
                    MainFrame.stateLabelName = "解密解不动了...";
                }
            }
        }
    }

    void creatPandQ() {
        System.out.println("创建参数p、q中...");
        MainFrame.stateLabelName = "加密前准备中，请稍后...(1/4)";
        while (true) {
            p = new BigInteger(520, new Random());
            if (p.isProbablePrime(10) && p.bitLength() >= 500) {//判断创建的p是否为素数，是否大于500位
                while (true) {
                    q = new BigInteger(520, new Random());
                    if (q.isProbablePrime(10) && q.bitLength() >= 500) {//判断创建的q是否为素数，是否大于500位
                        System.out.println("p=" + p);
                        System.out.println("q=" + q);
                        break;
                    }
                }
                break;
            }
        }
    }

    void calculateNandQn() {
        System.out.println("计算参数n、Qn中...");
        MainFrame.stateLabelName = "加密前准备中，请稍后...(2/4)";
        n = p.multiply(q);//p*q
        Qn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));//(p-1)*(q-1)
        System.out.println("n=" + n);
        System.out.println("Qn=" + Qn);
    }

    void creatE() {
        System.out.println("创建参数e中...");
        MainFrame.stateLabelName = "加密前准备中，请稍后...(3/4)";
        while (true) {
            e = new BigInteger(20, new Random());
            if (Qn.gcd(e).compareTo(new BigInteger("1")) == 0
                    && e.compareTo(Qn) < 0
                    && e.bitLength() >= 16) {
                System.out.println("e=" + e);
                break;
            }
        }
    }

    void calculateD() {//需要d，Qn，e
        System.out.println("计算参数d中...");
        MainFrame.stateLabelName = "加密前准备中，请稍后...(4/4)";
        BigInteger a;//a作为参数来循环计算d
        for (a = new BigInteger("1");; a = a.add(new BigInteger("1"))) {
            d = ((a.multiply(Qn)).add(new BigInteger("1"))).divide(e);//((a*Qn)+1)/e
            if ((d.multiply(e)).mod(Qn).compareTo(new BigInteger("1")) == 0) {//如果(d*e)modQn==1，则退出循环
                System.out.println("d=" + d);
                break;
            }
        }
    }

    void savePrivateKey(File dir, String fileName, BigInteger d, BigInteger n) {//保存解密密钥
        write = new WriteFile(dir, fileName);//设置文件位置与加密文件相同
        write.write(d.toString());//保存d
        write.write("\n");
        write.write(n.toString());//保存n
        write.writeOver();
        //JOptionPane.showMessageDialog(null, "密钥保存成功！");
    }

    void savePrivateKey(File dir) {//保存解密密钥
        write = new WriteFile(dir, "PrivateKey.key");//设置文件位置与加密文件相同
        write.write(d.toString());//保存d
        write.write("\n");
        write.write(n.toString());//保存n
        write.writeOver();
        JOptionPane.showMessageDialog(null, "密钥保存成功！");
    }

    void setPrivateKey(BigInteger d, BigInteger n) {//设置解密密钥
        this.d = d;
        this.n = n;
    }

    void encrypt(String str, File dir, String fileName) {
        write = new WriteFile(dir, fileName + ".txt");
        write.write(str);
        write.write("\n");
        write.writeOver();
        write = new WriteFile(dir, fileName + ".rsa");
        char[] c = str.toCharArray();
        MainFrame.downText.append("密文：\n");
        for (int i = 0; i < str.length(); i++) {
            data = BigInteger.valueOf((int) c[i]);
            String s = data.modPow(e, n).toString();
            write.write(s);
            MainFrame.downText.append(s);
            MainFrame.downText.append("\n");
            write.write("\n");
        }
        write.writeOver();
    }

    void encrypt(File dir, String fileName) {
        this.encrypted = false;//将加密状态设为false
        MainFrame.ifSavedKey = false;//用户没有保存密钥
        MainFrame.stopButton.setVisible(true);//显示主窗口的停止按钮
        MainFrame.savePrivateKeyButton.setVisible(false);//隐藏主窗口的保存密钥按钮
        if (MainFrame.ifDatabaseEncrypt == true) {//如果是数据库加密，则刷新两个文本区域
            MainFrame.upText.setText(null);
            MainFrame.downText.setText(null);
            MainFrame.upText.append("明文：\n");
            MainFrame.ifSavedKey = true;//数据库加密不需要保存密钥
        }
        System.out.println("加密中...");
        MainFrame.stateLabelName = "加密中...";
        this.sentToDataBase();
        MainFrame.downText.setText(null);
        MainFrame.downText.append("密文：\n");//初始化MainFrame的text内容
        ReadFile read = new ReadFile(dir, fileName);
        String sourceData[] = read.readData();
        String name[] = fileName.split(".txt");//拆分字符串，获取文件名，转成encrypt XXXX.rsa
        this.fileName = "encrypt" + name[0] + ".rsa";
        this.dir = dir;
        write = new WriteFile(dir, "encrypt" + name[0] + ".rsa");
        char[] c;
        for (int i = 0; i < sourceData.length; i++) {
            if (sourceData[i] != null) {
                if (MainFrame.ifDatabaseEncrypt == true) {
                    if (sourceData[i].equals("\\n")) {
                        MainFrame.upText.append("\n");
                    } else {
                        MainFrame.upText.append(sourceData[i]);
                    }
                }
                c = sourceData[i].toCharArray();
                for (int j = 0; j < c.length; j++) {
                    data = BigInteger.valueOf((int) c[j]);
                    String s = data.modPow(e, n).toString();
                    write.write(s);
                    MainFrame.downText.append(s);
                    MainFrame.downText.append("\n");
                    write.write("\n");
                }
            }
        }
        write.writeOver();
        String message = null;//设置两种加密后的提示信息
        if (MainFrame.ifDatabaseEncrypt == true) {//如梭是采用数据库加密
            message = "数据库加密操作成功！加密文件已经保存！";
        } else {                                    //如果是本地加密
            message = "本地加密操作成功！加密文件已经保存！";
        }
        System.out.println(message);
        MainFrame.stateLabelName = message;
        encrypted = true;//设置加密状态为true
        MainFrame.stopButton.setVisible(false);//隐藏停止按钮
        MainFrame.savePrivateKeyButton.setVisible(true);//显示保存解密密钥按钮
        if (MainFrame.ifDatabaseEncrypt == true) {//如果是数据库加密则设置d为0以便重复操作时的判断
            d = new BigInteger("0");
        }
    }

    void decrypt(File dir, String fileName) {
        this.decrypted = false;//设置解密状态为false

        MainFrame.upText.setText(null);//刷新MainFrame的两个文本域
        MainFrame.downText.setText(null);

        MainFrame.stopButton.setVisible(true);//显示停止按钮
        System.out.println("解密中...");
        MainFrame.stateLabelName = "解密中...";//改变MainFrame的标签
        source = new StringBuffer();//实例化source，以便重复操作时的source刷新
        ReadFile read = new ReadFile(dir, fileName);//实例化ReadFile来读取rsa文件（已加密的文件）
        String[] encryptData = read.readEncryptData();//读取rsa文件
        MainFrame.upText.append("密文：\n");//初始化MainFrame的text内容
        for (int i = 0; i < encryptData.length; i++) {//将密文填写至文本框中
            if (encryptData[i] != null) {//如果后继的值不为null
                MainFrame.upText.append(encryptData[i]);//将密文写入文本域中
                MainFrame.upText.append("\n");
            }
        }
        MainFrame.downText.append("明文：\n");
        String name[];//name与name1用来拆分文件名
        String name1[];
        if (fileName.contains("encrypt")) {//如果文件名包含encrypt（即通过加密操作后得到的rsa文件）
            name = fileName.split("encrypt");
            name1 = name[1].split(".rsa");//拆分字符串，获取原文件名，转成decrypt XXXX.txt
            this.fileName = "decrypt" + name1[0] + ".txt";
            this.dir = dir;
            write = new WriteFile(dir, "decrypt" + name1[0] + ".txt");
        } else {//如果文件名包含encrypt（即没有通过加密操作后得到的rsa文件或者用户更改了文件名）
            name = fileName.split(".rsa");
            this.fileName = "decrypt" + name[0] + ".txt";
            this.dir = dir;
            write = new WriteFile(dir, "decrypt" + name[0] + ".txt");
        }
        BigInteger encryptDataa;//中间参数，用来转换从文件读取出来的密文（String转换为BigInteger）,再进行解密算法
        if (d != null && n != null && d.compareTo(new BigInteger("0")) != 0 && n.compareTo(new BigInteger("0")) != 0) {//如果内存中有解密密钥（本地加密后不退出程序再进行解密操作）
            //解密操作
            this.creat_label_Thread();
            label_Thread.start();
            for (int i = 0; i < encryptData.length; i++) {
                if (encryptData[i] != null) {
                    encryptDataa = new BigInteger(encryptData[i]);
                    int c = encryptDataa.modPow(d, n).intValue();//将密文解密后转为int型
                    source.append((char) c);//添加至source，准备拆分
                }
            }
        } else {//内存中没有解密密钥（退出程序或者选择的是数据库加密）
            MainFrame.stateLabelName = "选择解密方式中...";
            System.out.println("选择解密方式中...");
            if (ModifyTable.ifConnect == true) {
                int state = JOptionPane.showConfirmDialog(null, "检测到内存中没有加密密钥，"
                        + "是否连接数据库解密？", "选择对话框", JOptionPane.YES_NO_OPTION);
                if (state == JOptionPane.YES_OPTION) {
                    ModifyTable modify = new ModifyTable();
                    this.creat_label_Thread();
                    label_Thread.start();
                    modify.setDatasourceName(RSA.datasourcrName);
                    modify.setTableName(RSA.tableName);
                    modify.setSQL("SELECT * FROM " + RSA.tableName);
                    databaseData = modify.getDataBase();//读取数据库资料至内存
                    for (int j = 0; j < databaseData.length; j++) {
                        MainFrame.stateLabelName = "在数据库中寻找适配密钥中，请稍后...";
                        n = new BigInteger(databaseData[j][0]);
                        e = new BigInteger(databaseData[j][1]);
                        d = new BigInteger(databaseData[j][2]);

                        encryptDataa = new BigInteger(encryptData[0]);//取密文中的第一行数据
                        int c = encryptDataa.modPow(d, n).intValue();//将第一行密文解密后生成一个int型数据
                        BigInteger biginteger = new BigInteger(String.valueOf(c));//将上一行得到的int型数据再加密
                        if (encryptDataa.compareTo(biginteger.modPow(e, n)) == 0) {//如果再加密得到的数据与从文件读取出来的数据相同，则说明再数据库中找到了正确的解密密钥
                            //解密操作
                            System.out.println("密钥位于数据库的位置:" + j);
                            MainFrame.stateLabelName = "解密中...";
                            for (int i = 0; i < encryptData.length; i++) {
                                if (encryptData[i] != null) {
                                    MainFrame.upText.append(encryptData[i]);
                                    MainFrame.upText.append("\n");
                                    encryptDataa = new BigInteger(encryptData[i]);
                                    c = encryptDataa.modPow(d, n).intValue();
                                    source.append((char) c);
                                }
                            }
                            break;
                        }
                        if (j >= databaseData.length - 1) {//没有找到解密密钥
                            MainFrame.stateLabelName = "数据库中无适配解密密钥！解密失败！";
                            this.decrypted = true;
                            MainFrame.stopButton.setVisible(false);//将停止按钮的菜单隐藏
                            d = new BigInteger("0");//将d的值初始化
                            System.out.println("取消");
                            this.label_Thread.stop();//终止线程
                            this.terminateThread();//销毁线程
                        }
                    }
                } else if (state == JOptionPane.NO_OPTION) {//用户点击“否”，系统判定用户要选择解密密钥来解密
                    JOptionPane.showMessageDialog(null, "请选择解密密钥");
                    fileOpenKeyChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("key文件", "key");//弹出选择key文件的对话框
                    fileOpenKeyChooser.setFileFilter(filter);
                    int state1 = fileOpenKeyChooser.showOpenDialog(null);
                    while (true) {
                        if (state1 == JFileChooser.APPROVE_OPTION) {
                            File dir1 = fileOpenKeyChooser.getCurrentDirectory();//获取文件位置
                            String fileName1 = fileOpenKeyChooser.getSelectedFile().getName();//获取文件名
                            int index = fileName1.lastIndexOf(".key");//判断用户选择的是否为key文件
                            if (index == fileName1.length() - 4) {//是key文件
                                MainFrame.stateLabelName = "解密中...";
                                this.creat_label_Thread();
                                label_Thread.start();
                                read = new ReadFile(dir1, fileName1);
                                String[] privateKay = read.readEncryptData();//读取的密钥
                                this.d = new BigInteger(privateKay[0]);
                                this.n = new BigInteger(privateKay[1]);
                                for (int i = 0; i < encryptData.length; i++) {
                                    if (encryptData[i] != null) {
                                        MainFrame.upText.append(encryptData[i]);
                                        MainFrame.upText.append("\n");
                                        encryptDataa = new BigInteger(encryptData[i]);
                                        int c = encryptDataa.modPow(d, n).intValue();
                                        source.append((char) c);
                                    }
                                }
                                break;
                            } else {//不是key文件
                                JOptionPane.showMessageDialog(null, "请选择一个key文件！", "错误", JOptionPane.ERROR_MESSAGE);
                                state1 = fileOpenKeyChooser.showOpenDialog(null);
                            }
                        } else {
                            MainFrame.stateLabelName = "操作终止！因用户未选择任何解密方式！";
                            MainFrame.stopButton.setVisible(false);
                            this.creat_label_Thread();
                            decrypted = true;
                            encrypted = true;
                            this.terminateThread();
                        }
                    }
                }
            } else {//用户选择key文件解密
                JOptionPane.showMessageDialog(null, "请选择解密密钥");
                fileOpenKeyChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("key文件", "key");//弹出选择key文件的对话框
                fileOpenKeyChooser.setFileFilter(filter);
                int state1 = fileOpenKeyChooser.showOpenDialog(null);
                while (true) {
                    if (state1 == JFileChooser.APPROVE_OPTION) {
                        File dir1 = fileOpenKeyChooser.getCurrentDirectory();//获取文件位置
                        String fileName1 = fileOpenKeyChooser.getSelectedFile().getName();//获取文件名
                        int index = fileName1.lastIndexOf(".key");//判断用户选择的是否为key文件
                        if (index == fileName1.length() - 4) {//是key文件
                            MainFrame.stateLabelName = "解密中...";
                            this.creat_label_Thread();
                            label_Thread.start();
                            read = new ReadFile(dir1, fileName1);
                            String[] privateKay = read.readEncryptData();//读取的密钥
                            this.d = new BigInteger(privateKay[0]);
                            this.n = new BigInteger(privateKay[1]);
                            for (int i = 0; i < encryptData.length; i++) {
                                if (encryptData[i] != null) {
                                    MainFrame.upText.append(encryptData[i]);
                                    MainFrame.upText.append("\n");
                                    encryptDataa = new BigInteger(encryptData[i]);
                                    int c = encryptDataa.modPow(d, n).intValue();
                                    source.append((char) c);
                                }
                            }
                            break;
                        } else {//不是key文件
                            JOptionPane.showMessageDialog(null, "请选择一个key文件！", "错误", JOptionPane.ERROR_MESSAGE);
                            state1 = fileOpenKeyChooser.showOpenDialog(null);
                        }
                    } else {
                        MainFrame.stateLabelName = "操作终止！因用户未选择任何解密方式！";
                        MainFrame.stopButton.setVisible(false);
                        this.creat_label_Thread();
                        decrypted = true;
                        encrypted = true;
                        this.terminateThread();
                    }
                }
            }
        }
        String a = new String(source);//分割以“\n”为分割符的字符串（因为使用split()方法有问题，所以用的自己写的分割方法）
        if (a.contains("\\n")) {
            int b = 0;
            b = a.indexOf("\\n", b);
            int b1 = 0;
            str = a.substring(0, b);
            write.write(str);
            write.write("\n");
            MainFrame.downText.append(str);
            MainFrame.downText.append("\n");
            for (; b < a.lastIndexOf("\\n");) {
                b1 = b + 2;
                b = a.indexOf("\\n", b + 1);
                str = a.substring(b1, b);
                System.out.println(str);
                MainFrame.downText.append(str);//在MainFrame窗口显示解密出来的内容
                MainFrame.downText.append("\n");
                write.write(str);//将解密后的内容写入到文件中
                write.write("\n");
            }
        } else {//这种情况为解密密钥不匹配
            MainFrame.downText.append(a);//在MainFrame窗口显示解密出来的内容
            MainFrame.downText.append("\n");
            write.write(a);//将解密后的内容写入到文件中
            write.write("\n");
        }
        write.writeOver();
        str = "";
        System.out.println("解密完成!");
        this.decrypted = true;
        MainFrame.stopButton.setVisible(false);
        MainFrame.stateLabelName = "解密完成！解密文件已经保存！";
    }

    void sentToDataBase() {
        if (RSA.ifSentToDataBase == true) {//如果要写入数据库中
            ModifyTable modify = new ModifyTable();
            System.out.println("已写入到数据库中");
            modify.setDatasourceName(RSA.datasourcrName);
            modify.setTableName(RSA.tableName);
            modify.setSQL("INSERT INTO " + RSA.tableName + " VALUES '" + n + "','" + e + "','" + d + "'");
            modify.insertData();
        }
    }

    public void run() {
        switch (Thread.currentThread().getName()) {
            case "creatPandQ":
                label_Thread.start();
                this.creatPandQ();
                calculateNandQn_Thread.start();
                break;
            case "calculateNandQn":
                this.calculateNandQn();
                creatE_Thread.start();
                break;
            case "creatE":
                this.creatE();
                calculateD_Thread.start();
                break;
            case "calculateD":
                this.calculateD();
                encrypt_Thread.start();
                break;
            case "encrypt":
                if (!label_Thread.isAlive()) {
                    label_Thread.start();
                }
                this.encrypt(dir, fileName);
                break;
            case "decrypt":
                this.decrypt(dir, fileName);
                break;
            case "label":
                this.changelabel();
                break;
            default:
                break;
        }
        System.out.println("线程" + Thread.currentThread().getName() + "结束");
    }

    public void terminateKey() {//销毁密钥对
        d = new BigInteger("0");
    }
}

package rsa;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Arithmetic implements Runnable {

    private BigInteger p, q, n, Qn, e, d, data;//p,q,n,Qn,e,dΪ���ܵĸ���������dataΪ���ݼ��ܺ����ɵ�����
    Random random;//����࣬�������ɲ���p,q
    WriteFile write;
    StringBuffer source;//�ڽ����У������洢���س��������ݣ�֮���ٽ��в�ֲ���
    Thread creatPandQ_Thread,//����p,q���߳�
            calculateNandQn_Thread,//����n,Qn���߳�
            creatE_Thread,//����e���߳�
            calculateD_Thread,//����d���߳�
            encrypt_Thread,//���ܵ��߳�
            decrypt_Thread,//���ܵ��߳�
            label_Thread;//�ı�������label���߳�
    boolean encrypted = true;//����״̬��Ϊtrue��������ɣ�
    boolean decrypted = true;//����״̬��Ϊtrue��������ɣ�
    public static String fileName;//�ļ�����
    public static File dir;//�ļ�λ��
    String[][] databaseData;//�����ݿ��ȡ��������Ϣ
    JFileChooser fileOpenKeyChooser;//ѡ��key�ļ���������Կ���Ի���
    String str;//�����жϽ��ܺ���������Ƿ��С�\n�����еĻ�����

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
                MainFrame.timeLabelName = "��ʱ����" + i + "��";
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            if (this.encrypted == false && this.decrypted == true) {//������
                if (i > 10 && i > 20) {
                    MainFrame.stateLabelName = "��������...";
                } else if (i > 20) {
                    MainFrame.stateLabelName = "���ܼӲ�����...    >.<";
                }
            } else if (this.encrypted == true && this.decrypted == false) {//������
                if (i > 10 && i < 20) {
                    MainFrame.stateLabelName = "��������...";
                } else if (i > 20) {
                    MainFrame.stateLabelName = "���ܽⲻ����...";
                }
            }
        }
    }

    void creatPandQ() {
        System.out.println("��������p��q��...");
        MainFrame.stateLabelName = "����ǰ׼���У����Ժ�...(1/4)";
        while (true) {
            p = new BigInteger(520, new Random());
            if (p.isProbablePrime(10) && p.bitLength() >= 500) {//�жϴ�����p�Ƿ�Ϊ�������Ƿ����500λ
                while (true) {
                    q = new BigInteger(520, new Random());
                    if (q.isProbablePrime(10) && q.bitLength() >= 500) {//�жϴ�����q�Ƿ�Ϊ�������Ƿ����500λ
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
        System.out.println("�������n��Qn��...");
        MainFrame.stateLabelName = "����ǰ׼���У����Ժ�...(2/4)";
        n = p.multiply(q);//p*q
        Qn = (p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1")));//(p-1)*(q-1)
        System.out.println("n=" + n);
        System.out.println("Qn=" + Qn);
    }

    void creatE() {
        System.out.println("��������e��...");
        MainFrame.stateLabelName = "����ǰ׼���У����Ժ�...(3/4)";
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

    void calculateD() {//��Ҫd��Qn��e
        System.out.println("�������d��...");
        MainFrame.stateLabelName = "����ǰ׼���У����Ժ�...(4/4)";
        BigInteger a;//a��Ϊ������ѭ������d
        for (a = new BigInteger("1");; a = a.add(new BigInteger("1"))) {
            d = ((a.multiply(Qn)).add(new BigInteger("1"))).divide(e);//((a*Qn)+1)/e
            if ((d.multiply(e)).mod(Qn).compareTo(new BigInteger("1")) == 0) {//���(d*e)modQn==1�����˳�ѭ��
                System.out.println("d=" + d);
                break;
            }
        }
    }

    void savePrivateKey(File dir, String fileName, BigInteger d, BigInteger n) {//���������Կ
        write = new WriteFile(dir, fileName);//�����ļ�λ��������ļ���ͬ
        write.write(d.toString());//����d
        write.write("\n");
        write.write(n.toString());//����n
        write.writeOver();
        //JOptionPane.showMessageDialog(null, "��Կ����ɹ���");
    }

    void savePrivateKey(File dir) {//���������Կ
        write = new WriteFile(dir, "PrivateKey.key");//�����ļ�λ��������ļ���ͬ
        write.write(d.toString());//����d
        write.write("\n");
        write.write(n.toString());//����n
        write.writeOver();
        JOptionPane.showMessageDialog(null, "��Կ����ɹ���");
    }

    void setPrivateKey(BigInteger d, BigInteger n) {//���ý�����Կ
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
        MainFrame.downText.append("���ģ�\n");
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
        this.encrypted = false;//������״̬��Ϊfalse
        MainFrame.ifSavedKey = false;//�û�û�б�����Կ
        MainFrame.stopButton.setVisible(true);//��ʾ�����ڵ�ֹͣ��ť
        MainFrame.savePrivateKeyButton.setVisible(false);//���������ڵı�����Կ��ť
        if (MainFrame.ifDatabaseEncrypt == true) {//��������ݿ���ܣ���ˢ�������ı�����
            MainFrame.upText.setText(null);
            MainFrame.downText.setText(null);
            MainFrame.upText.append("���ģ�\n");
            MainFrame.ifSavedKey = true;//���ݿ���ܲ���Ҫ������Կ
        }
        System.out.println("������...");
        MainFrame.stateLabelName = "������...";
        this.sentToDataBase();
        MainFrame.downText.setText(null);
        MainFrame.downText.append("���ģ�\n");//��ʼ��MainFrame��text����
        ReadFile read = new ReadFile(dir, fileName);
        String sourceData[] = read.readData();
        String name[] = fileName.split(".txt");//����ַ�������ȡ�ļ�����ת��encrypt XXXX.rsa
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
        String message = null;//�������ּ��ܺ����ʾ��Ϣ
        if (MainFrame.ifDatabaseEncrypt == true) {//�����ǲ������ݿ����
            message = "���ݿ���ܲ����ɹ��������ļ��Ѿ����棡";
        } else {                                    //����Ǳ��ؼ���
            message = "���ؼ��ܲ����ɹ��������ļ��Ѿ����棡";
        }
        System.out.println(message);
        MainFrame.stateLabelName = message;
        encrypted = true;//���ü���״̬Ϊtrue
        MainFrame.stopButton.setVisible(false);//����ֹͣ��ť
        MainFrame.savePrivateKeyButton.setVisible(true);//��ʾ���������Կ��ť
        if (MainFrame.ifDatabaseEncrypt == true) {//��������ݿ����������dΪ0�Ա��ظ�����ʱ���ж�
            d = new BigInteger("0");
        }
    }

    void decrypt(File dir, String fileName) {
        this.decrypted = false;//���ý���״̬Ϊfalse

        MainFrame.upText.setText(null);//ˢ��MainFrame�������ı���
        MainFrame.downText.setText(null);

        MainFrame.stopButton.setVisible(true);//��ʾֹͣ��ť
        System.out.println("������...");
        MainFrame.stateLabelName = "������...";//�ı�MainFrame�ı�ǩ
        source = new StringBuffer();//ʵ����source���Ա��ظ�����ʱ��sourceˢ��
        ReadFile read = new ReadFile(dir, fileName);//ʵ����ReadFile����ȡrsa�ļ����Ѽ��ܵ��ļ���
        String[] encryptData = read.readEncryptData();//��ȡrsa�ļ�
        MainFrame.upText.append("���ģ�\n");//��ʼ��MainFrame��text����
        for (int i = 0; i < encryptData.length; i++) {//��������д���ı�����
            if (encryptData[i] != null) {//�����̵�ֵ��Ϊnull
                MainFrame.upText.append(encryptData[i]);//������д���ı�����
                MainFrame.upText.append("\n");
            }
        }
        MainFrame.downText.append("���ģ�\n");
        String name[];//name��name1��������ļ���
        String name1[];
        if (fileName.contains("encrypt")) {//����ļ�������encrypt����ͨ�����ܲ�����õ���rsa�ļ���
            name = fileName.split("encrypt");
            name1 = name[1].split(".rsa");//����ַ�������ȡԭ�ļ�����ת��decrypt XXXX.txt
            this.fileName = "decrypt" + name1[0] + ".txt";
            this.dir = dir;
            write = new WriteFile(dir, "decrypt" + name1[0] + ".txt");
        } else {//����ļ�������encrypt����û��ͨ�����ܲ�����õ���rsa�ļ������û��������ļ�����
            name = fileName.split(".rsa");
            this.fileName = "decrypt" + name[0] + ".txt";
            this.dir = dir;
            write = new WriteFile(dir, "decrypt" + name[0] + ".txt");
        }
        BigInteger encryptDataa;//�м����������ת�����ļ���ȡ���������ģ�Stringת��ΪBigInteger��,�ٽ��н����㷨
        if (d != null && n != null && d.compareTo(new BigInteger("0")) != 0 && n.compareTo(new BigInteger("0")) != 0) {//����ڴ����н�����Կ�����ؼ��ܺ��˳������ٽ��н��ܲ�����
            //���ܲ���
            this.creat_label_Thread();
            label_Thread.start();
            for (int i = 0; i < encryptData.length; i++) {
                if (encryptData[i] != null) {
                    encryptDataa = new BigInteger(encryptData[i]);
                    int c = encryptDataa.modPow(d, n).intValue();//�����Ľ��ܺ�תΪint��
                    source.append((char) c);//�����source��׼�����
                }
            }
        } else {//�ڴ���û�н�����Կ���˳��������ѡ��������ݿ���ܣ�
            MainFrame.stateLabelName = "ѡ����ܷ�ʽ��...";
            System.out.println("ѡ����ܷ�ʽ��...");
            if (ModifyTable.ifConnect == true) {
                int state = JOptionPane.showConfirmDialog(null, "��⵽�ڴ���û�м�����Կ��"
                        + "�Ƿ��������ݿ���ܣ�", "ѡ��Ի���", JOptionPane.YES_NO_OPTION);
                if (state == JOptionPane.YES_OPTION) {
                    ModifyTable modify = new ModifyTable();
                    this.creat_label_Thread();
                    label_Thread.start();
                    modify.setDatasourceName(RSA.datasourcrName);
                    modify.setTableName(RSA.tableName);
                    modify.setSQL("SELECT * FROM " + RSA.tableName);
                    databaseData = modify.getDataBase();//��ȡ���ݿ��������ڴ�
                    for (int j = 0; j < databaseData.length; j++) {
                        MainFrame.stateLabelName = "�����ݿ���Ѱ��������Կ�У����Ժ�...";
                        n = new BigInteger(databaseData[j][0]);
                        e = new BigInteger(databaseData[j][1]);
                        d = new BigInteger(databaseData[j][2]);

                        encryptDataa = new BigInteger(encryptData[0]);//ȡ�����еĵ�һ������
                        int c = encryptDataa.modPow(d, n).intValue();//����һ�����Ľ��ܺ�����һ��int������
                        BigInteger biginteger = new BigInteger(String.valueOf(c));//����һ�еõ���int�������ټ���
                        if (encryptDataa.compareTo(biginteger.modPow(e, n)) == 0) {//����ټ��ܵõ�����������ļ���ȡ������������ͬ����˵�������ݿ����ҵ�����ȷ�Ľ�����Կ
                            //���ܲ���
                            System.out.println("��Կλ�����ݿ��λ��:" + j);
                            MainFrame.stateLabelName = "������...";
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
                        if (j >= databaseData.length - 1) {//û���ҵ�������Կ
                            MainFrame.stateLabelName = "���ݿ��������������Կ������ʧ�ܣ�";
                            this.decrypted = true;
                            MainFrame.stopButton.setVisible(false);//��ֹͣ��ť�Ĳ˵�����
                            d = new BigInteger("0");//��d��ֵ��ʼ��
                            System.out.println("ȡ��");
                            this.label_Thread.stop();//��ֹ�߳�
                            this.terminateThread();//�����߳�
                        }
                    }
                } else if (state == JOptionPane.NO_OPTION) {//�û�������񡱣�ϵͳ�ж��û�Ҫѡ�������Կ������
                    JOptionPane.showMessageDialog(null, "��ѡ�������Կ");
                    fileOpenKeyChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("key�ļ�", "key");//����ѡ��key�ļ��ĶԻ���
                    fileOpenKeyChooser.setFileFilter(filter);
                    int state1 = fileOpenKeyChooser.showOpenDialog(null);
                    while (true) {
                        if (state1 == JFileChooser.APPROVE_OPTION) {
                            File dir1 = fileOpenKeyChooser.getCurrentDirectory();//��ȡ�ļ�λ��
                            String fileName1 = fileOpenKeyChooser.getSelectedFile().getName();//��ȡ�ļ���
                            int index = fileName1.lastIndexOf(".key");//�ж��û�ѡ����Ƿ�Ϊkey�ļ�
                            if (index == fileName1.length() - 4) {//��key�ļ�
                                MainFrame.stateLabelName = "������...";
                                this.creat_label_Thread();
                                label_Thread.start();
                                read = new ReadFile(dir1, fileName1);
                                String[] privateKay = read.readEncryptData();//��ȡ����Կ
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
                            } else {//����key�ļ�
                                JOptionPane.showMessageDialog(null, "��ѡ��һ��key�ļ���", "����", JOptionPane.ERROR_MESSAGE);
                                state1 = fileOpenKeyChooser.showOpenDialog(null);
                            }
                        } else {
                            MainFrame.stateLabelName = "������ֹ�����û�δѡ���κν��ܷ�ʽ��";
                            MainFrame.stopButton.setVisible(false);
                            this.creat_label_Thread();
                            decrypted = true;
                            encrypted = true;
                            this.terminateThread();
                        }
                    }
                }
            } else {//�û�ѡ��key�ļ�����
                JOptionPane.showMessageDialog(null, "��ѡ�������Կ");
                fileOpenKeyChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("key�ļ�", "key");//����ѡ��key�ļ��ĶԻ���
                fileOpenKeyChooser.setFileFilter(filter);
                int state1 = fileOpenKeyChooser.showOpenDialog(null);
                while (true) {
                    if (state1 == JFileChooser.APPROVE_OPTION) {
                        File dir1 = fileOpenKeyChooser.getCurrentDirectory();//��ȡ�ļ�λ��
                        String fileName1 = fileOpenKeyChooser.getSelectedFile().getName();//��ȡ�ļ���
                        int index = fileName1.lastIndexOf(".key");//�ж��û�ѡ����Ƿ�Ϊkey�ļ�
                        if (index == fileName1.length() - 4) {//��key�ļ�
                            MainFrame.stateLabelName = "������...";
                            this.creat_label_Thread();
                            label_Thread.start();
                            read = new ReadFile(dir1, fileName1);
                            String[] privateKay = read.readEncryptData();//��ȡ����Կ
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
                        } else {//����key�ļ�
                            JOptionPane.showMessageDialog(null, "��ѡ��һ��key�ļ���", "����", JOptionPane.ERROR_MESSAGE);
                            state1 = fileOpenKeyChooser.showOpenDialog(null);
                        }
                    } else {
                        MainFrame.stateLabelName = "������ֹ�����û�δѡ���κν��ܷ�ʽ��";
                        MainFrame.stopButton.setVisible(false);
                        this.creat_label_Thread();
                        decrypted = true;
                        encrypted = true;
                        this.terminateThread();
                    }
                }
            }
        }
        String a = new String(source);//�ָ��ԡ�\n��Ϊ�ָ�����ַ�������Ϊʹ��split()���������⣬�����õ��Լ�д�ķָ����
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
                MainFrame.downText.append(str);//��MainFrame������ʾ���ܳ���������
                MainFrame.downText.append("\n");
                write.write(str);//�����ܺ������д�뵽�ļ���
                write.write("\n");
            }
        } else {//�������Ϊ������Կ��ƥ��
            MainFrame.downText.append(a);//��MainFrame������ʾ���ܳ���������
            MainFrame.downText.append("\n");
            write.write(a);//�����ܺ������д�뵽�ļ���
            write.write("\n");
        }
        write.writeOver();
        str = "";
        System.out.println("�������!");
        this.decrypted = true;
        MainFrame.stopButton.setVisible(false);
        MainFrame.stateLabelName = "������ɣ������ļ��Ѿ����棡";
    }

    void sentToDataBase() {
        if (RSA.ifSentToDataBase == true) {//���Ҫд�����ݿ���
            ModifyTable modify = new ModifyTable();
            System.out.println("��д�뵽���ݿ���");
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
        System.out.println("�߳�" + Thread.currentThread().getName() + "����");
    }

    public void terminateKey() {//������Կ��
        d = new BigInteger("0");
    }
}

package rsa;

public class RSA {

    public static String datasourcrName = "Rsa.accdb";//�������ݿ��λ�����ͷ��
    public static String tableName = "RsaDataBase";
    public static boolean ifSentToDataBase = true;//����Ĭ���Ƿ���Կ���������ݿ�
    public static String accleratorFile = "Acclerator.txt";//���ÿ�ݼ��ļ���λ��
    public static int font = 15;//���ô�������������С
    public static int length = 450;//���������ڵĴ�С
    public static int width = 430;

    public static void main(String[] args) {
        new MainFrame();//ʵ����������
    }
}

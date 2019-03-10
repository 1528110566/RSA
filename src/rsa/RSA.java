package rsa;

public class RSA {

    public static String datasourcrName = "Rsa.accdb";//设置数据库的位置与表头名
    public static String tableName = "RsaDataBase";
    public static boolean ifSentToDataBase = true;//设置默认是否将密钥对送入数据库
    public static String accleratorFile = "Acclerator.txt";//设置快捷键文件的位置
    public static int font = 15;//设置窗口组件的字体大小
    public static int length = 450;//设置主窗口的大小
    public static int width = 430;

    public static void main(String[] args) {
        new MainFrame();//实例化主窗口
    }
}

package util;

public class SystemInfo {

    /**
     * 获取本机用户名
     *
     * @return
     */
    public static String getUsername(){
        return System.getProperties().getProperty("user.name");
    }

    public static void main(String[] args) {
        System.out.println(SystemInfo.getUsername());
    }

}

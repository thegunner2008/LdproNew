package tamhoang.ldpro4.util;

public class Convert {

    static public String versionCodeToDate(int versionCode) {
        int year = versionCode / 10000;
        int month = (versionCode % 10000) / 100;
        int date = (versionCode % 10000) % 100;
        return date + "/" + month + "/" + year;
    }
}

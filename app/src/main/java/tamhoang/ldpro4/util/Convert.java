package tamhoang.ldpro4.util;

public class Convert {

    static public String versionCodeToDate(int versionCode) {
        int year = versionCode / 10000;
        int month = (versionCode % 10000) / 100;
        int date = (versionCode % 10000) % 100;
        return date + "/" + month + "/" + year;
    }

    static public String convertToLatin(String str) {
        String result = str;
        final String transl = "aaaaaaaaaaaaaaaaadeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyd";
        final String origin = "àáảãạăắằẵặẳâầấậẫẩđèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđ";
        for (int i = 0; i < transl.length(); i++) {
            result = result.replace(origin.charAt(i), transl.charAt(i));
        }
        return result;
    }
}

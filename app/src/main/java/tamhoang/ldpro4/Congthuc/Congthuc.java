package tamhoang.ldpro4.Congthuc;

import android.util.Log;


import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import org.json.JSONException;

import tamhoang.ldpro4.MainActivity;

public class Congthuc {
    public static String NhanTinNhan(String str) throws JSONException {
        String str22 = "";
        String dayso = "";
        String dayso2;
        int i3;
        String dayso3 = "";
        String dayso4 = "";
        String nd_phantich = Xuly_DauTN(fixTinNhan1(convertKhongDau(str.replaceAll("\n", " ").replaceAll("\\.", ","))));
        if (nd_phantich.length() < 3 || nd_phantich.length() < 8) {
            return "Không hiểu " + nd_phantich;
        }
        String phanTichTN = PhanTichTinNhan(nd_phantich);
        if (!phanTichTN.contains("x ") && !phanTichTN.contains("Không hiểu")) {
            return "Không hiểu " + phanTichTN;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        hourFormat.setTimeZone(TimeZone.getDefault());
        String theodoi = null;
        if (phanTichTN.contains("Không hiểu")) {
            return phanTichTN;
        }
        String[][] mang = new String[200][6];
        phanTichTN = phanTichTN.replaceAll(" , ", " ");
        for (int i = 1; i < 10; i++) {
            phanTichTN = phanTichTN.replaceAll(" {2}", " ");
        }
        String dayso6 = "";
        phanTichTN = phanTichTN.trim() + " ";
        int k = 0;
        String str25 = "";
        int i1 = -1;
        int rw = 0;
        while (true) {
            int indexOf = phanTichTN.indexOf(" x ", i1 + 1);
            i1 = indexOf;
            if (indexOf == -1) {
                str22 = theodoi;
                dayso = str25;
                break;
            }
            int i2 = i1;
            StringBuilder tienBuilder = new StringBuilder();
            while (i2 < phanTichTN.length()) {
                if (phanTichTN.charAt(i2) == ' ' && tienBuilder.length() > 0) {
                    break;
                }
                if ("0123456789,tr".contains(phanTichTN.substring(i2, i2 + 1))) {
                    tienBuilder.append(phanTichTN.charAt(i2));
                }
                i2++;
            }
            String dtien = "";
            int i33 = i2;
            while (true) {
                if (i33 < phanTichTN.length()) {
                    if (!Character.isLetter(phanTichTN.charAt(i33)) && dtien.length() > 0) {
                        break;
                    }
                    dtien = dtien + phanTichTN.charAt(i33);
                    i33++;
                } else {
                    break;
                }
            }
            if (i2 == i33) {
                i33--;
            }
            String[] keyWords = {"dau", "dit", "tong", "cham", "dan", "boj", "lo", "de", "xi", "xn", "hc",
                    "xq", "xg", "bc", "kep", "sat", "to", "nho", "chan", "le", "ko", "chia", "duoi", "be", };

            if (Arrays.stream(keyWords).noneMatch(dtien::contains)) {
                if (dtien.contains("x ")) {
                    i3 = i2 - 1;
                    while (true) {
                        if (i3 <= 0) {
                            dayso2 = dayso6;
                            break;
                        } else if (!isNumeric(phanTichTN.charAt(i3) + "")) {
                            dayso2 = phanTichTN.substring(k, i3 + 1);
                            theodoi = phanTichTN.substring(i3 + 1);
                            k = i3 + 1;
                            break;
                        } else {
                            i3--;
                        }
                    }
                } else {
                    String dayso8 = phanTichTN.substring(k, i33);
                    if (dayso8.substring(0, 4).contains("bor")) {
                        dayso4 = "de " + dayso8;
                    } else {
                        dayso4 = dayso8;
                    }
                    i3 = i33;
                    k = i3;
                    theodoi = phanTichTN.substring(i3);
                    dayso2 = dayso4;
                }
                mang[rw][0] = dayso2;
                if (dayso2.contains("lo")) {
                    mang[rw][1] = "lo";
                    dayso3 = dayso2.replaceAll("lo", "").replaceAll("lo:", "").replaceAll("lo :", "");
                } else {
                    if (dayso2.contains("dea")) {
                        mang[rw][1] = "de dau db";
                        dayso3 = dayso2.replaceAll("dea", "").replaceAll("dea:", "").replaceAll("dea :", "");
                    } else if (dayso2.contains("deb")) {
                        mang[rw][1] = "de dit db";
                        dayso3 = dayso2.replaceAll("deb", "").replaceAll("deb:", "").replaceAll("deb :", "");
                    } else if (dayso2.contains("dec")) {
                        mang[rw][1] = "de dau nhat";
                        dayso3 = dayso2.replaceAll("dec", "").replaceAll("dec:", "").replaceAll("dec :", "");
                    } else if (dayso2.contains("ded")) {
                        mang[rw][1] = "de dit nhat";
                        dayso3 = dayso2.replaceAll("ded", "").replaceAll("ded:", "").replaceAll("ded :", "");
                    } else if (dayso2.contains("de")) {
                        mang[rw][1] = "de dit db";
                        dayso3 = dayso2.replaceAll("de", "").replaceAll("de:", "").replaceAll("de :", "");
                    } else if (dayso2.contains("bc")) {
                        mang[rw][1] = "bc";
                        dayso3 = dayso2.replaceAll("bc", "").replaceAll("bc:", "").replaceAll("bc :", "");
                    } else if (dayso2.contains("xi")) {
                        mang[rw][1] = "xi";
                        dayso3 = dayso2.replaceAll("xi", "").replaceAll("xien", "");
                    } else if (dayso2.contains("xq")) {
                        mang[rw][1] = "xq";
                        dayso3 = dayso2.replaceAll("xq", "");
                    } else {
                        mang[rw][1] = mang[rw - 1][1];
                        dayso3 = dayso2;
                    }
                }
                mang[rw][2] = XulyLoDe(dayso3.substring(0, dayso3.indexOf("x")));
                mang[rw][3] = XulyTien(dayso3.substring(dayso3.indexOf("x"), dayso3.length()), null);
                str25 = "";
                if (!mang[rw][2].contains("Không hiểu")) {
                } else if (mang[rw][2].length() < 3) {
                    break;
                } else if (mang[rw][3].contains("Không hiểu")) {
                    str22 = theodoi;
                    dayso = "Không hiểu " + dayso3.substring(dayso3.indexOf("x"));
                    break;
                } else {
                    dayso6 = dayso3;
                }
                rw++;
            }
        }
        String replace = str22.replaceAll(" ", "")
                .replaceAll("\\.", "").replaceAll(",", "");
        if (replace.length() > 0) {
            return "Không hiểu " + str22;
        }
        if (dayso.contains("Không hiểu ")) {
            return dayso;
        }
        String result = "";
        for (String[] aMang : mang) {
            if (aMang[0] == null) break;
            if (!aMang[2].contains("Không hiểu ") && !aMang[3].contains("Không hiểu ")) {
                result = result + aMang[1] + ":" + aMang[2] + "x" + aMang[3] + "\n";
            }
        }
        return result;
    }

    public static String convertKhongDau(String stringInput) {
        String stringInput2 = (stringInput.toLowerCase() + " ").replaceAll("bỏ", "bor").replaceAll("bộ", "boj").replaceAll("\\.", ",").replaceAll("́", "").replaceAll("̀", "").replaceAll("̉", "").replaceAll("̣", "").replaceAll("̃", "").replaceAll("\\+", "!");
        for (int i = 0; i < "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".length(); i++) {
            stringInput2 = stringInput2.replace("ăâàằầáắấảẳẩãẵẫạặậễẽểẻéêèềếẹệôòồơờóốớỏổởõỗỡọộợưúùứừủửũữụựìíỉĩịỳýỷỹỵđ×".charAt(i), "aaaaaaaaaaaaaaaaaeeeeeeeeeeeooooooooooooooooouuuuuuuuuuuiiiiiyyyyydx".charAt(i));
        }
        String stringInput3 = stringInput2.replaceAll("\n d", "\nd");
        if (stringInput3.contains("\nđ") || stringInput3.contains("\nd")) {
            int i1 = -1;
            while (true) {
                int indexOf = stringInput3.indexOf("\nd", i1 + 1);
                i1 = indexOf;
                if (indexOf == -1) {
                    break;
                } else if (i1 < stringInput3.length() - 1) {
                    String SSS = stringInput3.substring(i1 + 2, i1 + 3);
                    if (isNumeric(SSS) || SSS.indexOf(" ") > -1 || SSS.indexOf(":") > -1) {
                        stringInput3 = stringInput3.replaceAll("\nd" + SSS, "!d" + SSS);
                    }
                }
            }
        }
        for (int i2 = 1; i2 < 10; i2++) {
            stringInput3 = stringInput3.replaceAll("  ", " ");
        }
        return stringInput3.replaceAll("\\s+", " ").replace("d e", "de").replace("d au", "dau").replace("d it", "dit").replace("ja", "ia").replace("dich", "dit").replace("je", "ie").replace("nde", "n de").replace("nlo", "n lo").replace("nxi", "n xi").replace("nda", "n da").replace("ndi", "n di").replace("nto", "n to").replace("x i", "xi").replace("x j", "xi").replace("xj", "xi").replace("x 3 bc", "x 3, bc");
    }

    public static String Xuly_DauTN(String str) {
        String str2 = str.replaceAll(" ̂ ", " ").replaceAll("tong k ", "tong ko ")
                .replaceAll("tong 0 chia", "tong ko chia").replaceAll("botrung", "bor trung").replaceAll(" ̂", "");
        for (int i = 0; i < MainActivity.formList.size(); i++)
            str2 = str2.replaceAll(Objects.requireNonNull(MainActivity.formList.get(i).get("datas")),
                    Objects.requireNonNull(MainActivity.formList.get(i).get("type"))).replaceAll(" {2}", " ");

        for (int i = 0; i < MainActivity.formArray.size(); i++)
            str2 = str2.replaceAll(Objects.requireNonNull(MainActivity.formArray.get(i).get("str")),
                    Objects.requireNonNull(MainActivity.formArray.get(i).get("repl_str"))).replaceAll(" {2}", " ");

        for (int i3 = 1; i3 < 10; i3++)
            str2 = str2.replaceAll(" {2}", " ");

        String str3 = str2.replaceAll("xie n", "xi").replaceAll("le ch", "lech").replace("\n", " ")
                .replace("\\.", ",").replaceAll(";,", ";").replaceAll("; ,", ";")
                .replaceAll("; lo", "lo").replaceAll("va ", ";").replaceAll(";lo", "lo")
                .replaceAll("; de", "de").replaceAll(";de", "de").replaceAll("; xi", "xi")
                .replaceAll("dedau", "de dau").replaceAll("dedit", "de dit")
                .replaceAll("decham", "de cham").replaceAll("dedinh", "de cham").replaceAll(";xn", "xn")
                .replaceAll(";xi", "xi").replaceAll("; bc", "bc").replaceAll(";bc", "bc")

                .replaceAll("bc", " bc ").replace("dan", " dan ").replace("cua", " trung ")
                .replace("chia", " chia ").replace("dau", " dau ").replace("dit", " dit ")
                .replace("tong", " tong ").replace("cham", " cham ").replace("boj", " boj ")
                .replace("bor", " bor ").replace("dea", " dea ").replaceAll("deb", " deb ")
                .replaceAll("dec", " dec ").replaceAll("ded", " ded ").replace("lo ", " lo ")
                .replaceAll("xg", " xg ").replaceAll("xn", " xn ");
        if (!str3.contains("dea") && !str3.contains("deb") && !str3.contains("dec") && !str3.contains("ded") && !str3.contains("det") && str3.contains("de")) {
            return str3.replaceAll("de", "deb ");
        }
        return str3;
    }

    public static String fixTinNhan1(String str) {
        String str2 = str.replaceAll(" ,", ", ");
        int i = 0;
        int j = str2.length();
        while (i < j - 1) {
            i++;
            j = str2.length() - 1;
            if (Character.isLetter(str2.charAt(i)) && !Character.isLetter(str2.charAt(i + 1))) {
                str2 = str2.substring(0, i + 1) + " " + str2.substring(i + 1);
                i++;
            } else if (!Character.isLetter(str2.charAt(i)) && Character.isLetter(str2.charAt(i + 1))) {
                str2 = str2.substring(0, i + 1) + " " + str2.substring(i + 1);
                i++;
            }
        }
        String str3 = str2 + " ";
        for (int i2 = 1; i2 < 10; i2++) {
            str3 = str3.replaceAll(" {2}", " ");
        }
        if (str3.contains("(") && str3.contains(")")) {
            int i1 = -1;
            while (true) {
                int indexOf = str3.indexOf("(", i1 + 1);
                i1 = indexOf;
                if (indexOf == -1) {
                    break;
                }
                int i22 = i1;
                while (i22 < str3.length() && !str3.substring(i22, i22 + 1).contains(")")) {
                    i22++;
                }
                if (isNumeric(str3.substring(i1 + 1, i22).replaceAll(" ", ""))) {
                    for (int i3 = i1; i3 < i22; i3++) {
                        Log.i("ISNUMBERIC", i1 + "");
                        if (isNumeric(str3.substring(i3 - 1, i3)) && str3.substring(i3, i3 + 1).contains(" ") && isNumeric(str3.substring(i3 + 1, i3 + 2))) {
                            str3 = str3.substring(0, i3) + "," + str3.substring(i3 + 1);
                        }
                    }
                }
            }
        }
        return str3;
    }

    public static String fixTinNhan(String str) {
        String str2 = str + " ";
        str2.replaceAll(" ", ",").replaceAll("\\.", ",").replaceAll(":", ",").replaceAll(";", ",").replaceAll("/", ",").split(",");
        int i = -1;
        if (str2.contains("Không hiểu")) {
            return str2;
        }
        for (int i2 = 0; i2 < str2.length(); i2++) {
            if (str2.charAt(i2) > 127 || str2.charAt(i2) < 31) {
                str2 = str2.substring(0, i2) + " " + str2.substring(i2 + 1);
            }
        }
        String str3 = str2.trim();
        if (str3.charAt(str3.length() - 1) == 'x') {
            str3 = str3.substring(0, str3.length() - 1);
        }
        int dem = -1;
        while (true) {
            int indexOf = str3.indexOf("x ", dem + 1);
            dem = indexOf;
            if (indexOf == -1) {
                break;
            }
            int i3 = dem + 2;
            while (i3 < str3.length() && !isNumeric(str3.substring(i3, i3 + 1))) {
                i3++;
            }
            int j = i3;
            while (j < str3.length() && (isNumeric(str3.substring(j, j + 1)) || " tr".indexOf(str3.substring(j, j + 1)) != -1)) {
                j++;
            }
            if (isNumeric(str3.substring(dem + 1, j).trim()) && str3.substring(dem + 1, j).trim().length() > 1) {
                str3 = str3.substring(0, j) + " " + str3.substring(j);
            } else if (j - i3 > 1 && str3.substring(dem).indexOf("to") != (j - dem) - 1 && str3.substring(dem).indexOf("tin") != (j - dem) - 1 && str3.substring(dem).indexOf(",") != j - dem) {
                str3 = str3.substring(0, j) + " " + str3.substring(j);
            } else if (j - i3 == 1 && str3.substring(dem).indexOf("tr") == -1) {
                str3 = str3.substring(0, j) + " " + str3.substring(j);
            }
        }
        String str4 = str3 + " ";
        int dem2 = str4.length();
        while (dem2 > str4.length() - 9) {
            String Sss = str4.substring(dem2);
            if (Sss.trim().indexOf("t ") > i) {
                String Sss1 = "";
                for (int i4 = dem2; i4 > 0; i4--) {
                    Sss1 = str4.substring(i4, dem2);
                    if (!isNumeric(Sss1) && Sss1.trim().length() > 0) {
                        break;
                    }
                }
                if (Sss1.trim().length() > 1 || !isNumeric(Sss1)) {
                    String Sss2 = Sss.replaceAll("t", "").replaceAll(" ", "").replaceAll(",", "");
                    if (!isNumeric(Sss2) || Integer.parseInt(Sss2) >= 99) {
                        if (Sss2.length() != 0) {
                            break;
                        }
                        str4 = str4.substring(0, dem2 + 1) + "?";
                    } else {
                        str4 = str4.substring(0, dem2);
                    }
                }
            }
            dem2--;
            i = -1;
        }
        String str5 = str4.trim();
        try {
            if (str5.substring(str5.length() - 1).indexOf("@") > -1) {
                int i5 = str5.length() - 2;
                while (true) {
                    if (i5 <= 0) {
                        break;
                    } else if (str5.substring(i5, i5 + 1).indexOf("@") > -1) {
                        break;
                    } else {
                        i5--;
                    }
                }
                if (i5 > str5.length() - 13 && isNumeric(str5.substring(i5).replaceAll("@", ""))) {
                    str5 = str5.substring(0, i5);
                }
            }
        } catch (Exception e) {
        }
        try {
            if (MainActivity.jSon_Setting.getInt("baotinthieu") == 0) {
                str5 = str5.trim();
                for (int i6 = 6; i6 > 0; i6--) {
                    String Sss3 = str5.substring(0, i6);
                    if (Sss3.trim().contains("t") && isNumeric(Sss3.replaceAll("t", "").replaceAll(",", ""))) {
                        str5 = str5.substring(i6);
                    }
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        String str6 = str5 + " ";
        int i1 = -1;
        while (true) {
            try {
                int indexOf2 = str6.indexOf("tin", i1 + 1);
                i1 = indexOf2;
                if (indexOf2 == -1) {
                    break;
                }
                int i7 = i1 + 5;
                while (true) {
                    if (i7 >= i1 + 10) {
                        break;
                    } else if (!isNumeric(str6.substring(i1 + 4, i7))) {
                        break;
                    } else {
                        i7++;
                    }
                }
                if (i7 - i1 > 5) {
                    str6 = str6.substring(0, i1) + str6.substring(i7);
                }
            } catch (Exception e3) {
            }
        }
        String str7 = str6.trim();
        if (str7.substring(0, 1).indexOf(",") > -1) {
            return str7.substring(1);
        }
        return str7;
    }

    public static String FixDan(String str) {
        String DaySo = "";
        String[] array = str.replaceAll(":", "").replaceAll(" //. ", "").replaceAll(" , ", "").replaceAll("\\.", ",").split(",");
        for (int i = 0; i < array.length; i++) {
            if (isNumeric(array[i]) && array[i].length() == 2) {
                DaySo = DaySo + array[i] + ",";
            } else if (isNumeric(array[i]) && array[i].length() == 3) {
                if (array[i].charAt(0) != array[i].charAt(2)) {
                    return "Không hiểu " + array[i];
                }
                DaySo = (DaySo + array[i].substring(0, 2) + ",") + array[i].substring(1, 3) + ",";
            } else if (array[i].length() > 0) {
                return "Không hiểu " + array[i];
            }
        }
        return DaySo;
    }

    public static String PhanTichTinNhan(String str) {
        String str2 = str.replace("  ", " ");
        if (str2.contains("Không hiểu")) return str2;

        if (!str2.substring(0, 5).contains("de") && !str2.substring(0, 5).contains("lo") && !str2.substring(0, 5).contains("hc") && !str2.substring(0, 5).contains("xi")
                && !str2.substring(0, 5).contains("xq") && !str2.substring(0, 5).contains("xn") && !str2.substring(0, 5).contains("bc") && !str2.substring(0, 5).contains("xg")) {
            return "Không hiểu dạng";
        }
        String str3 = str2 + "      ";
        str3.toCharArray();
        int I2 = 3;
        while (I2 < str3.length() - 4) {
            if (isNumeric(str3.substring(I2, I2 + 1)) && str3.charAt(I2 + 1) == ' ' && "ndk".contains(str3.substring(I2 + 2, I2 + 3)) && str3.charAt(I2 + 3) == ' ') {
                int I3 = I2;
                while (I3 > 0 && isNumeric(str3.substring(I3, I3 + 1))) {
                    I3--;
                }
                str3 = str3.substring(0, I3) + " x " + str3.substring(I3);
                I2 += 6;
            }
            I2++;
        }
        for (int i = 1; i < 10; i++) {
            str3 = str3.replaceAll(" {2}", " ");
        }
        for (int i2 = 1; i2 < 4; i2++) {
            str3 = str3.replaceAll(" {2}", " ").replaceAll(": x", " x").replaceAll(":x", " x").replaceAll("x x", "x")
                    .replaceAll("xx", "x").replaceAll(", x", " x").replaceAll(",x", " x").replaceAll("-x", " x").replaceAll("- x", " x");
        }
        return str3;
    }

    public static String Xu3cang(String str) {
        String DaySo = "";
        if (str.length() < 2) {
            DaySo = "Không hiểu";
        }
        if (str.replaceAll(" ", "").length() > 0) {
            String[] array = str.trim().replaceAll(":", " ").replaceAll(" //. ", "").replaceAll(" , ", "").replaceAll(";", " ").replaceAll("/", "").replaceAll("\\.", ",").replaceAll(" ", ",").split(",");
            for (int i = 0; i < array.length; i++) {
                if (isNumeric(array[i]) && array[i].length() == 3) {
                    DaySo = DaySo + array[i] + ",";
                } else if (array[i].length() > 0) {
                    return "Không hiểu " + array[i];
                }
            }
        }
        return DaySo;
    }

    public static String XulyXien(String str) {//format: "12,34 56,78" "121,232" -> "12,21,23,32"
        str = str.replace(", ", ",");
        String replace;
        String strKQ = "";
        if (str.length() > 6) {
            String replaceAll = str.trim().replaceAll("[,.;-]", " ");
            if (replaceAll.startsWith("2 ") || replaceAll.startsWith("3 ") || replaceAll.startsWith("4 ")) {
                int i4 = replaceAll.startsWith("2 ") ? 5 : replaceAll.startsWith("3 ") ? 8 : replaceAll.startsWith("4 ") ? 11 : 0;
                String XulySo = XulySo(replaceAll.substring(2));
                int i5 = 0;
                while (XulySo.length() - i5 > i4) {
                    int i6 = i5 + i4;
                    i5 = i6 + 1;

                    XulySo = XulySo.substring(0, i6) + " " + XulySo.substring(i5);
                    if (XulySo.substring(i5).length() < i4 && XulySo.substring(i5).length() > 2) {
                        XulySo = "Không hiểu " + XulySo.substring(i5);
                        break;
                    }
                }
                return XulySo.trim();
            }
        }
        replace = (str.trim().startsWith("2 ")) ? str.trim().replace("2 ", "") : str.trim();
        if (replace.contains(";")) {
            String[] splitArr = replace.split(";");
            strKQ = "";
            boolean kiemtra = true;
            for (String split : splitArr) {
                split = split.replaceAll(" ", ",");
                split = split.replaceAll(",,", ",");
                String[] split2Arr = split.split(",");
                for (String s : split2Arr) {
                    if (s.length() == 3 && isNumeric(s)) {
                        String So1 = s.charAt(0) + "";
                        String So2 = s.charAt(1) + "";
                        String So3 = s.charAt(2) + "";
                        if(!So1.equals(So2) && So1.equals(So3)) {
                            String sb = So1 + So2 + "," + So2 + So1;
                            replace = replace.replaceAll(s, sb);
                        } else {
                            strKQ = "";
                            kiemtra = false;
                            break;
                        }
                    } else {
                        if (s.length() != 2 || !isNumeric(s)) {
                            if (s.length() > 1) {
                                strKQ = "";
                                kiemtra = false;
                                break;
                            }
                        }
                    }
                }
                if (kiemtra) {
                    strKQ = strKQ + split + " ";
                }
            }
        } else {
            strKQ = replace.replaceAll(" ", "");
            boolean reCaculate = false;
            String[] split3 = strKQ.split(",");
            for (String s : split3) {
                if (s.length() == 3 && isNumeric(s)) {
                    String So1 = s.charAt(0) + "";
                    String So2 = s.charAt(1) + "";
                    String So3 = s.charAt(2) + "";
                    if(!So1.equals(So2) && So1.equals(So3)) {
                        String sb = So1 + So2 + "," + So2 + So1;
                        strKQ = strKQ.replaceAll(s, sb);
                        reCaculate = true;
                    }
                } else {
                    if ((s.length() != 2 || !isNumeric(s)) && s.length() > 1) {
                        break;
                    }
                }
            }

            String kQTmp = reCaculate? strKQ : "";
            strKQ = "";

            String[] spaces = {" ", ","};
            for (String Space: spaces) {
                String[] splits = replace.split(Space);
                boolean kiemtra = true;
                for (String spl: splits) {
                    String trim = spl.trim();
                    if(trim.length() == 0) break;

                    if (trim.split(" ").length == 1) {
                        String[] split2 = trim.split(",");
                        if (split2.length == 1) {
                            strKQ = kQTmp.split(",").length > 1 ? kQTmp : "";
                            break;
                        }
                        String str4 = trim;
                        for (String s: split2) {
                            if (s.length() == 3 && isNumeric(s)) {
                                String So1 = s.charAt(0) + "";
                                String So2 = s.charAt(1) + "";
                                String So3 = s.charAt(2) + "";
                                if(!So1.equals(So2) && So1.equals(So3)) {
                                    String sb = So1 + So2 + "," + So2 + So1;
                                    str4 = str4.replaceAll(s, sb);
                                }
                            } else {
                                if ((s.length() != 2 || !isNumeric(s)) && s.length() > 1) {
                                    strKQ = "";
                                    kiemtra = false;
                                    break;
                                }
                            }
                        }
                        if (kiemtra && str4.length() > 4) {
                            strKQ = strKQ + str4.replaceAll(" ", ",") + " ";
                        }
                    } else {
                        String[] split7 = trim.split(" ");
                        if (split7.length == 1) {
                            strKQ = "";
                            break;
                        }
                        for (String s : split7) {
                            if (s.length() == 3 && isNumeric(s)) {
                                String So1 = s.charAt(0) + "";
                                String So2 = s.charAt(1) + "";
                                String So3 = s.charAt(2) + "";
                                if(!So1.equals(So2) && So1.equals(So3)) {
                                    String sb = So1 + So2 + "," + So2 + So1;
                                    trim = trim.replaceAll(s, sb);
                                }
                            } else {
                                if ((s.length() != 2 || !isNumeric(s)) && s.length() > 1) {
                                    strKQ = "";
                                    kiemtra = false;
                                    break;
                                }
                            }
                        }
                        if (kiemtra && trim.length() > 4) {
                            strKQ = strKQ + trim.replaceAll(" ", ",") + " ";
                        }
                    }
                }
                if (strKQ.length() > 0) {
                    break;
                }
            }
        }
        return strKQ.trim();
    }

    public static String sortXien(String xien) {
        String[] sort;
        ArrayList numberList = new ArrayList();
        String newXien = "";
        for (String str : xien.split(",")) {
            numberList.add(str);
        }
        Collections.sort(numberList);
        for (int i = 0; i < numberList.size(); i++) {
            newXien = newXien + numberList.get(i) + ",";
        }
        return newXien;
    }

    public static ArrayList<String> XulyXienGhep(String str, int ghep) {
        ArrayList<String> listXien = new ArrayList<>();
        if (ghep == 2) {
            String[] ArrXien = str.split(",");
            for (int s1 = 0; s1 < ArrXien.length - 1; s1++) {
                for (int s2 = s1 + 1; s2 < ArrXien.length; s2++) {
                    if (ArrXien[s1] != ArrXien[s2]) {
                        listXien.add(sortXien(ArrXien[s1] + "," + ArrXien[s2]));
                    }
                }
            }
        } else if (ghep == 3) {
            String[] ArrXien2 = str.split(",");
            for (int s12 = 0; s12 < ArrXien2.length - 2; s12++) {
                for (int s22 = s12 + 1; s22 < ArrXien2.length - 1; s22++) {
                    for (int s3 = s22 + 1; s3 < ArrXien2.length; s3++) {
                        if (!(ArrXien2[s12] == ArrXien2[s22] || ArrXien2[s12] == ArrXien2[s3] || ArrXien2[s22] == ArrXien2[s3])) {
                            listXien.add(sortXien(ArrXien2[s12] + "," + ArrXien2[s22] + "," + ArrXien2[s3]));
                        }
                    }
                }
            }
        } else if (ghep == 4) {
            String[] ArrXien3 = str.split(",");
            for (int s13 = 0; s13 < ArrXien3.length - 3; s13++) {
                for (int s23 = s13 + 1; s23 < ArrXien3.length - 2; s23++) {
                    for (int s32 = s23 + 1; s32 < ArrXien3.length - 1; s32++) {
                        for (int s4 = s32 + 1; s4 < ArrXien3.length; s4++) {
                            if (!(ArrXien3[s13] == ArrXien3[s23] || ArrXien3[s13] == ArrXien3[s32] || ArrXien3[s13] == ArrXien3[s4] || ArrXien3[s23] == ArrXien3[s32] || ArrXien3[s23] == ArrXien3[s4] || ArrXien3[s32] == ArrXien3[s4])) {
                                listXien.add(sortXien(ArrXien3[s13] + "," + ArrXien3[s23] + "," + ArrXien3[s32] + "," + ArrXien3[s4]));
                            }
                        }
                    }
                }
            }
        }
        return listXien;
    }

    public static String XulyLoDe(String str) {
        String DanGoc;
        String sauloc = "";
        if (str.contains("bor trung")) {
            String[] ArrBT = str.split("bor trung ");
            String DanBo = "";
            String DanTrung = "";
            if (ArrBT[0].contains("bor trung")) {
                DanGoc = XulySo(ArrBT[0].replaceAll("bor trung", ""));
            } else {
                DanGoc = XulySo(ArrBT[0]);
            }
            if (DanGoc.contains("Không hiểu")) {
                return DanGoc;
            }
            String[] ArrBorTrung = DanGoc.split(",");
            if (ArrBT.length > 1) {
                if (ArrBT[1].length() > 0 && ArrBT[1].contains("bor")) {
                    DanBo = XulySo(ArrBT[1].replaceAll("bor", ""));
                    if (DanBo.contains("Không hiểu")) {
                        return DanBo;
                    }
                } else if (ArrBT[1].length() > 0 && ArrBT[1].contains("trung")) {
                    DanTrung = XulySo(ArrBT[1].replaceAll("trung", ""));
                    if (DanTrung.contains("Không hiểu")) {
                        return DanTrung;
                    }
                }
            }
            String sauloc2 = "";
            for (String s : ArrBorTrung) {
                try {
                    if (DanBo.length() == 0 && DanTrung.length() == 0) {
                        if (!sauloc2.contains(s)) {
                            sauloc2 = sauloc2 + s + ",";
                        }
                    } else if (DanBo.length() <= 0) {
                        if (!sauloc2.contains(s) && DanTrung.contains(s)) {
                            sauloc2 = sauloc2 + s + ",";
                        }
                    } else {
                        if (!sauloc2.contains(s) && !DanBo.contains(s)) {
                            sauloc2 = sauloc2 + s + ",";
                        }
                    }
                } catch (Exception e) {
                    return "Không hiểu " + str;
                }
            }
            return sauloc2;
        }
        if (!str.contains("trung") && !str.contains("bor")) {
            try {
                String mDanSo = XulySo(str);
                if (mDanSo.contains("Không hiểu")) {
                    if (mDanSo.length() > 11) {
                        return mDanSo;
                    }
                    return "Không hiểu " + str;
                }
            } catch (Exception e) {
                return "Không hiểu " + str;
            }
        } else if (!str.contains("trung") || !str.contains("bor")) {
            if (!str.contains("trung") && str.contains("bor")) {
                String[] ArrBor = str.split("bor");
                List<String> mBor = new ArrayList<>();
                for (String str2 : ArrBor) {
                    String ss = XulySo(str2);
                    if (ss.contains("Không hiểu")) {
                        return ss;
                    }
                    mBor.add(ss);
                }
                try {
                    String[] ArrSoBor = mBor.get(0).split(",");
                    for (String s : ArrSoBor) {
                        int m_Dem = 0;
                        int k3 = 1;
                        while (true) {
                            if (k3 >= mBor.size()) {
                                break;
                            } else if (mBor.get(k3).contains(s)) {
                                break;
                            } else {
                                m_Dem++;
                                k3++;
                            }
                        }
                        if (m_Dem == mBor.size() - 1) {
                            sauloc = sauloc + s + ",";
                        }
                    }
                    if (sauloc.length() > 0) {
                        return sauloc;
                    }
                    return "Không hiểu " + str.substring(str.indexOf("bor"));
                } catch (Exception e3) {
                    return "Không hiểu " + str;
                }
            } else if (str.contains("trung") && !str.contains("bor")) {
                String[] ArrTrung = str.split("trung");
                List<String> mTrung = new ArrayList<>();
                for (String str3 : ArrTrung) {
                    String ss2 = XulySo(str3);
                    if (ss2.contains("Không hiểu")) {
                        return ss2;
                    }
                    mTrung.add(ss2);
                }
                try {
                    String[] ArrSoTrung = mTrung.get(0).split(",");
                    for (String s : ArrSoTrung) {
                        int m_Dem2 = 0;
                        int k32 = 1;
                        while (true) {
                            if (k32 >= mTrung.size()) {
                                break;
                            }
                            if (!mTrung.get(k32).contains(s)) {
                                break;
                            }
                            m_Dem2++;
                            k32++;
                        }
                        if (m_Dem2 == mTrung.size() - 1) {
                            sauloc = sauloc + s + ",";
                        }
                    }
                    return sauloc;
                } catch (Exception e4) {
                    return "Không hiểu " + str;
                }
            }
        } else if (str.indexOf("trung") < str.indexOf("bor")) {
            if (str.substring(0, str.indexOf("trung")).length() > 1) {
                try {
                    String DanGoc2 = XulySo(str.substring(0, str.indexOf("trung")));
                    if (DanGoc2.contains("Không hiểu")) {
                        if (DanGoc2.length() > 11) {
                            return DanGoc2;
                        }
                        return "Không hiểu " + str;
                    } else if (str.substring(str.indexOf("trung") + 5, str.indexOf("bor")).length() > 1) {
                        try {
                            String DanTrung2 = XulySo(str.substring(str.indexOf("trung") + 5, str.indexOf("bor")));
                            if (DanTrung2.contains("Không hiểu")) {
                                if (DanTrung2.length() > 11) {
                                    return DanTrung2;
                                }
                                return "Không hiểu " + str;
                            } else if (str.substring(str.indexOf("bor") + 3).replaceAll("bor", sauloc).length() > 1) {
                                try {
                                    String DanBo2 = XulySo(str.substring(str.indexOf("bor") + 3).replaceAll("bor", sauloc));
                                    if (!DanBo2.contains("Không hiểu")) {
                                        String[] danlayS = DanGoc2.split(",");
                                        String sauloc3 = "";
                                        for (String danlay : danlayS) {
                                            if (DanTrung2.contains(danlay) && !DanBo2.contains(danlay)) {
                                                sauloc3 = sauloc3 + danlay + ",";
                                            }
                                        }
                                        return sauloc3;
                                    } else if (DanBo2.length() > 11) {
                                        return DanBo2;
                                    } else {
                                        return "Không hiểu " + str;
                                    }
                                } catch (Exception e5) {
                                    return "Không hiểu " + str.substring(str.indexOf("bor") + 3).replaceAll("bor", sauloc);
                                }
                            } else {
                                return "Không hiểu " + str;
                            }
                        } catch (Exception e6) {
                            return "Không hiểu " + str;
                        }
                    } else {
                        return "Không hiểu " + str;
                    }
                } catch (Exception e7) {
                    return "Không hiểu " + str;
                }
            } else {
                return "Không hiểu " + str;
            }
        } else if (str.substring(0, str.indexOf("bor")).length() > 1) {
            try {
                String DanGoc3 = XulySo(str.substring(0, str.indexOf("bor")));
                if (DanGoc3.contains("Không hiểu")) {
                    if (DanGoc3.length() > 11) {
                        return DanGoc3;
                    }
                    return "Không hiểu " + str;
                } else if (str.substring(str.indexOf("bor") + 4, str.indexOf("trung")).length() > 1) {
                    try {
                        String DanBo3 = XulySo(str.substring(str.indexOf("bor") + 4, str.indexOf("trung")));
                        if (DanBo3.contains("Không hiểu")) {
                            if (DanBo3.length() > 11) {
                                return DanBo3;
                            }
                            return "Không hiểu " + str;
                        } else if (str.substring(str.indexOf("trung") + 5).length() > 1) {
                            try {
                                String DanTrung3 = XulySo(str.substring(str.indexOf("trung") + 5).replaceAll("trung", sauloc));
                                if (!DanTrung3.contains("Không hiểu")) {
                                    String[] danlayS2 = DanGoc3.split(",");
                                    String sauloc4 = "";
                                    for (String s : danlayS2) {
                                        if (DanTrung3.contains(s) && !DanBo3.contains(s)) {
                                            sauloc4 = sauloc4 + s + ",";
                                        }
                                    }
                                    return sauloc4;
                                } else if (DanTrung3.length() > 11) {
                                    return DanTrung3;
                                } else {
                                    return "Không hiểu " + str;
                                }
                            } catch (Exception e8) {
                                return "Không hiểu " + str;
                            }
                        } else {
                            return "Không hiểu " + str;
                        }
                    } catch (Exception e9) {
                        return "Không hiểu " + str;
                    }
                } else {
                    return "Không hiểu " + str;
                }
            } catch (Exception e10) {
                return "Không hiểu " + str;
            }
        } else {
            return "Không hiểu " + str;
        }
        return XulySo(str);
    }

    public static String ToMauError(String value, String NoiDung) {
        String tomau = "ldpro" + value + "</font>";
        if (NoiDung.contains(value)) {
            return NoiDung.replace(value, tomau);
        }
        return "ldpro" + NoiDung + "</font>";
    }

    public static String XulySo(String str) {
        String str_Dit = "";
        String str_Tong;
        String ndung1;
        String Dan;
        String ndung2;
        String ndung12;
        String so1 = null;
        String str2;
        String SKtra;
        String ndung22;
        String str_Dau;
        String ndung23;
        String ndung13;
        String Dan2;
        String ndung24;
        String ndung25;
        String ndung14;
        String str_Dau2;
        String str_Tong2;
        String str_Dit2;
        String str_Dit3;
        String Dan3;
        String str_Dit4;
        String Ktra;
        String str_Dit5;
        String Dan4;
        String Dan5;
        String ndung15;
        String ndung16;
        String str13;
        String str_Dau3;
        String ndung17;
        String ndung26;
        String str14;
        String ndung27;
        String ndung18;
        String str16 = str.replaceAll("tong ko chia", "ko chia 3").replaceAll("tong chia 3 du 1", "chia 3 du 1").replaceAll("tong chia 3 du 2", "chia 3 du 2");
        if (str16.contains("tong 10")) {
            return "Không hiểu tong 10";
        }
        String str18 = str16.replaceAll(":", " ").replaceAll(";", " ").replaceAll(" ,", ", ")
                .replaceAll("tong > 10", "so 29,38,39,47,48,49,56,57,58,59,65,66,67,68,69,74,75,76,77,78,79,83,84,85,86,87,88,89,92,93,94,95,96,97,98,99,")
                .replaceAll("tong < 10", "so 01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,20,21,22,23,24,25,26,27,30,31,32,33,34,35,36,40,41,42,43,44,45,50,51,52,53,54,60,61,62,63,70,71,72,80,81,90,")
                .replaceAll("dau > dit", "so 10,20,21,30,31,32,40,41,42,43,50,51,52,53,54,60,61,62,63,64,65,70,71,72,73,74,75,76,80,81,82,83,84,85,86,87,90,91,92,93,94,95,96,97,98,")
                .replaceAll("dit < dau", "so 10,20,21,30,31,32,40,41,42,43,50,51,52,53,54,60,61,62,63,64,65,70,71,72,73,74,75,76,80,81,82,83,84,85,86,87,90,91,92,93,94,95,96,97,98,")
                .replaceAll("dau < dit", "so 01,02,03,04,05,06,07,08,09,12,13,14,15,16,17,18,19,23,24,25,26,27,28,29,34,35,36,37,38,39,45,46,47,48,49,56,57,58,59,67,68,69,78,79,89,")
                .replaceAll("dit > dau", "so 01,02,03,04,05,06,07,08,09,12,13,14,15,16,17,18,19,23,24,25,26,27,28,29,34,35,36,37,38,39,45,46,47,48,49,56,57,58,59,67,68,69,78,79,89,")
                .replaceAll("ko chia 3", "so 00,01,04,07,10,13,16,19,22,25,28,31,34,37,40,43,46,49,52,55,58,61,64,67,70,73,76,79,82,85,88,91,94,97,02,05,08,11,14,17,20,23,26,29,32,35,38,41,44,47,50,53,56,59,62,65,68,71,74,77,80,83,86,89,92,95,98,")
                .replaceAll("tong chia 3", "so 03,06,09,12,15,18,21,24,27,30,33,36,39,42,45,48,51,54,57,60,63,66,69,72,75,78,81,84,87,90,93,96,99, ")
                .replaceAll("chia 3 du 1", "so 01,04,07,10,13,16,19,22,25,28,31,34,37,40,43,46,49,52,55,58,61,64,67,70,73,76,79,82,85,88,91,94,97, ")
                .replaceAll("chia 3 du 2", "so 02,05,08,11,14,17,20,23,26,29,32,35,38,41,44,47,50,53,56,59,62,65,68,71,74,77,80,83,86,89,92,95,98, ");
        if (str.trim().length() < 2) {
            return "Không hiểu ";
        }
        int DemVonglap = 0;
        String SKtra2 = "";
        String DaySo2 = "";
        String DaySo3 = null;
        String ndung28 = "";
        String ndung19 = "";
        String str_Tong4 = "";
        String str_Tong5 = "";
        String str_Dit6 = "";
        String str_Dau4 = "";
        String str22 = "";
        loop0:
        while (true) {
            String str1 = str22;
            String str23 = str_Dau4;
            int DemVonglap2 = DemVonglap + 1;
            DemVonglap++;
            if (DemVonglap2 > 50) {
                return "Không hiểu " + str;
            }
            String str_Dit7 = str_Tong5;
            if ((!str18.contains(SKtra2) || str18.length() != SKtra2.length()) && str18.length() != 0) {
                String SKtra3 = str18;
                str18 = " " + str18.trim() + " ";
                String str_Tong6 = str_Tong4;
                if (str18.contains("den")) {
                    ndung12 = ndung19;
                    if (str18.substring(0, 3).contains("tu")) {
                        str18 = str18.substring(3);
                    }
                    int i2 = str18.indexOf("den");
                    int i1 = i2;
                    while (i1 > -1 && !isNumeric(str18.substring(i1, i1 + 2))) {
                        i1--;
                    }
                    String so12 = str18.substring(i1, i1 + 2);
                    while (true) {
                        ndung2 = ndung28;
                        if (i2 >= str18.length() || isNumeric(str18.substring(i2, i2 + 2))) {
                            String so2 = str18.substring(i2, i2 + 2);
                            Dan = DaySo3;
                        } else {
                            i2++;
                            break;
                        }
                    }
                    String so22 = str18.substring(i2, i2 + 2);
                    Dan = DaySo3;
                    if (Integer.parseInt(so12) < Integer.parseInt(so22) && so12.length() > 0 && so22.length() > 0) {
                        for (int i = Integer.parseInt(so12); i < Integer.parseInt(so22) + 1; i++) {
                            DaySo2 = DaySo2 + (i < 10 ? "0" : "") + i + ",";
                            if (i < 10) {
                                DaySo2 = DaySo2 + "0" + i + ",";
                            } else if (i > 9) {
                                DaySo2 = DaySo2 + i + ",";
                            }
                        }
                        str18 = str18.substring(0, i1) + " " + str18.substring(i2 + 2);
                    }
                } else {
                    ndung12 = ndung19;
                    ndung2 = ndung28;
                    Dan = DaySo3;
                }
                String KHONG_HIEU = "Không hiểu";

                if (str18.contains("ghep dit")) {
                    int i3 = str18.indexOf("ghep dit");
                    do {
                        i3--;
                    } while (!str18.startsWith("dau", i3) && i3 > 0);

                    int j = str18.indexOf("ghep dit") + 9;
                    while (j < str18.length() - 1) {
                        if (!", ".contains(str18.charAt(j) + "") && !isNumeric(str18.charAt(j)+ "")) {
                            j--;
                            break;
                        }
                        j++;
                    }
                    String ndung110 = str18.substring(i3, str18.indexOf("ghep dit")).replaceAll("dau", "");
                    String ndung29 = str18.substring(str18.indexOf("ghep dit"), j + 1).replaceAll("ghep dit", "");
                    if (ndung110.length() == 0) {
                        return "Không hiểu " + str18;
                    } else if (ndung29.length() == 0) {
                        return "Không hiểu " + str;
                    } else if (!isNumericComma(ndung110)) {
                        return "Không hiểu " + str18.substring(i3, str18.indexOf("ghep dit"));
                    } else if (!isNumericComma(ndung29)) {
                        return "Không hiểu " + str18.substring(str18.indexOf("ghep dit"), j + 1);
                    } else {
                        if (!isNumericComma(ndung110) || !isNumericComma(ndung29)) {
                            str2 = KHONG_HIEU;
                            ndung18 = ndung110;
                            ndung27 = ndung29;
                            SKtra = SKtra3;
                            str_Dau = str_Dit6;
                        } else {
                            str_Dau = GhepDau(ndung110);
                            ndung18 = ndung110;
                            if (str_Dau.contains(KHONG_HIEU)) {
                                return "Không hiểu " + str;
                            }
                            String str_Dit8 = GhepDit(ndung29);
                            ndung27 = ndung29;
                            if (str_Dit8.contains(KHONG_HIEU)) {
                                return "Không hiểu " + str;
                            }
                            int i22 = 0;
                            String DaySo4 = DaySo2;
                            while (true) {
                                SKtra = SKtra3;
                                if (i22 >= 100) {
                                    break;
                                }
                                if (i22 < 10) {
                                    if (str_Dau.contains("0" + i22)) {
                                        if (str_Dit8.contains("0" + i22)) {
                                            DaySo4 = DaySo4 + "0" + i22 + ",";
                                            i22++;
                                        }
                                    }
                                }
                                if (i22 > 9) {
                                    if (str_Dau.contains("" + i22)) {
                                        if (str_Dit8.contains("" + i22)) {
                                            DaySo4 = DaySo4 + i22 + ",";
                                        }
                                    }
                                }
                                i22++;
                                SKtra3 = SKtra;
                            }
                            str2 = KHONG_HIEU;
                            str_Dit7 = str_Dit8;
                            DaySo2 = DaySo4;
                        }
                        str18 = (str18.substring(0, i3) + " " + str18.substring(j + 1)).trim();
                        ndung22 = ndung27;
                        ndung19 = ndung18;
                    }
                } else {
                    str2 = KHONG_HIEU;
                    SKtra = SKtra3;
                    str_Dau = str_Dit6;
                    ndung19 = ndung12;
                    ndung22 = ndung2;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit7;
                    str_Tong4 = str_Tong6;
                    break;
                }

                if (str18.contains("ghep dau")) {
                    int i4 = str18.indexOf("ghep dau");
                    do {
                        i4--;
                    } while (!str18.startsWith("dit", i4));
                    int j2 = str18.indexOf("ghep dau") + 9;
                    while (true) {
                        if (j2 >= str18.length() - 1) {
                            str_Dau3 = str_Dau;
                            break;
                        }
                        str_Dau3 = str_Dau;
                        if (!", ".contains(str18.substring(j2, j2 + 1)) && !isNumeric(str18.substring(j2, j2 + 1))) {
                            j2--;
                            break;
                        }
                        j2++;
                        str_Dau = str_Dau3;
                    }
                    String ndung111 = str18.substring(i4, str18.indexOf("ghep dau")).replaceAll("dit", "");
                    String ndung210 = str18.substring(str18.indexOf("ghep dau"), j2 + 1).replaceAll("ghep dau", "");
                    if (ndung111.length() == 0) {
                        return "Không hiểu " + str18;
                    } else if (ndung210.length() == 0) {
                        return "Không hiểu " + str;
                    } else if (!isNumericComma(ndung111)) {
                        return "Không hiểu " + str18.substring(i4, str18.indexOf("ghep dau"));
                    } else if (!isNumericComma(ndung210)) {
                        return "Không hiểu " + str18.substring(str18.indexOf("ghep dau"), j2 + 1);
                    } else {
                        if (!isNumericComma(ndung111) || !isNumericComma(ndung210)) {
                            ndung17 = ndung111;
                            ndung26 = ndung210;
                            str_Dau = str_Dau3;
                        } else {
                            String str_Dau5 = GhepDau(ndung210);
                            ndung26 = ndung210;
                            String str24 = str2;
                            if (str_Dau5.contains(KHONG_HIEU)) {
                                return "Không hiểu " + str;
                            }
                            String str_Dit9 = GhepDit(ndung111);
                            ndung17 = ndung111;
                            if (str_Dit9.contains(KHONG_HIEU)) {
                                return "Không hiểu " + str;
                            }
                            int i23 = 0;
                            DaySo2 = DaySo2;
                            while (i23 < 100) {
                                if (i23 < 10) {
                                    str14 = str24;
                                    if (str_Dau5.contains("0" + i23)) {
                                        if (str_Dit9.contains("0" + i23)) {
                                            DaySo2 = DaySo2 + "0" + i23 + ",";
                                            i23++;
                                            str24 = str14;
                                        }
                                    }
                                } else {
                                    str14 = str24;
                                }
                                if (i23 > 9) {
                                    if (str_Dau5.contains("" + i23)) {
                                        if (str_Dit9.contains("" + i23)) {
                                            DaySo2 = DaySo2 + i23 + ",";
                                        }
                                    }
                                }
                                i23++;
                                str24 = str14;
                            }
                            str2 = str24;
                            str_Dit7 = str_Dit9;
                            str_Dau = str_Dau5;
                        }
                        str18 = (str18.substring(0, i4) + " " + str18.substring(j2 + 1)).trim();
                        ndung22 = ndung26;
                        ndung19 = ndung17;
                    }
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit7;
                    str_Tong4 = str_Tong6;
                    break;
                }

                if (str18.contains("ghepdd")) {
                    int i5 = str18.indexOf("ghepdd");
                    int j3 = str18.indexOf("ghepdd") + 11;
                    while (true) {
                        if (j3 >= str18.length()) {
                            break;
                        }
                        if (!", ".contains(str18.substring(j3, j3 + 1)) && !isNumeric(str18.substring(j3, j3 + 1))) {
                            j3--;
                            break;
                        }
                        j3++;
                    }
                    String Dan6 = str18.substring(str18.indexOf("ghepdd"), j3).replaceAll("ghepdd", "").trim();
                    if (Dan6.length() == 0) {
                        return "Không hiểu " + str;
                    } else if (isNumericComma(Dan6)) {
                        String[] DanArr = Dan6.split(",");
                        ndung13 = ndung19;
                        int i32 = 0;
                        while (true) {
                            ndung23 = ndung22;
                            if (i32 >= DanArr.length) {
                                String str_Dau6 = GhepDau(Dan6);
                                String str25 = str2;
                                if (str_Dau6.contains(KHONG_HIEU)) {
                                    return "Không hiểu " + str;
                                }
                                String str_Dit10 = GhepDit(Dan6);
                                if (str_Dit10.contains(KHONG_HIEU)) {
                                    return "Không hiểu " + str;
                                }
                                int i24 = 0;
                                String DaySo5 = DaySo2;
                                while (i24 < 100) {
                                    if (i24 < 10) {
                                        str13 = str25;
                                        if (str_Dau6.contains("0" + i24)) {
                                            if (str_Dit10.contains("0" + i24)) {
                                                DaySo5 = DaySo5 + "0" + i24 + ",";
                                                i24++;
                                                str25 = str13;
                                            }
                                        }
                                    } else {
                                        str13 = str25;
                                    }
                                    if (i24 > 9) {
                                        if (str_Dau6.contains("" + i24)) {
                                            if (str_Dit10.contains("" + i24)) {
                                                DaySo5 = DaySo5 + i24 + ",";
                                            }
                                        }
                                    }
                                    i24++;
                                    str25 = str13;
                                }
                                str2 = str25;
                                str18 = (str18.substring(0, i5) + " " + str18.substring(j3)).trim();
                                str_Dit7 = str_Dit10;
                                DaySo2 = DaySo5;
                                Dan2 = Dan6;
                                str_Dau = str_Dau6;
                            } else if (DanArr[i32].length() == 2 && isNumeric(DanArr[i32]) && i32 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr[i32]), i5);
                            } else if (!isNumeric(DanArr[i32])) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr[i32]), i5);
                            } else {
                                i32++;
                                ndung22 = ndung23;
                                DaySo2 = DaySo2;
                            }
                        }
                    } else {
                        return "Không hiểu " + str18.substring(str18.indexOf("ghepdd"), j3);
                    }
                } else {
                    ndung13 = ndung19;
                    ndung23 = ndung22;
                    Dan2 = Dan;
                }

                if (!str18.contains("dan") || str18.indexOf("dan") >= 5) {
                    str_Dit = str_Dit7;
                    ndung19 = ndung13;
                    ndung24 = ndung23;
                } else if (str18.length() >= 5) {
                    int i6 = -1;
                    ndung19 = ndung13;
                    ndung24 = ndung23;
                    while (true) {
                        int i12 = str18.indexOf("dan", i6 + 1);
                        String str_Dau7 = str_Dau;
                        if (i12 == -1) {
                            str_Dau = str_Dau7;
                            str_Dit = str_Dit7;
                            break;
                        }
                        int i7 = i12 + 4;
                        while (true) {
                            if (i7 >= str18.length()) {
                                ndung16 = ndung19;
                                break;
                            }
                            if (!isNumeric(str18.substring(i7, i7 + 1))) {
                                ndung16 = ndung19;
                                if (!", ".contains(str18.substring(i7, i7 + 1))) {
                                    i7--;
                                    break;
                                }
                            } else {
                                ndung16 = ndung19;
                            }
                            i7++;
                            ndung19 = ndung16;
                        }
                        String Dan7 = str18.substring(i12 + 4, i7);
                        if (Dan7.length() == 0) {
                            return "Không hiểu " + str;
                        }
                        String Dan8 = Dan7.trim().replaceAll(" ", ",").trim().replaceAll(",,", ",").trim().replaceAll(",,", ",");
                        if (Dan8.length() != 3 || !Dan8.contains(",")) {
                            Dan2 = Dan8;
                        } else {
                            Dan2 = Dan8.replace(",", "");
                        }
                        String[] DanArr2 = Dan2.split(",");
                        int k = 0;
                        while (k < DanArr2.length) {
                            DanArr2[k] = DanArr2[k].replaceAll(" ", "");
                            if (DanArr2[k].length() != 2) {
                                break loop0;
                            } else if (!isNumeric(DanArr2[k])) {
                                break loop0;
                            } else if (Integer.parseInt(DanArr2[k].substring(0, 1)) >= Integer.parseInt(DanArr2[k].substring(1, 2)) - 1) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr2[k]), i7);
                            } else if (!isNumeric(DanArr2[k])) {
                                return "Không hiểu " + str18.substring(i12, i7);
                            } else {
                                String ndung211 = "";
                                if (DanArr2[k].length() == 2 && isNumeric(DanArr2[k])) {
                                    int j4 = Integer.parseInt(DanArr2[k].substring(0, 1));
                                    String ndung112 = "";
                                    for (int i8 = 1; j4 < Integer.parseInt(DanArr2[k].substring(i8)) + i8; i8 = 1) {
                                        ndung112 = ndung112 + j4;
                                        ndung211 = ndung211 + j4;
                                        j4++;
                                    }
                                    String str_Dau8 = GhepDau(ndung112);
                                    ndung16 = ndung112;
                                    if (str_Dau8.contains(str2)) {
                                        return "Không hiểu " + str;
                                    }
                                    String[] s = str_Dau8.split(",");
                                    String str_Dit11 = GhepDit(ndung211);
                                    str_Dau7 = str_Dau8;
                                    int indexOf = str_Dit11.indexOf(str2);
                                    if (indexOf > -1) {
                                        return "Không hiểu " + str;
                                    }
                                    String str26 = str_Dit11;
                                    int j5 = 0;
                                    String DaySo6 = DaySo2;
                                    while (true) {
                                        str_Dit7 = str_Dit11;
                                        if (j5 >= s.length) {
                                            break;
                                        }
                                        if (str26.contains(s[j5])) {
                                            DaySo6 = DaySo6 + s[j5] + ",";
                                        }
                                        j5++;
                                        str_Dit11 = str_Dit7;
                                    }
                                    str23 = str26;
                                    k++;
                                    DaySo2 = DaySo6;
                                    ndung24 = ndung211;
                                }
                            }
                        }
                        str18 = (str18.substring(0, i12) + " " + str18.substring(i7)).trim();
                        str_Dau = str_Dau7;
                        ndung19 = ndung16;
                        i6 = 0;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Tong4 = str_Tong6;
                    break;
                }
                String str_Dit12 = str_Dit;

                if (!str18.contains("boj") || str18.indexOf("boj") >= 5) {
                    str_Dau2 = str_Dau;
                    ndung14 = ndung19;
                    ndung25 = ndung24;
                } else if (str18.length() >= 5) {
                    int i9 = -1;
                    while (true) {
                        int i13 = str18.indexOf("boj", i9 + 1);
                        str_Dau2 = str_Dau;
                        if (i13 == -1) {
                            ndung14 = ndung19;
                            ndung25 = ndung24;
                            break;
                        }
                        int i10 = i13 + 4;
                        while (true) {
                            if (i10 >= str18.length()) {
                                ndung15 = ndung19;
                                break;
                            }
                            ndung15 = ndung19;
                            if (!", ".contains(str18.substring(i10, i10 + 1)) && !isNumeric(str18.substring(i10, i10 + 1))) {
                                i10--;
                                break;
                            }
                            i10++;
                            ndung19 = ndung15;
                        }
                        try {
                            String Dan9 = str18.substring(i13, i10).replaceAll("boj", "").trim();
                            if (Dan9.trim().length() == 0) {
                                return "Không hiểu " + str18;
                            }
                            String Dan10 = Dan9.trim().replaceAll(" ", ",").trim().replaceAll(",,", ",");
                            String[] s2 = Dan10.split(",");
                            int i25 = 0;
                            while (i25 < s2.length) {
                                if (isNumeric(s2[i25]) && s2[i25].length() == 2) {
                                    DaySo2 = DaySo2 + GhepBo(s2[i25]);
                                    i25++;
                                }
                            }
                            str18 = (str18.substring(0, i13) + " " + str18.substring(i10)).trim();
                            Dan2 = Dan10;
                            ndung19 = ndung15;
                            i9 = 0;
                            str_Dau = str_Dau2;
                        } catch (Exception e) {
                            return "Không hiểu " + str;
                        }
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit12;
                    str_Tong4 = str_Tong6;
                    ndung19 = ndung14;
                    break;
                }

                if (!str18.contains("cham tong") || str18.indexOf("cham tong") >= 5) {
                    str_Dit = str_Dit12;
                    str_Tong4 = str_Tong6;
                } else if (str18.length() <= 10) {
                    return "Không hiểu " + str;
                } else if (str18.substring(0, 11).contains("cham tong")) {
                    int i14 = -1;
                    String str_Dau9 = str_Dau2;
                    while (true) {
                        int i15 = str18.indexOf("cham tong", i14 + 1);
                        if (i15 == -1) {
                            str_Dau2 = str_Dau9;
                            str_Dit = str_Dit12;
                            str_Tong4 = str_Tong6;
                            break;
                        }
                        int i11 = i15 + 10;
                        while (true) {
                            if (i11 >= str18.length()) {
                                break;
                            }
                            if (!"0123456789, ".contains(str18.substring(i11, i11 + 1))) {
                                i11--;
                                break;
                            }
                            i11++;
                        }
                        if (str18.length() > 10) {
                            Dan5 = str18.substring(i15, i11).replaceAll("cham tong", "").trim().replaceAll(" ", ",");
                        } else {
                            Dan5 = "";
                        }
                        if (Dan5.length() == 0) {
                            return "Không hiểu " + str18.substring(i15, i11);
                        }
                        String[] DanArr3 = Dan5.split(",");
                        int i33 = 0;
                        while (i33 < DanArr3.length) {
                            if (DanArr3[i33].length() == 2 && isNumeric(DanArr3[i33]) && i33 > 0 && !isNumericComma(Dan5)) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr3[i33]), i11);
                            } else if (DanArr3[i33].length() == 3 && isNumeric(DanArr3[i33]) && i33 > 0 && !isNumericComma(Dan5)) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr3[i33]), i11);
                            } else if (!isNumeric(DanArr3[i33])) {
                                return "Không hiểu " + str18.substring(i15, i11);
                            } else {
                                i33++;
                            }
                        }
                        String str_Dau10 = GhepDau(Dan5);
                        if (str_Dau10.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        String str_Dit13 = GhepDit(Dan5);
                        if (str_Dit13.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        String str_Tong7 = GhepTong(Dan5);
                        if (str_Tong7.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        int i26 = 0;
                        String DaySo7 = DaySo2;
                        while (i26 < 100) {
                            if (i26 < 10) {
                                DaySo7 = DaySo7 + "0" + i26 + ",";
                                i26++;
                            } else {
                            }
                            if (i26 > 9) {
                                if (!str_Dau10.contains("" + i26)) {
                                    if (!str_Dit13.contains("" + i26)) {
                                        if (!str_Tong7.contains("" + i26)) {
                                        }
                                    }
                                }
                                DaySo7 = DaySo7 + i26 + ",";
                            }
                            i26++;
                        }
                        str18 = (str18.substring(0, i15) + " " + str18.substring(i11)).trim();
                        i14 = 0;
                        str_Tong6 = str_Tong7;
                        str_Dit12 = str_Dit13;
                        DaySo2 = DaySo7;
                        str_Dau9 = str_Dau10;
                        Dan2 = Dan5;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    ndung19 = ndung14;
                    break;
                }

                if (!str18.contains("cham") || str18.indexOf("cham") >= 5) { //xu ly khi các muc truoc do da xu ly
                    str_Tong2 = str_Tong4;
                } else if (str18.length() >= 6) {
                    int i16 = -1;
                    while (true) {
                        int i17 = str18.indexOf("cham", i16 + 1);
                        if (i17 == -1) {
                            str_Tong2 = str_Tong4;
                            break;
                        }
                        int i18 = i17 + 5;
                        while (true) {
                            if (i18 >= str18.length()) {
                                break;
                            }
                            if (!"0123456789, ".contains(str18.substring(i18, i18 + 1))) {
                                i18--;
                                break;
                            }
                            i18++;
                        }
                        if (str18.length() > 5) {
                            Dan4 = str18.substring(i17, i18).replaceAll("cham", "").trim().replaceAll(" ", ",");
                        } else {
                            Dan4 = "";
                        }
                        if (Dan4.length() == 0) {
                            return "Không hiểu " + str18.substring(i17, i18);
                        }
                        String[] DanArr4 = Dan4.split(",");
                        int i34 = 0;
                        while (i34 < DanArr4.length) {// 2 hoac 3 chu so hoac khong phai so => khong hiu
                            if (DanArr4[i34].length() == 2 && isNumeric(DanArr4[i34]) && i34 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr4[i34]), i18);
                            } else if (DanArr4[i34].length() == 3 && isNumeric(DanArr4[i34]) && i34 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr4[i34]), i18);
                            } else if (!isNumeric(DanArr4[i34])) {
                                return "Không hiểu " + str18.substring(i17, i18);
                            } else {
                                i34++;
                            }
                        }

                        String ghepDau = GhepDau(Dan4);
                        if (ghepDau.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        String ghepDit = GhepDit(Dan4);
                        if (ghepDit.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }

                        for (int i = 0; i < 100; i++) {
                            String so = i > 10 ? String.valueOf(i) : "0" + i;
                            if(ghepDau.contains(so) || ghepDit.contains(so)) DaySo2 = DaySo2 + so + ",";
                        }

                        str18 = (str18.substring(0, i17) + " " + str18.substring(i18)).trim();
                        Log.e("ContentValues", "XulySo: DaySo2 " + DaySo2 + " Str18 "+ str18 );
                        i16 = 0;
                        str_Dau2 = ghepDau;
                        str_Dit = ghepDit;
                        Dan2 = Dan4;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }

                if (!str18.contains("tong") || str18.indexOf("tong") >= 5) {
                    str_Dit2 = str_Dit;
                } else if (str18.length() >= 6) {
                    int i19 = -1;
                    while (true) {
                        int i110 = str18.indexOf("tong", i19 + 1);
                        if (i110 == -1) {
                            str_Dit2 = str_Dit;
                            break;
                        }
                        int i20 = i110 + 5;
                        while (true) {
                            if (i20 >= str18.length()) {
                                break;
                            }
                            if (!", ".contains(str18.substring(i20, i20 + 1)) && !isNumeric(str18.substring(i20, i20 + 1))) {
                                i20--;
                                break;
                            }
                            i20++;
                        }
                        if (str18.substring(i110, i20).length() > 5) {
                            Dan2 = str18.substring(i110, i20).replaceAll("tong", "").trim().replaceAll(" ", ",");
                        } else {
                            Dan2 = "";
                        }
                        if (Dan2.length() == 0) {
                            return "Không hiểu " + str18;
                        }
                        String[] DanArr5 = Dan2.split(",");
                        int i35 = 0;
                        while (i35 < DanArr5.length) {
                            if (DanArr5[i35].length() == 2 && isNumeric(DanArr5[i35]) && i35 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr5[i35]), i20);
                            } else if (DanArr5[i35].length() == 3 && isNumeric(DanArr5[i35]) && i35 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr5[i35]), i20);
                            } else if (!isNumeric(DanArr5[i35])) {
                                return "Không hiểu " + str18.substring(i110, i20);
                            } else {
                                i35++;
                            }
                        }
                        String str_Tong8 = GhepTong(Dan2);
                        if (str_Tong8.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        DaySo2 = DaySo2 + str_Tong8;
                        str18 = (str18.substring(0, i110) + " " + str18.substring(i20)).trim();
                        i19 = 0;
                        str_Tong2 = str_Tong8;
                        str_Dit = str_Dit;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit2;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                int i21 = -1;
                if (!str18.contains("ghep") && str18.contains("dau dit")) {
                    int i111 = -1;
                    str_Dit = str_Dit2;
                    while (true) {
                        int i112 = str18.indexOf("dau dit", i111 + 1);
                        if (i112 == i21) {
                            break;
                        }
                        int i28 = i112 + 8;
                        while (true) {
                            if (i28 >= str18.length()) {
                                break;
                            }
                            if (!", ".contains(str18.substring(i28, i28 + 1)) && !isNumeric(str18.substring(i28, i28 + 1))) {
                                i28--;
                                break;
                            }
                            i28++;
                        }
                        if (str18.length() > 8) {
                            String Dan11 = str18.substring(i112, i28);
                            if (Dan11.substring(0, 8).contains("dau dit")) {
                                Dan2 = Dan11.replaceAll("dau dit", "").trim().replaceAll(" ", ",");
                            } else {
                                Dan2 = Dan11;
                            }
                        } else {
                            Dan2 = "";
                        }
                        if (Dan2.length() == 0) {
                            return "Không hiểu " + str;
                        }
                        String[] DanArr6 = Dan2.split(",");
                        int i36 = 0;
                        while (i36 < DanArr6.length) {
                            if (DanArr6[i36].length() == 2 && isNumeric(DanArr6[i36]) && i36 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr6[i36]), i28);
                            } else if (DanArr6[i36].length() == 3 && isNumeric(DanArr6[i36]) && i36 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr6[i36]), i28);
                            } else if (!isNumeric(DanArr6[i36])) {
                                return "Không hiểu " + str18.substring(i112, i28);
                            } else {
                                i36++;
                            }
                        }
                        String str_Dau12 = GhepDau(Dan2);
                        if (str_Dau12.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        String DaySo8 = DaySo2 + str_Dau12;
                        String str_Dit15 = GhepDit(Dan2);
                        if (str_Dit15.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        DaySo2 = DaySo8 + str_Dit15;
                        str18 = (str18.substring(0, i112) + " " + str18.substring(i28)).trim();
                        i111 = 0;
                        str_Dau2 = str_Dau12;
                        i21 = -1;
                        str_Dit = str_Dit15;
                    }
                } else {
                    str_Dit = str_Dit2;
                }
                if (str18.trim().length() == 0) {
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                if (str18.indexOf("dau") <= -1 || str18.indexOf("dau") >= 5) {
                    str_Dit3 = str_Dit;
                } else if (str18.indexOf("ghep") != -1 || str18.indexOf("dau dit") != -1) {
                    str_Dit3 = str_Dit;
                } else if (str18.length() >= 5) {
                    int i113 = -1;
                    while (true) {
                        int i114 = str18.indexOf("dau", i113 + 1);
                        if (i114 == -1) {
                            str_Dit3 = str_Dit;
                            break;
                        }
                        int i29 = i114 + 5;
                        while (true) {
                            if (i29 >= str18.length()) {
                                break;
                            }
                            if (", ".indexOf(str18.substring(i29, i29 + 1)) <= -1 && !isNumeric(str18.substring(i29, i29 + 1))) {
                                i29--;
                                break;
                            }
                            i29++;
                        }
                        if (str18.length() > 4) {
                            String Dan12 = str18.substring(i114, i29);
                            str_Dit5 = str_Dit;
                            if (Dan12.substring(0, 4).indexOf("dau") > -1) {
                                Dan2 = Dan12.replaceAll("dau", "").trim().replaceAll(" ", ",");
                            } else {
                                Dan2 = Dan12;
                            }
                        } else {
                            str_Dit5 = str_Dit;
                            Dan2 = "";
                        }
                        if (Dan2.length() == 0) {
                            return "Không hiểu " + str18;
                        }
                        String[] DanArr7 = Dan2.split(",");
                        int i37 = 0;
                        while (i37 < DanArr7.length) {
                            if (DanArr7[i37].length() == 2 && isNumeric(DanArr7[i37]) && i37 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr7[i37]), i29);
                            } else if (DanArr7[i37].length() == 3 && isNumeric(DanArr7[i37]) && i37 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr7[i37]), i29);
                            } else if (!isNumeric(DanArr7[i37])) {
                                return "Không hiểu " + str18.substring(i114, i29);
                            } else {
                                i37++;
                            }
                        }
                        String str_Dau13 = GhepDau(Dan2);
                        if (str_Dau13.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        DaySo2 = DaySo2 + str_Dau13;
                        str18 = (str18.substring(0, i114) + " " + str18.substring(i29)).trim();
                        i113 = 0;
                        str_Dau2 = str_Dau13;
                        str_Dit = str_Dit5;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit3;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                if (!str18.contains("dit") || str18.indexOf("dit") >= 5 || str18.contains("ghep") || str18.contains("dau dit")) {
                    str_Dit = str_Dit3;
                } else if (str18.length() >= 5) {
                    int i115 = -1;
                    str_Dit = str_Dit3;
                    while (true) {
                        int i116 = str18.indexOf("dit", i115 + 1);
                        if (i116 == -1) {
                            break;
                        }
                        int i30 = i116 + 5;
                        while (true) {
                            if (i30 >= str18.length()) {
                                break;
                            }
                            if (", ".indexOf(str18.substring(i30, i30 + 1)) <= -1 && !isNumeric(str18.substring(i30, i30 + 1))) {
                                i30--;
                                break;
                            }
                            i30++;
                        }
                        if (str18.length() > 4) {
                            String Dan13 = str18.substring(i116, i30);
                            if (Dan13.substring(0, 4).indexOf("dit") > -1) {
                                Dan2 = Dan13.replaceAll("dit", "").trim().replaceAll(" ", ",");
                            } else {
                                Dan2 = Dan13;
                            }
                        } else {
                            Dan2 = "";
                        }
                        if (Dan2.length() == 0) {
                            return "Không hiểu " + str;
                        }
                        String[] DanArr8 = Dan2.split(",");
                        int i38 = 0;
                        while (i38 < DanArr8.length) {
                            if (DanArr8[i38].length() == 2 && isNumeric(DanArr8[i38]) && i38 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr8[i38]), i30);
                            } else if (DanArr8[i38].length() == 3 && isNumeric(DanArr8[i38]) && i38 > 0) {
                                return "Không hiểu " + str18.substring(str18.indexOf(DanArr8[i38]), i30);
                            } else if (!isNumeric(DanArr8[i38])) {
                                return "Không hiểu " + str18.substring(i116, i30);
                            } else {
                                i38++;
                            }
                        }
                        String str_Dit16 = GhepDit(Dan2);
                        if (str_Dit16.contains(KHONG_HIEU)) {
                            return "Không hiểu " + str;
                        }
                        DaySo2 = DaySo2 + str_Dit16;
                        str18 = (str18.substring(0, i116) + " " + str18.substring(i30)).trim();
                        i115 = 0;
                        str_Dit = str_Dit16;
                    }
                } else {
                    return "Không hiểu " + str;
                }
                if (str18.trim().length() == 0) {
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                int i31 = -1;
                if (str18.indexOf("to ") > -1 || str18.indexOf("nho") > -1) {
                    if (str18.indexOf("to") <= -1 || str18.indexOf("nho") <= -1) {
                        str_Dit4 = str_Dit;
                        Dan3 = Dan2;
                    } else {
                        while (true) {
                            if ((str18.indexOf("to ") > i31 && str18.indexOf("to ") < 5) || (str18.indexOf("nho ") > -1 && str18.indexOf("nho") < 5)) {
                                int i210 = DemVonglap2 + 1;
                                if (i210 <= 100) {
                                    String Ktra2 = str18;
                                    str_Dit4 = str_Dit;
                                    if (str18.indexOf("to") < str18.indexOf("nho")) {
                                        int i117 = -1;
                                        while (true) {
                                            int indexOf2 = str18.indexOf("to", i117 + 1);
                                            i117 = indexOf2;
                                            DemVonglap2 = i210;
                                            if (indexOf2 == -1) {
                                                Ktra = Ktra2;
                                                Dan3 = Dan2;
                                                break;
                                            }
                                            int i211 = i117 + 1;
                                            while (true) {
                                                if (i211 >= str18.length()) {
                                                    Ktra = Ktra2;
                                                    break;
                                                }
                                                Ktra = Ktra2;
                                                if (str18.substring(i117 + 1, i211).indexOf("to") > -1 || str18.substring(i117 + 1, i211).indexOf("nho") > -1) {
                                                    break;
                                                }
                                                i211++;
                                                Ktra2 = Ktra;
                                            }
                                            String sss = str18.substring(i117, i211).replaceAll(" ", "").replaceAll(",", "");
                                            Dan3 = Dan2;
                                            if (sss.indexOf("toto") > -1) {
                                                str18 = str18.substring(0, i117) + " " + str18.substring(i211);
                                                DaySo2 = DaySo2 + "55,56,57,58,59,65,66,67,68,69,75,76,77,78,79,85,86,87,88,89,95,96,97,98,99,";
                                                break;
                                            } else if (sss.indexOf("tonho") > -1) {
                                                str18 = str18.substring(0, i117) + " " + str18.substring(i211);
                                                DaySo2 = DaySo2 + "50,51,52,53,54,60,61,62,63,64,70,71,72,73,74,80,81,82,83,84,90,91,92,93,94,";
                                                break;
                                            } else {
                                                i210 = DemVonglap2;
                                                Ktra2 = Ktra;
                                                Dan2 = Dan3;
                                            }
                                        }
                                    } else {
                                        DemVonglap2 = i210;
                                        Ktra = Ktra2;
                                        Dan3 = Dan2;
                                        if (str18.indexOf("nho") < str18.indexOf("to")) {
                                            int i118 = -1;
                                            while (true) {
                                                int indexOf3 = str18.indexOf("nho", i118 + 1);
                                                i118 = indexOf3;
                                                if (indexOf3 == -1) {
                                                    break;
                                                }
                                                int i212 = i118 + 1;
                                                while (i212 < str18.length() && str18.substring(i118 + 1, i212).indexOf("to") <= -1 && str18.substring(i118 + 1, i212).indexOf("nho") <= -1) {
                                                    i212++;
                                                }
                                                String sss2 = str18.substring(i118, i212).replaceAll(" ", "").replaceAll(",", "");
                                                if (sss2.indexOf("nhoto") > -1) {
                                                    str18 = str18.substring(0, i118) + " " + str18.substring(i212);
                                                    DaySo2 = DaySo2 + "05,06,07,08,09,15,16,17,18,19,25,26,27,28,29,35,36,37,38,39,45,46,47,48,49,";
                                                    break;
                                                } else if (sss2.indexOf("nhonho") > -1) {
                                                    str18 = str18.substring(0, i118) + " " + str18.substring(i212);
                                                    DaySo2 = DaySo2 + "00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,";
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (str18.indexOf("to") == -1 || str18.indexOf("nho") == -1) {
                                        break;
                                    }
                                    str_Dit = str_Dit4;
                                    Dan2 = Dan3;
                                    i31 = -1;
                                } else {
                                    return "Không hiểu " + str;
                                }
                            } else {
                                str_Dit4 = str_Dit;
                                Dan3 = Dan2;
                            }
                        }
                        str_Dit4 = str_Dit;
                        Dan3 = Dan2;
                    }
                    if (str18.indexOf("to") > -1 && str18.indexOf("nho") == -1) {
                        if (str18.indexOf("toto") > -1) {
                            DaySo2 = DaySo2 + "55,56,57,58,59,65,66,67,68,69,75,76,77,78,79,85,86,87,88,89,95,96,97,98,99,";
                            str18 = str18.substring(0, str18.indexOf("toto")) + " " + str18.substring(str18.indexOf("toto") + 4);
                        }
                        if (str18.indexOf("to to") > -1) {
                            DaySo2 = DaySo2 + "55,56,57,58,59,65,66,67,68,69,75,76,77,78,79,85,86,87,88,89,95,96,97,98,99,";
                            str18 = str18.substring(0, str18.indexOf("to to")) + " " + str18.substring(str18.indexOf("to to") + 5);
                        }
                    }
                    if (str18.indexOf("to") == -1 && str18.indexOf("nho") > -1) {
                        if (str18.indexOf("nhonho") > -1) {
                            DaySo2 = DaySo2 + "00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,";
                            str18 = str18.substring(0, str18.indexOf("nhonho")) + " " + str18.substring(str18.indexOf("nhonho") + 6);
                        }
                        if (str18.indexOf("nho nho") > -1) {
                            str18 = str18.substring(0, str18.indexOf("nho nho")) + " " + str18.substring(str18.indexOf("nho nho") + 7);
                            DaySo2 = DaySo2 + "00,01,02,03,04,10,11,12,13,14,20,21,22,23,24,30,31,32,33,34,40,41,42,43,44,";
                        }
                    }
                } else {
                    str_Dit4 = str_Dit;
                    Dan3 = Dan2;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit4;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                if (str18.indexOf("chan") > -1 || (str18.indexOf("le") > -1 && str18.indexOf("chan") < 5 && str18.indexOf("le") < 5)) {
                    int i39 = -1;
                    if (str18.indexOf("chan") > -1 && str18.indexOf("le") > -1) {
                        while (true) {
                            if (str18.indexOf("lechan") <= i39 && str18.indexOf("chanle") <= i39 && str18.indexOf("le chan") <= i39 && str18.indexOf("chan le") <= i39) {
                                break;
                            }
                            int DemVonglap4 = DemVonglap2 + 1;
                            if (DemVonglap4 <= 100) {
                                if (str18.indexOf("chan") < str18.indexOf("le")) {
                                    int i119 = -1;
                                    while (true) {
                                        int indexOf4 = str18.indexOf("chan", i119 + 1);
                                        i119 = indexOf4;
                                        if (indexOf4 == -1) {
                                            DemVonglap2 = DemVonglap4;
                                            break;
                                        }
                                        int i213 = i119 + 1;
                                        while (i213 < str18.length() && str18.substring(i119 + 1, i213).indexOf("chan") <= -1 && str18.substring(i119 + 1, i213).indexOf("le") <= -1) {
                                            i213++;
                                        }
                                        String sss3 = str18.substring(i119, i213).replaceAll(" ", "").replaceAll(",", "");
                                        DemVonglap2 = DemVonglap4;
                                        if (sss3.indexOf("chanchan") > -1) {
                                            str18 = str18.substring(0, i119) + " " + str18.substring(i213);
                                            DaySo2 = DaySo2 + "00,02,04,06,08,20,22,24,26,28,40,42,44,46,48,60,62,64,66,68,80,82,84,86,88,";
                                            break;
                                        } else if (sss3.indexOf("chanle") > -1) {
                                            str18 = str18.substring(0, i119) + " " + str18.substring(i213);
                                            DaySo2 = DaySo2 + "01,03,05,07,09,21,23,25,27,29,41,43,45,47,49,61,63,65,67,69,81,83,85,87,89,";
                                            break;
                                        } else {
                                            DemVonglap4 = DemVonglap2;
                                        }
                                    }
                                } else {
                                    DemVonglap2 = DemVonglap4;
                                    if (str18.indexOf("le") < str18.indexOf("chan")) {
                                        int i120 = -1;
                                        while (true) {
                                            int indexOf5 = str18.indexOf("le", i120 + 1);
                                            i120 = indexOf5;
                                            if (indexOf5 == -1) {
                                                break;
                                            }
                                            int i214 = i120 + 1;
                                            while (i214 < str18.length() && str18.substring(i120 + 1, i214).indexOf("chan") <= -1 && str18.substring(i120 + 1, i214).indexOf("le") <= -1) {
                                                i214++;
                                            }
                                            String sss4 = str18.substring(i120, i214).replaceAll(" ", "").replaceAll(",", "");
                                            if (sss4.indexOf("lechan") > -1) {
                                                str18 = str18.substring(0, i120) + " " + str18.substring(i214);
                                                DaySo2 = DaySo2 + "10,12,14,16,18,30,32,34,36,38,50,52,54,56,58,70,72,74,76,78,90,92,94,96,98,";
                                                break;
                                            } else if (sss4.indexOf("lele") > -1) {
                                                str18 = str18.substring(0, i120) + " " + str18.substring(i214);
                                                DaySo2 = DaySo2 + "11,13,15,17,19,31,33,35,37,39,51,53,55,57,59,71,73,75,77,79,91,93,95,97,99,";
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!str18.contains("chan") || !str18.contains("le")) {
                                    break;
                                }
                                i39 = -1;
                            } else {
                                return "Không hiểu " + str;
                            }
                        }
                    }
                    if (str18.contains("chan") && !str18.contains("le")) {
                        if (str18.contains("chanchan")) {
                            DaySo2 = DaySo2 + "00,02,04,06,08,20,22,24,26,28,40,42,44,46,48,60,62,64,66,68,80,82,84,86,88,";
                            str18 = str18.substring(0, str18.indexOf("chanchan")) + " " + str18.substring(str18.indexOf("chanchan") + 8);
                        }
                        if (str18.contains("chan chan")) {
                            DaySo2 = DaySo2 + "00,02,04,06,08,20,22,24,26,28,40,42,44,46,48,60,62,64,66,68,80,82,84,86,88,";
                            str18 = str18.substring(0, str18.indexOf("chan chan")) + " " + str18.substring(str18.indexOf("chan chan") + 9);
                        }
                    }
                    if (!str18.contains("chan") && str18.contains("le")) {
                        if (str18.contains("lele")) {
                            DaySo2 = DaySo2 + "11,13,15,17,19,31,33,35,37,39,51,53,55,57,59,71,73,75,77,79,91,93,95,97,99,";
                            str18 = str18.substring(0, str18.indexOf("lele")) + " " + str18.substring(str18.indexOf("lele") + 4);
                        }
                        if (str18.contains("le le")) {
                            str18 = str18.substring(0, str18.indexOf("le le")) + " " + str18.substring(str18.indexOf("le le") + 5);
                            DaySo2 = DaySo2 + "11,13,15,17,19,31,33,35,37,39,51,53,55,57,59,71,73,75,77,79,91,93,95,97,99,";
                        }
                    }
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit4;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                if (str18.contains("kep")) {
                    str18 = str18.replaceAll("bang", "");
                    int i121 = -1;
                    while (true) {
                        int indexOf6 = str18.indexOf("kep", i121 + 1);
                        i121 = indexOf6;
                        if (indexOf6 == -1) {
                            break;
                        } else if (!str18.contains("sat") || str18.indexOf("sat") >= i121 || str18.indexOf("lech") >= i121 + 6 || str18.indexOf("lech") <= -1) {
                            if (str18.indexOf("sat") < i121 && str18.contains("sat")) {
                                String sss5 = str18.substring(str18.indexOf("sat"), i121 + 3).replaceAll(" ", "").replaceAll(",", "");
                                if (sss5.contains("satkep")) {
                                    str18 = str18.substring(0, str18.indexOf("sat")) + " " + str18.substring(i121 + 3, str18.length());
                                    i121 = 0;
                                    DaySo2 = DaySo2 + "01,10,12,21,23,32,34,43,45,54,56,65,67,76,78,87,89,98,";
                                } else if (sss5.contains("sathaikep")) {
                                    str18 = str18.substring(0, str18.indexOf("sat")) + " " + str18.substring(i121 + 3, str18.length());
                                    i121 = 0;
                                    DaySo2 = DaySo2 + "01,10,12,21,23,32,34,43,45,54,56,65,67,76,78,87,89,98,04,06,51,15,17,60,62,26,28,71,73,37,39,82,84,48,93,95,";
                                } else if (sss5.indexOf("sat2kep") > -1) {
                                    str18 = str18.substring(0, str18.indexOf("sat")) + " " + str18.substring(i121 + 3, str18.length());
                                    i121 = 0;
                                    DaySo2 = DaySo2 + "01,10,12,21,23,32,34,43,45,54,56,65,67,76,78,87,89,98,04,06,51,15,17,60,62,26,28,71,73,37,39,82,84,48,93,95,";
                                }
                            } else if (str18.indexOf("lech") <= -1 || str18.indexOf("lech") >= i121 + 5) {
                                if (str18.indexOf("le") <= -1 || str18.indexOf("le") >= i121 + 5) {
                                    if (str18.indexOf("chan") <= -1 || str18.indexOf("chan") >= i121 + 5) {
                                        if (str18.indexOf(" 2 kep") <= -1 || str18.indexOf(" 2 kep") >= i121 + 3) {
                                            DaySo2 = DaySo2 + "00,11,22,33,44,55,66,77,88,99,";
                                            str18 = str18.substring(0, i121) + " " + str18.substring(i121 + 3);
                                        } else {
                                            DaySo2 = DaySo2 + "00,11,22,33,44,55,66,77,88,99,05,50,16,61,27,72,38,83,49,94,";
                                            str18 = str18.substring(0, i121 - 2) + " " + str18.substring(i121 + 3);
                                        }
                                    } else if (str18.substring(i121, str18.indexOf("chan") + 4).replaceAll(" ", "").replaceAll(",", "").indexOf("kepchan") > -1) {
                                        str18 = str18.substring(0, i121) + " " + str18.substring(str18.indexOf("chan") + 4);
                                        DaySo2 = DaySo2 + "00,22,44,66,88,";
                                    }
                                } else if (str18.substring(i121, str18.indexOf("le") + 2).replaceAll(" ", "").replaceAll(",", "").indexOf("keple") > -1) {
                                    str18 = str18.substring(0, i121) + " " + str18.substring(str18.indexOf("le") + 2);
                                    DaySo2 = DaySo2 + "11,33,55,77,99,";
                                }
                            } else if (str18.substring(i121, str18.indexOf("lech") + 4).replaceAll(" ", "").replaceAll(",", "").indexOf("keplech") > -1) {
                                str18 = str18.substring(0, i121) + " " + str18.substring(str18.indexOf("lech") + 4);
                                DaySo2 = DaySo2 + "05,50,16,61,27,72,38,83,49,94,";
                            }
                        } else if (str18.substring(str18.indexOf("sat"), str18.indexOf("lech") + 4).replaceAll(" ", "").replaceAll(",", "").indexOf("satkeplech") > -1) {
                            str18 = str18.substring(0, str18.indexOf("sat")) + " " + str18.substring(str18.indexOf("lech") + 4, str18.length());
                            i121 = 0;
                            DaySo2 = DaySo2 + "04,06,51,15,17,60,62,26,28,71,73,37,39,82,84,48,93,95,";
                        }
                    }
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit4;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                if (str18.contains("so") && str18.indexOf("so") < 7) {
                    String str27 = str18.trim();
                    if (str27.length() <= 3) {
                        return "Không hiểu " + str;
                    } else if (!str27.substring(0, 2).contains("so")) {
                        return "Không hiểu " + str;
                    } else {
                        str18 = str27.substring(str27.indexOf("so") + 2);
                    }
                } else if (str18.contains("con") && str18.indexOf("con") < 4) {
                    if (str18.trim().length() <= 4) {
                        return "Không hiểu " + str;
                    } else if (!str18.substring(0, 5).contains("con")) {
                        return "Không hiểu " + str;
                    } else {
                        str18 = str18.substring(str18.indexOf("con") + 3);
                    }
                }
                int i122 = 0;
                while (true) {
                    if (i122 >= str18.length()) {
                        break;
                    }
                    if (!", ".contains(str18.substring(i122, i122 + 1)) && !isNumeric(str18.substring(i122, i122 + 1))) {
                        i122--;
                        break;
                    }
                    i122++;
                }
                if (i122 < 0) {
                    i122 = 0;
                }
                if (str18.substring(0, i122).length() > 2) {
                    String[] array = str18.substring(0, i122).trim().replaceAll(" ", ",").split(",");
                    int i40 = 0;
                    while (i40 < array.length) {
                        if (isNumeric(array[i40]) && array[i40].length() == 2) {
                            DaySo2 = DaySo2 + array[i40] + ",";
                        } else if (isNumeric(array[i40]) && array[i40].length() == 4) {
                            if (array[i40].substring(2, 4).indexOf("00") > -1) {
                                return "Không hiểu " + array[i40];
                            }
                            DaySo2 = (DaySo2 + array[i40].substring(0, 2) + ",") + array[i40].substring(2, 4) + ",";
                        } else if (isNumeric(array[i40]) && array[i40].length() == 3) {
                            if (array[i40].charAt(0) != array[i40].charAt(2)) {
                                return "Không hiểu " + array[i40];
                            } else if (array[i40].charAt(0) == array[i40].charAt(1) && array[i40].charAt(0) == array[i40].charAt(2)) {
                                return "Không hiểu ," + array[i40] + ",";
                            } else {
                                DaySo2 = (DaySo2 + array[i40].substring(0, 2) + ",") + array[i40].substring(1, 3) + ",";
                            }
                        } else if (array[i40].length() != 0) {
                            if (array[i40].length() == 1) {
                                String kytuktra = "";
                                if (str18.indexOf(" " + array[i40] + " ") > -1) {
                                    kytuktra = " " + array[i40] + " ";
                                }
                                if (str18.indexOf(" " + array[i40] + ",") > -1) {
                                    kytuktra = " " + array[i40] + ",";
                                }
                                if (str18.indexOf("," + array[i40] + ",") > -1) {
                                    kytuktra = "," + array[i40] + ",";
                                }
                                if (str18.indexOf("," + array[i40] + " ") > -1) {
                                    kytuktra = "," + array[i40] + " ";
                                }
                                return "Không hiểu " + kytuktra + "";
                            }
                            return "Không hiểu " + array[i40];
                        }
                        i40++;
                    }
                    if (i40 < array.length) {
                        str18 = "  " + str18 + "  ";
                        String str110 = str18.trim();
                        if (str110.trim().length() > 10) {
                            if (array[i40].length() == 1) {
                                i122 = -1;
                                while (true) {
                                    int indexOf7 = str18.indexOf(array[i40], i122 + 1);
                                    i122 = indexOf7;
                                    if (indexOf7 == -1) {
                                        str1 = str110;
                                        break;
                                    }
                                    if (str18.indexOf(" " + array[i40] + " ") > -1) {
                                        return "Không hiểu  " + array[i40] + " ";
                                    }
                                    if (str18.indexOf(" " + array[i40] + ",") > -1) {
                                        return "Không hiểu  " + array[i40] + ",";
                                    }
                                    if (str18.indexOf("," + array[i40] + " ") > -1) {
                                        return "Không hiểu ," + array[i40] + " ";
                                    }
                                    if (str18.indexOf("," + array[i40] + ",") > -1) {
                                        return "Không hiểu ," + array[i40] + ",";
                                    }
                                }
                            } else {
                                return "Không hiểu " + array[i40];
                            }
                        } else if (str18.trim().length() == 1) {
                            return "Không hiểu  " + str18.trim() + " ";
                        } else {
                            return "Không hiểu " + str18.trim();
                        }
                    }
                    str18 = str18.substring(i122).trim();
                    str22 = str1;
                } else {
                    str22 = str1;
                }
                if (str18.trim().length() == 0) {
                    str_Dit = str_Dit4;
                    str_Tong4 = str_Tong2;
                    ndung19 = ndung14;
                    break;
                }
                str_Dau4 = str23;
                DemVonglap = DemVonglap2;
                str_Tong5 = str_Dit4;
                str_Dit6 = str_Dau2;
                str_Tong4 = str_Tong2;
                ndung19 = ndung14;
                ndung28 = ndung25;
                DaySo3 = Dan3;
                SKtra2 = SKtra;
            }
        }
        if (str18.replaceAll(" ", "").length() > 0) {
            String str111 = str18.trim().replaceAll(":", " ").replaceAll(" //. ", "").replaceAll(" , ", "").replaceAll(";", " ").replaceAll("/", "").replaceAll("\\.", ",").replaceAll(" ", ",").replaceAll(" ", ",").replaceAll(" ", ",");
            if (str18.indexOf("so") <= -1 || str18.indexOf("so") >= 3) {
                if (str18.indexOf("con") > -1 && str18.indexOf("con") < 4) {
                    if (str111.length() <= 4) {
                        return "Không hiểu " + str;
                    } else if (str18.substring(0, 5).indexOf("con") == -1) {
                        return "Không hiểu " + str;
                    }
                }
            } else if (str111.length() <= 3) {
                return "Không hiểu " + str;
            } else if (str18.substring(0, 4).indexOf("so") == -1) {
                return "Không hiểu " + str;
            }
            String[] array2 = str111.split(",");
            int i41 = 0;
            while (true) {
                if (i41 >= array2.length) {
                    break;
                }
                if (!isNumeric(array2[i41]) || array2[i41].length() != 2) {
                    if (!isNumeric(array2[i41]) || array2[i41].length() != 3) {
                        str_Tong = str_Tong4;
                        ndung1 = ndung19;
                        if (array2[i41].length() != 0) {
                            break;
                        }
                    } else {
                        str_Tong = str_Tong4;
                        if (array2[i41].charAt(0) != array2[i41].charAt(2)) {
                            break;
                        }
                        if (array2[i41].charAt(0) == array2[i41].charAt(1) && array2[i41].charAt(0) == array2[i41].charAt(2)) {
                            break;
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(DaySo2);
                        ndung1 = ndung19;
                        sb2.append(array2[i41].substring(0, 2));
                        sb2.append(",");
                        DaySo2 = sb2.toString() + array2[i41].substring(1, 3) + ",";
                    }
                } else {
                    DaySo2 = DaySo2 + array2[i41] + ",";
                    str_Tong = str_Tong4;
                    ndung1 = ndung19;
                }
                i41++;
                ndung19 = ndung1;
                str_Dit = str_Dit;
                str_Tong4 = str_Tong;
            }
            if (i41 < array2.length) {
                String str28 = "  " + str18 + "  ";
                if (str111.trim().length() > 10) {
                    if (array2[i41].length() == 1) {
                        int i123 = -1;
                        do {
                            int indexOf8 = str28.indexOf(array2[i41], i123 + 1);
                            i123 = indexOf8;
                            if (indexOf8 != -1) {
                                if (str28.indexOf(" " + array2[i41] + " ") > -1) {
                                    return "Không hiểu  " + array2[i41] + " ";
                                }
                                if (str28.indexOf(" " + array2[i41] + ",") > -1) {
                                    return "Không hiểu  " + array2[i41] + ",";
                                }
                                if (str28.indexOf("," + array2[i41] + " ") > -1) {
                                    return "Không hiểu ," + array2[i41] + " ";
                                }
                            }
                        } while (str28.indexOf("," + array2[i41] + ",") <= -1);
                        return "Không hiểu ," + array2[i41] + ",";
                    }
                    return "Không hiểu " + array2[i41];
                } else if (str28.trim().length() == 1) {
                    return "Không hiểu  " + str28.trim() + " ";
                } else {
                    return "Không hiểu " + str28.trim();
                }
            }
        }
        if (DaySo2.length() > 0) {
            return DaySo2;
        }
        return "Không hiểu " + str;
    }

    public static String XulyTien(String str, String theLoai) throws JSONException {// tra ve so tien vd: 100k => 100; 1,2tr => 1200
        String tien = "";
        if (str.length() - str.replaceAll("x", "").length() > 1) {// count 'x' > 1
            return "Không hiểu " + str;
        } else if (str.length() == 0) {
            return "Không hiểu ";
        } else {
            String str2 = str.replaceAll("x", "").replaceAll(" ", "").trim();
            if (str2.length() <= 0) {
                return "Không hiểu";
            }
            if (str2.endsWith("tr")) {// trieu
                String str3 = str2.replaceAll("tr", "").trim().replaceAll("\\.", "");
                String[] Mtien = str3.split(",");
                if (Mtien.length > 2) {
                    return "Không hiểu " + str3;
                } else if (Mtien.length == 1) {
                    return Mtien[0] + "000";
                } else if (Mtien.length == 2) {
                    if (Mtien[1].length() == 0) {
                        return Mtien[0] + "000";
                    } else if (Mtien[1].length() == 1) {
                        return Mtien[0] + Mtien[1] + "00";
                    } else if (Mtien[1].length() == 2) {
                        return Mtien[0] + Mtien[1] + "0";
                    } else if (Mtien[1].length() == 3) {
                        return Mtien[0] + Mtien[1];
                    } else {
                        return "Không hiểu " + str3;
                    }
                }
            } else {
                int i = 0;
                while (i < str2.length() && !isNumeric(str2.substring(i, i + 1))) {
                    i++;
                }
                while (i < str2.length() && isNumeric(str2.substring(i, i + 1))) {
                    tien = tien + str2.charAt(i);
                    i++;
                }
                if (MainActivity.jSon_Setting.getInt("canhbaodonvi") == 1 ) {
                    try {
                        int tienInt = Integer.parseInt(tien);
                        boolean checkTien = (theLoai.contains("de") && tienInt > 5000)
                                || (theLoai.contains("lo") && tienInt > 1000)
                                ||(theLoai.contains("bc") && tienInt > 2000);
                        if (checkTien && str2.replaceAll(tien, "").replaceAll(",", "").replaceAll("\\.", "").replaceAll("/", "")
                                .replaceAll(" ", "").length() <= 0)
                            return "Không hiểu " + str;
                    } catch (Exception e) {
                        return "Không hiểu " + str;
                    }
                }
                if (str2.replaceAll(tien, "").replaceAll("ng", "").replaceAll("n", "").replaceAll("d", "")
                        .replaceAll("k", "").replaceAll(",", "").replaceAll("\\.", "").replaceAll("/", "").replaceAll(" ", "").length() > 0) {
                    return "Không hiểu " + str;
                }
            }
            try {
                if (Integer.parseInt(tien) > 0) {
                    return tien;
                }
                return "Không hiểu " + str;
            } catch (Exception e) {
                return "Không hiểu " + str;
            }
        }
    }

    public static String GhepDau(String str) {// lay dau (co the là 1 day so vd: '12' '1,2')
        String[] arr = new String[15];
        String str1 = "";
        if (!isNumericComma(str)) {
            return "Không hiểu " + str;
        }
        arr[0] = "00,01,02,03,04,05,06,07,08,09,";
        arr[1] = "10,11,12,13,14,15,16,17,18,19,";
        arr[2] = "20,21,22,23,24,25,26,27,28,29,";
        arr[3] = "30,31,32,33,34,35,36,37,38,39,";
        arr[4] = "40,41,42,43,44,45,46,47,48,49,";
        arr[5] = "50,51,52,53,54,55,56,57,58,59,";
        arr[6] = "60,61,62,63,64,65,66,67,68,69,";
        arr[7] = "70,71,72,73,74,75,76,77,78,79,";
        arr[8] = "80,81,82,83,84,85,86,87,88,89,";
        arr[9] = "90,91,92,93,94,95,96,97,98,99,";
        for (int i = 0; i < str.length(); i++) {
            if (isNumeric(str.substring(i, i + 1))) {
                str1 = str1 + arr[Character.getNumericValue(str.charAt(i))];
            }
        }
        return str1;
    }

    public static String GhepDit(String str) {// lay dit (co the là 1 day so vd: '12' '1,2')
        String[] arr = new String[15];
        String str1 = "";
        if (!isNumericComma(str)) {
            return "Không hiểu " + str;
        }
        arr[0] = "00,10,20,30,40,50,60,70,80,90,";
        arr[1] = "01,11,21,31,41,51,61,71,81,91,";
        arr[2] = "02,12,22,32,42,52,62,72,82,92,";
        arr[3] = "03,13,23,33,43,53,63,73,83,93,";
        arr[4] = "04,14,24,34,44,54,64,74,84,94,";
        arr[5] = "05,15,25,35,45,55,65,75,85,95,";
        arr[6] = "06,16,26,36,46,56,66,76,86,96,";
        arr[7] = "07,17,27,37,47,57,67,77,87,97,";
        arr[8] = "08,18,28,38,48,58,68,78,88,98,";
        arr[9] = "09,19,29,39,49,59,69,79,89,99,";
        for (int i = 0; i < str.length(); i++) {
            if (isNumeric(str.substring(i, i + 1))) {
                str1 = str1 + arr[Character.getNumericValue(str.charAt(i))];
            }
        }
        return str1;
    }

    public static String GhepTong(String str) {
        String[] arr = new String[15];
        String str1 = "";
        if (!isNumericComma(str)) {
            return "Không hiểu " + str;
        }
        arr[0] = "00,19,28,37,46,55,64,73,82,91,";
        arr[1] = "01,10,29,38,47,56,65,74,83,92,";
        arr[2] = "02,11,20,39,48,57,66,75,84,93,";
        arr[3] = "03,12,21,30,49,58,67,76,85,94,";
        arr[4] = "04,13,22,31,40,59,68,77,86,95,";
        arr[5] = "05,14,23,32,41,50,69,78,87,96,";
        arr[6] = "06,15,24,33,42,51,60,79,88,97,";
        arr[7] = "07,16,25,34,43,52,61,70,89,98,";
        arr[8] = "08,17,26,35,44,53,62,71,80,99,";
        arr[9] = "09,18,27,36,45,54,63,72,81,90,";
        for (int i = 0; i < str.length(); i++) {
            if (isNumeric(str.substring(i, i + 1))) {
                str1 = str1 + arr[Character.getNumericValue(str.charAt(i))];
            }
        }
        return str1;
    }

    public static String GhepBo(String str) {
        String str1 = "";
        String[] arr = {"00,050,55", "010,060,515,565", "020,070,525,575", "030,080,535,585", "040,090,545,595", "11,66,161", "121,171,626,676", "131,181,636,686", "141,191,646,696", "22,77,272", "232,282,737,787", "242,292,747,797", "33,88,383", "343,393,848,898", "44,494,99"};
        for (int i = 0; i < str.length() - 1; i++) {
            String doi = str.substring(i, i + 2);
            if (isNumeric(doi)) {
                for (String s : arr) {
                    if (s.contains(doi)) {
                        str1 = str1 + s + ",";
                        break;
                    }
                }
            }
        }
        return FixDan(str1);
    }

    public static boolean isNumeric(String str) { //la so
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.matches("[0-9]+");
    }

    public static Boolean isNumericComma(String str) { // kiem tra có phai la day so khong ( remove ',')
        String check = str.replaceAll(",", "").trim();
        return check.length() > 0 && check.matches("[0-9]+");
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("HH:mm", Locale.US).parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public static boolean CheckTime(String time) { // da qua Time chua?
        Date gioKT = parseDate(time);
        Calendar now = Calendar.getInstance();
        int hour = now.get(11);
        int minute = now.get(12);

        //TODO: fake checktime aways false
//        return false;
        return parseDate(hour + ":" + minute) .after(gioKT);
    }

    public static boolean CheckDate(String time) {
        if (time == null) {
            time = "01/01/2018";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(time));
            c.add(5, 1);
            return new Date()
                    .before(sdf.parse(sdf.format(c.getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean CheckIsToday(Date date) { // co phai la ngay hom nay khong?
        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        int CurrentMonth = now.get(Calendar.MONTH);
        int CurrentYear = now.get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        return (day == currentDay && month == CurrentMonth && year == CurrentYear);
    }
}
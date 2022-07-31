package tamhoang.ldpro4.akaman;

import android.database.Cursor;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;
import tamhoang.ldpro4.Util;
import tamhoang.ldpro4.data.Database;

public class AkaManSec {
    public static boolean akaEnable = false;
    public static String akaMainIMEI;
    public static String akaMainURL;
    public static String encryptString;
    public static int pwdMode = 0;
    public static String resetPwd;
    public static String separator = ",";
    public static int truncateMode = 0;
    public static String truncatePwd;
    public static int useTruncate = 0;
    public static String userPwd;

    public static native String getAkaCipher();

    public static native String getAkaKeyD();

    public static native String getAkaKeyP();

    public static native String getAkaS();

    public static native String getAlgorithm();

    public static native String getCharsetName();

    static {
        System.loadLibrary("akces");
    }

    public static String readKeyFile() {
        String str;
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(getAkaMainPath()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            boolean z = false;
            while (!z) {
                try {
                    str = bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    str = null;
                }
                boolean z2 = str == null;
                if (str != null) {
                    sb.append(str);
                }
                z = z2;
            }
            try {
                bufferedReader.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            fileInputStream.close();
            return sb.toString();
        } catch (Exception e3) {
            e3.printStackTrace();
            Log.w("AkaSec", "Something went Wrong");
            return null;
        }
    }

    public static String getAkaManSec(String str) {
        try {
            byte[] decode = Base64.decode(str, 0);
            if (decode.length < 17) {
                Log.w("AkaSec", "Something went Wrong");
            }
            byte[] copyOfRange = Arrays.copyOfRange(decode, 0, 16);
            byte[] copyOfRange2 = Arrays.copyOfRange(decode, 16, decode.length);
            Cipher instance = Cipher.getInstance(getAkaCipher());
            instance.init(2, new SecretKeySpec(getAkaS().getBytes(getCharsetName()), getAlgorithm()), new IvParameterSpec(copyOfRange, 0, instance.getBlockSize()));
            return new String(instance.doFinal(copyOfRange2));
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initSecTable(Database database) {
        database.QueryData("CREATE TABLE IF NOT EXISTS tbl_active(ID INTEGER PRIMARY KEY AUTOINCREMENT, encrypt_string TEXT, user_pwd TEXT, reset_pwd TEXT, truncate_pwd TEXT, truncate_mode INTEGER DEFAULT 0, pwd_mode INTEGER DEFAULT 0, use_truncate INTEGER DEFAULT 0)");
    }

    public static void saveAkaManSec(String str, Database database) {
        userPwd = "";
        truncatePwd = "";
        pwdMode = 0;
        truncateMode = 0;
        useTruncate = 0;
        database.QueryData("DELETE FROM tbl_active");
        database.QueryData("INSERT Into tbl_active (encrypt_string, user_pwd, reset_pwd, truncate_pwd, truncate_mode, pwd_mode, use_truncate) Values ('" + str + "', '" + userPwd + "', '" + resetPwd + "', '" + truncatePwd + "', " + truncateMode + ", " + pwdMode + ", " + useTruncate + ")");
    }

    public static void updateAkaManSec(Database database) {
        database.QueryData("DELETE FROM tbl_active");
        database.QueryData("INSERT Into tbl_active (encrypt_string, user_pwd, reset_pwd, truncate_pwd, truncate_mode, pwd_mode, use_truncate) Values ('" + encryptString + "', '" + userPwd + "', '" + resetPwd + "', '" + truncatePwd + "', " + truncateMode + ", " + pwdMode + ", " + useTruncate + ")");
    }

    public static void queryAkaManPwd(Database database) {
        Cursor GetData = database.GetData("Select user_pwd, reset_pwd, truncate_pwd, pwd_mode, truncate_mode, encrypt_string, use_truncate From tbl_active LIMIT 1;");
        try {
            if (GetData.moveToNext()) {
                userPwd = GetData.getString(0);
                resetPwd = GetData.getString(1);
                truncatePwd = GetData.getString(2);
                pwdMode = GetData.getInt(3);
                truncateMode = GetData.getInt(4);
                encryptString = GetData.getString(5);
                useTruncate = GetData.getInt(6);
            }
            if (GetData == null || GetData.isClosed()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Util.writeLog(e);
            if (GetData == null || GetData.isClosed()) {
                return;
            }
        } catch (Throwable th) {
            if (GetData != null && !GetData.isClosed()) {
                GetData.close();
            }
            throw th;
        }
        GetData.close();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.isClosed() == false) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0019, code lost:
        if (r2.isClosed() == false) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        r2.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String queryAkaManSec(tamhoang.ldpro4.data.Database r2) {
        /*
            java.lang.String r0 = ""
            java.lang.String r1 = "Select encrypt_string From tbl_active LIMIT 1;"
            android.database.Cursor r2 = r2.GetData(r1)
            boolean r1 = r2.moveToNext()     // Catch:{ Exception -> 0x0021 }
            if (r1 == 0) goto L_0x0013
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch:{ Exception -> 0x0021 }
        L_0x0013:
            if (r2 == 0) goto L_0x0031
            boolean r1 = r2.isClosed()
            if (r1 != 0) goto L_0x0031
        L_0x001b:
            r2.close()
            goto L_0x0031
        L_0x001f:
            r0 = move-exception
            goto L_0x0032
        L_0x0021:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x001f }
            tamhoang.ldpro4.Util.writeLog(r1)     // Catch:{ all -> 0x001f }
            if (r2 == 0) goto L_0x0031
            boolean r1 = r2.isClosed()
            if (r1 != 0) goto L_0x0031
            goto L_0x001b
        L_0x0031:
            return r0
        L_0x0032:
            if (r2 == 0) goto L_0x003d
            boolean r1 = r2.isClosed()
            if (r1 != 0) goto L_0x003d
            r2.close()
        L_0x003d:
            goto L_0x003f
        L_0x003e:
            throw r0
        L_0x003f:
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: tamhoang.ldpro4.akaman.AkaManSec.queryAkaManSec(tamhoang.ldpro4.data.Database):java.lang.String");
    }

    public static String md5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAkaMainPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "keys.txt";
    }
}

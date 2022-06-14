package tamhoang.ldpro4;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Util {
    static final int BUFFER_SIZE = 2048;
    public static String DIRECTORY_PATH = (Environment.getExternalStorageDirectory() + File.separator + "ldpro_logs.txt");
    public static boolean ON = false;
    static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static void writeLog(Exception exc) {
        if (ON) {
            checkFileSize();
            String format = formatter.format(new Date());
            try {
                String stackTraceString = Log.getStackTraceString(exc);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(DIRECTORY_PATH), true), StandardCharsets.UTF_8));
                bufferedWriter.append(format).append(":\n");
                bufferedWriter.append(stackTraceString).append("\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeLogInfo(String str) {
        if (ON) {
            checkFileSize();
            String format = formatter.format(new Date());
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(DIRECTORY_PATH), true), StandardCharsets.UTF_8));
                bufferedWriter.append(format).append(":").append(str).append("\n");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkFileSize() {
        try {
            File file = new File(DIRECTORY_PATH);
            if ((file.length() / 1024) / 1024 > 5) {
                String format = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                file.renameTo(new File(Environment.getExternalStorageDirectory() + File.separator + "ldpro_logs_" + format + ".txt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File r9, File r10) throws IOException {
        /*
            java.io.File r0 = r10.getParentFile()
            boolean r0 = r0.exists()
            if (r0 != 0) goto L_0x0011
            java.io.File r0 = r10.getParentFile()
            r0.mkdirs()
        L_0x0011:
            boolean r0 = r10.exists()
            if (r0 != 0) goto L_0x001a
            r10.createNewFile()
        L_0x001a:
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0048 }
            r1.<init>(r9)     // Catch:{ all -> 0x0048 }
            java.nio.channels.FileChannel r9 = r1.getChannel()     // Catch:{ all -> 0x0048 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x0043 }
            r1.<init>(r10)     // Catch:{ all -> 0x0043 }
            java.nio.channels.FileChannel r0 = r1.getChannel()     // Catch:{ all -> 0x0043 }
            r4 = 0
            long r6 = r9.size()     // Catch:{ all -> 0x0043 }
            r2 = r0
            r3 = r9
            r2.transferFrom(r3, r4, r6)     // Catch:{ all -> 0x0043 }
            if (r9 == 0) goto L_0x003d
            r9.close()
        L_0x003d:
            if (r0 == 0) goto L_0x0042
            r0.close()
        L_0x0042:
            return
        L_0x0043:
            r10 = move-exception
            r8 = r0
            r0 = r9
            r9 = r8
            goto L_0x004a
        L_0x0048:
            r10 = move-exception
            r9 = r0
        L_0x004a:
            if (r0 == 0) goto L_0x004f
            r0.close()
        L_0x004f:
            if (r9 == 0) goto L_0x0054
            r9.close()
        L_0x0054:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: tamhoang.ldpro4.Util.copyFile(java.io.File, java.io.File):void");
    }

//    public static void zip(String[] strArr, String str) throws IOException {
//        BufferedInputStream bufferedInputStream;
//        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(str)));
//        try {
//            byte[] bArr = new byte[2048];
//            for (int i = 0; i < strArr.length; i++) {
//                bufferedInputStream = new BufferedInputStream(new FileInputStream(strArr[i]), 2048);
//                zipOutputStream.putNextEntry(new ZipEntry(strArr[i].substring(strArr[i].lastIndexOf(InternalZipConstants.ZIP_FILE_SEPARATOR) + 1)));
//                while (true) {
//                    int read = bufferedInputStream.read(bArr, 0, 2048);
//                    if (read == -1) {
//                        break;
//                    }
//                    zipOutputStream.write(bArr, 0, read);
//                }
//                bufferedInputStream.close();
//            }
//            zipOutputStream.close();
//        } catch (Throwable th) {
//            zipOutputStream.close();
//            throw th;
//        }
//    }

    public static void unzip(String str, String str2) throws IOException {
        FileOutputStream fileOutputStream;
        try {
            File file = new File(str2);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
            while (true) {
                try {
                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                    if (nextEntry != null) {
                        String str3 = str2 + nextEntry.getName();
                        if (nextEntry.isDirectory()) {
                            File file2 = new File(str3);
                            if (!file2.isDirectory()) {
                                file2.mkdirs();
                            }
                        } else {
                            fileOutputStream = new FileOutputStream(str3, false);
                            while (true) {
                                int read = zipInputStream.read();
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(read);
                            }
                            zipInputStream.closeEntry();
                            fileOutputStream.close();
                        }
                    } else {
                        zipInputStream.close();
                        return;
                    }
                } catch (Throwable th) {
                    zipInputStream.close();
                    throw th;
                }
            }
        } catch (Exception unused) {
        }
    }
}

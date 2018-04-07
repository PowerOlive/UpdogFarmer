package com.steevsapps.idledaddy.utils;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Utils {

    /**
     * Convert byte array to hex string
     * https://stackoverflow.com/a/9855338
     */
    public static String bytesToHex(@Nullable byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Convert ArrayList to comma-separated String
     */
    public static String arrayToString(List<String> list) {
        final StringBuilder builder = new StringBuilder();
        for (int i=0,size=list.size();i<size;i++) {
            final String string = list.get(i);
            builder.append(string);
            if (i + 1 < size) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    /**
     * Convert array to comma-separated String
     */
    public static String arrayToString(String[] array) {
        return arrayToString(Arrays.asList(array));
    }

    /**
     * Save Logcat to file
     */
    public static void saveLogcat(File file) throws IOException {
        final Process p = Runtime.getRuntime().exec("logcat -d");
        try ( final BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
              final BufferedWriter bw = new BufferedWriter(new FileWriter(file)) ) {
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Strips non-ASCII characters from String
     */
    public static String removeSpecialChars(String s) {
        return s.replaceAll("[^\\u0000-\\u007F]", "");
    }

    /**
     * Check if API key is valid
     */
    public static boolean isValidKey(String key) {
        return key.matches("^[0-9A-Fa-f]+$");
    }

    /**
     * Calculate the SHA-1 hash of a file
     */
    public static byte[] calculateSHA1(File file) throws IOException, NoSuchAlgorithmException {
        try (final InputStream fis = new FileInputStream(file)) {
            final MessageDigest md = MessageDigest.getInstance("SHA-1");
            final byte[] buffer = new byte[8192];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                md.update(buffer, 0, n);
            }
            return md.digest();
        }
    }

}

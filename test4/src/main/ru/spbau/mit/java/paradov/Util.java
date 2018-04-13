package ru.spbau.mit.java.paradov;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String getHashOfFile(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            return getHashFromInputStream(fis);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            // Impossible!
            throw new RuntimeException("MD5 doesn't exist");
        }
    }

    public static String getHashOfString(String s) {
        try (InputStream is = new ByteArrayInputStream(s.getBytes())) {
            return getHashFromInputStream(is);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            // Impossible!
            throw new RuntimeException("MD5 doesn't exist");
        }
    }

    private static String getHashFromInputStream(InputStream is)
            throws NoSuchAlgorithmException, IOException {
        DigestInputStream dis = new DigestInputStream(is, MessageDigest.getInstance("MD5"));

        byte[] byteArray = new byte[1024];
        while (true) {
            // IDEA recommended me to write it this way.
            if (dis.read(byteArray, 0, 1024) == -1) break;
        }

        MessageDigest messageDigest = dis.getMessageDigest();
        byte[] result = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(Integer.toString(b & 0xFF, 16));
        }

        return sb.toString();
    }
}

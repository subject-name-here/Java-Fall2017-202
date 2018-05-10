package ru.spbau.mit.java.paradov;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility functions for hashers.
 */
public class Util {
    /**
     * Counts MD5 hash of a single file.
     * @param file file, hash of which we count
     * @return MD5 hash of file
     */
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

    /**
     * Counts MD5 hash of a string.
     * @param s string, hash of which we count
     * @return MD5 hash of string
     */
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

    /**
     * Gets MD5 hash of symbols from InputStream.
     * @param is input stream of symbols
     * @return hash of sequence of symbols from InputStream
     * @throws NoSuchAlgorithmException if MD5 doesn't exists
     * @throws IOException if InputStream has blocked reading
     */
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

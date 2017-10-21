package ru.spbau.mit.java.paradov;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Contains public method that can find zip files, look inside them and extract
 * those files that matching the regular expression. Also contains private methods
 * to make main method less overloaded.
 */
public class ZipOperation {
    /** Constant of buffer size for extracting file from archive. */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Finds zip files in given directory, find all files that are matching pattern
     * and extracts them.
     * @param path directory where we looking for zip files
     * @param regex regular expression to match files we need
     * @param outputPath place where we put files we found
     * @throws IOException if given paths are not a directory,
     * or exception was thrown from findZipFiles() or unpackMatchingFiles
     */
    public static void findAndUnzipMatchingFiles(String path, String regex, String outputPath)
            throws IOException {
        checkPaths(path, outputPath);

        Pattern p = Pattern.compile(regex);

        Vector<File> zipFiles = findZipFiles(path);
        for (File f : zipFiles) {
            unpackMatchingFiles(f, p, outputPath);
        }
    }

    /**
     * Checks if given paths are valid paths to directories.
     * @param inPath path from where we are supposed to read
     * @param outPath path to where we are supposed to write
     * @throws IOException if one of paths is not a directory
     */
    private static void checkPaths(String inPath, String outPath) throws IOException{
        File dirToFind = new File(inPath);
        if (!dirToFind.isDirectory()) {
            throw new NotDirectoryException("Given path is not a directory.");
        }

        File dirToUnpack = new File(outPath);
        if (!dirToUnpack.isDirectory()) {
            throw new NotDirectoryException("Given output path is not a directory.");
        }
    }

    /**
     * Finds all zip files in given directory.
     * @param path directory where we are looking for zip files
     * @return vector of zip files
     * @throws IOException if we can't create FileInputStream or ZipInputStream
     */
    private static Vector<File> findZipFiles(String path)
            throws IOException {
        File[] filesList = new File(path).listFiles();
        Vector<File> zipFiles = new Vector<>();

        for (File f : filesList) {
            if (!f.isDirectory()) {
                try (FileInputStream fis = new FileInputStream(f)) {
                    if (new ZipInputStream(fis).getNextEntry() != null) {
                        zipFiles.addElement(f);
                    }
                }
            }
        }

        return zipFiles;
    }

    /**
     * Walks in zip and extracts all files matching the pattern.
     * @param zipFile archive in which we are looking for files
     * @param p pattern files should match
     * @param path place to extract files
     * @throws IOException if we extract file that already exists,
     * if we can't create FileInputStream or ZipInputStream
     * or if exception was thrown from extractZipFile()
     */
    private static void unpackMatchingFiles(File zipFile, Pattern p, String path)
            throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                Matcher m = p.matcher(entry.getName());
                if (m.matches()) {
                    File newFile = new File(path + File.separator
                            + Paths.get(entry.getName()).getFileName().toString());

                    if (!entry.getName().endsWith(File.separator)){
                        if (!newFile.createNewFile()) {
                            throw new FileAlreadyExistsException("File collision while extracting files from zip.");
                        }
                        extractFileFromZip(newFile, zis);
                    }
                }
                entry = zis.getNextEntry();
            }

        }
    }

    /**
     * Writes all data from file in archive to file from outside.
     * @param newFile file to where we write data from archive
     * @param zis stream of zip archive from where we read file
     * @throws IOException if can't create FileOutputStream or can't write in it
     */
    private static void extractFileFromZip(File newFile, ZipInputStream zis)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            byte[] buff = new byte[BUFFER_SIZE];
            int len = zis.read(buff);
            while (len != -1) {
                fos.write(buff, 0, len);
                len = zis.read();
            }
        }
    }
}

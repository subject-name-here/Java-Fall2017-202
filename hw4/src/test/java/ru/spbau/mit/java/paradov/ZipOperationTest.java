package ru.spbau.mit.java.paradov;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;

import static org.junit.Assert.*;

/** Class to test method findAndUnzipMatchingFiles() from ZipOperation class. */
public class ZipOperationTest {
    /** File separator. Used to write less symbols. */
    private static final String SEP = File.separator;

    /** Path to place where all test files are. */
    private static final String resourcesPath =
            "src" + SEP + "test" + SEP + "resources" + SEP + "testFolder" + SEP;

    /**
     * Recursively deletes all files and directories from given directory,
     * and then deletes the directory itself.
     * Helps to clean temporary files after tests.
     * @param file directory or file to delete
     */
    private void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteRecursive(f);
            }
        }

        file.delete();
    }

    /**
     * Checks if the method extracts matching file from archive.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodFindsAndExtractsFile() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test1",
                ".*", outputDir.getPath());

        File f = new File(resourcesPath + "temp" + SEP + "file");
        assertEquals(true, f.exists());

        deleteRecursive(outputDir);
    }

    /**
     * Checks if the method extracts matching file from archive and doesn't
     * lose its content.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodFindsAndExtractsFileCorrectly() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test1",
                ".*", outputDir.getPath());

        File f = new File(resourcesPath + "temp" + SEP + "file");
        FileInputStream fis = new FileInputStream(f);
        byte[] buff = new byte[23];
        fis.read(buff);
        String result = new String(buff);
        assertEquals("something written here\n", result);

        deleteRecursive(outputDir);
    }

    /**
     * Checks if the method doesn't extracts not matching file from archive.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodDoesNotExtractNotMatchingFile() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test1",
                "file", outputDir.getPath());

        File f = new File(resourcesPath + "temp" + SEP + "fail");
        assertEquals(false, f.exists());

        deleteRecursive(outputDir);
    }

    /**
     * Checks if the method doesn't look for zips inside the directories in given one
     * and doesn't extract them.
     * @throws Exception if can't create temporary directory or delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodDoesNotLookInsideDirectories() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test3",
                "file", outputDir.getPath());

        File f = new File(resourcesPath + "test3");
        assertEquals(2, f.listFiles().length);

        deleteRecursive(outputDir);
    }

    /**
     * Checks if the method extracts matching file from archive's inner directories.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodExtractsFileFromInnerDirectories() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test4",
                ".*", outputDir.getPath());

        File f = new File(resourcesPath + "temp" + SEP + "file");
        assertEquals(true, f.exists());

        deleteRecursive(outputDir);
    }

    /**
     * Checks if the method extracts matching file from archive's inner directories
     * and doesn't change its content.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodExtractsFileFromInnerDirectoriesCorrectly() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test5",
                ".*", outputDir.getPath());

        File f = new File(resourcesPath + "temp" + SEP + "file");
        FileInputStream fis = new FileInputStream(f);
        byte[] buff = new byte[28];
        fis.read(buff);
        String result = new String(buff);
        assertEquals("this file must be extracted\n", result);

        deleteRecursive(outputDir);
    }

    /**
     * Tests if the method throws exception when trying to extract file,
     * and it's already in output directory.
     * @throws Exception if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test(expected = FileAlreadyExistsException.class)
    public void testMethodThrowsExceptionWhenFileCollisionOccures() throws Exception {
        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test6",
                ".*", resourcesPath + "test6");
    }

    /**
     * Tests if the method works correctly when extracting many files from many archives.
     * @throws Exception if can't create temporary directory or can't delete it
     * or if exception was thrown from findAndUnzipMatchingFiles()
     */
    @Test
    public void testMethodWorksCorrectlyWithManyFilesAndArchives() throws Exception {
        File outputDir = new File(resourcesPath + "temp");
        outputDir.mkdir();

        ZipOperation.findAndUnzipMatchingFiles(resourcesPath + "test7",
                ".*file.*", outputDir.getPath());

        byte[] buff = new byte[16];
        String[] resList = new String[3];
        int cnt = 0;

        for (File f : outputDir.listFiles()){
            FileInputStream fis = new FileInputStream(f);
            fis.read(buff);
            resList[cnt++] = new String(buff);
        }
        Arrays.sort(resList);

        StringBuilder result = new StringBuilder();
        for (String s : resList) {
            result.append(s);
        }

        assertEquals("one  point  one\nthree point one\ntwo  point  one\n",
                result.toString());

        deleteRecursive(outputDir);
    }

}
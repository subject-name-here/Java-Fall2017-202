package sp;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.PI;
import static org.junit.Assert.*;

public class SecondPartTasksTest {
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void testFindQuotes() throws IOException {
        File testFile1 = testFolder.newFile("file1");
        File testFile2 = testFolder.newFile("file2");
        File testFile3 = testFolder.newFile("file3");

        List<String> lines1 = Arrays.asList("cat\n", "dog\n", "cat & dog\n", "shark\n");
        List<String> lines2 = Arrays.asList("kitten\n", "puppy\n", "cat & kitten\n", "whale-cat\n");
        List<String> lines3 = Arrays.asList("a cat in house\n", "lonely\n", "lonely cat\n");

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile1))) {
            for (String line : lines1)
                bufferedWriter.write(line);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile2))) {
            for (String line : lines2)
                bufferedWriter.write(line);
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile3))) {
            for (String line : lines3)
                bufferedWriter.write(line);
        }

        List<String> goodLines = Arrays.asList("cat", "cat & dog", "cat & kitten", "whale-cat", "a cat in house", "lonely cat");

        assertEquals(goodLines, SecondPartTasks.findQuotes(Arrays.asList(testFile1.getAbsolutePath(),
                testFile2.getAbsolutePath(),
                testFile3.getAbsolutePath()), "kitten"));

    }

    /** Tests if results of piDividedBy4() are not far from expected. */
    @Test
    public void testPiDividedBy4() {
        for (int i = 0; i < 5; i++) {
            assertEquals(PI / 4, SecondPartTasks.piDividedBy4(), 1e-3);
        }

    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> compositions = new TreeMap<>();
        compositions.put("Khrabrov", Arrays.asList("Introduction", "Sequences of real numbers",
                "Limit and continuity of functions"));
        compositions.put("Afanasieva", Arrays.asList("Groups", "Rings", "unclear"));
        compositions.put("Linskii", Arrays.asList("const", "static",
                "unique ptr", "Shared pointer"));
        assertEquals("Khrabrov", SecondPartTasks.findPrinter(compositions));

        compositions.put("Omelchenko", Arrays.asList("Recurrent equalities", "Something about sets",
                "Graphs", "Many graphs", "Much more graphs", "Graphs and graphs again"));
        compositions.put("My brain", Arrays.asList("a", "b", "I", "u", "d", "f", "c", "alpha", "omega"));
        assertEquals("Omelchenko", SecondPartTasks.findPrinter(compositions));
    }

    @Test
    public void testCalculateGlobalOrder() {
        assertEquals(
                ImmutableMap.of(
                        "potato", 10,
                        "tomato", 15,
                        "bananas", 42),
                SecondPartTasks.calculateGlobalOrder(Arrays.asList(
                        ImmutableMap.of(
                        "potato", 2,
                        "tomato", 3,
                        "bananas", 5
                        ), ImmutableMap.of(
                                "potato", 8,
                                "tomato", 10,
                                "bananas", 30
                        ), ImmutableMap.of(
                                "tomato", 2,
                                "bananas", 7
                        )

                )));

    }
}
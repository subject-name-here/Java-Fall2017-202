package ru.spbau.mit.java.paradov;

import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.*;
import java.util.function.Function;

import static org.junit.Assert.*;

/** Class that tests methods of Maybe class. */
public class MaybeTest {

    /** Tests if Maybe.just creates not empty Maybe. */
    @Test
    public void testJustCreatesNotEmptyMaybe() {
        Maybe<Integer> mi = Maybe.just(42);

        assertEquals(true, mi.isPresent());
    }

    /**
     * Tests if Maybe.just correctly saves the given object.
     * @throws GetEmptyMaybeException if Maybe.get was called on empty Maybe.
     */
    @Test
    public void testJustSavesObject() throws GetEmptyMaybeException {
        Maybe<Integer> mi = Maybe.just(42);

        assertEquals(42, (int)mi.get());
    }

    /** Tests if Maybe.nothing creates empty Maybe. */
    @Test
    public void testNothingCreatesEmptyMaybe() {
        Maybe<Integer> mi = Maybe.nothing();

        assertEquals(false, mi.isPresent());
    }

    /**
     * Tests if Maybe.get() on empty Maybe throws GetEmptyMaybeException.
     * @throws GetEmptyMaybeException if Maybe.get was called on empty Maybe.
     */
    @Test(expected = GetEmptyMaybeException.class)
    public void testGetOnEmptyMaybeThrowsException()
            throws GetEmptyMaybeException {
        Maybe<Integer> mi = Maybe.nothing();
        mi.get();
    }

    /** Tests if Maybe.map() on empty Maybe returns empty Maybe. */
    @Test()
    public void testMapCreatesEmptyMaybe() {
        Maybe<Integer> mi = Maybe.nothing();
        Maybe<Integer> mi2 = mi.map(Function.identity());

        assertEquals(false, mi2.isPresent());
    }

    /** Tests if Maybe.map() on not empty Maybe returns not empty Maybe. */
    @Test()
    public void testMapCreatesNotEmptyMaybe() {
        Maybe<Integer> mi = Maybe.just(42);
        Maybe<Integer> mi2 = mi.map(Function.identity());

        assertEquals(true, mi2.isPresent());
    }

    /**
     * Tests if Maybe.map() creates new maybe with correct value.
     * @throws GetEmptyMaybeException if Maybe.get() was called on empty Maybe.
     */
    @Test()
    public void testMapCreatesMaybeWithRightValue() throws GetEmptyMaybeException {
        Function<Integer, String> FortyTwoToString = n -> {
            if (n == 42) {
                return "Forty-Two";
            }
            return "Not Forty-Two";
        };

        Maybe<Integer> mi = Maybe.just(42);
        Maybe<String> mi2 = mi.map(FortyTwoToString);

        assertEquals("Forty-Two", mi2.get());
    }

    /** File separator. Used to write less symbols. */
    private final static String FILE_SEP = File.separator;

    /** Path to file where data for test to read are. */
    private final static String PATH_TO_INPUT_FILE = "src" + FILE_SEP + "test"
            + FILE_SEP + "resources" + FILE_SEP + "file.in";

    /** Path to file where we will write data. */
    private final static String PATH_TO_OUTPUT_FILE = "src" + FILE_SEP + "test"
            + FILE_SEP + "resources" + FILE_SEP + "file.out";


    /**
     * Function that reads line from BufferedReader, checks if it's integer.
     * If it is, returns Maybe with this integer, else returns empty Maybe.
     * If can't read another line, it returns null.
     * @param br reader, from which we get line
     * @return if line couldn't be read, returns null; if line that was read
     * converted to integer, returns Maybe with this integer, otherwise returns
     * empty Maybe.
     * @throws IOException if readLine() threw an exception
     */
    @Nullable
    private Maybe<Integer> readLineAndCastToMaybe(BufferedReader br)
            throws IOException {
        String line = br.readLine();
        if (line == null) {
            return null;
        }

        Maybe<Integer> result;
        try {
            result = Maybe.just(Integer.parseInt(line));
        } catch (NumberFormatException e){
            result = Maybe.nothing();
        }

        return result;
    }

    /**
     * Writes Maybe to file: if it was empty, writes "null", else writes its content.
     * @param m Maybe that we want to write
     * @param bw writer that we use to write
     * @throws IOException if write() or newLine() threw an exception
     * @throws GetEmptyMaybeException never (if it works correctly), because before
     * calling method get(), we check, if Maybe is not empty, which means it will not
     * throw an exception
     */
    private void CastMaybeToLine(Maybe<Integer> m, BufferedWriter bw)
            throws IOException, GetEmptyMaybeException {
        if (m.isPresent()) {
            String s = m.get().toString();
            bw.write(s, 0, s.length());
            bw.newLine();
        } else {
            bw.write("null\n", 0, 5);
        }
    }

    /**
     * Reads lines from file, if there is an integer on a line, it will square it
     * and write it to output file; otherwise it will write "null".
     * @throws Exception if we can't create BufferedReader or BufferedWriter,
     * or we can't cast line to Maybe, or we can't write Maybe to line
     */
    @Test
    public void testFromTask() throws Exception {
        File in = new File(PATH_TO_INPUT_FILE);
        File out = new File(PATH_TO_OUTPUT_FILE);
        try (BufferedReader br = new BufferedReader(new FileReader(in));
                BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
            Function<Integer, Integer> sqr = integer -> integer * integer;

            Maybe<Integer> m, newM;
            while ((m = readLineAndCastToMaybe(br)) != null){
                newM = m.map(sqr);
                CastMaybeToLine(newM, bw);
            }

        }
    }

}
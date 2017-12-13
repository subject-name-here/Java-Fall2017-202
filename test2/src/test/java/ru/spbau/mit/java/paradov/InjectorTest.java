package ru.spbau.mit.java.paradov;

import org.junit.Test;
import ru.spbau.mit.java.paradov.test.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

/** Class that tests Injector.initialize() works correctly. */
public class InjectorTest {

    /** Tests if initializer works on classes with constructor without parameters. */
    @Test
    public void testInitializeSimple() throws Exception, Throwable {
        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyJunkClass",
                new ArrayList<>());

        assertEquals(9, (int) ((MyJunkClass) actual).getA());
    }

    /** Tests if it works with classes with constructor parameter. */
    @Test
    public void testInitializeWithParameter() throws Exception, Throwable {
        ArrayList<Class<?>> parametersList = new ArrayList<>();
        parametersList.add(MyJunkClass.class);

        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyJunkyard",
                parametersList);

        assertEquals(11, (int) ((MyJunkyard) actual).getJunks());
    }

    /** Tests if it works on many dependencies. */
    @Test
    public void testInitializeWithParameters() throws Exception, Throwable {
        ArrayList<Class<?>> parametersList = new ArrayList<>();
        parametersList.add(MyJunkyard.class);
        parametersList.add(MyJunkClass.class);
        parametersList.add(MyHouse.class);

        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyYard",
                parametersList);

        assertEquals(12, (int) ((MyYard) actual).getThings());
    }

    /** Tests if ImplementationNotFoundException is thrown. */
    @Test(expected = ImplementationNotFoundException.class)
    public void testInitializeThrowsImplementationNotFoundException() throws Exception, Throwable {
        ArrayList<Class<?>> parametersList = new ArrayList<>();
        parametersList.add(MyJunkClass.class);
        parametersList.add(MyJunkyard.class);

        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyYard",
                parametersList);
    }

    /** Tests if it works with implements. */
    @Test
    public void testInitializeWorksWithImplements() throws Exception, Throwable {
        ArrayList<Class<?>> parametersList = new ArrayList<>();
        parametersList.add(MyRoads.class);
        parametersList.add(MyNowhere.class);

        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyCountry",
                parametersList);

        assertEquals(1, (int) ((MyCountry) actual).getPeople());
    }


    /** Tests something difficult, with implements and extends. */
    @Test
    public void testInitializeBig() throws Exception, Throwable {
        ArrayList<Class<?>> parametersList = new ArrayList<>();
        parametersList.add(MyRoads.class);
        parametersList.add(MyNothingExtension.class);
        parametersList.add(MyCountry.class);
        parametersList.add(MyHouse.class);
        parametersList.add(MyNowhere.class);
        parametersList.add(MyJunkClass.class);
        parametersList.add(MyJunkyard.class);
        parametersList.add(MyYard.class);

        Object actual = Injector.initialize("ru.spbau.mit.java.paradov.test.MyEverything",
                parametersList);

        assertEquals(42, (int) ((MyEverything) actual).getWhat());
    }
}
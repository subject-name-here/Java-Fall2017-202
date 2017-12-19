package ru.spbau.mit.java.paradov;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class Injector {
    private static Map<Class<?>, Integer> classMap;
    private static Map<Class<?>, Class<?>> implementationMap;

    /**
     * Creates object of given classname, using given classes as parameters.
     * It seems to work with extends and implements. Not correctly, but sometimes it works.
     * It also cannot track if we created object of one class more than once.
     * @param rootClassName name of class we want to create
     * @param dependencies classes that we are allowed to use
     * @return object of given class
     * @throws Exception if something goes wrong TODO: make exceptions as it's said in task
     */
    public static Object initialize(String rootClassName, Collection<Class<?>> dependencies)
            throws Exception, ImplementationNotFoundException {
        try {
            Class<?> rootClass = Class.forName(rootClassName);
            classMap = new HashMap<>();
            implementationMap = new HashMap<>();

            dependencies = dependencies.stream()
                    .filter(c -> !c.equals(rootClass))
                    .distinct()
                    .collect(Collectors.toList());

            classMap.put(rootClass, 0);
            int counter = 1;
            for (Class<?> c : dependencies) {
                classMap.put(c, counter++);
            }

            for (Class<?> c : dependencies) {
                findImplements(c, c);
            }

            return createClass(rootClass);
        } catch (Exception e) {
            throw new Exception("something");
        }
    }

    private static Object createClass(Class<?> c) throws ImplementationNotFoundException, Exception {
        Constructor<?> constructor = c.getConstructors()[0];
        Class<?>[] constructorParametersTypes = constructor.getParameterTypes();
        ArrayList<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : constructorParametersTypes) {
            if (classMap.containsKey(parameterType)) {
                parameters.add(createClass(parameterType));
            } else if (implementationMap.containsKey(parameterType)) {
                parameters.add(createClass(implementationMap.get(parameterType)));
            } else {
                throw new ImplementationNotFoundException();
            }
        }

        return constructor.newInstance(parameters.toArray());
    }

    private static void findImplements(Class<?> c, Class<?> implementor) {
        Class<?> father = c.getSuperclass();
        if (father != null && !implementationMap.containsKey(father) && !classMap.containsKey(father)) {
            implementationMap.put(father, implementor);
            findImplements(father, implementor);
        }

        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> i : interfaces) {
            if (i != null && !implementationMap.containsKey(i)) {
                implementationMap.put(i, implementor);
                findImplements(i, implementor);
            }
        }

    }
}

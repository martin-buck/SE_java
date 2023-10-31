import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import javax.management.RuntimeErrorException;

public class Unit {
    public static Map<String, Throwable> testClass(String name) {
        // Map will store Strings representing methods of the given class and the return results of @Tests
        Map<String, Throwable> errors = new HashMap<String, Throwable>();
        Class<?> cl;
        Constructor<?> cons;
        Object o;
        Method m;

        // Instantiate a new instance of the given Class
        try {
            cl = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found.");
        }
        try {
            cons = cl.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No zero-argument constructor for this class.");
        }
        try {
            o = cons.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize object for this class");
        }

        // iterate through the methods of this class then separate and store those that have different annotations. Throw exception if multiple annotations
        Method[] methods = cl.getMethods();
        ArrayList<String> before_class_str = new ArrayList<String>();
        ArrayList<String> after_class_str = new ArrayList<String>();
        ArrayList<String> before_str = new ArrayList<String>();
        ArrayList<String> after_str = new ArrayList<String>();
        ArrayList<String> tests_str = new ArrayList<String>();
        
        for (Method m1 : methods){
            Annotation[] annots = m1.getAnnotations();

            // each method is only allowed one annotation
            if (annots.length != 1){
                throw new RuntimeException("A method must have exactly one annotation.");
            }
            
            // first gather into appropriate lists
            Annotation annot = annots[0];
            if (annot.annotationType().equals(BeforeClass.class)){
                before_class_str.add(m1.getName());
            }else if (annot.annotationType().equals(Before.class)){
                before_str.add(m1.getName());
            }else if (annot.annotationType().equals(Test.class)){
                tests_str.add(m1.getName());
            }else if (annot.annotationType().equals(After.class)){
                after_str.add(m1.getName());
            }else if (annot.annotationType().equals(AfterClass.class)){
                after_class_str.add(m1.getName());
            }else{
                throw new RuntimeException("Annotation is of unknown type.");
            }
        }

        Collections.sort(before_class_str);
        Collections.sort(before_str);
        Collections.sort(tests_str);
        Collections.sort(after_str);
        Collections.sort(after_class_str);

        // execute @BeforeClass methods in alphabetical order. Throw an exception if method is not static
        for (String s: before_class_str){
            try {
                m = cl.getMethod(s);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error getting methods from this class.");
            }
            
            if (Modifier.isStatic(m.getModifiers())){
                throw new RuntimeException("@BeforeClass can only annotate a static method.");
            }

            try {
                m.invoke(o);
            } catch (Exception e) {
                throw new RuntimeException("Illegal access to a method from this class.");
            }
        }

        // execute the @Before, @Test, @After methods in alphabetical order (@Before, @After should not be run if no other methods)
        if (tests_str.size() > 0){
            for (String s: before_str){
                try {
                    m = cl.getMethod(s);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Error getting methods from this class.");
                }

                try {
                    m.invoke(o);
                } catch (Exception e) {
                    throw new RuntimeException("Illegal access to a method from this class.");
                }
            }
            
            for (String s: tests_str){
                try {
                    m = cl.getMethod(s);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Error getting methods from this class.");
                }

                // do we need to specify exceptions here? It could be that invoke throws an exception or the test fails and m throws an exception
                try {
                    m.invoke(o);
                    errors.put(s, null);
                } catch (Exception e) {
                    errors.put(s, e);
                }
            }

            for (String s: after_str){
                try {
                    m = cl.getMethod(s);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Error getting methods from this class.");
                }

                try {
                    m.invoke(o);
                } catch (Exception e) {
                    throw new RuntimeException("Illegal access to a method from this class.");
                }
            }
        }

        // execute @AfterClass methods in alphabetical order. Throw an exception if method is not static
        for (String s: after_class_str){
            try {
                m = cl.getMethod(s);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error getting methods from this class.");
            }
            if (Modifier.isStatic(m.getModifiers())){
                throw new RuntimeException("@AfterClass can only annotate a static method.");
            }
            try {
                m.invoke(o);
            } catch (Exception e) {
                throw new RuntimeException("Illegal access to a method from this class.");
            }
        }
        return errors;
    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }
}
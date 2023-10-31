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

        // Instantiate a new instance of the given Class. NEED TO CHECK FOR EXCEPTIONS THROWN
        Class<?> cl = Class.forName(name);
        Constructor<?> cons = cl.getConstructor();
        Object o = cons.newInstance();

        // iterate through the methods of this class then separate and store those that have different annotations. Throw exception if multiple annotations
        Method[] methods = cl.getMethods();
        ArrayList<Method> before_class = new ArrayList<Method>(); ArrayList<Method> before_class_str = new ArrayList<Method>();
        ArrayList<Method> after_class = new ArrayList<Method>(); ArrayList<Method> after_class_str = new ArrayList<Method>();
        ArrayList<Method> before = new ArrayList<Method>(); ArrayList<Method> before_str = new ArrayList<Method>();
        ArrayList<Method> after = new ArrayList<Method>(); ArrayList<Method> after_str = new ArrayList<Method>();
        ArrayList<Method> tests = new ArrayList<Method>(); ArrayList<Method> tests_str = new ArrayList<Method>();
        
        for (Method m : methods){
            Annonation [] annots = m.getAnnotations();

            // each method is only allowed one annotation
            if (annots.length != 1){
                throw new RuntimeException("A method must have exactly one annotation.");
            }
            
            // first gather into appropriate lists
            Annotation annot = Annotations[0];
            if (annot.annotationType().equals(BeforeClass.class)){
                before_class.add(m);
                before_class_str.add(m.getName());
            }else if (annot.annotationType().equals(Before.class)){
                before.add(m);
                before_str.add(m.getName());
            }else if (annot.annotationType().equals(Test.class)){
                tests.add(m);
                tests_str.add(m.getName());
            }else if (annot.annotationType().equals(After.class)){
                after.add(m);
                after_str.add(m.getName());
            }else if (annot.annotationType().equals(AfterClass.class)){
                after_class.add(m);
                after_class_str.add(m);
            }else{
                throw new RuntimeException("Annotation is of unknown type.");
            }
        }

        before_class_str.sort();
        before_str.sort();
        tests_str.sort();
        after_str.sort();
        after_class_str.sort();

        // execute @BeforeClass methods in alphabetical order. Throw an exception if method is not static
        for (String s: before_class_str){
            Method m = cl.getMethod(s);
            if (Modifier.isStatic(m.getModifiers())){
                throw new RuntimeException("@BeforeClass can only annotate a static method.");
            }
            m.invoke(o);
        }

        // execute the @Before, @Test, @After methods in alphabetical order (@Before, @After should not be run if no other methods)
        if (tests_str.size() > 0){
            for (String s: before){
                Method m = cl.getMethod(s);
                m.invoke(o);
            }
            
            for (String s: tests_str){
                Method m = cl.getMethod(s);
                try {
                    m.invoke(o);
                    errors.put(s, null);
                } catch (Exception e) {
                    errors.put(e);
                }
            }

            for (String s: after_str){
                Method m = cl.getMethod(s);
                try {
                    m.invoke(o);
                    errors.put(s, null);
                } catch (Exception e) {
                    errors.put(e);
                }
            }
        }

        // execute @AfterClass methods in alphabetical order. Throw an exception if method is not static
        for (String s: after_class_str){
            Method m = cl.getMethod(s);
            if (Modifier.isStatic(m.getModifiers())){
                throw new RuntimeException("@AfterClass can only annotate a static method.");
            }
            m.invoke(o);
        }

    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	throw new UnsupportedOperationException();
    }
}
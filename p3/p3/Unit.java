import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
            throw new RuntimeException("Cannot initialize object for this class.");
        }

        // iterate through the methods of this class then separate and store those that have different annotations. Throw exception if multiple annotations
        Method[] methods = cl.getMethods();
        ArrayList<String> before_class_str = new ArrayList<String>();
        ArrayList<String> after_class_str = new ArrayList<String>();
        ArrayList<String> before_str = new ArrayList<String>();
        ArrayList<String> after_str = new ArrayList<String>();
        ArrayList<String> tests_str = new ArrayList<String>();
        
        for (Method m1 : methods){
            Annotation[] annots = m1.getDeclaredAnnotations();

            // each method is only allowed at most one annotation
            if (annots.length > 1){
                throw new RuntimeException("A method must have at most one annotation.");
            }
            else if (annots.length == 1){
                // first gather into appropriate lists. It looks like there are other annotations in the test cases
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
                }
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
            
            if (!Modifier.isStatic(m.getModifiers())){
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
            for (String t: tests_str){

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
                
                try {
                    m = cl.getMethod(t);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Error getting methods from this class.");
                }

                try {
                    m.invoke(o);
                    errors.put(t, null);
                } catch (InvocationTargetException e) {
                    errors.put(t, e.getCause());
                } catch (IllegalAccessException e){
                    throw new RuntimeException("Cannot access method of this object.");
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
        }

        // execute @AfterClass methods in alphabetical order. Throw an exception if method is not static
        for (String s: after_class_str){
            try {
                m = cl.getMethod(s);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error getting methods from this class.");
            }
            if (!Modifier.isStatic(m.getModifiers())){
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
        // is there any reason why this should be a field of Unit, instead of local?
        HashMap<String, Object[]> errors = new HashMap<String, Object[]>();
        Class<?> cl;
        Constructor<?> cons;
        Object o;
        Method m;
        Class[] param_classes;
        Class param_class;
        Annotation[][] param_annots;
        Annotation param_annot;

        // input String name is the name of a class that contains method annotated with @Property
        try {
            cl = Class.forName(name);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Class not found.");
        }
        try {
            cons = cl.getConstructor();
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No zero-argument constructor for this class.");
        }
        try {
            o = cons.newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Cannot initialize object for this class.");
        }

        // get the methods with the @Property annotation
        Method[] methods = cl.getDeclaredMethods();
        ArrayList<String> methods_str = new ArrayList<String>();

        // sort by name
        for (Method m1: methods){
            methods_str.add(m1.getName());
        }
        Collections.sort(methods_str);

        // iterate through methods in alphabetical order
        for(String s: methods_str){
            for (Method m1: methods){
                if (m1.getName().equals(s)){
                    Annotation[] annots = m1.getDeclaredAnnotations();

                    // each method is only allowed at most one annotation
                    if (annots.length > 1){
                        System.out.println("A method must have at most one annotation.");
                        throw new RuntimeException("A method must have at most one annotation.");
                    }
                    else if (annots.length == 1){
                        // if the annotation on this method is @Property get its parameters and parameter annotations
                        Annotation annot = annots[0];
                        if (annot.annotationType().equals(Property.class)){
                            try {
                                param_classes = m1.getParameterTypes();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException("Unable to get parameter types.");
                            }
                            try {
                                param_annots = m1.getParameterAnnotations();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                throw new RuntimeException("Unable to get parameter annotations.");
                            }

                            // should there only be one parameter?
                            if (param_classes.length == 1 && (param_annots[0].length > 1 || param_annots[0].length == 0)){
                                System.out.println("Exactly one annotation per parameter allowed.");
                                throw new RuntimeException("Exactly one annotation per parameter allowed.");
                            } else if (param_classes.length == 1 && param_annots[0].length == 1){
                                param_annot = param_annots[0][0];
                                param_class = param_classes[0];
                                boolean return_val = false;
                                Object[] obj_arr = new Object[1];
                                int count = 0;

                                // we invoke the method with arguments depending on the parameter type.
                                if (param_class.equals(Integer.class) && param_annot.annotationType().equals(IntRange.class)){
                                    IntRange range = (IntRange) param_annot;
                                    int min = range.min();
                                    int max = range.max();

                                    // run a loop until 100 iterations or until the method returns false/Throwable
                                    for (int i = min; (i < max + 1) && (count < 100); i++){
                                        try {
                                            return_val = (Boolean) m1.invoke(o, i);
                                            if (!return_val){
                                                obj_arr[0] = i;
                                                errors.put(s, obj_arr);
                                                break;
                                            }
                                        } catch (Exception e) {
                                            obj_arr[0] = i;
                                            errors.put(s, obj_arr);
                                            break;
                                        }
                                        count = count + 1;
                                    }

                                    // put null for the value if we did not exit the above for-loop early
                                    if (return_val){
                                        errors.put(s, null);
                                    }
                                }else if (param_class.equals(String.class) && param_annot.annotationType().equals(StringSet.class)){
                                    StringSet string_set = (StringSet) param_annot;
                                    String[] strings = string_set.strings();

                                    // run a loop until 100 iterations or until the method returns false/Throwable
                                    for (String t: strings){
                                        if (count >= 100){
                                            break;
                                        }
                                        try {
                                            return_val = (Boolean) m1.invoke(o, t);
                                            if (!return_val){
                                                obj_arr[0] = t;
                                                errors.put(s, obj_arr);
                                                break;
                                            }
                                        } catch (Exception e) {
                                            obj_arr[0] = t;
                                            errors.put(s, obj_arr);
                                            break;
                                        }
                                        count = count + 1;
                                    }

                                    // put null for the value if we did not exit the above for-loop early
                                    if (return_val){
                                        errors.put(s, null);
                                    }
                                }else if (param_class.equals(List.class) && param_annot.annotationType().equals(ListLength.class)){
                                    ListLength list_len = (ListLength) param_annot;
                                    int i1 = list_len.min();
                                    int i2 = list_len.max();

                                    // this array contains the annotated parameters such as @ListLength(min=0, max=2) List<@IntRange(min=5, max=7) Integer>
                                    AnnotatedType[] types_annot = m1.getAnnotatedParameterTypes();
                                    Annotation[] sub_param_annots;
                                    Class sub_param_class;
                                    Type sub_param_type;

                                    /* it looks like at runtime these AnnotatedTypes are in fact AnnotatedParametrizedTypes which
                                    * have the method getAnnotatedActualTypeArguments() which is an array of the annotated parametrized types.
                                    * For example, @IntRange(min=5, max=7) Integer. Is this casting safe? Calling getClass returns some absurdly
                                    * long sun.* class that is not helpful
                                    */

                                    // iterate over all annotated parameters present
                                    for (AnnotatedType t1: types_annot){
                                        // cast as AnnotatedParameterType
                                        AnnotatedParameterizedType t2 = (AnnotatedParameterizedType) t1;
                                        // get annotations on T in List<T>
                                        for (AnnotatedType t3: t2.getAnnotatedActualTypeArguments()){
                                            // sub_param annots should contain for example @IntRange(min=5, max=7)
                                            sub_param_annots = t3.getDeclaredAnnotations();
                                            // sub_param_type should contain for example java.lang.Integer
                                            sub_param_type = t3.getType();

                                            // We can now do checks on a case-by-case basis matching the sub_param_type to sub_param_annots
                                            if (sub_param_type.equals(Integer.class) && sub_param_annots.length == 1 && sub_param_annots[0].annotationType().equals(IntRange.class)){
                                                IntRange int_range = (IntRange) sub_param_annots[0];
                                                int min = int_range.min();
                                                int max = int_range.max();

                                                // iterate over all possible list lengths
                                                for (int i = i1; i < i2 + 1; i++){
                                                    ArrayList<Integer> test_list = new ArrayList<Integer>();
                                                    if (i == 0){
                                                        try {
                                                            return_val = (Boolean) m1.invoke(o, test_list);
                                                            if (!return_val){
                                                                obj_arr[0] = test_list;
                                                                errors.put(s, obj_arr);
                                                                return errors;
                                                            }
                                                        } catch (Exception e) {
                                                            obj_arr[0] = test_list;
                                                            errors.put(s, obj_arr);
                                                            return errors;
                                                        }
                                                        count = count + 1;
                                                    } else if (i == 1){
                                                        for (int j = min; j < max + 1 && count < 100; j++){
                                                            test_list.clear();
                                                            test_list.add(j);
                                                            try {
                                                                return_val = (Boolean) m1.invoke(o, test_list);
                                                                if (!return_val){
                                                                    obj_arr[0] = test_list;
                                                                    errors.put(s, obj_arr);
                                                                    return errors;
                                                                }
                                                            } catch (Exception e) {
                                                                obj_arr[0] = test_list;
                                                                errors.put(s, obj_arr);
                                                                return errors;
                                                            }
                                                            count = count + 1;
                                                        }
                                                    } else if (i == 2){
                                                        for (int j = min; j < max + 1 && count < 100; j++){
                                                            for (int k = min; k < max + 1 && count < 100; k++){
                                                                test_list.clear();
                                                                test_list.add(j);
                                                                test_list.add(k);
                                                                try {
                                                                    return_val = (Boolean) m1.invoke(o, test_list);
                                                                    if (!return_val){
                                                                        obj_arr[0] = test_list;
                                                                        errors.put(s, obj_arr);
                                                                        return errors;
                                                                    }
                                                                } catch (Exception e) {
                                                                    obj_arr[0] = test_list;
                                                                    errors.put(s, obj_arr);
                                                                    return errors;
                                                                }
                                                                count = count + 1;
                                                            }
                                                        }
                                                    } else if (i == 3){
                                                        for (int j = min; j < max + 1 && count < 100; j++){
                                                            for (int k = min; k < max + 1 && count < 100; k++){
                                                                for (int l = min; l < max + 1 && count < 100; l++){
                                                                    test_list.clear();
                                                                    test_list.add(j);
                                                                    test_list.add(k);
                                                                    test_list.add(l);
                                                                    try {
                                                                        return_val = (Boolean) m1.invoke(o, test_list);
                                                                        if (!return_val){
                                                                            obj_arr[0] = test_list;
                                                                            errors.put(s, obj_arr);
                                                                            return errors;
                                                                        }
                                                                    } catch (Exception e) {
                                                                        obj_arr[0] = test_list;
                                                                        errors.put(s, obj_arr);
                                                                        return errors;
                                                                    }
                                                                    count = count + 1;
                                                                }
                                                            }
                                                        }
                                                    } else if (i > 3){
                                                        for (int j = min; j < max + 1 && count < 100; j++){
                                                            for (int k = min; k < max + 1 && count < 100; k++){
                                                                for (int l = min; l < max + 1 && count < 100; l++){
                                                                    for (int n = min; n < max + 1 && count < 100; n++){
                                                                        test_list.clear();
                                                                        test_list.add(j);
                                                                        test_list.add(k);
                                                                        test_list.add(l);
                                                                        test_list.add(n);
                                                                        for (int n1 = 4; n1 < i; n1++){
                                                                            test_list.add(n);
                                                                        }
                                                                        try {
                                                                            return_val = (Boolean) m1.invoke(o, test_list);
                                                                            if (!return_val){
                                                                                obj_arr[0] = test_list;
                                                                                errors.put(s, obj_arr);
                                                                                return errors;
                                                                            }
                                                                        } catch (Exception e) {
                                                                            obj_arr[0] = test_list;
                                                                            errors.put(s, obj_arr);
                                                                            return errors;
                                                                        }
                                                                        count = count + 1;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }                                                    
                                                }

                                                if (return_val){
                                                    errors.put(s, null);
                                                }
                                            }else if (sub_param_type.equals(String.class)){
                                                StringSet string_set = (StringSet) param_annot;
                                                String[] strings = string_set.strings();

                                                for (int i = i1; i < i2 + 1; i++){
                                                    ArrayList<String> test_list = new ArrayList<String>();
                                                    if (i == 0){
                                                        try {
                                                            return_val = (Boolean) m1.invoke(o, test_list);
                                                            if (!return_val){
                                                                obj_arr[0] = test_list;
                                                                errors.put(s, obj_arr);
                                                                return errors;
                                                            }
                                                        } catch (Exception e) {
                                                            obj_arr[0] = test_list;
                                                            errors.put(s, obj_arr);
                                                            return errors;
                                                        }
                                                        count = count + 1;
                                                    } else if (i == 1){
                                                        for (int j = 0; j < strings.length && count < 100; j++){
                                                            test_list.clear();
                                                            test_list.add(strings[j]);
                                                            try {
                                                                return_val = (Boolean) m1.invoke(o, test_list);
                                                                if (!return_val){
                                                                    obj_arr[0] = test_list;
                                                                    errors.put(s, obj_arr);
                                                                    return errors;
                                                                }
                                                            } catch (Exception e) {
                                                                obj_arr[0] = test_list;
                                                                errors.put(s, obj_arr);
                                                                return errors;
                                                            }
                                                            count = count + 1;
                                                        }
                                                    }
                                            }
                                            }else if (sub_param_type.equals(Object.class)){

                                            }else if (sub_param_type.equals(List.class)){

                                            }
                                        }
                                    }


                                }else if (param_class.equals(Object.class) && param_annot.annotationType().equals(ForAll.class)){
                                    ForAll forall = (ForAll) param_annot;
                                    String meth_name = forall.name();
                                    int times = forall.times();
                                    Method meth;
                                    Object o1 = null;

                                    try {
                                        meth = cl.getDeclaredMethod(meth_name);
                                    } catch (NoSuchMethodException e) {
                                        throw new RuntimeException(e.getMessage());
                                    }

                                    // call the method corresponding to meth_name a certain number of times. It will return an object which we pass as a parameter to m1
                                    for (int i = 0; i < times && count < 100; i++){
                                        try {
                                            o1 = meth.invoke(o);
                                            return_val = (Boolean) m1.invoke(o, o1);
                                            if (!return_val){
                                                obj_arr[0] = o1;
                                                errors.put(s, obj_arr);
                                                break;
                                            }
                                        } catch (Exception e) {
                                            obj_arr[0] = o1;
                                            errors.put(s, obj_arr);
                                            break;
                                        }
                                    }

                                    // put null for the value if we did not exit the above for-loop early
                                    if (return_val){
                                        errors.put(s, null);
                                    }
                                }else{
                                    throw new RuntimeException("Parameter is not an acceptable class.");
                                }
                            } else if (param_classes.length > 1){
                                System.out.println("here");
                                // Create arrays that will store parameters to pass
                                ArrayList<ArrayList<?>> arrays = new ArrayList<ArrayList<?>>();
                                ArrayList<Integer> count_args = new ArrayList<Integer>();
                                boolean return_val = false;
                                int count = 0;
                                System.out.println("here");

                                // iterate over parameter classes
                                for (int i = 0; i < param_classes.length; i++){
                                    System.out.println("here");
                                    if (param_annots[i].length != 1){
                                        System.out.println("One of the parameters in a @Property method does not have exactly one annotation.");
                                        throw new RuntimeException("One of the parameters in a @Property method does not have exactly one annotation.");
                                    }
                                    if (param_classes[i].equals(Integer.class) && param_annots[i][0].annotationType().equals(IntRange.class)){
                                        System.out.println(Integer.class);
                                        param_classes[i] = Integer.class;
                                        IntRange range = (IntRange) param_annots[i][0];
                                        ArrayList<Integer> int_array = new ArrayList<Integer>();
                                        count_args.add(0, 0);
                                        int min = range.min();
                                        int max = range.max();

                                        for (int j = min; (j < max + 1) ; j++){
                                            int_array.add(j);
                                        }
                                        arrays.add(int_array);
                                    } else if (param_classes[i].equals(String.class) && param_annots[i][0].annotationType().equals(StringSet.class)){
                                        System.out.println(String.class);
                                        param_classes[i] = String.class;
                                        StringSet string_set = (StringSet) param_annots[i][0];
                                        ArrayList<String> str_array = new ArrayList<String>();
                                        count_args.add(0, 0);
                                        String[] strings = string_set.strings();

                                        for (String t: strings){
                                            str_array.add(t);
                                        }
                                        arrays.add(str_array);
                                    } else if (param_classes[i].equals(List.class) && param_annots[i][0].annotationType().equals(ListLength.class)){
                                        System.out.println(List.class);
                                        param_classes[i] = List.class;
                                        param_annots[i][0] = (ListLength) param_annots[i][0];
                                        arrays.add(0, new ArrayList<List>());
                                        count_args.add(0, 0);
                                        
                                    } else if (param_classes[i].equals(Object.class) && param_annots[i][0].annotationType().equals(ForAll.class)){
                                        System.out.println(Object.class);
                                        param_classes[i] = Object.class;
                                        ForAll forall = (ForAll) param_annots[i][0];
                                        ArrayList<Object> obj_array = new ArrayList<Object>();
                                        String name1 = forall.name();
                                        int times1 = forall.times();
                                        count_args.add(0, 0);
                                        Method meth;
                                        
                                        try {
                                            meth = cl.getDeclaredMethod(name1);
                                        } catch (NoSuchMethodException e) {
                                            throw new RuntimeException(e.getMessage());
                                        }

                                        for (int j = 0; j < times1; j++){
                                            try {
                                                obj_array.add(meth.invoke(o));
                                            } catch (Exception e) {
                                                throw new RuntimeException(e.getMessage());
                                            }
                                        }

                                        arrays.add(obj_array);
                                    } else{
                                        System.out.println("Parameter is not an acceptable class.");
                                        throw new RuntimeException("Parameter is not an acceptable class.");
                                    }
                                }

                                System.out.println("here1");
                                boolean break_while = false;
                                try{
                                    // collect a set of parameters to then invoke the method
                                    Object[] args = new Object[param_classes.length];
                                    if (param_classes.length == 2){
                                        for (int i = 0; i < 100; i++){
                                            for (int j = 0; j < 100; j++){
                                                ArrayList<?> temp0 = arrays.get(0);
                                                ArrayList<?> temp1 = arrays.get(1);
                                                if (i >= temp0.size() || j >= temp1.size() || count > 100){
                                                    break;
                                                }
                                                args[0] = arrays.get(0).get(i);
                                                args[1] = arrays.get(1).get(j);

                                                try {
                                                    return_val = (Boolean) m1.invoke(o, args);
                                                    if (!return_val){
                                                        errors.put(s, args);
                                                        break;
                                                    }
                                                } catch (Exception e) {
                                                    errors.put(s, args);
                                                    break;
                                                }
                                                count = count + 1;
                                            }
                                        }
                                    }
                                }
                                catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                                if (return_val){
                                    errors.put(s, null);
                                }
                            }            
                        }
                    }
                }
            }
        }
        System.out.println(errors.keySet());
        System.out.println(errors.values());
        return errors;
    }
}
package jrails;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class JRouter {

    private HashMap<String, String> routes = new HashMap<String, String>();

    public void addRoute(String verb, String path, Class clazz, String method) {
        routes.put(verb + path, clazz.getName() + "#" +  method);
    }

    // Returns "clazz#method" corresponding to verb+URN
    // Null if no such route
    public String getRoute(String verb, String path) {
        return routes.get(verb + path);
    }

    // Call the appropriate controller method and
    // return the result
    public Html route(String verb, String path, Map<String, String> params) {
        Class<?> tocall;
        Constructor<?> cons;
        Object o1;
        Object o2;
        if (this.routes.get(verb + path) == null){
            throw new UnsupportedOperationException("No route of this type exists.");
        }
        String[] tocall_str = this.routes.get(verb + path).split("#");
        try {
            tocall = Class.forName(tocall_str[0]);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            cons = tocall.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            o1 = cons.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            for (Method l: tocall.getMethods()){
                System.out.println(l.getName());
                if (l.getName().equals(tocall_str[1])){
                    o2 = l.invoke(o1, params);
                    if (o2.getClass().equals(Html.class)){
                        return (Html) o2;
                    } else{
                        throw new RuntimeException("HTML was not returned.");
                    }
                }
            }
            throw new RuntimeException("Found no matching method.");
        } catch (Exception e) {
            throw new RuntimeException("Cannot invoke this method.");
        }
    }
}

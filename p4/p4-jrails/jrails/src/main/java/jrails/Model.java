package jrails;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;

import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;

public class Model {
    protected int id;
    protected static Random random = new Random();   
    protected final Path path;
    Field[] fields;

    public Model(){
        this.id = 0;
        this.path = Paths.get(this.getClass().toString() + ".csv");
        this.fields = this.getClass().getFields();

        int count = 0;
        String fields_columns_str_concat = "";
        ArrayList<String> fields_columns_str_concat_arr = new ArrayList<String>();
        try {
            for (Field f: this.fields){
                Annotation annot = f.getAnnotation(Column.class);
                if (annot != null && !(f.getType() == int.class) && !(f.getType().equals(String.class)) && !(f.getType() == boolean.class)){
                    throw new RuntimeException("A field annotated with @Column does not have an allowed class.");
                }
                if (count == 0){
                    fields_columns_str_concat = f.getName();
                }else{
                    fields_columns_str_concat = f.getName() + "," + fields_columns_str_concat;
                }
                count = count + 1;
            }
            fields_columns_str_concat = "id," + fields_columns_str_concat;
            fields_columns_str_concat_arr.add(fields_columns_str_concat);
            if (!Files.exists(this.path)){
                Files.write(this.path, fields_columns_str_concat_arr, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
        } catch (IOException e){
            throw new RuntimeException("IOException within constructor of Model().");
        }
    }

    public void save() {
        // this is an instance of the current model. First field should be the id of this model
        String fields_str_concat = "";
        int count = 0;
        try {
            for (Field f: this.fields){
                Annotation annot = f.getAnnotation(Column.class);
                if (annot != null && !(f.getType() == int.class) && !(f.getType().equals(String.class)) && !(f.getType() == boolean.class)){
                    throw new RuntimeException("A field annotated with @Column does not have an allowed class.");
                }
                if (count == 0){
                    if (f.get(this) != null && f.get(this).equals("")) {
                        fields_str_concat = "empty_str";
                    } else if (f.get(this) != null){
                        fields_str_concat = f.get(this).toString();
                    }
                    else{
                        fields_str_concat = "NULLVAL";
                    }
                } else{
                    if (f.get(this) != null && f.get(this).equals("")) {
                        fields_str_concat = "empty_str" + "," + fields_str_concat;
                    } else if (f.get(this) != null){
                        fields_str_concat = f.get(this).toString() + "," + fields_str_concat;
                    }
                    else{
                        fields_str_concat = "NULLVAL" + "," + fields_str_concat;
                    }
                }
                count = count + 1;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException within save().");
        }
        
        // line will have the corresponding data corresponding to each @Columns
        ArrayList<String> fields_str_concat_arr = new ArrayList<String>();
        try {
            // if the current id is zero, we add a row to the current table, else search table for current id
            if (this.id == 0){
                this.id = random.nextInt(1000000);
                String id_str = Integer.toString(this.id);
                fields_str_concat = id_str + "," + fields_str_concat;
                fields_str_concat_arr.add(fields_str_concat);
                Files.write(this.path, fields_str_concat_arr, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            } else{
                // look in the database to see if this id exists, throw an exception if not
                List<String> lines = Files.readAllLines(this.path);
                String id_str = Integer.toString(this.id);
                fields_str_concat = id_str + "," + fields_str_concat;
                boolean check = false;
                // should we break this loop? There shouldn't be more than one location with this id in the first place
                for (String s: lines){
                    if (s.indexOf(Integer.toString(this.id)) != -1){
                        check = true;
                        lines.set(lines.indexOf(s), fields_str_concat);
                    }
                }

                // write the entirety of the database again with the new edited/updated row
                if (!check){
                    throw new RuntimeException("Saving a model with a non-zero ID that is not already in the database");
                } else{
                    Files.delete(this.path);
                    Files.write(this.path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException within save() - likely from writing a new row to the table.");
        }
    }

    public int id() {
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        Constructor<T> cl;
        Object o;
        Model m;
        HashMap<String, Class> fieldMap = new HashMap<>();

        try {
            cl = c.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            o = cl.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        if (o instanceof Model){
            m = (Model) o;
                for (Field f: (m.fields)){
                    Annotation annot = f.getAnnotation(Column.class);
                    if (annot != null && !(f.getType() == int.class) && !(f.getType().equals(String.class)) && !(f.getType() == boolean.class)){
                        throw new RuntimeException("A field annotated with @Column does not have an allowed class.");
                    }
                    fieldMap.put(f.getName(), f.getType());
                }

            Path local_path = m.path;
            String local_data = "";
            List<String> lines;
            String header;
            String id_str = Integer.toString(id);

            try {
                lines = Files.readAllLines(local_path);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read lines of this file.");
            }

            // assumes (rightly) that the first row of the database on file is the header row with the name of @Column fields
            boolean check = false;
            header = lines.get(0);
            for (String s: lines){
                String[] line = s.split(",");
                for (String t: line){
                    if (t.equals(id_str)){
                        check = true;
                        local_data = s;
                        break;                    
                    }
                }
            }

            // set the fields. This heavily depends on a specific implementation above where the id is the first column
            if (check){
                String[] splits = local_data.split(",");
                String[] header_splits = header.split(",");
                System.out.println(splits.length);
                System.out.println(header_splits.length);
                m.id = Integer.valueOf(splits[0]);
                for (Field f: m.fields){
                    for (int i = 0; i < header_splits.length; i++){
                        if (f.getName().equals(header_splits[i])){
                            try {
                                if (f.getType() == int.class){
                                    f.set(m, Integer.valueOf(splits[i]));
                                } else if (f.getType() == boolean.class){
                                    f.set(m, (boolean) Boolean.valueOf(splits[i]));
                                } else if (f.getType().equals(String.class)){
                                    if (splits[i].equals("empty_str")){
                                        f.set(m, "");
                                    } else{
                                        f.set(m, splits[i]);
                                    }
                                } else{
                                    throw new RuntimeException("Field is not retreived as a valid type.");
                                }
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("IllegalAccessException within find().");
                            }
                        }
                    }
                }
            } else{
                return null;
            }
        }else{
            throw new RuntimeException("The input class is not a Model.");
        }

        return (T) m;
    }

    public static <T> List<T> all(Class<T> c) {
        // Returns a List<element type>
        List<T> list_all = new ArrayList<T>();

        // get all the ids in the current database and call find()
        Constructor<T> cl;
        Object o;
        Model m;

        try {
            cl = c.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            o = cl.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        if (o instanceof Model){
            m = (Model) o;

            List<String> lines;
            Path local_path = m.path;
            try {
                lines = Files.readAllLines(local_path);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            // iterate over lines and get the id in the first column after splitting
            System.out.println(lines.get(0));
            for (int i = 1; i < lines.size(); i++){
                System.out.println(lines.get(i));
                String[] local_data = lines.get(i).split(",");
                int id_loc = Integer.valueOf(local_data[0]);
                list_all.add(Model.find(c, id_loc));
            }
        } else{
            throw new RuntimeException("The input class is not a Model.");
        }

        return list_all;
    }

    public void destroy() {
        if (Model.find(this.getClass(), this.id) == null){
            throw new RuntimeException("Cannot find receiver in database.");
        } else{
            List<String> lines;
            Path local_path = this.path;
            try {
                lines = Files.readAllLines(local_path);
            } catch (IOException e) {
                throw new RuntimeException("Cannot read file.");
            }

            // iterate over lines and get the id in the first column after splitting
            int remove = -1;
            int count = 0;
            // skip the first line which will have column information
            for (String s : lines){
                if (count == 0){
                    count = count + 1;
                    continue;
                }
                String[] local_data = s.split(",");
                int id = Integer.valueOf(local_data[0]);
                if (id == this.id()){
                    remove = lines.indexOf(s);
                }
                count = count + 1;
            }
            lines.remove(remove);
            // delete the current .csv file and create a new one with the correct line ommited
            try {
                Files.delete(this.path);
                Files.write(this.path, lines, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (Exception e) {
                throw new RuntimeException("Faield to delete or write file within destroy().");
            }
        }
    }

    public static void reset() {
        // open the only .csv file and delete all rows except the first
        File folder = new File("./");
        File[] listOfFiles = folder.listFiles();
        String csv = "";
        // repeat this process for all .csv files in this directory since reset() should reset databases for all models
        for (File f: listOfFiles){
            if (f.getName().indexOf(".csv") != -1){
                csv = f.getName();

                Path local_path = Paths.get(csv);
                List<String> lines;
                try {
                    lines = Files.readAllLines(local_path);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                ArrayList<String> first_line = new ArrayList<String>();
                first_line.add(lines.get(0));
                // delete the current .csv file and create a new one with the first line with the id and @Column names
                try {
                    Files.delete(local_path);
                    Files.write(local_path, first_line, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete or write file within reset().");
                }
            }
        }
    }
}

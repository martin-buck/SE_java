package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.nio.file.Files;
import java.util.*;

import books.Book;
import main.java.books.Cat;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.beans.Transient;
import java.io.IOException;

public class ModelTest {

    private Model model;
    private Book book1;
    private Book book2;
    private Cat cat1;

    @Before
    public void setUp() throws Exception {
        // reset and initialize models including another custom model
        Model.reset();
        model = new Model(){};
        book1 = new Book();
        book2 = new Book();
        cat1 = new Cat();

        assert book1.id() == 0;
        book1.title = "Zombies";
        book1.author = "Michael Jackson";
        book1.num_copies = 10;
        book1.save();

        assert book2.id() == 0;
        book2.title = "Mummies";
        book2.author = "Janet Jackson";
        book2.num_copies = 1;
        book2.save();

        assert cat1.id() == 0;
        cat1.isSleep = true;
        cat1.name = null;
        cat1.num = 1;
        cat1.save();
    }

    @Test
    public void save_find() {
        // from original code
        assertThat(model.id(), notNullValue());

        // tests that find() retrieves correct information about books from database
        Book book1_copy = Book.find(Book.class, book1.id());
        assert book1_copy.id() == book1.id();
        assert book1_copy.author.equals(book1.author);
        assert book1_copy.title.equals(book1.title);
        assert book1_copy.num_copies == book1.num_copies;
        Book book2_copy = Book.find(Book.class, book2.id());
        assert book2_copy.id() == book2.id();
        assert book2_copy.author.equals(book2.author);
        assert book2_copy.title.equals(book2.title);
        assert book2_copy.num_copies == book2.num_copies;
        Cat cat1_copy = Model.find(Cat.class, cat1.id());
        assert cat1_copy.isSleep;
        assert cat1_copy.name.equals("NULLVAL");
        cat1.name = "";
        cat1.save();
        Cat cat2_copy = Model.find(Cat.class, cat1.id());
        System.out.println(cat2_copy.name);
        assert cat2_copy.name.equals("");


        // make sure that the data is not written to the database prematurely before save() is called
        book2.title = "Return of Mummies";
        assert !book2_copy.title.equals(book2.title);
        book2.save();
        assert !book2_copy.title.equals(book2.title);

        // ensure that find returns null when an id is not present in the database
        assert Book.find(Book.class, 0) == null;
        assert Book.find(Book.class, 1991) == null;
        assert Model.find(Book.class, 0) == null;
        assert Model.find(Book.class, 0) == null;
        assert Model.find(Model.class, 1991) == null;

        // test that @Columns cannot contain types other than int, String, or boolean
    }

    @Test
    public void all_destroy() throws IOException{
        // test that all() returns correct Book information
        ArrayList<String> authors = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<Integer> ids = new ArrayList<Integer>();

        // if we input Model.class, should this return all instances of subclasses of a Model?
        List<Book> books = Book.all(Book.class);
        List<Book> models = Model.all(Book.class);
        for (Book b: books){
            authors.add(b.author);
            titles.add(b.title);
            ids.add(b.id);
        }

        assert books.size() == 2;
        assert models.size() == 2;
        assert authors.contains(book1.author);
        assert authors.contains(book2.author);
        assert titles.contains(book1.title);
        assert titles.contains(book2.title);
        assert ids.contains(book1.id);
        assert ids.contains(book2.id);

        book1.destroy();
        assert Book.find(Book.class, book1.id()) == null;
        try {
            book1.destroy();
            assert false;
        } catch (Exception e) {
            assert e.getMessage().equals("Cannot find receiver in database.");
        }

        Book.reset();
        assert Book.find(Book.class, book2.id()) == null;
    }

    @Test
    public void testList() throws IOException{
        
    }

    @After
    public void tearDown() throws Exception {
    }

}
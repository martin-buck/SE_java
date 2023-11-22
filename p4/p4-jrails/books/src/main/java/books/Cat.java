package main.java.books;

import jrails.Column;
import jrails.Model;

public class Cat extends Model{
    @Column 
    public String name;
    @Column
    public int num;
    @Column
    public boolean isSleep;
}

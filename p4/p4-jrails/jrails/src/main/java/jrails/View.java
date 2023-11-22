package jrails;

public class View {
    public static Html empty() {
        return new Html();
    }

    public static Html br() {
        Html new_html = new Html();
        return new_html.br();
    }

    public static Html t(Object o) {
        // Use o.toString() to get the text for this
        Html new_html = new Html();
        return new_html.t(o);
    }

    public static Html p(Html child) {
        Html new_html = new Html();
        return new_html.p(child);
    }

    public static Html div(Html child) {
        Html new_html = new Html();
        return new_html.div(child);
    }

    public static Html strong(Html child) {
        Html new_html = new Html();
        return new_html.strong(child);
    }

    public static Html h1(Html child) {
        Html new_html = new Html();
        return new_html.h1(child);
    }

    public static Html tr(Html child) {
        Html new_html = new Html();
        return new_html.tr(child);
    }

    public static Html th(Html child) {
        Html new_html = new Html();
        return new_html.th(child);
    }

    public static Html td(Html child) {
        Html new_html = new Html();
        return new_html.td(child);
    }

    public static Html table(Html child) {
        Html new_html = new Html();
        return new_html.table(child);
    }

    public static Html thead(Html child) {
        Html new_html = new Html();
        return new_html.thead(child);
    }

    public static Html tbody(Html child) {
        Html new_html = new Html();
        return new_html.tbody(child);
    }

    public static Html textarea(String name, Html child) {
        Html new_html = new Html();
        return new_html.textarea(name, child);
    }

    public static Html link_to(String text, String url) {
        Html new_html = new Html();
        return new_html.link_to(text, url);
    }

    public static Html form(String action, Html child) {
        Html new_html = new Html();
        return new_html.form(action, child);
    }

    public static Html submit(String value) {
        Html new_html = new Html();
        return new_html.submit(value);
    }
}

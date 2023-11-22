package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is; // <-- must import additional to work

public class HtmlTest {

    private Html html;

    @Before
    public void setUp() throws Exception {
        html = new Html();
    }

    @Test
    public void tests() {
        assertThat(View.empty().toString(), isEmptyString());
        Html link = View.link_to("Edit", "/edit?id=8");
        assertEquals(link.toString(), "<a href=\"/edit?id=8\">Edit</a>");
        Html form = View.form("/update", link);
        assertEquals(form.toString(), "<form action=\"/update\" accept-charset=\"UTF-8\" method=\"post\"><a href=\"/edit?id=8\">Edit</a></form>");
    }

    @Test
    public void sampleTags() {
        Html text = View.t("lorum ipsum");
        Html pText = View.p(text);
        assert(pText.toString().equals("<p>lorum ipsum</p>"));
        assert(View.div(pText).toString().equals("<div><p>lorum ipsum</p></div>"));
        assert(View.strong(text).toString().equals("<strong>lorum ipsum</strong>"));
        assert(View.h1(text).toString().equals("<h1>lorum ipsum</h1>"));
    }

    @Test
    public void htmlAndViewTest() {
        Html link_test = View.link_to("Search", "www.google.com");
        assertEquals("<a href=\"www.google.com\">Search</a>", link_test.toString());
    }

    @Test
        public void testForm() {
        String action = "/create";
        Html child = View.t("child");
        Html form = View.form(action, child);
        String answer = "<form action=\"/create\" accept-charset=\"UTF-8\" method=\"post\">child</form>";
        assertThat(form.toString(), is(answer));
    }

    @Test
    public void testLinkTo() {
        Html html = View.link_to("qq", "http://www.qq.com");
        assertEquals("<a href=\"http://www.qq.com\">qq</a>", html.toString());
    }

}

package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class JRouterTest {

    private JRouter jRouter;

    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
    }

    @Test
    public void addRoute() {
        jRouter.addRoute("GET", "/", String.class, "index");
        assertThat(jRouter.getRoute("GET", "/"), is("java.lang.String#index"));
        jRouter.addRoute("POST", "/fake_page", Object.class, "hashcode");
        assertThat(jRouter.getRoute("POST", "/fake_page"), is("java.lang.Object#hashcode"));
        jRouter.addRoute("POST", "/", String.class, "index");
        assertThat(jRouter.getRoute("POST", "/"), is("java.lang.String#index"));
        assert jRouter.getRoute("FAKE", "/") == null;
        try {
            jRouter.route("FAKE", "/", new HashMap<String, String>());
            assert false;
        } catch (UnsupportedOperationException e) {
            assert true;
        }
    }
}
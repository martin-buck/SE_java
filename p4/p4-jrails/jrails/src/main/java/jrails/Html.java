package jrails;

public class Html {
    protected String html = "";

    public String toString() {
        return html;
    }

    public Html seq(Html h) {
        Html new_html = new Html();
        new_html.html = this.html.concat(h.html);
        return new_html;
    }

    public Html br() {
        Html new_html = new Html();
        new_html.html = this.html.concat("<br/>");
        return new_html;
    }

    public Html t(Object o) {
        // Use o.toString() to get the text for this
        Html new_html = new Html();
        new_html.html = o.toString();
        return new_html;
    }

    public Html p(Html child) {
        String p = "<p>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</p>"));
        return new_html;
    }

    public Html div(Html child) {
        String p = "<div>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</div>"));
        return new_html;
    }

    public Html strong(Html child) {
        String p = "<strong>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</strong>"));
        return new_html;
    }

    public Html h1(Html child) {
        String p = "<h1>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</h1>"));
        return new_html;
    }

    public Html tr(Html child) {
        String p = "<tr>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</tr>"));
        return new_html;
    }

    public Html th(Html child) {
        String p = "<th>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</th>"));
        return new_html;
    }

    public Html td(Html child) {
        String p = "<td>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</td>"));
        return new_html;
    }

    public Html table(Html child) {
        String p = "<table>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</table>"));
        return new_html;
    }

    public Html thead(Html child) {
        String p = "<thead>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</thead>"));
        return new_html;
    }

    public Html tbody(Html child) {
        String p = "<tbody>";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</tbody>"));
        return new_html;
    }

    public Html textarea(String name, Html child) {
        String p = "<textarea name=" +  "\"" + name + "\"" + ">";
        Html new_html = new Html();
        new_html.html = p.concat(child.html.concat("</textarea>"));
        return new_html;
    }

    public Html link_to(String text, String url) {
        Html new_html = new Html();
        new_html.html = "<a href=" + "\"" + url + "\"" + ">" + text + "</a>";
        return new_html;
    }

    public Html form(String action, Html child) {
        Html new_html = new Html();
        new_html.html = "<form action=" + "\"" + action + "\"" + " accept-charset=\"UTF-8\" method=\"post\">" + child.html +"</form>";
        return new_html;
    }

    public Html submit(String value) {
        Html new_html = new Html();
        new_html.html = "<input type=\"submit\" value=" + "\"" + value + "\"" + "/>";
        return new_html;
    }
}
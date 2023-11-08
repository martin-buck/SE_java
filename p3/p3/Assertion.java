public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    static AssertThatObject assertThat(Object o) {
        return new AssertThatObject(o);
    }
    static AssertThatString assertThat(String s) {
        return new AssertThatString(s);
    }
    static AssertThatBoolean assertThat(boolean b) {
        return new AssertThatBoolean(b);
    }
    static AssertThatInt assertThat(int i) {
        return new AssertThatInt(i);
    }
}
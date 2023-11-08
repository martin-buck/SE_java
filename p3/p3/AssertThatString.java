public class AssertThatString {
    private String s;

    public AssertThatString(String s1){
        this.s = s1;
    }

    public AssertThatString startsWith(String s2){
        if (!this.s.substring(0,s2.length()).equals(s2)){
            throw new RuntimeException("String does not start with given substring");
        }
        return this;
    }

    public AssertThatString isEmpty(){
        if (!this.s.isEmpty()){
            throw new RuntimeException("String is not empty.");
        }
        return this;
    }

    public AssertThatString contains(String s2){
        if (this.s.indexOf(s2) == -1){
            throw new RuntimeException("String does not contain substring");
        }
        return this;
    }

    public AssertThatString isNotNull(){
        if (this.s == null){
            throw new RuntimeException("String is null.");
        }
        return this;
    }

    public AssertThatString isNull(){
        if (this.s != null){
            throw new RuntimeException("Object is not null");
        }
        return this;
    }

    public AssertThatString isEqualTo(Object o1){
        if (!this.s.equals(o1)){
            throw new RuntimeException("Objects are not the same");
        }
        return this;
    }

    public AssertThatString isNotEqualTo(Object o1){
        if (this.s.equals(o1)){
            throw new RuntimeException("Objects are the same");
        }
        return this;
    }
}

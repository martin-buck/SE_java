public class AssertThatObject {
    private Object o;

    public AssertThatObject(Object o1){
        this.o = o1;
    }

    public AssertThatObject isNotNull(){
        if (this.o == null){
            throw new RuntimeException("Object is null.");
        }
        return this;
    }

    public AssertThatObject isNull(){
        if (this.o != null){
            throw new RuntimeException("Object is not null");
        }
        return this;
    }

    public AssertThatObject isEqualTo(Object o1){
        if (!this.o.equals(o1)){
            throw new RuntimeException("Objects are not the same");
        }
        return this;
    }

    public AssertThatObject isNotEqualTo(Object o1){
        if (this.o.equals(o1)){
            throw new RuntimeException("Objects are the same");
        }
        return this;
    }

    public AssertThatObject isInstanceOf(Class c){
        if (!this.o.getClass().equals(c)){
            throw new RuntimeException("Object is not an instance of given class");
        }
        return this;
    }
}

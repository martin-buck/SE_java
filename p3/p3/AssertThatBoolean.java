public class AssertThatBoolean {
    private boolean b;

    public AssertThatBoolean(boolean b){
        this.b = b;
    }
    
    public AssertThatBoolean isEqualTo(boolean b2){
        if (b != b2){
            throw new RuntimeException("Booleans are not equal.");
        }
        return this;
    }

    public AssertThatBoolean isTrue(){
        if (!this.b){
            throw new RuntimeException("Boolean is not true.");
        }
        return this;
    }

    public AssertThatBoolean isFalse(){
        if(this.b){
            throw new RuntimeException("Boolean is true.");
        }
        return this;
    }
}

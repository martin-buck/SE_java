public class AssertThatInt {
    private int i;

    public AssertThatInt(int i){
        this.i = i;
    }

    public AssertThatInt isEqualTo(int i2){
        if (i != i2){
            throw new RuntimeException("Ints are not equal.");
        }
        return this;
    }

    public AssertThatInt isLessThan(int i2){
        if (i >= i2){
            throw new RuntimeException("Int is greater than given int.");
        }
        return this;
    }

    public AssertThatInt isGreaterThan(int i2){
        if (i <= i2){
            throw new RuntimeException("Int is less than given int.");
        }
        return this;
    }
}

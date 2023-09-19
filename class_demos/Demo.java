public class Demo{
    private int x;
    public int y;

    public int getX(){
        return this.x;
    }

    // this works because it within the Demo class
    public static void main(String[] args){
        Demo d = new Demo();
        int a = d.x;
        int b = d.y;
        System.out.println(a);
    }
}
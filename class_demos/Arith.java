public class Arith
{
    public static int add1(int x){return x+1;}
    public static int add2(int x){return add1(add1(x));}

    public static void main(String[] args){
        int a = Arith.add1(2);
        int b = Arith.add2(2);
        System.out.println(a);
        System.out.println(b);
    }
}


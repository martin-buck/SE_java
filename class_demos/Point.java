public class Point
{
    // fields
    private int x;
    private int y;

    // constructor
    Point(int a, int b){
        this.x = a;
        this.y = b;
    }

    // static method is called 'class' method
    public static Point shift(Point p, int dx, int dy){
        return new Point(p.x + dx, p.y + dy);
    }

    public static double dist(Point p1, Point p2){
        return Math.sqrt(Math.pow(Math.abs(p1.x-p2.x), 2) + 
        Math.pow(Math.abs(p1.y-p2.y), 2));
    }

    // 'instance' method
    public int getX(){
        return this.x;
    }

    // 'class' method
    public static int getX(Point p){
        return p.x + p.y;
    }

    public static void main(String args[]){
        Point p1 = new Point(1,2);
        Point p2 = new Point(3,4);

        Point p3 = Point.shift(p1, -1, 5);
        double d = Point.dist(p2, p3);
        int inst_x = p3.getX();
        int class_x = Point.getX(p3);

        System.out.println(d);
        System.out.println(inst_x);
        System.out.println(class_x);

    }
}
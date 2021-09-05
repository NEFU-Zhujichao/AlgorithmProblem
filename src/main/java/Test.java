public class Test {
    // static 修饰的变量是静态变量，是全局变量，不能放在方法中定义为局部变量
    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        Integer c = 1;
        Integer d = 1;
        Integer e = 1000;
        Integer f = 1000;
        System.out.println(a == b); // false
        System.out.println(a == c); // false
        System.out.println(c == d); // true
        System.out.println(e == f); // false
    }
}

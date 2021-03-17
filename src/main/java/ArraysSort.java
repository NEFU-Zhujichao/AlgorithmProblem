import java.util.*;

public class ArraysSort {
    private volatile int a = 5;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int k = in.nextInt();
        int[] arr = {50,3,5,2,2};
        Arrays.sort(arr,0,5);
        // -127 ~ 128
        Integer v1 = 1;
        Integer v2 = 1;
        Integer v3 = 6666;
        Integer v4 = 6666;
        System.out.println(v1 == v2);
        System.out.println(v3.equals(v4));
    }
}

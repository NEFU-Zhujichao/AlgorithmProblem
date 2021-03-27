import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            int n = scanner.nextInt(),k = scanner.nextInt(),ans = 0;
            int[] res = new int[n];
            for(int i = 0;i < n;i++){
                res[i] = scanner.nextInt();
                ans += res[i];
            }
            System.out.println(ans);
        }
    }
}

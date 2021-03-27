import java.util.Scanner;

public class Test1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        for(int i = 0;i < k;i++) {
            int n = scanner.nextInt();
            String res = scanner.next();
            int l = 0, r = 0, flag = -2;
            int left = 0, right = n - 1;
            StringBuilder sb = new StringBuilder(res);
            while (left <= right) {
                if (sb.charAt(left) != sb.charAt(right)) {
                    flag = left;
                    l++;
                }
                left++;
                right--;
            }
            if (l == 0) {
                if (n % 2 == 1) sb.setCharAt(n / 2, '0');
                r = 1;
                System.out.println(sb);
            }
            if (l == 1) {
                r = 1;
                char min = (char) Math.min(res.charAt(flag), res.charAt(n - 1 - flag));
                sb.setCharAt(flag, min);
                sb.setCharAt(n - 1 - flag, min);
                System.out.println(sb);
            }
            if (r == 0) {
                for (int j = 0; j < n; j++) {
                    if (sb.charAt(j) != '0') {
                        sb.setCharAt(j, '0');
                        break;
                    }
                }
                System.out.println(sb);
            }
        }
    }
}

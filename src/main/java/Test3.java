import java.util.Scanner;

public class Test3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            int n = scanner.nextInt(),k = scanner.nextInt();
            int[] res = new int[n],dp = new int[n+1];
            for (int i = 0;i < n;i++){
                res[i] = scanner.nextInt();
                dp[i] = res[i];
            }
            for (int i = 0;i < n;i++){
                for(int j = 0;j < k;j++){
                    if (i+1 < n)
                    dp[i] = Math.max(dp[i],dp[i]^res[i+1]);
                }
            }
            int ans = 0;
            for(int i = 0;i < n;i++){
                //System.out.println("dp[i]="+dp[i]);
                ans = Math.max(ans,dp[i]);
            }
            System.out.println(ans);
        }
    }
}

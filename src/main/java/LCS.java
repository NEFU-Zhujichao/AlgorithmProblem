import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LCS {
    public static void main(String[] args) {
        // 动态规划 O(n^2) 超时
        int[] arr = {4,5,8,7,1,2,3};
        int[] dp = new int[arr.length];
        int max = 0,cnt = 0;
        Arrays.fill(dp,1);
        for(int i = 0;i < arr.length;i++){
            for(int j = 0;j < i;j++){
                if(arr[j] < arr[i]){
                    dp[i] = Math.max(dp[i],dp[j]+1);
                }
            }
            max = Math.max(max,dp[i]);
            cnt = max;
        }
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for(int i = arr.length-1;i >= 0;i--){
            if(dp[i] == cnt){
                cnt--;
                ArrayList<Integer> list = new ArrayList<>();
                list.add(arr[i]);
                for(int j = i - 1;j >= 0;j--){
                    if(dp[j] == cnt){
                        list.add(arr[j]);
                        cnt--;
                    }
                }
                Collections.sort(list);
                res.add(list);
                cnt = max;
            }
        }
        Collections.sort(res,(a,b) -> {
            for(int i = 0;i < a.size();i++){
                return a.get(i) - b.get(i);
            }
            return 0;
        });
        int[] r = new int[max];
        for(int i = 0;i < res.get(0).size();i++){
            r[i] = res.get(0).get(i);
        }
    }
}


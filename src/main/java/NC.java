import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class NC {
    public static void main(String[] args) {
        String s = "h i";
        String[] s1 = s.split(" ");
        for (String s2 : s1) {
            System.out.println(s2);
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < s1.length / 2;i++){
            s = s1[i];
            s1[i] = s1[s1.length - i - 1];
            s1[s1.length - i - 1] = s;
        }
        for(int i = 0;i < s1.length;i++){
            if (i != s1.length-1)
            sb.append(s1[i]+" ");
            else sb.append(s1[i]);
        }
        char[] ans = sb.toString().toCharArray();
        for (int i = 0; i < ans.length; i++) {
            if(ans[i] == ' ') continue;
            if(ans[i] >= 'a' && ans[i] <= 'z') ans[i] = (char) (ans[i] - 'a' + 'A');
            else ans[i] = (char) (ans[i] - 'A' + 'a');
        }
        sb.delete(0,4);
        for (int i = 0; i < ans.length; i++) {
            sb.append(ans[i]);
        }
        System.out.println(sb.toString());
    }
}

import java.util.Scanner;

public class Test4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            int n = scanner.nextInt(),c1 = scanner.nextInt(),c2 = scanner.nextInt();
            String res = scanner.next();
            int c = Math.min(c1,c2),vis = 0;
            long ans = 0;
            char[] r = res.toCharArray();
            for (int i = 0;i < n;i++){
                if (r[i] == 'F'){
                    if (vis == 2){
                        ans += c;
                        vis = 0;
                    }
                    else{
                        vis++;
                    }
                }else{
                    vis = 0;
                }
            }
            System.out.println(ans);
        }
    }
}

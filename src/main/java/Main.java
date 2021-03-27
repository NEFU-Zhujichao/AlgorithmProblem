import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            int n = scanner.nextInt(),m = scanner.nextInt();
            int[] res = new int[50001];
            HashMap<Integer, int[]> map = new HashMap<>();
            for(int i = 0;i < n;i++){
                res[i] = scanner.nextInt();
                if (!map.containsKey(res[i])){
                    int[] stack = new int[2];
                    //Stack<Integer> stack = new Stack<>();
                    //stack.push(i+1);
                    stack[0] = i+1;
                    map.put(res[i],stack);
                } else {
                    int[] tmp = map.get(res[i]);
                    if (tmp[0] !=0 &&tmp[1] == 0){
                        tmp[1] = i+1;
                        map.put(res[i],tmp);
                    }else{
                        tmp[1] = i+1;
                        map.put(res[i],tmp);
                    }
                }
            }
            for(int i = 0;i < m;i++){
                int x = scanner.nextInt();
                if (!map.containsKey(x)) System.out.println(0);
                else{
                    int[] stack2 = map.get(x);
                    if (stack2[1] != 0){
                        System.out.print(stack2[0]+" ");
                        System.out.println(stack2[1]);
                    }else
                    {
                        System.out.print(stack2[0]+" ");
                        System.out.println(stack2[0]);
                    }
                }
            }
        }
    }

}

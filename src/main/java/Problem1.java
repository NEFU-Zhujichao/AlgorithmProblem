import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Problem1 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Integer> fav = new ArrayList<>();
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0;i < n;i++){
            fav.add(in.nextInt());
        }
        for (int i = 0;i < n;i++){
            if (map.containsKey(fav.get(i))){
                List<Integer> list = map.get(fav.get(i));
                list.add(i+1);
            }
            else {
                List<Integer> list = new ArrayList<>();
                list.add(i+1);
                map.put(fav.get(i),list);
            }
        }
        int k = in.nextInt();
        for (int i = 0;i < k;i++){
            int l = in.nextInt();
            int r = in.nextInt();
            int key = in.nextInt();
            int count = 0;
            List<Integer> list = map.get(key);
            for (Integer integer : list) {

            }
            System.out.println(count);
        }
    }
}

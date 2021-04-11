import java.util.ArrayList;
import java.util.List;
public class Perm {
    public static void main(String[] args) {
        int[] nums = {1,2,3};
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        perm(res,nums,tmp,0);
        res.forEach(System.out::print);
    }
    private static void perm(List<List<Integer>> res,int[] nums,List<Integer> tmp,int cur){
        if(cur == nums.length){
            tmp.forEach(System.out::print);
            System.out.println();
            res.add(new ArrayList<>(tmp));
            return;
        }
        for(int i = cur;i < nums.length;i++){
            tmp.add(nums[i]);
            swap(nums,i,cur);
            perm(res,nums,tmp,cur+1);
            tmp.remove(tmp.size()-1);
            swap(nums,i,cur);
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

}

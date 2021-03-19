import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 找出数组中第count小的数
 */
public class QuickSortTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(),count = scanner.nextInt();
        int[] nums = new int[n];
        for (int i = 0;i < n;i++) nums[i] = scanner.nextInt();
        quickSort(nums,0,n-1);
        Set<Integer> set = new HashSet<>();
        for(int i = 0;i < n;i++){
            set.add(nums[i]);
            if (set.size() == count){
                System.out.println(nums[i]);
                break;
            }
        }
    }
    private static void quickSort(int[] nums,int left,int right){
        if(left >= right) return;
        int i = left,j = right,mid = nums[left];
        while(i < j){
            while(i < j && nums[j] > mid) j--;
            while(i < j && nums[i] <= mid) i++;
            swap(nums,i,j);
        }
        swap(nums,i,left);
        quickSort(nums,left,i-1);
        quickSort(nums,i+1,right);
    }

    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

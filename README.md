# 算法题

### [找出数组中第K大的数](https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf?tpId=117&tqId=37791&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：快排，n-K即为第K大的数(正向排序)
```java
import java.util.*;

public class Solution {
    public int findKth(int[] a, int n, int K) {
        // write code here
        quickSort(a,0,n-1);
        
        return a[K-1];
    }
    private void quickSort(int[] a,int left,int right){
        if(left >= right)return;
        int mid = a[left],i = left,j = right;
        while(i < j){
            while(i < j && a[j] <= mid)j--;
            while(i < j && a[i] > mid)i++;
            swap(a,i,j);
        }
        //下标，最左面数的下标！！！left！！！
        swap(a,i,left);
        quickSort(a,left,i-1);
        quickSort(a,i+1,right);
    }
    private void swap(int[] arr,int i,int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
```
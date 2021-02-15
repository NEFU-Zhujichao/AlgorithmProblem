# 算法题(牛客网高频TOP200)

### [NC88寻找第K大](https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf?tpId=117&tqId=37791&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
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
### [NC78反转链表](https://www.nowcoder.com/practice/75e878df47f24fdc9dc3e400ec6058ca?tpId=117&tqId=37777&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：p -> head.next,head.next -> pre,pre -> head, head -> p;
```java
public class Solution {
    public ListNode ReverseList(ListNode head) {
        ListNode pre = null,p = null;
        while(head != null){
            p = head.next;
            head.next = pre;
            pre = head;
            head = p;
        }
        return pre;
    }
}
```
### [NC105二分查找](https://www.nowcoder.com/practice/7bc4a1c7c371425d9faa9d1b511fe193?tpId=117&tqId=37829&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：二分查找
```java
public class Solution {
    public int upper_bound_ (int n, int v, int[] a) {
        // write code here
        if(a[n-1] < v) return n+1;
        int left = 0,right = n -1;
        while(left < right){
            int mid = left + (right - left) / 2;
            if (a[mid] >= v)right = mid;
            else left = mid + 1;
        }
        return left + 1;
    }
}
```
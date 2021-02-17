# 算法题(牛客网高频TOP200)

### [NC88寻找第K大](https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf?tpId=117&tqId=37791&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)

- 思路：快排，n-K即为第K大的数(正向排序)

```java
import java.util.*;

public class Solution {
    public int findKth(int[] a, int n, int K) {
        // write code here
        quickSort(a, 0, n - 1);

        return a[n - K];
    }

    private void quickSort(int[] a, int left, int right) {
        if (left >= right) return;
        int mid = a[left], i = left, j = right;
        while (i < j) {
            while (i < j && a[j] > mid) j--;
            while (i < j && a[i] <= mid) i++;
            swap(a, i, j);
        }
        //下标，最左面数的下标！！！left！！！
        swap(a, i, left);
        quickSort(a, left, i - 1);
        quickSort(a, i + 1, right);
    }

    private void swap(int[] arr, int i, int j) {
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
        ListNode pre = null, p = null;
        while (head != null) {
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
    public int upper_bound_(int n, int v, int[] a) {
        // write code here
        if (a[n - 1] < v) return n + 1;
        int left = 0, right = n - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (a[mid] >= v) right = mid;
            else left = mid + 1;
        }
        return left + 1;
    }
}
```

### [NC15=求二叉树的层序遍历](https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3?tpId=117&tqId=37723&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)

- 思路：大根放入队中，判断是否存在左右子树，存在则放入队中！**每次遍历的size为队的size**

```java
public class Solution {
    public ArrayList<ArrayList<Integer>> levelOrder(TreeNode root) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
        if (root == null) return list;
        ArrayDeque<TreeNode> queue = new ArrayDeque<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            ArrayList<Integer> ans2 = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                ans2.add(node.val);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            list.add(ans2);
        }
        return list;
    }
}
```
### [NC4判断链表中是否有环](https://www.nowcoder.com/practice/650474f313294468a4ded3ce0f7898b9?tpId=117&tqId=37714&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：定义两个快慢指针，如果有环这两个指针一定会相遇。
```java
public class Solution {
    public boolean hasCycle(ListNode head) {
        ListNode p = head,q = head;
        while(p != null && q.next != null && q.next.next != null){
            p = p.next;
            q = q.next.next;
            if(p == q){
                return true;
            }
        }
        return false;
    }
}
```
### [NC32求平方根](https://www.nowcoder.com/practice/09fbfb16140b40499951f55113f2166c?tpId=117&tqId=37734&rp=1&ru=%2Fta%2Fjob-code-high&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：二分查找 注意：Long转int 防止溢出
```java
public class Solution {
    public int sqrt (int x) {
        if (x <= 0) return 0;
        int left = 1,right = x;
        while(left < right){
            Long mid = left + (right - left) / 2L;
            if (mid * mid <= x && (mid + 1) * (mid + 1) > x)return Integer.parseInt(String.valueOf(mid));
            if(mid * mid > x) right = Integer.parseInt(String.valueOf(mid));
            else left = Integer.parseInt(String.valueOf(mid)) + 1;
        }
        return left;
    }
}
```
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
            while (i < j && a[j] > mid) j--;  //while (i < j && a[j] < mid) j--;   逆向排序
            while (i < j && a[i] <= mid) i++; //while (i < j && a[i] >= mid) i++;
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
- 链表的尾插法
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
### [NC50链表中的节点每k个一组翻转](https://www.nowcoder.com/practice/b49c3dc907814e9bbfa8437c251b028e?tpId=117&tqId=37746&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high%2Fquestion-ranking&tab=answerKey)
- 思路：![链表头插法](https://uploadfiles.nowcoder.com/images/20200929/249586134_1601375359555_EE54FEDE4D1EA710514C806FC06BEB38)
```java
public class Solution {
    public ListNode reverseKGroup (ListNode head, int k) {
        // write code here
        if(head == null || head.next == null || k == 1) return head;
        ListNode  p = new ListNode(0);
        p.next = head;
        ListNode cur = head,pre = p,temp = null;
        int length = 0;
        while(head != null){
            length++;
            head = head.next;
        }
        for(int i = 0;i < length / k;i++){
            for(int j = 0;j < k-1;j++){
                temp = cur.next;
                cur.next = temp.next;
                temp.next = pre.next;
                pre.next = temp;
            }
            pre = cur;
            cur = cur.next;
        }
        return p.next;
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
### [NC7股票(一次交易)](https://www.nowcoder.com/practice/64b4262d4e6d4f6181cd45446a5821ec?tpId=188&tqId=37524&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey)
- 思路：因为一定需要先买入再卖出，因此每到一步都可以获取到这一步为止的最佳状态，不断这个最佳状态即可。
```java
public class Solution {
    public int maxProfit (int[] prices) {
        // write code here
        int max = 0,minVal = prices[0];
        for(int i = 1;i < prices.length;i++){
            if(prices[i] < minVal)minVal = prices[i];
                max = Math.max(max,prices[i] - minVal);
        }
        return max;
    }
}
```
### [NC 合并区间](https://www.nowcoder.com/practice/69f4e5b7ad284a478777cb2a17fb5e6a?tpId=117&tqId=691&tab=answerKey)
- 思路：先按左区间从小到大排序，若左区间相等则按有区间从大到小排序，然后从第二个开始遍历。
- 排序方法：lambda表达式自定义排序。
```java
(a,b) -> {
  if(a.start != b.start) return a.start - b.start;
  else return b.end - a.end;
  });
```
```java
import java.util.*;
/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
public class Solution {
    public ArrayList<Interval> merge(ArrayList<Interval> intervals) {
        if (intervals == null || intervals.size() < 2){
            return intervals;
        }
        ArrayList<Interval> res = new ArrayList<Interval>();
        Collections.sort(intervals, (a,b) -> {
            if(a.start != b.start) return a.start - b.start;
            else return b.end - a.end;
        });
        res.add(intervals.get(0));
        for(int i = 1;i < intervals.size();i++){
            if(intervals.get(i).start == res.get(res.size()-1).start) continue;
            if(res.get(res.size()-1).end < intervals.get(i).start){
                res.add(intervals.get(i));
            }else res.get(res.size()-1).end = Math.max(intervals.get(i).end,res.get(res.size()-1).end);
        }
        return res;
    }
}
```
### [NC128容器盛水问题](https://www.nowcoder.com/practice/31c1aed01b394f0b8b7734de0324e00f?tpId=188&tqId=37528&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey)
- 思路：可以双指针写。但是这里我采取的是评论区令一个老哥的思路。O(2n)的时间复杂度，也还可以。从左到右扫描出第一个比a[left]大的元素，这样中途记录tmp中间值，如果有比a[left]大的值，则这个点就是一个容器的边，sum += tmp,tmp清0继续扫描有没有比
这个点还大的边。扫描完成后，从右开始向左扫描，扫描出比a[right]大的元素，然后sum+=tmp，过程相同。
  - 注意：两次扫描只能有一次 <= 因为=可能重复算两次。
```java
import java.util.*;
public class Solution {
    public long maxWater (int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int low = 0;
        long sum = 0;
        long tmp = 0;
        //从左向右
        for (int i = 0; i < arr.length; i++) {
            if (arr[low] > arr[i]) {
                tmp = tmp + arr[low] - arr[i];
            }
            if (arr[low] <= arr[i]) {
                sum = sum + tmp;
                tmp = 0;
                low = i;
            }
        }
        low = arr.length-1;
        tmp = 0;
        //从右向左
        for (int j = arr.length-1; j >= 0; j--) {
            if (arr[low] > arr[j]) {
                tmp = tmp + arr[low] - arr[j];
            }
            //注意这里不能再 <=，否则可能会重复计算等于的情况
            if (arr[low] < arr[j]) {
                sum = sum + tmp;
                tmp = 0;
                low = j;
            }
        }
        return sum;
    }
}
```
### [NC12重建二叉树](https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6?tpId=188&tqId=37520&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey)
- 思路：一直先序和中序重建二叉树。 Arrays.CopyOfRange() 左闭右开
- 在中序中找到先序的第一个元素，这样中序左面的就是左子树，中序右面的就是右子树。不断递归寻找。
```java
import java.util.*;
public class Solution {
    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        if (pre.length == 0 || in.length == 0) return null;
        TreeNode root = new TreeNode(pre[0]);
        for(int i = 0;i < in.length;i++){
            if(in[i] == pre[0]){
                root.left = reConstructBinaryTree(Arrays.copyOfRange(pre,1,i+1),Arrays.copyOfRange(in,0,i));
                root.right = reConstructBinaryTree(Arrays.copyOfRange(pre,i+1,pre.length),Arrays.copyOfRange(in,i+1,in.length));
                }
        }
        return root;
    }
}
```
### [NC14 二叉树的之字形层序遍历](https://www.nowcoder.com/practice/47e1687126fa461e8a3aff8632aa5559?tpId=188&tqId=38035&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey)
- 思路：奇数时候正常顺序，偶数时候reverse数组。 Collections.reverse(ans)
```java
import java.util.*;
public class Solution {
    /**
     * 
     * @param root TreeNode类 
     * @return int整型ArrayList<ArrayList<>>
     */
    public ArrayList<ArrayList<Integer>> zigzagLevelOrder (TreeNode root) {
        // write code here
        if(root == null) return new ArrayList();
        Queue<TreeNode> queue = new LinkedList<>();
        ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
        if(root.left == null && root.right == null){
            ArrayList<Integer> ans = new ArrayList<>();
            ans.add(root.val);
            res.add(ans);
            return res;
        }
        queue.offer(root);
        int count = 1;
        while(!queue.isEmpty()){
            ArrayList<Integer> ans = new ArrayList<>();
            int size = queue.size();
            for(int i = 0;i < size;i++){
                TreeNode tmp = queue.poll();
                ans.add(tmp.val);
                if(tmp.left != null) queue.add(tmp.left);
                if(tmp.right != null) queue.add(tmp.right);
            }
            if(count % 2 == 1){
                res.add(ans);
                count++;
            }else{
                Collections.reverse(ans);
                res.add(ans);
                count++;
            }
        }
        return res;
    }
}
```
### [NC134 股票无限次交易](https://www.nowcoder.com/practice/9e5e3c2603064829b0a0bbfca10594e9?tpId=188&tqId=38042&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey) 
- 思路：只要是两天之间有上升就加上。
```java
import java.util.*;
public class Solution {
    public int maxProfit (int[] prices) {
        if(prices.length <= 1) return 0;
        int min = prices[0],res = 0;
        for(int i = 1;i < prices.length;i++){
            if(prices[i] > min) res += (prices[i]-min);
            min = prices[i];
        }
        return res;
    }
}
```
### [NC26 括号生成](https://www.nowcoder.com/practice/c9addb265cdf4cdd92c092c655d164ca?tpId=188&tqId=38081&rp=1&ru=%2Factivity%2Foj&qru=%2Fta%2Fjob-code-high-week%2Fquestion-ranking&tab=answerKey) 
- 思路：递归先放左 '(' 一直放到不能放，处理全放左括号完事开始处理右括号。
```java
import java.util.*;
public class Solution {
    private ArrayList<String> res;
    public ArrayList<String> generateParenthesis (int n) {
        res = new ArrayList<>();
        recursion(n,n,"");
        return res;
    }
    private void recursion(int left,int right,String tmp){
        System.out.println("tmp="+tmp+" ");
        if(left == 0 && right ==0){
            res.add(tmp);
            //System.out.println(tmp);
        }else {
            if(left > 0) recursion(left-1,right,tmp+"(");
            if(right > left) recursion(left,right-1,tmp+")");
        }
    }
}
/**
 tmp=( 
 tmp=(( 
 tmp=((( 
 tmp=((() 
 tmp=((()) 
 tmp=((())) 
 tmp=(() 
 tmp=(()( 
 tmp=(()() 
 tmp=(()()) 
 tmp=(()) 
 tmp=(())( 
 tmp=(())() 
 tmp=() 
 tmp=()( 
 tmp=()(( 
 tmp=()(() 
 tmp=()(()) 
 tmp=()() 
 tmp=()()( 
 tmp=()()() 
 */
```

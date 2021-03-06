# 算法题(力扣高频TOP100)
<< 左移1位相当于乘2 相当于二进制右面补0  
   右移1位相当于除2 相当于二进制左面补0
### [3. 无重复字符的最长子串](https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/)
- 思路：双指针，用map存储字符是否存在，不断更新最大值。
```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        if(len <= 1) return len;
        HashMap<Character,Integer> map = new HashMap<>();
        int max = 0,end = 0,i = 0,j = 0;
        while (i <= j && j < len){
            if(!map.containsKey(s.charAt(j))){
                map.put(s.charAt(j),j);
                if (max < map.size()){
                    max = map.size();
                    end = j;
                }
                j++;
            }else {
                while (s.charAt(i) != s.charAt(j)){
                    map.remove(s.charAt(i));
                    i++;
                }
                map.remove(s.charAt(i));
                i++;
            }
        }
        // end记录最大值的最大位置j，方便获取最长字串。
        //System.out.println(s.substring(end-max+1,end+1));
        return max;
    }
}
```
### [11. 盛最多水的容器](https://leetcode-cn.com/problems/container-with-most-water/)
- 思路：双指针。
![](https://pic.leetcode-cn.com/a2a97349454ee3657a8a3d9db0399921894c7f581c2e9540d0e2c3df122fec95-Picture1.png)  
![](https://pic.leetcode-cn.com/239c53727f392398829ae835df2d4b8dce374954b2aac2e3745b3ff9c32d7a9f-Picture2.png)  
```java
class Solution {
    public int maxArea(int[] height) {
        if (height == null) return 0;
        int i = 0, j = height.length - 1, ans = 0, tmp = 0;
        while (i < j) {
            tmp = Math.min(height[i], height[j]) * (j - i);
            ans = Math.max(ans, tmp);
            if (height[i] <= height[j]) i++;
            else j--;
        }
        return ans;
    }
}
```
### [55. 跳跃游戏](https://leetcode-cn.com/problems/jump-game/)
- 思路：跳就完事了。每次更新能跳到的最远距离。若是此时i大于max则无法到达，若max >= nums.length() - 1则能到达。
```java
class Solution {
    public boolean canJump(int[] nums) {
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > max) return false;
            if (max >= nums.length - 1) return true;
            max = Math.max(max, i + nums[i]);
        }
        return false;
    }
}
```
### [剑指Offer16 数值的整数次方](https://leetcode-cn.com/problems/shu-zhi-de-zheng-shu-ci-fang-lcof/)
- 思路：[快速幂解析（二分法角度）](https://leetcode-cn.com/problems/shu-zhi-de-zheng-shu-ci-fang-lcof/solution/mian-shi-ti-16-shu-zhi-de-zheng-shu-ci-fang-kuai-s/)
```java
class Solution {
    public double myPow(double x, int n) {
        double ans = 1;
        long tmp = n;
        if (n < 0) {
            x = 1 / x;
            tmp = -tmp;
        }
        while (tmp > 0) {
            if ((tmp & 1) == 1) ans *= x;
            x *= x;
            tmp >>= 1;
        }
        return ans;
    }
}
```
### [剑指Offer26 树的子结构](https://leetcode-cn.com/problems/shu-de-zi-jie-gou-lcof/)
- 思路：[递归查询左子树和右子树](https://leetcode-cn.com/problems/shu-de-zi-jie-gou-lcof/solution/mian-shi-ti-26-shu-de-zi-jie-gou-xian-xu-bian-li-p/)
```java
class Solution {
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null) return false;
        if (A.val == B.val && (help(A.left, B.left) && help(A.right, B.right))) return true;
        return isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }
    private boolean help(TreeNode A, TreeNode B) {
        if (B == null) return true;
        if (A == null) return false;
        if (A.val == B.val) return help(A.left, B.left) && help(A.right, B.right);
        else return false;
    }
}
```
### [5. 最长回文子串](https://leetcode-cn.com/problems/longest-palindromic-substring/)
- 思路：从每个索引向两边扩展回文串，找到最长的。 时间复杂度：O(n^2)。
- [多解题思路](https://leetcode-cn.com/problems/longest-palindromic-substring/solution/zhong-xin-kuo-san-dong-tai-gui-hua-by-liweiwei1419/)
```java
class Solution {
    public String longestPalindrome(String s) {
        if (s.length() < 2) return s;
        String maxLen = s.substring(0, 1);
        for (int i = 0; i < s.length(); i++) {
            String oodLen = get(i, i, s);
            String evenLen = get(i, i + 1, s);
            String tmpLen = oodLen.length() > evenLen.length() ? oodLen : evenLen;
            if (tmpLen.length() > maxLen.length()) {
                maxLen = tmpLen;
            }
        }
        // maxLen.length() 最大长度
        return maxLen;
    }
    private String get(int left, int right, String A) {
        while (left >= 0 && right < A.length()) {
            if (A.charAt(left) == A.charAt(right)) {
                left--;
                right++;
            } else break;
        }
        return A.substring(left + 1, right);
    }
}
```
### [从无重复元素数组中选出n个数之和为k](https://leetcode-cn.com/problems/combination-sum/)
- 思路：回溯法
```java
class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        int n = candidates.length;
        reback(res,candidates,n,0,0,target,new ArrayList<Integer>());
        return res;
    }
    private void reback(List<List<Integer>> res,int[] candidates,int n,int sum,int cur,int target,ArrayList<Integer> tmp){
        if(sum == target){
            res.add(new ArrayList<Integer>(tmp));
            return;
        }
        if(sum < target){
            for(int i = cur;i < n;i++){
                /* 剪枝
                    1. 排序，遇到 rs > target 时就break
                    2. 不排序，遇到 rs > target 时就continue
                */
                int rs = sum + candidates[i];
                if(rs <= target){
                tmp.add(candidates[i]);
                reback(res,candidates,n,rs,i,target,tmp);
                tmp.remove(tmp.size()-1);
                } else break;
            }
        }
    }
}
```
### [54. 螺旋矩阵](https://leetcode-cn.com/problems/spiral-matrix/)
- [思路：](https://leetcode-cn.com/problems/spiral-matrix/solution/cxiang-xi-ti-jie-by-youlookdeliciousc-3/)
1. 定义上下左右边界值
2. 其次向右移动到最右，此时第一行因为已经使用过了，可以将其从图中删去，体现在代码中就是重新定义上边界
3. 判断若重新定义后，上下边界交错，表明螺旋矩阵遍历结束，跳出循环，返回答案
4. 若上下边界不交错，则遍历还未结束，接着向下向左向上移动，操作过程与第一，二步同理
5. 不断循环以上步骤，直到某两条边界交错，跳出循环，返回答案
```java
import java.util.Collections;
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        if (matrix == null) return res;
        int l = 0, r = matrix[0].length - 1, u = 0, d = matrix.length - 1;
        while (true) {
            for (int i = l; i <= r; i++) res.add(matrix[u][i]);
            if (++u > d) return res;
            for (int i = u; i <= d; i++) res.add(matrix[i][r]);
            if (--r < l) return res;
            for (int i = r; i >= l; i--) res.add(matrix[d][i]);
            if (--d < u) return res;
            for (int i = d; i >= u; i--) res.add(matrix[i][l]);
            if (++l > r) return res;
        }
    }
}
```
### [NC127 最长公共子串](https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac?tpId=117&tab=answerKey)
- 思路：经典dp flag数组记录该点由谁得到的
- 子串是连续的，所以当两个字母不相等时，dp[i][j] = 0, 如果是子序列， dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);
```java
import java.util.*;
public class Solution {
    public String LCS(String str1, String str2) {
        if (str1 == null || str2 == null || str1.equals("") || str2.equals("")) return "-1";
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        int maxLength = 0;   //记录最长公共子串长度
        int end = 0;          //记录最长子串最后一个字符的下标
        int m = str1.length();
        int n = str2.length();
        //初始化表格边界
        for (int i = 0; i <= m; ++i) dp[i][0] = 0;
        for (int j = 0; j <= n; ++j) dp[0][j] = 0;
        //循环"填表"
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = 0;
                }
                //记录最长子串的长度和当前下标
                if (dp[i][j] >= maxLength) {
                    maxLength = dp[i][j];
                    // 如果是i-1 则截s1 如果是j-1 则截s2
                    end = i - 1;
                }
            }
        }
        if (maxLength == 0) return "-1";
        return str1.substring(end - maxLength + 1, end + 1);
    }
}
```
### [1143. 最长公共子序列](https://leetcode-cn.com/problems/longest-common-subsequence/submissions/)
- 思路：经典dp flag数组记录该点由谁得到的
```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length(), max = 0;
        int[][] dp = new int[m + 1][n + 1];
        int[][] flag = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            char t1 = text1.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char t2 = text2.charAt(j - 1);
                if (t1 == t2) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    flag[i][j] = 1;
                } else {
                    flag[i][j] = dp[i - 1][j] >= dp[i][j - 1] ? 2 : 3;
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
                max = Math.max(max, dp[i][j]);
            }
        }
        int i = m, j = n;
        StringBuilder sb = new StringBuilder();
        while (max > 0) {
            char t1 = text1.charAt(i - 1);
            char t2 = text2.charAt(j - 1);
            if (t1 == t2) {
                sb.insert(0, t1);
                --i;
                --j;
                --max;
            } else if (flag[i][j] == 2) --i;
            else --j;
        }
        System.out.println(sb.toString());
        return max;
    }
}
```
### [92. 反转链表 II](https://leetcode-cn.com/problems/reverse-linked-list-ii/)
- 思路：找出反转链表之前的一个节点和之后的一个节点。断成三断链表，最后拼接。 
![](https://pic.leetcode-cn.com/1615105168-ZQRZew-image.png) 
```java
class Solution {
    public ListNode reverseBetween(ListNode head, int left, int right) {
        // 因为头节点有可能发生变化，使用虚拟头节点可以避免复杂的分类讨论
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;
        ListNode pre = dummyNode;
        // 第 1 步：从虚拟头节点走 left - 1 步，来到 left 节点的前一个节点
        for (int i = 0; i < left - 1; i++) {
            pre = pre.next;
        }
        // 第 2 步：从 pre 再走 right - left + 1 步，来到 right 节点
        ListNode rightNode = pre;
        for (int i = 0; i < right - left + 1; i++) {
            rightNode = rightNode.next;
        }
        // 第 3 步：切断出一个子链表
        ListNode leftNode = pre.next;
        ListNode curr = rightNode.next;
        // 注意：切断链接
        pre.next = null;
        rightNode.next = null;
        // 第 4 步：反转链表的子区间
        reverseLinkedList(leftNode);
        // 第 5 步：接回到原来的链表中
        pre.next = rightNode;
        leftNode.next = curr;
        return dummyNode.next;
    }
    private void reverseLinkedList(ListNode head) {
        ListNode pre = null,cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
    }
}
```
### [17. 电话号码的字母组合](https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/)
- 思路：回溯法
```java
class Solution {
    public List<String> letterCombinations(String digits) {
        ArrayList<String> res = new ArrayList<>();
        if(digits.length() == 0) return res;
        HashMap<Character,String> map = new HashMap<>(){{
            put('2', "abc");
            put('3', "def");
            put('4', "ghi");
            put('5', "jkl");
            put('6', "mno");
            put('7', "pqrs");
            put('8', "tuv");
            put('9', "wxyz");
        }};
        backtrace(res,map,digits,0,new StringBuilder());
        return res;
    }
    private void backtrace(ArrayList<String> res,HashMap<Character,String> map,String digits,int index,StringBuilder sb){
        if(index == digits.length()){
            res.add(sb.toString());
            return;
        }
        String tmp = map.get(digits.charAt(index));
        for(int i = 0;i < tmp.length();i++){
            sb.append(tmp.charAt(i));
            backtrace(res,map,digits,index+1,sb);
            sb.deleteCharAt(index);
        }
    }
}
```
### [15. 三数之和](https://leetcode-cn.com/problems/3sum/)
- 思路：两数之和变形  a + b = target -> a + b + c = 0 == a + b = -c
```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        if(nums == null) return res;
        for(int i = 0;i < nums.length;i++){
            // 去除重复情况
            if(i > 0 && nums[i] == nums[i-1]) continue;
            int l = i + 1,r = nums.length - 1,target = 0 - nums[i];
            while(l < r){
                if(nums[l] + nums[r] == target){
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(nums[l]);
                    list.add(nums[r]);
                    list.add(nums[i]);
                    res.add(list);
                    l++;r--;
                    // 去除重复情况
                    while(l < r && (nums[l] == nums[l-1])) l++;
                    while(l < r && (nums[r] == nums[r+1])) r--;
                }else if(nums[l] + nums[r] < target) l++;
                else r--;
            }
        }
        return res;
    }
}
```
### [46. 全排列](https://leetcode-cn.com/problems/permutations/)
- 回溯法：
```java
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        perm(res,nums,tmp,0);
        return res;
    }
    private static void perm(List<List<Integer>> res,int[] nums,List<Integer> tmp,int cur){
        if(cur == nums.length){
            // 这里如果放的是tmp，那么放的是tmp的引用，到最后的时候tmp一定会全部删除变成空集合
            // 所以一定要new ArrayList<>(tmp)
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
```
### [98. 验证二叉搜索树](https://leetcode-cn.com/problems/validate-binary-search-tree/)
- 思路：中序遍历树，保存中序遍历结果，若是升序数组，则为一棵BST
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        if(root == null) return false;
        ArrayList<Integer> res = new ArrayList<>();
        inorder(root,res);
        long tmp = -Long.MAX_VALUE;
        boolean flag = true;
        for(int i = 0;i < res.size();i++){
            if(res.get(i) > tmp) tmp = res.get(i);
            else {
                flag = false;
                break;
            }
        }
        return flag;
    }
    private void inorder(TreeNode root,ArrayList<Integer> res){
        if(root == null) return;
        inorder(root.left,res);
        res.add(root.val);
        inorder(root.right,res);
    }
}
```
- 思路：递归判断左右子树是否满足每个树节点的值在(min,max)之间。
```java
class Solution {
    public boolean isValidBST(TreeNode root) {
        if(root == null) return false;
        return isValidBST(root,Long.MIN_VALUE,Long.MAX_VALUE);
    }
    public boolean isValidBST(TreeNode root,long min,long max){
        if(root == null) return true;
        if(root.val <= min || root.val >= max) return false;
        return isValidBST(root.left,min,root.val) && isValidBST(root.right,root.val,max);
    }
}
```
### [739. 每日温度](https://leetcode-cn.com/problems/daily-temperatures/)
- 思路：单调减栈 栈里存放数组下标，每遇到大于栈中T[st.peek()]时，弹出栈，记录i-st.pop() 为弹出栈的res值。
```java
class Solution {
    public int[] dailyTemperatures(int[] T) {
        int n = T.length;
        int[] res = new int[n];
        Stack<Integer> st = new Stack<>();
        for(int i = 0;i < n;i++){
            while(!st.isEmpty() && T[i] > T[st.peek()]){
                int tmp = st.pop();
                res[tmp] = i - tmp;
            }
            st.push(i);
        }
        return res;
    }
}
```
### [148. 排序链表](https://leetcode-cn.com/problems/sort-list/)
- 解答一：归并排序（递归法）
- 归并排序 时间复杂度 O(nlongn) 空间复杂度：递归调用函数将带来O(logn)的空间复杂度
![](https://pic.leetcode-cn.com/8c47e58b6247676f3ef14e617a4686bc258cc573e36fcf67c1b0712fa7ed1699-Picture2.png)
- 解答二：归并排序（迭代法）
- 
```java
class Solution {
     public ListNode sortList(ListNode head) {
        // 1、递归结束条件
        if (head == null || head.next == null) {
            return head;
        }
        // 2、找到链表中间节点并断开链表 & 递归下探
        ListNode midNode = middleNode(head);
        ListNode rightHead = midNode.next;
        midNode.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(rightHead);
        // 3、当前层业务操作（合并有序链表）
        return mergeTwoLists(left, right);
    }
    //  找到链表中间节点（876. 链表的中间结点）
    private ListNode middleNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    // 合并两个有序链表（21. 合并两个有序链表）
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode sentry = new ListNode(-1);
        ListNode curr = sentry;
        while(l1 != null && l2 != null) {
            if(l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        curr.next = l1 != null ? l1 : l2;
        return sentry.next;
    }
}
```
### [169. 多数元素](https://leetcode-cn.com/problems/majority-element/)
- 进阶 [229. 求众数 II](https://leetcode-cn.com/problems/majority-element-ii/)
- 思路： O(n) 时间复杂度 O(1) 空间复杂度 所以不能用map存值 应用**摩尔投票法**  
摩尔投票法思路：  
候选人(cand_num)初始化为nums[0]，票数count初始化为1。   
当遇到与cand_num相同的数，则票数count = count + 1，否则票数count = count - 1。  
当票数count为0时，更换候选人，并将票数count重置为1。  
遍历完数组后，cand_num即为最终答案。  
为何这行得通呢？  
投票法是遇到相同的则票数 + 1，遇到不同的则票数 - 1。  
且“多数元素”的个数> ⌊ n/2 ⌋，其余元素的个数总和<= ⌊ n/2 ⌋。  
因此“多数元素”的个数 - 其余元素的个数总和 的结果 肯定 >= 1。  
这就相当于每个“多数元素”和其他元素 两两相互抵消，抵消到最后肯定还剩余至少1个“多数元素”。  
无论数组是1 2 1 2 1，亦或是1 2 2 1 1，总能得到正确的候选人。
```java
class Solution {
    public int majorityElement(int[] nums) {
        int res = nums[0];
        int count = 1;
        // 摩尔投票
        for(int i = 1;i < nums.length;i++){
            if(count == 0) res = nums[i];
            if(res == nums[i]) count++;
            else count--;
        }
        return res;
    }
}
```
### [229. 求众数 II](https://leetcode-cn.com/problems/majority-element-ii/)
- 思路：摩尔投票 维护两个数字 然后最后统计两个数字是否都大于 n/3 大于则返回，反之不返回。
```java
class Solution {
    public List<Integer> majorityElement(int[] nums) {
        List<Integer> list = new ArrayList<>();
        int n = nums.length;
        if(n < 2){
            list.add(nums[0]);
            return list;
        }
        int res1 = 0,res2 = 0;
        int cnt1 = 0,cnt2 = 0;
        for(int i = 0;i < n;i++){
            if(nums[i] == res1) cnt1++;
            else if(nums[i] == res2) cnt2++;
            else if(cnt1 == 0){
                cnt1++;
                res1 = nums[i];
            }else if(cnt2 == 0){
                cnt2++;
                res2 = nums[i];
            }else{
                cnt1--;
                cnt2--;
            }
        }
        cnt1 = 0;cnt2 = 0;
        for(int num : nums){
            if(num == res1) cnt1++;
            else if(num == res2) cnt2++;
        }
        if(cnt1 > n/3)
        list.add(res1);
        if(cnt2 > n/3)
        list.add(res2);
        return list;
    }
}
```
### [198. 打家劫舍](https://leetcode-cn.com/problems/house-robber/)
- 思路： 简单dp (记录自己做出简单dp的快乐！！！)
  - 只有一个房子时 return nums[0];
  - 两个房子时 return max(nums[0],nums[1]);
  - 多个房子时： dp[i] = max(dp[i-1],dp[i-2]+nums[i]); 此处的房子可以由前一座房子的最大价值或者前两个房子的最大价值加上此房子的值而来，所以取两者的最大值。
```java
class Solution {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        }
        return dp[n - 1];
    }
}
```
### [210. 课程表 II](https://leetcode-cn.com/problems/course-schedule-ii/)
- 思路：拓扑排序 + bfs
- 用List<List<Integer>>存有向边，indeg数组存每个点的入度。假设 [[1],[3],[3],[]] 代表的是 0 -> 1 1 -> 3 2 -> 3  0的入度为0 1的入度为1 2的入度为0 3的入度为2。bfs初始把入度为0的节点入队开始bfs，每次队首取出的点，遍历以这个点为起点的点，遍历的每个点都相应的入度-1。用res记录队中的弹出的值，如果弹出的值的长度等于总课程的数量，那么认为能够全部修完。否则就是出现了环，无法完成排课。
```java
class Solution {
    List<List<Integer>> edges;
    int[] indeg;
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 构建点
        edges = new ArrayList<List<Integer>>();
        for (int i = 0; i < numCourses; ++i) {
            edges.add(new ArrayList<Integer>());
        }
        indeg = new int[numCourses];
        // 构建有向边，从 index -> x
        for (int[] info : prerequisites) {
            edges.get(info[1]).add(info[0]);
            // 存入度
            ++indeg[info[0]];
        }
        Queue<Integer> queue = new LinkedList<Integer>();
        // 把入度为0的节点入队
        for (int i = 0; i < numCourses; ++i) {
            if (indeg[i] == 0) {
                queue.offer(i);
            }
        }
        // 开始bfs
        int[] res = new int[numCourses];
        int cnt = -1;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            res[++cnt] = u;
            for (int v: edges.get(u)) {
                --indeg[v];
                if (indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        if(cnt != numCourses-1) 
        return new int[0];
        return res;
    }
}
```
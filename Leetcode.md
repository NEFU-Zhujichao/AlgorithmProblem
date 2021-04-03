# 算法题(力扣高频TOP100)

### [11. 盛最多水的容器](https://leetcode-cn.com/problems/container-with-most-water/)
- 思路：双指针。
![](https://pic.leetcode-cn.com/a2a97349454ee3657a8a3d9db0399921894c7f581c2e9540d0e2c3df122fec95-Picture1.png)
![](https://pic.leetcode-cn.com/239c53727f392398829ae835df2d4b8dce374954b2aac2e3745b3ff9c32d7a9f-Picture2.png)
```java
class Solution {
    public int maxArea(int[] height) {
        if(height == null) return 0;
        int i = 0,j = height.length-1,ans = 0,tmp = 0;
        while(i < j){
            tmp = Math.min(height[i],height[j]) * (j - i);
            ans = Math.max(ans,tmp);
            if(height[i] <= height[j]) i++;
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
        for(int i = 0;i < nums.length;i++){
            if(i > max) return false;
            if(max >= nums.length - 1) return true;
            max = Math.max(max,i + nums[i]);
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
        if(n < 0){
            x  = 1 / x;
            tmp = -tmp;
        }
        while(tmp > 0){
            if((tmp & 1) == 1) ans *= x;
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
        if(A == null || B == null) return false;
        if(A.val == B.val && (help(A.left,B.left)&&help(A.right,B.right))) return true;
        return isSubStructure(A.left,B) || isSubStructure(A.right,B);
    }
    private boolean help(TreeNode A,TreeNode B){
        if(B == null) return true;
        if(A == null) return false;
        if(A.val == B.val) return help(A.left,B.left) && help(A.right,B.right);
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
        if(s.length() < 2) return s;
        String maxLen = s.substring(0,1);
        for(int i = 0;i < s.length();i++){
            String oodLen = get(i,i,s);
            String evenLen = get(i,i+1,s);
            String tmpLen = oodLen.length() > evenLen.length() ? oodLen : evenLen;
            if(tmpLen.length() > maxLen.length()){
                maxLen = tmpLen;
            }
        }
        // maxLen.length() 最大长度
        return maxLen;
    }
    private String get(int left,int right,String A){
        while(left >= 0 && right < A.length()){
            if(A.charAt(left) == A.charAt(right)){
                left--;right++;
            }else break;
        }
        return A.substring(left+1,right);
    }
}
```
### [从数组中选出n个数之和为k](https://blog.csdn.net/codeTZ/article/details/51125109?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.baidujs&dist_request_id=1328740.51642.16170958744643373&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.baidujs)
- 思路：回溯法
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
### [1143. 最长公共子序列](https://leetcode-cn.com/problems/longest-common-subsequence/submissions/)
- 思路：经典dp  flag数组记录该点由谁得到的
```java
class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(),n = text2.length(),max = 0;
        int[][] dp = new int[m+1][n+1];
        int[][] flag = new int[m+1][n+1];
        for(int i = 1;i <= m;i++){
            char t1 = text1.charAt(i-1);
            for(int j = 1;j <= n;j++){
                char t2 = text2.charAt(j-1);
                if(t1 == t2){
                    dp[i][j] = dp[i-1][j-1] + 1;
                    flag[i][j] = 1;
                }
                else{
                    flag[i][j] = dp[i-1][j] >= dp[i][j-1]?2:3;
                    dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]); 
                }
                max = Math.max(max,dp[i][j]);
            }
        }
        int i = m,j = n;
        StringBuilder sb = new StringBuilder();
        while(max > 0){
            char t1 = text1.charAt(i-1);
            char t2 = text2.charAt(j-1);
            if(t1 == t2){
                sb.insert(0,t1);
                --i;--j;--max;
            }else if(flag[i][j] == 2) --i;
             else --j;   
        }
        System.out.println(sb.toString());
        return max;
    }
}
```
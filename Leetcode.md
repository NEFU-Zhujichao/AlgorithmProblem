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
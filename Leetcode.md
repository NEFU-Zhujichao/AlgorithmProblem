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
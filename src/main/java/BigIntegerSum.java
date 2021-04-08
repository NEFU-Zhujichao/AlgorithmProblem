public class BigIntegerSum {
    public static void main(String[] args) {
        String s = "999",t = "100012";
        char[] s1 = s.toCharArray();
        char[] s2 = t.toCharArray();
        StringBuilder res = new StringBuilder();
        reverse(s1);
        reverse(s2);
        int flag = s1.length >= s2.length ? 1 : 0;
        int cur = 0,i = 0;
        if(flag == 1){
            for(i = 0;i < s2.length;i++){
                if(cur > 0) s1[i] = (char) ((int)s1[i]+1);
                cur = 0;
                int tmp = s1[i] - '0' + s2[i] - '0';
                if(tmp >= 10){
                    s1[i] = (char)(tmp % 10 + '0');
                    cur = tmp / 10;
                }else s1[i] = (char) (tmp + '0');
            }
            while(cur > 0 && i < s1.length){
                int tmp = s1[i] - '0' + cur;
                cur = 0;
                if(tmp >= 10){
                    s1[i] = (char)(tmp % 10 + '0');
                    cur = tmp / 10;
                }else s1[i] = (char) (tmp + '0');
                i++;
            }
            for(i = 0;i < s1.length;i++) res.append(s1[i]);
            if(cur > 0) res.append((char)(cur + '0'));
        }else{
            cur = 0;i = 0;
            for(i = 0;i < s1.length;i++){
                if(cur > 0) s2[i] = (char) ((int)s2[i]+1);
                cur = 0;
                int tmp = s1[i] - '0' + s2[i] - '0';
                if(tmp >= 10){
                    s2[i] = (char)(tmp % 10 + '0');
                    cur = tmp / 10;
                }else s2[i] = (char) (tmp + '0');
            }
            while(cur > 0 && i < s2.length){
                int tmp = s2[i] - '0' + cur;
                cur = 0;
                if(tmp >= 10){
                    s2[i] = (char)(tmp % 10 + '0');
                    cur = tmp / 10;
                }else s2[i] = (char) (tmp + '0');
                i++;
            }
            for(i = 0;i < s2.length;i++) res.append(s2[i]);
            if(cur > 0) res.append((char)(cur + '0'));
        }
    }
    private static void reverse(char[] c){
        for(int i = 0;i < c.length/2;i++){
            char tmp = c[i];
            c[i] = c[c.length-1-i];
            c[c.length-i-1] = tmp;
        }
    }
}

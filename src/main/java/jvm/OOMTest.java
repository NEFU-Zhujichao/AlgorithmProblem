package jvm;

import java.util.ArrayList;

public class OOMTest {
    byte[] res = new byte[1*1024*1024];
    public static void main(String[] args) {
        ArrayList<OOMTest> list = new ArrayList<>();
        int count = 0;
        try {
            while (true){
                list.add(new OOMTest());
                ++count;
            }
        }catch (Error error){
            System.out.println(count);
        }
    }
}

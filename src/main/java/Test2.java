import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Test2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        String[] params;
        while ((line = br.readLine()) != null) {
            params = line.trim().split(" ");
            int n = Integer.parseInt(params[0]);
            int m = Integer.parseInt(params[1]);
            params = br.readLine().trim().split(" ");
            int a = Integer.parseInt(params[0]);
            int b = Integer.parseInt(params[1]);
            /*3 5
            1 2 3*/
        }
    }
}

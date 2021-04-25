import java.util.*;

public class Test {
    static List<List<Integer>> edges;
    static int[] indeg;
    public static void main(String[] args) {
        int[][] nums = {{3,0},{2,1},{1,2}};
        System.out.println(canFinish(4, nums));
    }
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构建点
        edges = new ArrayList<List<Integer>>();
        for (int i = 0; i < numCourses; ++i) {
            edges.add(new ArrayList<Integer>());
        }
        indeg = new int[numCourses];
        // 构建有向边，从 index -> x
        for (int[] info : prerequisites) {
            edges.get(info[1]).add(info[0]);
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
       List<Integer> res =  new ArrayList<Integer>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            res.add(u);
            for (int v: edges.get(u)) {
                --indeg[v];
                if (indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        return res.size() == numCourses;
    }
}

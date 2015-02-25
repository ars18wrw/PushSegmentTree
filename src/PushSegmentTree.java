import java.io.*;

/**
 * Created by Уладзімір Асіпчук on 23.02.15.
 */
public class PushSegmentTree {
    private static int globN = 0;
    private static int y = 0;
    public static void main(String[] args) throws IOException{
        int n = 0;
        int[] treeSum = null;
        int[] treeMin = null;
        int[] treeMax = null;
        int[] tree = null;

        PrintWriter out = new PrintWriter(new FileWriter("output.txt"));

        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader("input.txt")));
        if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
            n = (int) st.nval;
            globN = n-1;
            treeSum = new int[4*n];
            treeMax = new int[4*n];
            treeMin = new int[4*n];
            tree = new int[4*n];
        }
        // work cycle
        int i = 0;
        int j = 0;
        int val = 0;
        int res = 0;

        while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
            switch((int)st.nval) {
                case 0:
                    for (int k = 0; k < treeSum.length; k++) {
                        System.out.println(treeSum[k]);
                    }
                    break;
                case 1:
                    i = 0;
                    val = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        val = (int)st.nval;
                    update(treeSum, treeMax, treeMin, tree, 1, 0, n-1, i, i, val);
                    break;
                case 2:
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        val = (int)st.nval;
                    updateAdd(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, val);
                    break;
                case 3:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = sum(treeSum, tree, 1, 0, n - 1, i, j);
                    out.println(res);
                    break;
                case 4:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = min(treeMin, tree, 1, 0, n - 1, i, j);
                    out.println(res);
                    break;
                case 5:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = max(treeMax, tree, 1, 0, n - 1, i, j);
                    out.println(res);
                    break;

            }

        }
        out.flush();

    }

    public static int sum (int[] treeSum, int[] tree, int v, int tl, int tr, int l, int r) {
        if (l > r)
            return 0;
        if (l == tl && r == tr) {
            if (treeSum[v] == 0)
                return tree[v]*(r-l+1);
            return treeSum[v];
        }
        pushAdd(tree, v);
        int tm = (tl + tr) / 2;
        treeSum[v] =  sum (treeSum, tree, v*2, tl, tm, l, Math.min(r, tm))
                + sum (treeSum, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r);
        return treeSum[v];
    }

    public static int min (int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r) {
        if (l > r)
            return Integer.MAX_VALUE;
        if (l == tl && r == tr) {
            if (treeMin[v] == 0)
                return tree[v];
            return treeMin[v];
        }
        pushAdd(tree, v);
        int tm = (tl + tr) / 2;
        treeMin[v] = Math.min(min(treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm)),
                min(treeMin, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r));
        return treeMin[v];
    }

    public static int max (int[] treeMax, int[] tree, int v, int tl, int tr, int l, int r) {
        if (l > r) {
            return Integer.MIN_VALUE;
        }
        if (l == tl && r == tr) {
            if (treeMax[v] == 0)
                return tree[v];
            return treeMax[v];
        }
        pushAdd(tree, v);
        int tm = (tl + tr) / 2;
        treeMax[v] = Math.max(max(treeMax, tree, v * 2, tl, tm, l, Math.min(r, tm)),
                max(treeMax, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r));
        return treeMax[v];
    }

    public static Cortege update (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int new_val) {
        if (l > r) {
            treeSum[v] = tree[v]*(l-r+1);
            treeMax[v] = tree[v];
            treeMin[v] = tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], false);
        }
        if (l == tl && r == tr) {
            treeSum[v] = new_val*(r-l+1);
            treeMax[v] = new_val;
            treeMin[v] = new_val;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
        else {
            pushAdd(tree, v);
            int tm = (tl + tr) / 2;
            Cortege left = update(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), new_val);
            Cortege right = update(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, new_val);
//            if (!left.isUsed && !right.isUsed)
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//            if (!left.isUsed) {
//                treeSum[v] += left.sum;
//                treeMax[v] = left.max;
//                treeMin[v] = left.min;
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//            }
//            if (!right.isUsed) {
//                treeSum[v] += right.sum;
//                treeMax[v] = right.max;
//                treeMin[v] = right.min;
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//
//            }
            treeSum[v] += left.sum + right.sum;
            treeMax[v] = Math.max(left.max, right.max);
            treeMin[v] = Math.min(left.min, right.min);
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
    }

    public static Cortege updateAdd (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int add) {
        if (l > r) {
            treeSum[v] = tree[v]*(tr-tl+1);
            treeMax[v] = tree[v];
            treeMin[v] = tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], false);
        }
        if (l == tl && tr == r) {
            tree[v] += add;
            treeSum[v] = tree[v]*(r-l+1);
            treeMax[v] = tree[v];
            treeMin[v] = tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        } else {
            pushAdd(tree, v);
            int tm = (tl + tr) / 2;

            Cortege left = updateAdd(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), add);
            Cortege right = updateAdd(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, add);
//            if (!left.isUsed && !right.isUsed)
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//            if (!left.isUsed) {
//                treeSum[v] += left.sum;
//                treeMax[v] = left.max;
//                treeMin[v] = left.min;
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//            }
//            if (!right.isUsed) {
//                treeSum[v] += right.sum;
//                treeMax[v] = right.max;
//                treeMin[v] = right.min;
//                return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
//
//            }
            treeSum[v] = left.sum + right.sum;
            treeMax[v] = Math.max(left.max, right.max);
            treeMin[v] = Math.min(left.min, right.min);
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
    }

    public static void pushAdd (int[] t, int v) {
        t[v*2] += t[v];
        t[v*2+1] += t[v];
        t[v] = 0;
    }


}

class Cortege {
    public static Cortege CortegeFactory(int s, int ma, int mi, boolean is) {
        return new Cortege(s, ma, mi, is);
    }
    int sum;
    int max;
    int min;
    boolean isUsed;
    public Cortege(int s, int ma, int mi, boolean is) {
        sum = s;
        max = ma;
        min = mi;
        isUsed = is;
    }
}


import java.io.*;

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
            System.out.println("tree");
            for (int k = 0; k < tree.length; k++) {
                System.out.println(tree[k]);
            }
            System.out.println("treeSum");
            for (int k = 0; k < treeSum.length; k++) {
                System.out.println(treeSum[k]);
            }
            System.out.println("treeMax");
            for (int k = 0; k < treeMax.length; k++) {
                System.out.println(treeMax[k]);
            }
            System.out.println("treeMin");
            for (int k = 0; k < treeMin.length; k++) {
                System.out.println(treeMin[k]);
            }

            switch((int)st.nval) {
                case 0:
//                    for (int k = 0; k < treeMax.length; k++) {
//                        System.out.println(treeMax[k]);
//                    }
                    out.flush();
                    return;
                case 1:
                    i = 0;
                    val = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        val = (int)st.nval;
                    update(treeSum, treeMax, treeMin, tree, 1, 0, n-1, i, i, val, 0);
                    break;
                case 2:
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        val = (int)st.nval;
                    updateAdd(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, val, 0);
                    break;
                case 3:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = sum(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0);
                    out.println(res);
                    break;
                case 4:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = min(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0);
                    out.println(res);
                    break;
                case 5:
                    i = 0;
                    j = 0;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = max(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0);
                    out.println(res);
                    break;

            }

        }

    }

    public static Cortege update (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int new_val, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], false);
        }
        if (l == tl && r == tr) {
            treeSum[v] = new_val*(tr-tl+1);
            treeMax[v] = new_val;
            treeMin[v] = new_val;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
        else {
            int pushVal = push(tree, v);
            int tm = (tl + tr) / 2;
            Cortege left = update(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), new_val, pushVal);
            Cortege right = update(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, new_val, pushVal);

            treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
            treeMax[v] = Math.max(left.max, right.max) + tree[v];
            treeMin[v] = Math.min(left.min, right.min) + tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
    }

    public static int sum (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return 0;
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return treeSum[v];
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        treeSum[v] = sum (treeSum, treeMax, treeMin, tree, v*2, tl, tm, l, Math.min(r, tm), pushVal)
                + sum (treeSum, treeMax, treeMin, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r, pushVal) + tree[v]*(r-l+1);
        return treeSum[v];
    }

    public static int min (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return Integer.MAX_VALUE;
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return treeMin[v];
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        treeMin[v] = Math.min(min(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), pushVal),
                min(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, pushVal)) + tree[v];
        return treeMin[v];
    }


    public static int max (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return Integer.MIN_VALUE;
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return treeMax[v];
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        treeMax[v] = Math.max(max(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), pushVal),
                max(treeSum, treeMax, treeMin, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r, pushVal)) + tree[v];
        return treeMax[v];
    }

    public static Cortege updateAdd (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int add, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], false);
        }
        if (l == tl && tr == r) {
            tree[v] += add;
            treeSum[v] += (add+apushVal)*(tr-tl+1);
            treeMax[v] += add + apushVal;
            treeMin[v] += add + apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        } else {
            int pushVal = push(tree, v);
            int tm = (tl + tr) / 2;
            Cortege left = updateAdd(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), add, pushVal);
            Cortege right = updateAdd(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, add, pushVal);

            treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
            treeMax[v] = Math.max(left.max, right.max) + tree[v];
            treeMin[v] = Math.min(left.min, right.min) + tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], true);
        }
    }
    // we should do not push from leave
    // but this code wil execute only not from leave
    public static int push(int[] t, int v) {
        t[v*2] += t[v];
        t[v*2+1] += t[v];
        int temp = t[v];
        t[v] = 0;
        return temp;
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


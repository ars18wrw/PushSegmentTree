import java.io.*;

public class PushSegmentTree {
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
                    out.flush();
                    return;
                case 1:
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
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = sum(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0).add;
                    out.println(res);
                    break;
                case 4:
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = min(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0).add;
                    out.println(res);
                    break;
                case 5:
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        i = (int)st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER)
                        j = (int)st.nval;
                    res = max(treeSum, treeMax, treeMin, tree, 1, 0, n - 1, i, j, 0).add;
                    out.println(res);
                    break;
            }
        }
    }
    // the last param in Cortege is not used in updateAdd & update, but important in sum, max & min
    public static Cortege updateAdd (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int add, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
        if (l == tl && tr == r) {
            tree[v] += add;
            treeSum[v] += (add + apushVal)*(tr-tl+1);
            treeMax[v] += add + apushVal;
            treeMin[v] += add + apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        } else {
            int pushVal = push(tree, v);
            int tm = (tl + tr) / 2;
            Cortege left = updateAdd(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), add, pushVal);
            Cortege right = updateAdd(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, add, pushVal);

            treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
            treeMax[v] = Math.max(left.max, right.max) + tree[v];
            treeMin[v] = Math.min(left.min, right.min) + tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
    }

    public static Cortege update (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int new_val, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
        if (l == tl && r == tr) {
            // #1 error ?
            tree[v] = new_val;
            treeSum[v] = new_val*(tr-tl+1);
            treeMax[v] = new_val;
            treeMin[v] = new_val;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
        else {
            int pushVal = push(tree, v);
            int tm = (tl + tr) / 2;
            Cortege left = update(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), new_val, pushVal);
            Cortege right = update(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, new_val, pushVal);

            treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
            treeMax[v] = Math.max(left.max, right.max) + tree[v];
            treeMin[v] = Math.min(left.min, right.min) + tree[v];
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
    }
    // if (l > r) - we should resum, but not return sum!
    public static Cortege sum (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], 0);
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], treeSum[v]);
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        Cortege left = sum (treeSum, treeMax, treeMin, tree, v*2, tl, tm, l, Math.min(r, tm), pushVal);
        Cortege right = sum (treeSum, treeMax, treeMin, tree, v*2+1, tm+1, tr, Math.max(l, tm + 1), r, pushVal);

        treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
        treeMax[v] = Math.max(left.max, right.max) + tree[v];
        treeMin[v] = Math.min(left.min, right.min) + tree[v];
        // #2 error ?
        return new Cortege(treeSum[v], treeMax[v], treeMin[v], left.add + right.add + tree[v]*(tr-tl+1));
    }

    public static Cortege min (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], Integer.MAX_VALUE);
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], treeMin[v]);
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        Cortege left = min(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), pushVal);
        Cortege right = min(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, pushVal);

        treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
        treeMax[v] = Math.max(left.max, right.max) + tree[v];
        treeMin[v] = Math.min(left.min, right.min) + tree[v];
        return new Cortege(treeSum[v], treeMax[v], treeMin[v], Math.min(left.add, right.add)+tree[v]);
    }


    public static Cortege max (int[] treeSum, int[] treeMax, int[] treeMin, int[] tree, int v, int tl, int tr, int l, int r, int apushVal) {
        if (l > r) {
            treeSum[v] += apushVal*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], Integer.MIN_VALUE);
        }
        if (l == tl && r == tr) {
            treeSum[v] += (apushVal)*(tr-tl+1);
            treeMax[v] += apushVal;
            treeMin[v] += apushVal;
            return new Cortege(treeSum[v], treeMax[v], treeMin[v], treeMax[v]);
        }
        int tm = (tl + tr) / 2;
        int pushVal = push(tree, v);
        Cortege left = max(treeSum, treeMax, treeMin, tree, v * 2, tl, tm, l, Math.min(r, tm), pushVal);
        Cortege right = max(treeSum, treeMax, treeMin, tree, v * 2 + 1, tm + 1, tr, Math.max(l, tm + 1), r, pushVal);

        treeSum[v] = left.sum + right.sum + tree[v]*(tr-tl+1);
        treeMax[v] = Math.max(left.max, right.max) + tree[v];
        treeMin[v] = Math.min(left.min, right.min) + tree[v];
        return new Cortege(treeSum[v], treeMax[v], treeMin[v], Math.max(left.add, right.add) + tree[v]);

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

    int sum;
    int max;
    int min;
    int add;

    public Cortege(int s, int ma, int mi, int a) {
        sum = s;
        max = ma;
        min = mi;
        add = a;
    }
}


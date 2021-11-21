import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@SuppressWarnings("all")
public class AVLTree {
    private static class Node {
        int value;
        int bf;
        Node lchild;
        Node rchild;
        Node parent;

        public Node(int value, int bf, Node lchild, Node rchild, Node parent) {
            this.value = value;
            this.bf = bf;
            this.lchild = lchild;
            this.rchild = rchild;
            this.parent = parent;
        }
    }

    Node root = null;
    final int LH = 1;                        /*  左高 */
    final int EH = 0;                            /*  等高 */
    final int RH = -1;                           /*  右高 */

    final boolean FALSE = false;
    final boolean TRUE = true;

    /* 存放找到的数据 */
    public static ArrayList<Integer> ar = new ArrayList<>();
    /* 统计查找次数 */
    public static int count = 0;

    /* 对以T为根的二叉排序树作右旋处理 */
    /* 处理之后T的父节点指向T的左节点 */
    //右旋-顺时针旋转(如LL型就得对根结点做该旋转)
    private void R_Rotate(Node T) {
        Node L, P;
        P = T.parent;
        L = T.lchild;                      /*  L指向node的左子树根结点 */
        T.lchild = L.rchild;               /*  L的右子树挂接为node的左子树 */

        if (L.rchild != null)
            L.rchild.parent = T;
        L.rchild = T;
        L.parent = P;
        T.parent = L;

        if (P == null)
            root = L;
        else if (P.rchild == T)
            P.rchild = L;
        else
            P.lchild = L;

    }

    /* 对以T为根的二叉排序树作左旋处理， */
    /* 处理之后T的父节点指向T的右节点 */
    //左旋-逆时针旋转(如RR型就得对根结点做该旋转)
    private void L_Rotate(Node T) {
        Node R, P;
        P = T.parent;
        R = T.rchild;                    /* R指向T的右子树根结点 */
        T.rchild = R.lchild;         /* R的左子树挂接为T的右子树 */

        if (R.lchild != null)
            R.lchild.parent = T;
        R.lchild = T;
        R.parent = P;
        T.parent = R;

        if (P == null)
            root = R;
        else if (P.rchild == T)
            P.rchild = R;
        else
            P.lchild = R;

    }

    /*  对以指针T所指结点为根的二叉树作左平衡旋转处理 */
    /*  本算法结束时，指针T指向新的根结点 */
    public void LeftBalance(Node T) {
        Node L, Lr;
        L = T.lchild;                    /*  L指向T的左子树根结点 */
        switch (L.bf) {
            /* 检查T的左子树的平衡度，并作相应平衡处理 */
            case LH:                        /* 新结点插入在T的左孩子的左子树上，要作单右旋处理 */
                T.bf = L.bf = EH;
                R_Rotate(T);
                break;
            case RH:                        /* 新结点插入在T的左孩子的右子树上，要作双旋处理 */ //
                Lr = L.rchild;                /* Lr指向T的左孩子的右子树根 */
                switch (Lr.bf) {
                    /* 修改T及其左孩子的平衡因子 */
                    case LH:
                        T.bf = RH;
                        L.bf = EH;
                        break;
                    case EH:
                        T.bf = L.bf = EH;
                        break;
                    case RH:
                        T.bf = EH;
                        L.bf = LH;
                        break;
                }
                Lr.bf = EH;
                L_Rotate(T.lchild); /* 对T的左子树作左旋平衡处理 */
                R_Rotate(T);                /* 对T作右旋平衡处理 */
                break;
            case EH:      //特殊情况4,这种情况在添加时不可能出现，只在移除时可能出现，旋转之后整体树高不变
                L.bf = RH;
                T.bf = LH;
                R_Rotate(T);
                break;
        }
    }

    /*  对以指针T所指结点为根的二叉树作右平衡旋转处理， */
    /*  本算法结束时，指针T指向新的根结点 */
    public void RightBalance(Node T) {
        Node R, Rl;
        R = T.rchild;                      /*  R指向T的右子树根结点 */
        switch (R.bf) {
            /*  检查T的右子树的平衡度，并作相应平衡处理 */
            case RH:                        /*  新结点插入在T的右孩子的右子树上，要作单左旋处理 */
                T.bf = R.bf = EH;
                L_Rotate(T);
                break;
            case LH:                        /*  新结点插入在T的右孩子的左子树上，要作双旋处理 */ //最小不平衡树的根结点为负，其右孩子为正
                Rl = R.lchild;                /*  Rl指向T的右孩子的左子树根 */
                switch (Rl.bf) {
                    /*  修改T及其右孩子的平衡因子 */
                    case RH:
                        T.bf = LH;
                        R.bf = EH;
                        break;
                    case EH:
                        T.bf = R.bf = EH;
                        break;
                    case LH:
                        T.bf = EH;
                        R.bf = RH;
                        break;
                }
                Rl.bf = EH;
                R_Rotate(T.rchild); /*  对T的右子树作右旋平衡处理 */
                L_Rotate(T);                /*  对T作左旋平衡处理 */
                break;
            case EH:      //特殊情况4,这种情况在添加时不可能出现，只在移除时可能出现，旋转之后整体树高不变
                R.bf = LH;
                T.bf = RH;
                L_Rotate(T);
                break;

        }
    }


    public void insertAVL(int e) {
        if (root == null) {
            root = new Node(e, EH, null, null, null);
            return;
        }
        TS t = new TS();
        InsertAVL(root, e, t, null);
    }

    /*  若在平衡的二叉排序树T中不存在和e有相同关键字的结点，则插入一个 */
    /*  数据元素为e的新结点，并返回1，否则返回0。若因插入而使二叉排序树 */
    /*  失去平衡，则作平衡旋转处理，布尔变量taller反映T长高与否。 */
    private boolean InsertAVL(Node T, int e, TS tl, Node parent) {
        if (T == null) {
            /*  插入新结点，树“长高”，置taller为TRUE */
            Node nNode = new Node(e, EH, null, null, parent);
            if (e < parent.value)
                parent.lchild = nNode;
            else
                parent.rchild = nNode;
            tl.taller = TRUE;
        } else {
            if (e == T.value) {
                /*  树中已存在和e有相同关键字的结点则不再插入 */
                tl.taller = FALSE;
                return false;
            } else if (e < T.value) {
                /*  应继续在T的左子树中进行搜索 */
                if (!InsertAVL(T.lchild, e, tl, T))
                    return false;
                if (tl.taller)                             /*   已插入到T的左子树中且左子树“长高” */
                    switch (T.bf)                 /*  检查T的平衡度 */ {
                        case LH:                        /*  原本左子树比右子树高，需要作左平衡处理 */
                            LeftBalance(T);
                            tl.taller = FALSE;
                            break;
                        case EH:                        /*  原本左、右子树等高，现因左子树增高而使树增高 */
                            T.bf = LH;
                            tl.taller = TRUE;
                            break;
                        case RH:                        /*  原本右子树比左子树高，现左、右子树等高 */
                            T.bf = EH;
                            tl.taller = FALSE;
                            break;
                    }
            } else {
                /*  应继续在T的右子树中进行搜索 */
                if (!InsertAVL(T.rchild, e, tl, T))
                    return false;
                if (tl.taller)                             /*  已插入到T的右子树且右子树“长高” */ {
                    switch (T.bf)                 /*  检查T的平衡度 */ {
                        case LH:                        /*  原本左子树比右子树高，现左、右子树等高 */
                            T.bf = EH;
                            tl.taller = FALSE;
                            break;
                        case EH:                        /*  原本左、右子树等高，现因右子树增高而使树增高  */
                            T.bf = RH;
                            tl.taller = TRUE;
                            break;
                        case RH:                        /*  原本右子树比左子树高，需要作右平衡处理 */
                            RightBalance(T);
                            tl.taller = FALSE;
                            break;
                    }
                }
            }
        }
        return true;
    }


    //前序遍历
    private void preOrder(Node r) {
        if (r == null)
            return;
        System.out.print(r.value + ",");
        preOrder(r.lchild);
        preOrder(r.rchild);
    }

    public void preOrder() {
        preOrder(root);
    }

    //中序遍历
    private void inOrder(Node r) {
        if (r == null)
            return;
        inOrder(r.lchild);
        System.out.print(r.value + ",");
        inOrder(r.rchild);
    }

    public void inOrder() {
        inOrder(root);
    }


    /* 查找数据Search ↓ */
    /* 实现方法 */
    public void Search(Node r, int @NotNull [] key) {
        for (int v : key) {
            Search(r, v);
        }
    }
    /* 实现步骤 */
    public void Search(Node r, int key) {
        while (r != null) {
            /*
             * 比较集合中元素与节点值大小
             * 小于则返回小于0的值
             * 大于则返回大于0的值
             * 等于则返回0
             */
            int tmp = Integer.compare(key, r.value);

            if (tmp < 0) {
                count++;
                r = r.lchild;
            } else if (tmp > 0) {
                count++;
                r = r.rchild;
            } else {
                ar.add(r.value);
                count++;
                break;
            }
        }
    }
    /* 主函数调用的Search方法 */
    public void Search() throws IOException {

        int index = 0;
        String Mtmp;
        String[] Mar;
        int[] m;

        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        br.readLine();
        br.readLine();
        br.readLine();
        /* 读到第四行的要查找的序列 */
        Mtmp = br.readLine();
        Mar = Mtmp.split(" ");
        m = new int[Mar.length];

        for (String s : Mar) {
            if (!s.equals("")) {
                m[index] = Integer.parseInt(s);
                index++;
            }
        }
        br.close();
        Search(root, m);

    }


    /* 计算平衡树的高度 */
    public int getHeight(Node r) {
        if (r == null) return -1;
        int leftHeight = r.lchild != null ? getHeight(r.lchild) : -1;
        int rightHeight = r.rchild != null ? getHeight(r.rchild) : -1;
        return 1 + (Math.max(leftHeight, rightHeight));
    }

    /* 主函数调用 */
    public int getHeight() {
        return getHeight(root);
    }

    public static void main(String[] args) throws IOException {

        AVLTree tree = new AVLTree();
        BufferedReader br = new BufferedReader(new FileReader("data.txt"));
        String ncount = "";
        String mcount = "";
        String Ntmp;
        String Mtmp = "";
        String[] Nar;
        String[] Mar;
        int index = 0;

        /* 记录n的数量 */
        ncount = br.readLine();
        /* 读取n序列 */
        Ntmp = br.readLine();
        /* 分割Ntmp,得到一个字符串数组 */
        Nar = Ntmp.split(" ");

        int[] n = new int[Nar.length];

        /* 将字符串数组转换为整形数组 */
        for (String s : Nar) {
            if (!s.equals("") && (!s.equals(" "))) {
                n[index] = Integer.parseInt(s);
                index++;
            }
        }
        /* 插入，构建平衡二叉树 */
        for (int j : n) {
            tree.insertAVL(j);
        }
        /* 先序遍历 */
        tree.preOrder();
        System.out.println();

        /* 开始查找，并记录查找消耗时间 */
        long startTime = System.currentTimeMillis();
        tree.Search();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("共耗时：" + time + " 毫秒");
        /* 输出查找次数 count为全局变量 */
        System.out.println("查找次数" + count);
        /* 遍历查找到的数据 */
        for (int x : ar) {
            System.out.print(x + " ");
        }
        /* 得到树的高度 */
        int height = tree.getHeight();
        System.out.println();
        System.out.println("高度为：" + height);
        br.close();
        /* 输出数据 */
        PrintWriter pr = new PrintWriter("Result.txt");
        String ret = height + " " + count + " " + time;
        pr.write(ret);
        pr.flush();
        pr.close();
    }
}


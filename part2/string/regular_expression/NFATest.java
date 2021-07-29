/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class NFATest {

    private Digraph G;
    private String regex;
    private int m;

    public NFATest(String regex) {
        this.regex = regex;
        this.m = regex.length();
        Stack<Integer> ops = new Stack<>();
        G = new Digraph(m + 1); // 多一個accept state

        for (int i = 0; i < m; i++) {
            int lp = i;
            char c = regex.charAt(i);

            if (c == '(' || c == '|')
                ops.push(i);
            else if (c == ')') {
                int or = ops.pop();
                if (regex.charAt(or) == '|') {
                    lp = ops.pop();
                    G.addEdge(lp, or + 1);
                    G.addEdge(or, i);
                }
                else if (regex.charAt(or) == '(')
                    lp = or;
                else assert false;
            }

            if (i < m - 1 && regex.charAt(i + 1) == '*') {
                G.addEdge(lp, i + 1);
                G.addEdge(i + 1, lp);
            }

            if (c == '(' || c == '*' || c == ')')
                G.addEdge(i, i + 1); // next state
        }
        // final check
        if (ops.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }

    public boolean recognizes(String text) {
        // states reachable from start by ε-transitions
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) pc.add(v);
        }

        for (int i = 0; i < text.length(); i++) {
            // text不可包含特殊字元
            if (text.charAt(i) == '*' || text.charAt(i) == '|' || text.charAt(i) == '('
                    || text.charAt(i) == ')')
                throw new IllegalArgumentException(
                        "text contains the metacharacter '" + text.charAt(i) + "'");

            Bag<Integer> match = new Bag<>();
            for (int v : pc) {
                if (v == m) continue; // accept state
                if (regex.charAt(v) == text.charAt(i) || regex.charAt(v) == '.')
                    match.add(v + 1); // match往前移一個state
            }

            // ε-transitions
            dfs = new DirectedDFS(G, match);
            pc = new Bag<>();
            for (int v = 0; v < G.V(); v++) {
                if (dfs.marked(v)) pc.add(v);
            }
        }

        // 檢查是否有在accept state的
        for (int v : pc) {
            if (v == m) return true;
        }

        return false;
    }

    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFATest nfa = new NFATest(regexp);
        StdOut.println(nfa.recognizes(txt));
    }
}

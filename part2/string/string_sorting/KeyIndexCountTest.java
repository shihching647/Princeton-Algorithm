/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

/**
 * sort array a, where element between a ~ z (R = 26)
 */
public class KeyIndexCountTest {

    private static int R = 26;

    private KeyIndexCountTest() {

    }

    private static void sort(char[] a) {
        int n = a.length;
        char[] aux = new char[n];
        int[] count = new int[R + 1]; // 這邊R要+1

        // count the frequency of every element
        for (int i = 0; i < n; i++)
            count[a[i] - 'a' + 1]++; // 這邊要+1

        // compute cumulates
        for (int i = 0; i < R; i++)
            count[i + 1] += count[i]; // 要用+=

        // move items
        for (int i = 0; i < n; i++)
            aux[count[a[i] - 'a']++] = a[i]; // 這邊沒有+1, a的index要從count[0]開始填

        // copy back
        System.arraycopy(aux, 0, a, 0, n);

    }

    public static void main(String[] args) {
        char[] a = new char[50];
        for (int i = 0; i < a.length; i++)
            a[i] = (char) (StdRandom.uniform(26) + 'a');
        System.out.println(Arrays.toString(a));
        sort(a);
        System.out.println(Arrays.toString(a));
    }
}

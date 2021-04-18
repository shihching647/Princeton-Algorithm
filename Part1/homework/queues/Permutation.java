/* *****************************************************************************
 *  Name: 647
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    // Memory in O(n)
    // public static void main(String[] args) {
    //     int k = Integer.parseInt(args[0]);
    //     RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
    //     while (!StdIn.isEmpty()) {
    //         String str = StdIn.readString();
    //         randomizedQueue.enqueue(str);
    //     }
    //     // output
    //     while (k > 0) {
    //         System.out.println(randomizedQueue.dequeue());
    //         k--;
    //     }
    // }

    // Memory in O(k)
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int count = 0;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            count++;
            String str = StdIn.readString();
            // keep size equals to k
            if (randomizedQueue.size() == k) {
                double p = k / (count * 1.0);
                // System.out.println(p);
                if (StdRandom.bernoulli(p)) {
                    randomizedQueue.dequeue();
                    randomizedQueue.enqueue(str);
                }
                continue;
            }
            randomizedQueue.enqueue(str);
        }
        // output
        while (!randomizedQueue.isEmpty()) {
            System.out.println(randomizedQueue.dequeue());
        }
    }
}

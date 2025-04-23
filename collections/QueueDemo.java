package collections;

import java.util.PriorityQueue;

public class QueueDemo {
    public static void main(String[] args) {
        minHeapDemo();
    }

    static void minHeapDemo() {
        final PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.add(20);
        minHeap.add(10);
        minHeap.add(15);
        minHeap.add(30);
    
        System.out.println("minHeap = " + minHeap.peek());
    }
}

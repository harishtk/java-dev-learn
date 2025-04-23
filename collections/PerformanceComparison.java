/**
 * PerformanceComparison class demonstrates and compares different approaches
 * to retrieve the N smallest elements from multiple lists of objects. The
 * approaches include Parallel Sort, Priority Queue, TreeSet, Quickselect, and
 * Bucket Sort. Each approach has its own trade-offs in terms of time and space
 * complexity, and the choice of approach depends on the specific use case.
 *
 * Approaches:
 *
 * 1. Parallel Sort:
 *    - Description: Combines all elements into a single list, sorts them in
 *      parallel, and retrieves the first N elements.
 *    - Time Complexity: O(M log M), where M is the total number of elements
 *      across all lists.
 *    - Space Complexity: O(M) for storing all elements.
 *    - When to Use: Suitable when the input size is relatively small or when
 *      parallelism can significantly speed up sorting.
 *
 * 2. Priority Queue:
 *    - Description: Uses a max-heap (PriorityQueue) to maintain the N smallest
 *      elements while iterating through all elements.
 *    - Time Complexity: O(M log N), where M is the total number of elements and
 *      N is the number of smallest elements to retrieve.
 *    - Space Complexity: O(N) for the PriorityQueue.
 *    - When to Use: Ideal when N is much smaller than M, as it avoids sorting
 *      the entire list.
 *
 * 3. TreeSet:
 *    - Description: Uses a TreeSet to maintain the N smallest elements, ensuring
 *      they are always sorted.
 *    - Time Complexity: O(M log N), where M is the total number of elements and
 *      N is the number of smallest elements to retrieve.
 *    - Space Complexity: O(N) for the TreeSet.
 *    - When to Use: Useful when frequent access to sorted elements is required,
 *      but slightly slower than PriorityQueue due to TreeSet's overhead.
 *
 * 4. Quickselect:
 *    - Description: Combines all elements into a single list and uses the
 *      Quickselect algorithm to partition the list around the Nth smallest
 *      element, followed by sorting the first N elements.
 *    - Time Complexity: O(M) on average, O(M^2) in the worst case (rare).
 *    - Space Complexity: O(M) for storing all elements.
 *    - When to Use: Best for large datasets when average-case performance is
 *      acceptable and sorting the entire list is unnecessary.
 *
 * 5. Bucket Sort:
 *    - Description: Uses a counting array (buckets) to count occurrences of
 *      values and retrieves the N smallest elements directly from the buckets.
 *    - Time Complexity: O(M + K), where M is the total number of elements and K
 *      is the maximum value of elements.
 *    - Space Complexity: O(K) for the bucket array.
 *    - When to Use: Suitable when the range of values (K) is small compared to
 *      the number of elements (M), as it avoids sorting altogether.
 *
 * Summary:
 * - Parallel Sort: Use for small datasets or when parallelism is beneficial.
 * - Priority Queue: Use when N is much smaller than M and sorting is unnecessary.
 * - TreeSet: Use when sorted access to the smallest elements is required.
 * - Quickselect: Use for large datasets when average-case performance is
 *   acceptable.
 * - Bucket Sort: Use when the range of values is small and sorting can be avoided.
 */
package collections;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class PerformanceComparison {

    public static void main(String[] args) {
        // Configuration
        int numLists = 100; // Number of lists
        int listSize = 1000; // Size of each list
        int n = 50; // Number of smallest elements to retrieve
        int maxValue = 10000; // Maximum value of MyObject

        // Generate random data
        List<List<MyObject>> listOfLists = generateRandomData(numLists, listSize);

        // Comparator for MyObject
        Comparator<MyObject> comparator = Comparator.comparingInt(o -> o.value);

        // Measure performance of each approach
        System.out.println("Measuring performance...");
        measurePerformance("Parallel Sort", () -> parallelSortApproach(listOfLists, comparator, n));
        measurePerformance("Priority Queue", () -> priorityQueueApproach(listOfLists, comparator, n));
        measurePerformance("TreeSet", () -> treeSetApproach(listOfLists, comparator, n));
        measurePerformance("Quickselect", () -> quickselectApproach(listOfLists, comparator, n));
        measurePerformance("Bucket Sort", () -> bucketSortApproach(listOfLists, n, maxValue));
    }

    // Generate random data
    private static List<List<MyObject>> generateRandomData(int numLists, int listSize) {
        List<List<MyObject>> listOfLists = new ArrayList<>();
        for (int i = 0; i < numLists; i++) {
            List<MyObject> list = new ArrayList<>();
            for (int j = 0; j < listSize; j++) {
                list.add(new MyObject(ThreadLocalRandom.current().nextInt(1, 10000)));
            }
            listOfLists.add(list);
        }
        return listOfLists;
    }

    // Measure performance of a task
    public static void measurePerformance(String approachName, Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        System.out.printf("%s took %.2f ms%n", approachName, (endTime - startTime) / 1_000_000.0);
    }

    // Approach 1: Using parallel sort
    private static List<MyObject> parallelSortApproach(List<List<MyObject>> listOfLists, Comparator<MyObject> comparator, int n) {
        List<MyObject> allElements = new ArrayList<>();
        for (List<MyObject> list : listOfLists) {
            allElements.addAll(list);
        }
        allElements.parallelStream().sorted(comparator).limit(n).toList();
        return allElements.subList(0, Math.min(n, allElements.size()));
    }

    // Approach 2: Using PriorityQueue
    private static List<MyObject> priorityQueueApproach(List<List<MyObject>> listOfLists, Comparator<MyObject> comparator, int n) {
        PriorityQueue<MyObject> pq = new PriorityQueue<>(comparator.reversed());
        for (List<MyObject> list : listOfLists) {
            for (MyObject obj : list) {
                pq.offer(obj);
                if (pq.size() > n) {
                    pq.poll();
                }
            }
        }
        List<MyObject> result = new ArrayList<>(pq);
        result.sort(comparator); // Sort the final result
        return result;
    }

    // Approach 3: Using TreeSet
    private static List<MyObject> treeSetApproach(List<List<MyObject>> listOfLists, Comparator<MyObject> comparator, int n) {
        TreeSet<MyObject> treeSet = new TreeSet<>(comparator);
        for (List<MyObject> list : listOfLists) {
            for (MyObject obj : list) {
                treeSet.add(obj);
                if (treeSet.size() > n) {
                    treeSet.pollLast(); // Remove the largest element
                }
            }
        }
        return new ArrayList<>(treeSet);
    }

    // Approach 4: Using Quickselect
    private static List<MyObject> quickselectApproach(List<List<MyObject>> listOfLists, Comparator<MyObject> comparator, int n) {
        List<MyObject> allElements = new ArrayList<>();
        for (List<MyObject> list : listOfLists) {
            allElements.addAll(list);
        }

        // Perform Quickselect to find the N smallest elements
        quickselect(allElements, 0, allElements.size() - 1, n - 1, comparator);

        // Extract the first N elements and sort them
        List<MyObject> result = allElements.subList(0, n);
        result.sort(comparator);
        return result;
    }

    // Quickselect algorithm
    private static void quickselect(List<MyObject> list, int left, int right, int k, Comparator<MyObject> comparator) {
        if (left < right) {
            int pivotIndex = partition(list, left, right, comparator);
            if (pivotIndex == k) {
                return;
            } else if (pivotIndex < k) {
                quickselect(list, pivotIndex + 1, right, k, comparator);
            } else {
                quickselect(list, left, pivotIndex - 1, k, comparator);
            }
        }
    }

    // Partition method for Quickselect
    private static int partition(List<MyObject> list, int left, int right, Comparator<MyObject> comparator) {
        MyObject pivot = list.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (comparator.compare(list.get(j), pivot) < 0) {
                Collections.swap(list, i, j);
                i++;
            }
        }
        Collections.swap(list, i, right);
        return i;
    }

    // Approach 5: Using Bucket Sort
    private static List<MyObject> bucketSortApproach(List<List<MyObject>> listOfLists, int n, int maxValue) {
        // Step 1: Combine all elements into a single list
        List<MyObject> allElements = new ArrayList<>();
        for (List<MyObject> list : listOfLists) {
            allElements.addAll(list);
        }

        // Step 2: Create a bucket array to count occurrences
        int[] buckets = new int[maxValue + 1];
        for (MyObject obj : allElements) {
            buckets[obj.value]++;
        }

        // Step 3: Retrieve the N smallest elements
        List<MyObject> result = new ArrayList<>();
        for (int i = 0; i <= maxValue && result.size() < n; i++) {
            while (buckets[i] > 0 && result.size() < n) {
                result.add(new MyObject(i));
                buckets[i]--;
            }
        }

        return result;
    }

    // MyObject class
    static class MyObject {
        int value;

        MyObject(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
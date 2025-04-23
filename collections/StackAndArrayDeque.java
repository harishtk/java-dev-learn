package collections;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

public class StackAndArrayDeque {
    public static void main(String[] args) {
        StackAndArrayDeque app = new StackAndArrayDeque();
        System.out.println("Using stack: ");
        app.stackDemo();
        System.out.println("Using Deque for stack: ");
        app.stackUsingArrayDeque();
    }

    public void stackDemo() {
        // Create a stack
        Stack<String> stack = new Stack<>();

        // Push elements to the stack
        stack.push("Java");
        stack.push("Python");
        stack.push("JavaScript");

        // Pop elements from the stack
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    public void stackUsingArrayDeque() {
        // Create a stack using ArrayDeque
        Deque<String> deque = new LinkedList<>(); // or LinkedBlockingDeque<>(),
        // or LinkedList<>() or PriorityQueue<>();

        // Push elements to the stack
        deque.push("Java");
        deque.push("Python");
        deque.push("JavaScript");

        // Pop elements from the deque
        while (!deque.isEmpty()) {
            System.out.println(deque.pop());
        }

        // Push elements to the stack using dequeue methods
        deque.addFirst("Java");
        deque.addFirst("Python");
        deque.addFirst("JavaScript");

        // Pop elements from the deque using dequeue methods
        while (!deque.isEmpty()) {
            System.out.println(deque.removeFirst());
        }
    }
}

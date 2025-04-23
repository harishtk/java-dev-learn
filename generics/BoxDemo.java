package generics;

import java.util.List;

public class BoxDemo {
    public static void main(String[] args) {
        List<Box<Integer>> listOfIntegerBoxes = new java.util.ArrayList<>();
        BoxDemo.addBox(Integer.valueOf(10), listOfIntegerBoxes);
        BoxDemo.addBox(Integer.valueOf(20), listOfIntegerBoxes);
        BoxDemo.addBox(Integer.valueOf(30), listOfIntegerBoxes);
        BoxDemo.outputBoxes(listOfIntegerBoxes);
    }

    private static <U> void addBox(U u, java.util.List<Box<U>> boxes) {
        final Box<U> box = new Box<>();
        box.set(u);
        boxes.add(box);
    }

    private static <U> void outputBoxes(java.util.List<Box<U>> boxes) {
        int counter = 0;
        for (Box<U> box : boxes) {
            U boxContents = box.get();
            System.out.println("Box #" + counter + " contains [" + boxContents.toString() + "]");
            counter++;
        }
    }
}


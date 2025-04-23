import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadDemo {
    public static void main(String[] args) {
        final LinkedBlockingDeque<Message> queue = new LinkedBlockingDeque<Message>();

        Runnable producer = () -> {
            try {
                System.out.println("Adding messages to the queue");
                queue.put(new TextMessage("Hello"));
                Thread.sleep(1000);
                System.out.println("Adding messages to the queue");
                queue.put(new TextMessage("World"));
                Thread.sleep(1000);
                System.out.println("Adding end of messages to the queue");
                queue.put(new End());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                while (true) {
                    Message message = (Message) queue.take();
                    if (message instanceof TextMessage) {
                        System.out.println(((TextMessage) message).text);
                    } else if (message instanceof End) {
                        break;
                    }
                }
                System.out.println("End of consumer");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // final ExecutorService executor = Executors.newFixedThreadPool(2);
        // executor.submit(consumer);
        // executor.submit(producer);
        // executor.shutdown();

        // Thread producerThread = new Thread(producer);
        // Thread consumerThread = new Thread(consumer);

        // producerThread.start();
        // consumerThread.start();
        // try {
        //     producerThread.join();
        //     consumerThread.join();
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        final BlockingQueue<Runnable> taskQueue = new LinkedBlockingDeque<>();
        final WorkerThread worker = new WorkerThread(taskQueue);

        worker.start();

        taskQueue.add(producer);
        taskQueue.add(consumer);
        
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void unmanagedTread() {

        // This is an unmanaged thread, it is not managed by the executor service
        new Thread(() -> {
            System.out.println("Hello from unmanaged thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Goodbye from unmanaged thread");
        }).start();
    }

    static void managedThread() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        final Runnable myTask = () -> {
            System.out.println("Hello from managed thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Goodbye from managed thread");
        };
        Thread daemonThread = new Thread(myTask);
        daemonThread.setDaemon(true);
        executor.submit(daemonThread);  
        executor.shutdown();

        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Hello from CompletableFuture thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Goodbye from CompletableFuture thread");
            return 42;
        });
    }

    static abstract class Message {}

    static class TextMessage extends Message {
        String text;
        TextMessage(String text) {
            this.text = text;
        }
    }

    static class End extends Message {}

    static class WorkerThread extends Thread {

        final BlockingQueue<Runnable> taskQueue;

        WorkerThread(BlockingQueue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            System.out.println("Waiting for tasks..");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

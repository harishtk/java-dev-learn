package concurrent;

import java.util.concurrent.*;

public class JavaLooperHandler {

    private final BlockingQueue<Runnable> messageQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void post(Runnable runnable) {
        messageQueue.add(runnable);
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        scheduler.schedule(() -> post(runnable), delayMillis, TimeUnit.MILLISECONDS);
    }

    public void start() {
        executor.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Runnable message = messageQueue.take(); // Blocking operation
                    message.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
            } finally {
                System.out.println("Looper thread exiting");
            }
        });
    }

    public void stop() {
        executor.shutdownNow();
        scheduler.shutdownNow();
    }

    public static void main(String[] args) throws InterruptedException {
        JavaLooperHandler looper = new JavaLooperHandler();
        looper.start();

        // Post some messages
        looper.post(() -> System.out.println("Message 1: " + Thread.currentThread().getName()));
        looper.postDelayed(() -> System.out.println("Delayed Message: " + Thread.currentThread().getName()), 2000);
        looper.post(() -> System.out.println("Message 2: " + Thread.currentThread().getName()));

        Thread.sleep(5000);

        looper.stop();
    }
}
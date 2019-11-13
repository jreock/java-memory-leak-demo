import java.util.ArrayList;

/**
 * Create a memory leak in Java by populating an array with 10mb byte[] objects
 * in a thread loop
 * 
 * <p>
 * To run this, copy ArrayLeakExample.java into a directory and run:
 * 
 * <pre>
 * {@code
 *     javac ArrayLeakExample.java
 *     java -cp . ArrayLeakExample 
 * }
 * </pre>
 * 
 * <p>
 * You should see the heap fill quickly and start getting memory errors within a
 * minute or so depending on available memory
 * 
 */

public final class ArrayLeakExample {

    static volatile boolean running = true;

    /*
     * This is our leaky object, a boundless ArrayList that we will continually
     * populate
     */
    static ArrayList<byte[]> leakyArr = new ArrayList<byte[]>(0);

    /**
     * 
     * @param args
     * @throws Exception
     * 
     *                   Create a thread from the MainThread class and start it,
     *                   allow interrupt by keystroke
     * 
     */
    public static void main(String[] args) throws Exception {
        Thread thread = new MainThread();
        try {
            thread.start();
            System.out.println("Running .... any key to stop");
            System.in.read();
        } finally {
            running = false;
            thread.join();
        }
    }

    /**
     * Implement the thread as a while loop that repeats every 100ms and calls the
     * populateArray() method
     */
    static final class MainThread extends Thread {
        @Override
        public void run() {
            while (running) {
                try {
                    populateArray();
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println("Bye!");
                    running = false;
                }
            }
        }

        /**
         * Here's where the magic happens, add a 10mb byte array to the leaky ArrayList
         * object
         */
        static void populateArray() {

            final byte leakerByte[] = new byte[1024 * 1024 * 10];
            leakyArr.add(leakerByte);

        }
    }
}

package umcs.pl.utils;

public class EraserThread implements Runnable {
    private boolean stop;

    /**
     *@param prompt The prompt displayed to the user.
     */
    public EraserThread(String prompt) {
        System.out.print(prompt);

    }

    /**
     * Begin masking...display asterisks (*)
     */
    public void run() {
        stop = true;
        while (stop) {
            System.out.print("\010*");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Instruct the thread to stop masking
     */
    public void stopMasking() {
        this.stop = false;
    }
}

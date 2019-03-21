package sync;

class SynchronizedMethods {
    private int sum = 0;
    static int s_sum = 0;

    static synchronized void staticSyncCalculate() {
        ++s_sum;
    }

    void calculate() {
        setSum(getSum() + 1);
    }

    synchronized void syncCalculate() {
        setSum(getSum() + 1);
    }

    private void setSum(int sum) {
        this.sum = sum;
    }

    int getSum() {
        return this.sum;
    }
}

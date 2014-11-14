package ua.dp.skillsup;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class MyCounterDynamic implements Counter {
//make depends on array size
    private volatile AtomicLong[] counters;
    private AtomicBoolean rehashInProgress = new AtomicBoolean(false);

    public MyCounterDynamic() {
        this.counters = new AtomicLong[]{
                new AtomicLong(),
                new AtomicLong(),
                new AtomicLong(),
                new AtomicLong()
        };
    }

    public void inc() {
        AtomicLong[] atomicLongs = counters;
        AtomicLong localCounter = atomicLongs[getAddress(atomicLongs.length)];
        long currentValue = localCounter.get();

        int contentionCounter = 0;
        while (!localCounter.compareAndSet(currentValue, currentValue + 1)) {
            contentionCounter++;

            if (contentionCounter > atomicLongs.length) {
                rehashIfNecessary();
                contentionCounter = 0;
            }

            atomicLongs = counters;
            localCounter = atomicLongs[getAddress(atomicLongs.length)];
            currentValue = localCounter.get();
        }
    }

    private void rehashIfNecessary() {
        if (rehashInProgress.compareAndSet(false, true)) {
            rehash();
            rehashInProgress.set(false);
        }
    }

    private void rehash() {
        AtomicLong[] oldArray = counters;
        AtomicLong[] newArray = new AtomicLong[oldArray.length * 2];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);

        for (int i = oldArray.length; i < newArray.length; i++) {
            newArray[i] = new AtomicLong();
        }

        counters = newArray;
    }

    private int getAddress(int hashMask) {
        return Thread.currentThread().hashCode() % hashMask;
    }

    public long get() {
        AtomicLong[] localCounters = counters;
        long sum = 0;
        for (AtomicLong localCounter : localCounters) {
            sum += localCounter.get();
        }
        return sum;
    }
}

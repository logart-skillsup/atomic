package ua.dp.skillsup;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadersWriterLock {
    private AtomicInteger readers = new AtomicInteger(0);
    private AtomicBoolean writer = new AtomicBoolean(false);


    public void acquireReadLock() {
        do {
            readers.incrementAndGet();

            if (!writer.get()) {
                return;
            }

            readers.decrementAndGet();
            while (writer.get());
        } while (true);
    }

    public void releaseReadLock() {
        readers.getAndDecrement();
    }

    public void acquireWriteLock() {
        while (!writer.compareAndSet(false, true)) ;
        while (readers.get() > 0) ;

    }

    public void releaseWriteLock() {
        writer.set(false);
    }
}

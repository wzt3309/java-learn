package sync;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class SynchronizedMethodsTest {

    @Test
    @Ignore
    public void givenMultiThread_whenNoSyncMethod() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SynchronizedMethods noSync = new SynchronizedMethods();

        // from 0 to 999, let one thread in thread pool to execute 'calculate' method
        // so the no sync method was invoked 1000 times
        IntStream.range(0, 1000).forEach(count -> service.submit(noSync::calculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertThat(1000).isEqualTo(noSync.getSum());
    }

    @Test
    public void givenMultiThread_whenSyncMethod() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SynchronizedMethods sync = new SynchronizedMethods();

        IntStream.range(0, 1000).forEach(count -> service.submit(sync::syncCalculate));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertThat(1000).isEqualTo(sync.getSum());
    }

    @Test
    public void givenMultiThread_whenSyncStaticMethod() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        IntStream.range(0, 1000).forEach(count -> service.submit(SynchronizedMethods::staticSyncCalculate));
        service.shutdown();
        // after shutdown request: block until task finished, timeout or interrupted
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertThat(1000).isEqualTo(SynchronizedMethods.s_sum);
    }

}
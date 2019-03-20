package core;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaTryWithResourcesTest {
    private static final Logger LOG = LoggerFactory.getLogger(JavaTryWithResourcesTest.class);

    private static final String TEST_STRING_HELLO_WORLD = "hello, world";

    private Date date1, date2;

    // tests

    /* Test try-with-resource */
    @Test
    public void whenWritingToStringWriter_thenCorrectlyWritten() {
        final StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw, true)) {
            pw.print(TEST_STRING_HELLO_WORLD);
        }
        assertThat(TEST_STRING_HELLO_WORLD).isEqualTo(sw.getBuffer().toString());
    }

    /* Test multiple trr-with-resource */
    @Test
    public void  givenStringToScanner_whenWritingToStringWriter_thenCorrectlyWritten() {
        final StringWriter sw = new StringWriter();
        try (Scanner sc = new Scanner(TEST_STRING_HELLO_WORLD);
             PrintWriter pw = new PrintWriter(sw, true)) {
            pw.print(sc.nextLine());
        }
        assertThat(TEST_STRING_HELLO_WORLD).isEqualTo(sw.getBuffer().toString());
    }

    /* Test the order which the resource initialized and closed */
    @Test
    public void whenFirstAutoCloseableResourceInitializeFirst_thenReleasedLast() throws Exception {
        try (AutoCloseableResource first = new AutoCloseableResourceFrist();
             AutoCloseableResource second = new AutoCloseableResourceSecond()) {
            first.doSomething();
            second.doSomething();
        }

        assertThat(date1).isAfter(date2);

    }

    class AutoCloseableResource implements AutoCloseable {
        private String name;

        AutoCloseableResource(String name) {
            this.name = name;
            LOG.debug("constructor -> " + name);
        }

        void doSomething() {
            LOG.debug("something -> " + name);
        }

        @Override
        public void close() throws Exception {
            LOG.debug("close -> " + name);
        }
    }

    class AutoCloseableResourceFrist extends AutoCloseableResource {

        AutoCloseableResourceFrist() {
            super("AutoCloseableResource_First");
        }

        @Override
        public void close() throws Exception {
            super.close();
            date1 = new Date();
            Thread.sleep(1000); // execution is too fast
        }
    }

    class AutoCloseableResourceSecond extends AutoCloseableResource {

        AutoCloseableResourceSecond() {
            super("AutoCloseableResource_Second");
        }

        @Override
        public void close() throws Exception {
            super.close();
            date2 = new Date();
            Thread.sleep(1000);
        }
    }
}

package core.classloader;

import com.sun.javafx.util.Logging;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PrintClassLoader {

    public void printClassLoaders() throws ClassNotFoundException {
        System.out.println("class loader of this class: " + this.getClass().getClassLoader());
        System.out.println("class loader of class in classpath: " + LoggerFactory.class.getClassLoader());
        System.out.println("class loader of logging: " + Logging.class.getClassLoader());
        System.out.println("class loader of ArrayList: " + ArrayList.class.getClassLoader());
    }

    protected void test() {

    }
}

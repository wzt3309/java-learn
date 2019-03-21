package core.classloader;

import core.classloader.PrintClassLoader;
import org.junit.Test;

public class PrintClassLoaderTest {

    @Test
    public void givenClassName_thenPrintClassloader() throws Exception {
        PrintClassLoader pcl = (PrintClassLoader) Class.forName(PrintClassLoader.class.getName()).newInstance();
        pcl.printClassLoaders();
    }

    @Test(expected = ClassNotFoundException.class)
    public void givenClassName_whenLoadByParentClassLoader_thenThrowException() throws ClassNotFoundException {
        // delegate model, the parent classloader can't find PrintClassLoader
        Class.forName(PrintClassLoader.class.getName(), true,
                PrintClassLoader.class.getClassLoader().getParent());
    }
}

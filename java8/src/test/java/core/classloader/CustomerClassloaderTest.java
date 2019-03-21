package core.classloader;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class CustomerClassloaderTest {

    @Test
    public void customClassLoader() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        CustomerClassloader customerClassloader = new CustomerClassloader();
        Class<?> clazz = customerClassloader.getClass("core.classloader.PrintClassLoader", false);
        Object obj = clazz.newInstance();
        Method md = clazz.getMethod("printClassLoaders");
        md.invoke(obj);
    }

}
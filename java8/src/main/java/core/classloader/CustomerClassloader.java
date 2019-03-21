package core.classloader;

import java.io.*;

public class CustomerClassloader extends ClassLoader {

    public Class<?> getClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            byte[] b = loadClassFromFile(name);
            if (b != null) {
                // convert byte to Class must use this method and this method is native
                // to check this classloader can access this class
                c = defineClass(name, b, 0, b.length);
                if (resolve) {
                    resolveClass(c);
                }
            }
        }

        return c;
    }

    private byte[] loadClassFromFile(String name) throws ClassNotFoundException {
        if (name == null || name.isEmpty()) {
            throw new ClassNotFoundException();
        }
        final String fileName = name.replace(".", File.separator) + ".class";
        try (InputStream           fin = getClass().getClassLoader().getResourceAsStream(fileName);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (fin == null) {
                throw new ClassNotFoundException();
            }

            byte[] tmp = new byte[256];
            int len;
            while ((len = fin.read(tmp)) != -1) {
                out.write(tmp, 0, len);
            }
            return out.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

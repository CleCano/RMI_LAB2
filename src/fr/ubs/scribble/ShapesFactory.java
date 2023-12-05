package fr.ubs.scribble;

import fr.ubs.scribble.shapes.Shape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * A factory that makes {@link Shape} instances from classes found in the local 
 * classpath
 *
 * @author Pascale Launay
 */
public class ShapesFactory
{
    /**
     * Make shapes objects from the classes in the {@link Shape} package from the 
     * system classpath
     *
     * @return shapes made from the classes found in the system classpath
     */
    public Set<Shape> makeShapes()
    {
        String packagename = Shape.class.getPackageName();
        Set<String> names = listPackage(packagename);
        return makeShapes(names);
    }

    /**
     * Create instances of the given classes and return them if they are Shape 
     * instances
     *
     * @param classnames names of the classes in the same package as Shape
     * @return Shape instances
     */
    private Set<Shape> makeShapes(Set<String> classnames)
    {
        Set<Shape> shapes = new HashSet<>();
        for (String classname : classnames) {
            try {
                Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(classname);
                Object obj = clazz.getDeclaredConstructor().newInstance();
                if (obj instanceof Shape) {
                    shapes.add((Shape) obj);
                }
            } catch (Exception e) {
                // IGNORE
            }
        }
        return shapes;
    }

    /**
     * Give the names of the public classes that belong to the given package
     * found in the system classpath
     *
     * @param packagename the name of the package
     * @return the names of the classes found in the classpath
     */
    private Set<String> listPackage(String packagename)
    {
        Set<String> names = new HashSet<>();
        String classpath = System.getProperty("java.class.path");
        if (classpath != null && !classpath.equals("")) {
            for (String path : classpath.split(System.getProperty("path.separator"))) {
                try {
                    if (new File(path).isDirectory()) {
                        names.addAll(listDirectory(path, packagename));
                    } else if (new File(path).isFile() && path.endsWith(".jar")) {
                        names.addAll(listJarfile(path, packagename));

                    }
                } catch (IOException e) {
                    System.err.println("Error while loading classes from " + path + ": " + e.getClass().getName() + ". " + e.getMessage());
                }
            }
        }
        return names;
    }

    /**
     * Give the names of the public classes that belong to the given package
     * found in the given directory
     *
     * @param path        the directory path
     * @param packagename the name of the package
     * @return the names of the classes found in the directory
     */
    private Set<String> listDirectory(String path, String packagename)
    {
        Set<String> names = new HashSet<>();
        String packagepath = packagename.replaceAll("[.]", "/");
        File dir = new File(path + File.separator + packagepath);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles((dir1, name) -> name.endsWith(".class"))) {
                names.add(removeExtension(packagename + "." + file.getName(), ".class"));
            }
        }
        return names;
    }

    /**
     * Give the names of the public classes that belong to the given package
     * found in the given jar file
     *
     * @param path        the jar file path
     * @param packagename the name of the package
     * @return the names of the classes found in the jar file
     */
    private Set<String> listJarfile(String path, String packagename) throws IOException
    {
        Set<String> names = new HashSet<>();
        String packagepath = packagename.replaceAll("[.]", "/");
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {
                if (entry.getName().startsWith(packagepath) && entry.getName().endsWith(".class")) {
                    names.add(removeExtension(entry.getName().replaceAll("/", "."), ".class"));
                }
                entry = in.getNextEntry();
            }
        }
        return names;
    }

    /**
     * Remove the given extension at the end of the given name
     *
     * @param filename  a file name
     * @param extension a file extension
     * @return the filename without the extension
     */
    private String removeExtension(String filename, String extension)
    {
        if (filename.endsWith(extension)) {
            return filename.substring(0, filename.length() - extension.length());
        }
        return filename;
    }
}

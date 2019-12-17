package com.wyett.common.util;

import org.apache.jute.CsvOutputArchive;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/17 10:31
 * @description: TODO
 */

public class PackageUtilTest {

    private static final String DTO_PACKAGE = "com.wyett.common.dto";

    @Test
    public void test() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.println(loader.getResource("."));


        System.out.println("--------------url---------------------");

//        Enumeration<URL> urls = loader.getResources(DTO_PACKAGE.replace(".", "/"));
        URL url = loader.getResource(DTO_PACKAGE.replace(".", "/"));

        //file:/G:/java/dbmonitorform/monitor-common/target/classes/com/wyett/common/dto
        System.out.println(url);

        System.out.println(url.getProtocol());

        String path = url.getPath();
        System.out.println(path);

        String path1 = URLDecoder.decode(new String(path.getBytes("ISO-8859-1"), "UTF-8"), "UTF-8");
        System.out.println(path1);

        File file = new File(path1);
        File[] files = file.listFiles();
        Arrays.asList(files).stream().forEach(System.out::println);

        Arrays.asList(files).stream()
                .map(f -> f.getName())
                .forEach(System.out::println);
//                .filter(f -> f.endsWith(".class") && !f.contains("$") )
//                .map(f -> DTO_PACKAGE + "." + f.split("."))
//                .forEach(System.out::println);
//                .map(f -> {
//                    try {
//                        System.out.println(f);
//                        return Class.forName(DTO_PACKAGE + "." +f.split(".")[0]);
//                    } catch (ClassNotFoundException e) {
//                        throw new RuntimeException();
//                    }
//                })
//                .collect(Collectors.toList());

        System.out.println("-------------------jar-----------------");

        // jar
        JarFile jarFile = new JarFile("G:\\java\\dbmonitorform\\monitor-common\\target\\monitor-common-1.0-SNAPSHOT" +
                ".jar");

        List<Class<?>> lc = new ArrayList<>();
        lc = jarFile.stream()
                .filter(f -> !f.isDirectory())
                .map(f -> f.getName().replace("/", "."))
                .filter(f -> f.startsWith(DTO_PACKAGE) && f.endsWith(".class") && !f.contains("$"))
                .map(f -> f.replace(".class", ""))
                .map(f -> {
                    try {
                        return Class.forName(f);
                    } catch (ClassNotFoundException e) {
//                        LOG.error("cast classstr " + f + " to class failed");
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        lc.stream().forEach(f -> System.out.println(f));
        lc.stream().forEach(f -> System.out.println(f.getName()));
//        lc.stream().forEach(f -> System.out.println(f.getName().split(".")));
//        lc.stream().forEach(f -> System.out.println(f.getName().lastIndexOf("."),
//                f.getName().length() - f.getName().lastIndexOf(".")));
        lc.stream()
                .map(f -> f.getName().split("\\."))
                .filter(f -> f.length > 0)
                .map(f -> f[f.length - 1])
                .forEach(System.out::println);

        /*
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while(jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
//            System.out.println(jarEntry.getName());


            String jarEntryStr = jarEntry.getName().replace("/", ".");
            if(!jarEntry.isDirectory()
                    && jarEntryStr.startsWith(DTO_PACKAGE)
                    && jarEntryStr.endsWith(".class") && !jarEntryStr.contains("$")) {
                System.out.println(jarEntryStr);

            }

        }
         */

        /*
        while(urls.hasMoreElements()) {
            System.out.println(urls.nextElement());

        }
         */

        // get current path

    }

}

package com.wyett.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author : wyettLei
 * @date : Created in 2019/12/16 18:54
 * @description: TODO
 */

public class PackageUtil {

    private static final Logger LOG = LoggerFactory.getLogger("PackageUtil");

//    private static final String DTO_PACKAGE = "com.wyett.common.dto";

    static {

    }

    /**
     * get class name under package
     * @param packageName
     * @param isRecursion
     * @param postfix
     * @return
     */
    public static List<Class<?>>
    getClassListInPackage(String packageName, boolean isRecursion, String postfix) throws IOException, ClassNotFoundException {

        LOG.info("get Class in package " + packageName);

        List<Class<?>> lct = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packageName.replace(".", "/"));

        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                lct = getClassNameFromFile(url.getPath(), packageName, isRecursion, postfix);
            } else if (protocol.equals("jar")) {
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                lct = getClassNameFromJar(jarFile, packageName, isRecursion, postfix);
            }
        }

        return lct;
    }

    /**
     * get class name from package under folder
     * @param path
     * @param packageName
     * @param isRecursion
     * @param postfix
     * @return
     */
    private static List<Class<?>>
    getClassNameFromFile(String path, String packageName, boolean isRecursion, String postfix) {
        List<Class<?>> lct = new ArrayList<>();

        try {
            path = java.net.URLDecoder.decode(
                    new String(path.getBytes("ISO-8859-1"),"UTF-8"),"UTF-8");

            File file =new File(path);
            File[] files = file.listFiles();

            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    if (isRecursion) {
                        lct.addAll(getClassNameFromFile(childFile.getPath(),
                                packageName + "." +childFile.getName(), isRecursion, postfix));
                    }
                } else {
                    String fileName = childFile.getName();
                    if (fileName.endsWith(postfix) && !fileName.contains("$")) {
                        lct.add(Class.forName(fileName));
                    }
                }
            }
        }catch (UnsupportedEncodingException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lct;
    }

    /**
     * get class Name from jar
     * @param jarFile
     * @param packageName
     * @param isRecursion
     * @param postfix
     * @return
     */
    private static List<Class<?>>
    getClassNameFromJar(JarFile jarFile, String packageName, boolean isRecursion, String postfix) throws ClassNotFoundException {
        LOG.info("get Class List from jar " + jarFile.getName());

        List<Class<?>> lc = new ArrayList<>();

        lc = jarFile.stream()
                .filter(f -> !f.isDirectory())
                .map(f -> f.getName().replace("/", "."))
                .filter(f -> f.startsWith(packageName) && f.endsWith(postfix) && !f.contains("$"))
                .map(f -> f.replace(postfix, ""))
                .map(f -> {
                    try {
                        return Class.forName(f);
                    } catch (ClassNotFoundException e) {
                        LOG.error("cast classstr " + f + " to class failed");
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        return lc;
    }

    /**
     * cast List<Class<?>> to List<String>
     * @param lc
     * @return
     */
    public static List<String> convertClassToString(List<Class<?>> lc) {
        return lc.stream()
                .map(f -> f.getName().split("\\."))
                .filter(f -> f.length > 0)
                .map(f -> f[f.length - 1])
                .collect(Collectors.toList());
    }
}

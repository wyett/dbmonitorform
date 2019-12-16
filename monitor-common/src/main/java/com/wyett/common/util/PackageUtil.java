package com.wyett.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
     * @param isRecurse
     * @param postfix
     * @param <T>
     * @return
     */
    public static <T> List<Class<T>>
    getClassListInPackage(String packageName, boolean isRecursion, String postfix) throws IOException {

        List<Class<T>> lct = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packageName.replaceAll(".", "/"));

        if (url != null) {
            String protocol = url.getProtocol();
            if (protocol.equals("file")) {
                lct =getClassNameFromFile(url.getPath(), packageName, isRecursion, postfix);
            } else if (protocol.equals("jar")) {

            }
        }



        return null;
    }

    private static <T> List<Class<T>>
    getClassNameFromFile(String path, String packageName, boolean isRecursion, String postfix) {
        List<Class<T>> lct = new ArrayList<>();

        try {
            path = java.net.URLDecoder.decode(
                    new String(path.getBytes("ISO-8859-1"),"UTF-8"),"UTF-8");

            File file =new File(path);
            File[] files = file.listFiles();

            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    if (isRecursion) {
                        lct.addAll(getClassNameFromFile(childFile.getPath(),
                                packageName+"."+childFile.getName(), isRecursion, postfix));
                    }
                } else {
                    String fileName = childFile.getName();
                    if (fileName.endsWith(postfix) && !fileName.contains("$")) {
                        lct.add(fileName);
                    }
                }
            }
        }catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }
        return lct;
    }

    private static String getClassNameFromJar() {

    }
}

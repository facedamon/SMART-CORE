package com.facedamon.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
* @Description:    类加载工具
* @Author:         facedamon
* @CreateDate:     2018/8/1 12:29
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 12:29
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class ClassUtil {

    /**
     * 获取类加载器
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 通过类名加载
     * @param className 类名
     * @param isInitialized 是否立即加载
     * @throws ClassNotFoundException 主类未找到
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized) throws ClassNotFoundException {
        if (StringUtils.isBlank(className)){
            log.error("the className should be not empty");
            return null;
        }
        return Class.forName(className,isInitialized,getClassLoader());
    }

    /**
     * 静默加载类
     * @param className 类名
     * @param isInitialized 是否立即加载
     * @return
     */
    public static Class<?> loadClassQuietly(String className,boolean isInitialized){
        Class<?> clazz = null;
        try {
            clazz = loadClass(className,isInitialized);
        } catch (ClassNotFoundException e) {
            log.error("load class:{} failured",className);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 默认懒加载类
     * @param className 类名
     * @return
     * @throws ClassNotFoundException 主类未找到
     */
    public static Class<?> loadClassLazy(String className) throws ClassNotFoundException {
        return loadClass(className,false);
    }

    /**
     * 静默懒加载类
     * @param className 类名
     * @return
     */
    public static Class<?> loadClassLazyQuietly(String className){
        Class<?> clazz = null;
        try {
            clazz = loadClassLazy(className);
        } catch (ClassNotFoundException e) {
            log.error("lazy load class:{} failured",className);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 获取指定包名下的所有类
     * @param packageName 包名
     * @throws IOException 加载包路径异常
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName) throws IOException {
        if (StringUtils.isBlank(packageName)){
            log.error("the packageName should ne not empty");
            return null;
        }
        Set<Class<?>> classSet = new HashSet<>();
        Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
        while (urls.hasMoreElements()){
            URL url = urls.nextElement();
            if (null != url){
                String protocol = url.getProtocol();
                if (StringUtils.equalsIgnoreCase(protocol,"file")){
                    String packagePath = url.getPath().replaceAll("%20","");
                    addClass(classSet,packagePath,packageName);
                }
                if (StringUtils.equalsIgnoreCase(protocol,"jar")){
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (null != jarURLConnection){
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (null != jarFile){
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()){
                                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                String jarEntityName = jarEntry.getName();
                                if (StringUtils.isNotBlank(jarEntityName)
                                        && StringUtils.endsWith(jarEntityName,".class")){
                                    String className = jarEntityName.substring(0,jarEntityName.lastIndexOf("."))
                                            .replaceAll("/",".");
                                    addClass(classSet,className);
                                }
                            }
                        }
                    }
                }
            }
        }
        return classSet;
    }

    /**
     * 静默获取指定包名下的所有类
     * @param packageName 包名
     * @return
     */
    public static Set<Class<?>> getClassSetQuietly(String packageName){
        Set<Class<?>> classSet = null;
        try {
            classSet = getClassSet(packageName);
        } catch (IOException e) {
            log.error("get class set failured,packageName:{}",packageName);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    /**
     * 添加packagepath路径中全限定名为package+className的类到Set集合中
     * @param classSet 返回的加载主类集合
     * @param packagePath 包路径
     * @param packageName 包名
     */
    public static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
       File[] files = new File(packagePath).listFiles(
            (File f) -> {return f.isFile() && f.getName().endsWith(".class") || f.isDirectory();}
       );

       for (File file : files){
           String fileName = file.getName();
           if (file.isFile()){
               String className = fileName.substring(0,fileName.lastIndexOf("."));
               if (StringUtils.isNotBlank(packageName)){
                   className = packageName + "." + className;
               }
               addClass(classSet,className);
               /**
                * 文件夹
                 */
           }else{
                String subPackagePath = fileName;
                if (StringUtils.isNotBlank(packagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotBlank(packageName)){
                    subPackageName = packageName + "." + subPackageName;
                }
               /**
                * 递归调用
                */
               addClass(classSet, subPackagePath, subPackageName);
           }
       }
    }

    /**
     * 添加className的类模板到Set集合中
     * @param classSet 返回的加载主类集合
     * @param className 类名
     */
    public static void addClass(Set<Class<?>> classSet,String className){
        classSet.add(loadClassLazyQuietly(className));
    }
}

package com.vdzon.administratie.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Object getInstance(Class clazz) {
        try {
            return  clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Kon geen instantie maken");
    }

    public static Object callMethod(Method method, Object instance, Object... args) {
        try {
            return method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Kon methode niet aanroepen");
    }


}

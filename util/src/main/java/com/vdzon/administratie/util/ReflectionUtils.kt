package com.vdzon.administratie.util

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object ReflectionUtils {

    fun getInstance(clazz: Class<*>): Any {
        try {
            return clazz.newInstance()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        throw RuntimeException("Kon geen instantie maken")
    }

    fun callMethod(method: Method, instance: Any, vararg args: Any): Any? {
        try {
            return method.invoke(instance, *args)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        throw RuntimeException("Kon methode niet aanroepen")
    }


}

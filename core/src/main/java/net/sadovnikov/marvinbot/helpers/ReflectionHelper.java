package net.sadovnikov.marvinbot.helpers;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {

    /**
     * Gets the first generic between class parents and return's its first parameter type
     * @param clazz
     * @return
     */
    public static String getGenericParameterClass(Class clazz) {

        Type type = clazz.getGenericSuperclass();
        while (!(type instanceof ParameterizedType)) {
            clazz = clazz.getSuperclass();
            if (clazz == null) {
                return null;
            }
            type = clazz.getGenericSuperclass();
        }
        String genericTypeName  = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();

        return genericTypeName;
    }
}

package pl.ark.chr.buginator.util;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public class HibernateLazyInitiator {

    private final static Logger logger = LoggerFactory.getLogger(HibernateLazyInitiator.class);
    private static final int DEFAULT_DEPTH = 2;
    private final static String packageName = "pl.ark.chr.buginator";

    public static void initList(List<?> entites) {
        for (Object entity : entites) {
            init(entity);
        }
    }

    public static void init(Object entity) {
        init(entity, DEFAULT_DEPTH);
    }

    public static void initList(List<?> entites, int depth) {
        for (Object entity : entites) {
            init(entity, depth);
        }
    }

    public static void init(Object entity, int depth) {
        if (isObjectInvalid(entity, depth)) {
            return;
        }
        try {
            Hibernate.initialize(entity);
            Class klazz = entity.getClass();
            Field[] fields = klazz.getDeclaredFields();
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                if (isProxyObject(field.get(entity))) {
                    init(field.get(entity), depth - 1);
                }

                if (isCollectionObject(field.get(entity))) {
                    Collection collection = (Collection) field.get(entity);
                    for (Object o : collection) {
                        init(o, depth - 1);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void initListWithSuperclassFields(List<?> entites) {
        for (Object entity : entites) {
            initWithSuperclassFields(entity);
        }
    }

    public static void initListWithSuperclassFields(List<?> entites, int depth) {
        for (Object entity : entites) {
            initWithSuperclassFields(entity, depth);
        }
    }

    public static void initWithSuperclassFields(Object entity) {
        initWithSuperclassFields(entity, DEFAULT_DEPTH);
    }

    public static void initWithSuperclassFields(Object entity, int depth) {
        if (isObjectInvalid(entity, depth)) {
            return;
        }
        try {
            Hibernate.initialize(entity);
            Class klazz = entity.getClass();
            Field[] fields = klazz.getDeclaredFields();
            List<Field> allFields = new ArrayList<>(Arrays.asList(fields));
            Class superclass = klazz.getSuperclass();
            while (superclass != null) {
                Field[] superFields = superclass.getDeclaredFields();
                allFields.addAll(new ArrayList<>(Arrays.asList(superFields)));
                superclass = superclass.getSuperclass();
            }
            for (Field field : allFields) {
                ReflectionUtils.makeAccessible(field);
                if (isProxyObject(field.get(entity))) {
                    init(field.get(entity), depth - 1);
                }

                if (isCollectionObject(field.get(entity))) {
                    Collection collection = (Collection) field.get(entity);
                    for (Object o : collection) {
                        init(o, depth - 1);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static boolean isProxyObject(Object entity) {
        return entity instanceof HibernateProxy;
    }

    private static boolean isCollectionObject(Object entity) {
        return entity instanceof Collection;
    }

    private static <T> boolean isObjectInvalid(T entity, int depth) {
        return 0 == depth || null == entity || !isPackageMember(entity.getClass().getName(), packageName);
    }

    private static boolean isPackageMember(String className, String packageName) {
        if (!className.contains(".")) {
            return packageName == null || packageName.isEmpty();
        } else {
            String classPackage = className.substring(0, className.lastIndexOf('.'));
            return classPackage.contains(packageName);
        }
    }
}

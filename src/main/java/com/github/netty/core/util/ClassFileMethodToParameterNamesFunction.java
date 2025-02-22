package com.github.netty.core.util;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * read class file - based method variable parameter name function
 *
 * @author wangzihao
 */
public class ClassFileMethodToParameterNamesFunction implements Function<Method, String[]> {
    private static final String[] EMPTY = {};
    private final Map<Class<?>, Map<java.lang.reflect.Member, String[]>> parameterNamesCache = new ConcurrentReferenceHashMap<>(
            16, ConcurrentReferenceHashMap.ReferenceType.WEAK);

    public static Map<java.lang.reflect.Member, String[]> readParameterNameMap(Class<?> clazz) {
        try {
            JavaClassFile javaClassFile = new JavaClassFile(clazz);
            Map<java.lang.reflect.Member, String[]> result = new HashMap<>(6);
            for (JavaClassFile.Member member : javaClassFile.getMethods()) {
                result.put(member.toJavaMember(), member.getParameterNames());
            }
            return result;
        } catch (ClassNotFoundException | IOException | IllegalClassFormatException e) {
            return Collections.emptyMap();
        }
    }

    public static void main(String[] args) {
        Map<java.lang.reflect.Member, String[]> memberMap = readParameterNameMap(ClassFileMethodToParameterNamesFunction.class);
        System.out.println("memberMap = " + memberMap);
    }

    @Override
    public String[] apply(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.isInterface()) {
            return EMPTY;
        }
        Map<java.lang.reflect.Member, String[]> memberMap = parameterNamesCache.get(declaringClass);
        if (memberMap == null) {
            memberMap = readParameterNameMap(declaringClass);
            parameterNamesCache.put(declaringClass, memberMap);
        }
        String[] parameterNames = memberMap.get(method);
        if (parameterNames == null) {
            throw new IllegalStateException("bad method!. object=" + method.getDeclaringClass() + ",method=" + method);
        }
        return parameterNames;
    }
}

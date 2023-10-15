package br.com.felipe.todolist.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertuNames(source));
    }

    // get all attributes that is null
    private static String[] getNullPropertuNames(Object source) {
        final BeanWrapper beanWrapperSource = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapperSource.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (var propertyDescriptor : propertyDescriptors) {
            var sourceValue = beanWrapperSource.getPropertyValue(propertyDescriptor.getName());
            if (sourceValue == null) {
                emptyNames.add(propertyDescriptor.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}

package com.zterc.uos.test.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * 
 * element����ӳ��ʵ���������Թ�����
 * 
 * @author gong.yi
 * 
 */
public class DomUtil {

	/**
	 * ��Element��Attrӳ��ΪObject��Property
	 * 
	 * @param obj
	 * @param root
	 */
	public static void initAttr(Object obj, Element root) {
		Method[] methods = obj.getClass().getMethods();
		@SuppressWarnings("unchecked")
		List<Attribute> list = root.attributes();
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).getName();
			String methodName = "set" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);

			for (int j = 0; j < methods.length; j++) {
				if (methods[j].getName().equals(methodName)) {
					try {
						methods[j].invoke(obj, list.get(i).getValue());
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

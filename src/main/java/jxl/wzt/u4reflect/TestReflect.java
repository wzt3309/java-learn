package jxl.wzt.u4reflect;

import java.lang.reflect.Field;

public class TestReflect {

	public static void main(String[] args) throws Exception {
		Field[] fields = Person.class.getDeclaredFields();
		Person obj = Person.class.newInstance();
		for(Field field: fields) {
			if(field.getName().equals("name")) {
				field.setAccessible(true);
				field.set(obj, "wzt");
			}
		}
	}

}

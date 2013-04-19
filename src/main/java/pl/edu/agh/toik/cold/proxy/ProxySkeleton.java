package pl.edu.agh.toik.cold.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxySkeleton {
	
	public static void main(String args[]) {
		
		try {
			Class<?> klass = Class.forName("pl.edu.agh.toik.cold.proxy.ProxyListener");
			Object t = klass.newInstance();
			
			Method method = klass.getDeclaredMethod("printSth", String.class, String.class);
			
			Object o = method.invoke(t, "Ala", "ma kota");
			System.out.println((int)o);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int printSth(String sth, String sth2) {
		System.out.println(sth + " " + sth2);
		
		return 123;
	}

}

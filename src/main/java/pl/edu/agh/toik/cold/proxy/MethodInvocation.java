package pl.edu.agh.toik.cold.proxy;

import java.io.Serializable;

public class MethodInvocation implements Serializable {

	private static final long serialVersionUID = -9156639640443314868L;
	
	private Class<?> klass;
	private String beanId;
	private Class<?>[] argumentTypes;
	private Object[] argumentValues;

	public MethodInvocation(Class<?> className, String beanId,
			Class<?>[] argumentTypes, Object[] argumentValues) {

		this.klass = className;
		this.beanId = beanId;
		this.argumentTypes = argumentTypes;
		this.argumentValues = argumentValues;
	}

	public Class<?> getKlass() {
		return klass;
	}

	public String getBeanId() {
		return beanId;
	}

	public Class<?>[] getArgumentTypes() {
		return argumentTypes;
	}

	public Object[] getArgumentValues() {
		return argumentValues;
	}

}

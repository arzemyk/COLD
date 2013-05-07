package pl.edu.agh.toik.cold.proxy;

import java.io.Serializable;

public class MethodInvocation implements Serializable {

	private static final long serialVersionUID = -9156639640443314868L;

	private Class<?> targetClass;
	private String targetMethodName;
	private String beanId;
	private Class<?>[] argumentTypes;
	private Object[] argumentValues;

	public MethodInvocation(Class<?> targetClass, String targetMethodName,
			String beanId, Class<?>[] argumentTypes, Object[] argumentValues) {

		this.targetClass = targetClass;
		this.targetMethodName = targetMethodName;
		this.beanId = beanId;
		this.argumentTypes = argumentTypes;
		this.argumentValues = argumentValues;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public String getTargetMethodName() {
		return targetMethodName;
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

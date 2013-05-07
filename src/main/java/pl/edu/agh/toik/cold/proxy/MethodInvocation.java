package pl.edu.agh.toik.cold.proxy;

import java.io.Serializable;

public class MethodInvocation implements Serializable {

	private static final long serialVersionUID = -9156639640443314868L;

	private Class<?> targetClass;
	private String targetMethodName;
	private String beanId;
	private Class<?>[] parametersTypes;
	private Object[] parametersValues;

	public MethodInvocation(Class<?> targetClass, String targetMethodName,
			String beanId, Class<?>[] parametersTypes, Object[] parametersValues) {

		this.targetClass = targetClass;
		this.targetMethodName = targetMethodName;
		this.beanId = beanId;
		this.parametersTypes = parametersTypes;
		this.parametersValues = parametersValues;
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

	public Class<?>[] getParametersTypes() {
		return parametersTypes;
	}

	public Object[] getParametersValues() {
		return parametersValues;
	}

}

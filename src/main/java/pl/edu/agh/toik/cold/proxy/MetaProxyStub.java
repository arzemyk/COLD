package pl.edu.agh.toik.cold.proxy;

public class MetaProxyStub {

	private Class<?> klass;
	
	private String beanId;
	
	private String remoteHost;
	
	private int remotePort;
	
	public MetaProxyStub(Class<?> klass, String beanId, String remoteHost,
			int remotePort) {
		super();
		this.klass = klass;
		this.beanId = beanId;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	public Class<?> getKlass() {
		return klass;
	}

	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}

	public String getBeanId() {
		return beanId;
	}

	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	
	
}

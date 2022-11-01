/*
 * Copyright 2022-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package plus.wcj.apisix.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.util.StringUtils;

/**
 * @author changjin wei(魏昌进)
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 */
@ConfigurationProperties("spring.cloud.apisix.discovery")
public class ApisixDiscoveryProperties {

	/**
	 * Apache APISIX admin api address.
	 */
	private String address = "http://127.0.0.1:9180";

	/**
	 * Apache APISIX control api address.
	 */
	private String controlAddress = "http://127.0.0.1:9090";

	/**
	 * Apache APISIX admin api token.
	 */
	private String token = "edd1c9f034335f136f87ad84b625c8f1";

	/**
	 * Name and ID registered to upstream and route.
	 */
	@Value("${spring.cloud.apisix.discovery.service:${spring.application.name:}}")
	private String service;

	/**
	 * Weight of upstream node.
	 */
	private float weight = 1;

	/**
	 * Use ip address rather than hostname during registration.
	 */
	private boolean preferIpAddress = true;

	/**
	 * IP address to use when accessing service.
	 */
	private String ip;

	/**
	 * Host address to use when accessing service.
	 */
	private String host;

	/**
	 * Port to use when accessing service.
	 */
	private int port;

	/**
	 * scheme used when communicating with upstream, the default is
	 * {@link SchemeEnum#HTTP}.
	 */
	private SchemeEnum scheme = SchemeEnum.HTTP;

	/**
	 * Throw exceptions during config lookup if true, otherwise, log warnings.
	 */
	private boolean failFast = true;

	/**
	 * Is service discovery enabled?
	 */
	private boolean enabled = true;

	/**
	 * Register upstream and route to Apache APISIX.
	 */
	private boolean registerEnabled = true;

	/**
	 * Enable active health check when creating upstream.
	 */
	private boolean activeHealthCheck = false;

	/**
	 * Enable passive health check when creating upstream.
	 */
	private boolean passiveHealthCheck = false;

	/**
	 * Path to enable active health check when creating upstream.
	 */
	private String activeHealthCheckPath = "/apisix";

	// private Map<String, String> metadata = new HashMap<>();

	public ApisixDiscoveryProperties(InetUtils inetUtils) {
		InetUtils.HostInfo hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
		this.host = hostInfo.getHostname();
		this.ip = hostInfo.getIpAddress();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getControlAddress() {
		return controlAddress;
	}

	public void setControlAddress(String controlAddress) {
		this.controlAddress = controlAddress;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public boolean isPreferIpAddress() {
		return preferIpAddress;
	}

	public void setPreferIpAddress(boolean preferIpAddress) {
		this.preferIpAddress = preferIpAddress;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		if (this.preferIpAddress && StringUtils.hasText(this.ip)) {
			return this.ip;
		}
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public SchemeEnum getScheme() {
		return scheme;
	}

	public void setScheme(SchemeEnum scheme) {
		this.scheme = scheme;
	}

	public boolean isFailFast() {
		return failFast;
	}

	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRegisterEnabled() {
		return registerEnabled;
	}

	public void setRegisterEnabled(boolean registerEnabled) {
		this.registerEnabled = registerEnabled;
	}

	public boolean isActiveHealthCheck() {
		return activeHealthCheck;
	}

	public void setActiveHealthCheck(boolean activeHealthCheck) {
		this.activeHealthCheck = activeHealthCheck;
	}

	public boolean isPassiveHealthCheck() {
		return passiveHealthCheck;
	}

	public void setPassiveHealthCheck(boolean passiveHealthCheck) {
		this.passiveHealthCheck = passiveHealthCheck;
	}

	public String getActiveHealthCheckPath() {
		return activeHealthCheckPath;
	}

	public void setActiveHealthCheckPath(String activeHealthCheckPath) {
		this.activeHealthCheckPath = activeHealthCheckPath;
	}

	@Override
	public String toString() {
		return "ApisixDiscoveryProperties{" + "address='" + address + '\'' + ", controlAddress='" + controlAddress
				+ '\'' + ", token='" + token + '\'' + ", service='" + service + '\'' + ", weight='" + weight + '\''
				+ ", preferIpAddress=" + preferIpAddress + ", ip='" + ip + '\'' + ", host='" + host + '\'' + ", port="
				+ port + ", scheme=" + scheme + ", failFast=" + failFast + ", enabled=" + enabled + ", registerEnabled="
				+ registerEnabled + ", activeHealthCheck=" + activeHealthCheck + ", passiveHealthCheck="
				+ passiveHealthCheck + '}';
	}

}

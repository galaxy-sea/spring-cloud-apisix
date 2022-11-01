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

import java.net.URI;
import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;

/**
 * @author changjin wei(魏昌进)
 */
public class ApisixServiceInstance implements ServiceInstance {

	String instanceId;

	String serviceId;

	String scheme;

	String host;

	int port;

	private int weight;

	// Map<String, String> metadata;

	@Override
	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Override
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public boolean isSecure() {
		return SchemeEnum.isScheme(scheme);
	}

	@Override
	public URI getUri() {
		String uri = String.format("%s://%s:%s", scheme, host, port);
		return URI.create(uri);
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public Map<String, String> getMetadata() {
		return null;
	}

	// public void setMetadata(Map<String, String> metadata) {
	// this.metadata = metadata;
	// }

}

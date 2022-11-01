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

package plus.wcj.apisix.serviceregistry;

import java.net.URI;
import java.util.Map;

import plus.wcj.apisix.core.ApisixDiscoveryProperties;

import org.springframework.cloud.client.serviceregistry.Registration;

/**
 * @author changjin wei(魏昌进)
 */
public class ApisixRegistration implements Registration {

	private ApisixDiscoveryProperties apisixDiscoveryProperties;

	public ApisixRegistration(ApisixDiscoveryProperties apisixDiscoveryProperties) {
		this.apisixDiscoveryProperties = apisixDiscoveryProperties;
	}

	@Override
	public String getInstanceId() {
		return apisixDiscoveryProperties.getService() + "@" + apisixDiscoveryProperties.getHost() + ":"
				+ apisixDiscoveryProperties.getPort();
	}

	@Override
	public String getServiceId() {
		return apisixDiscoveryProperties.getService();
	}

	@Override
	public String getHost() {
		return apisixDiscoveryProperties.getHost();
	}

	@Override
	public int getPort() {
		return apisixDiscoveryProperties.getPort();
	}

	public void setPort(int port) {
		this.apisixDiscoveryProperties.setPort(port);
	}

	@Override
	public boolean isSecure() {
		return apisixDiscoveryProperties.getScheme().isSecure();
	}

	@Override
	public URI getUri() {
		String uri = String.format("%s://%s:%s", getScheme(), getHost(), getPort());
		return URI.create(uri);
	}

	@Override
	public Map<String, String> getMetadata() {
		return null;
	}

	@Override
	public String getScheme() {
		return apisixDiscoveryProperties.getScheme().getScheme();
	}

	public ApisixDiscoveryProperties getApisixDiscoveryProperties() {
		return apisixDiscoveryProperties;
	}

}

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

package plus.wcj.apisix.loadbalancer;

import plus.wcj.apisix.core.SchemeEnum;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/10/27
 */
public class ApisixGatewayConfig {

	private boolean enabled = true;

	private SchemeEnum scheme;

	private String host;

	private int port;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public SchemeEnum getScheme() {
		return scheme;
	}

	public void setScheme(SchemeEnum scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
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

	@Override
	public String toString() {
		return "LoadBalancerApisixGatewayConfig{" + "enabled=" + enabled + ", scheme=" + scheme + ", host='" + host
				+ '\'' + ", port='" + port + '\'' + '}';
	}

}

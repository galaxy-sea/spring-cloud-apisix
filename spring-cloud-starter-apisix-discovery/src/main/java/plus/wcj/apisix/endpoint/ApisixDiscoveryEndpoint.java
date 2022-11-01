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

package plus.wcj.apisix.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.core.ApisixClient;
import plus.wcj.apisix.core.ApisixDiscoveryProperties;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

/**
 * @author changjin wei(魏昌进)
 */
@Endpoint(id = "apisix")
public class ApisixDiscoveryEndpoint {

	private static final Logger log = LoggerFactory.getLogger(ApisixDiscoveryEndpoint.class);

	private ApisixDiscoveryProperties apisixDiscoveryProperties;

	private ApisixClient apisixClient;

	public ApisixDiscoveryEndpoint(ApisixDiscoveryProperties apisixDiscoveryProperties, ApisixClient apisixClient) {
		this.apisixDiscoveryProperties = apisixDiscoveryProperties;
		this.apisixClient = apisixClient;
	}

	@ReadOperation
	public Map<String, Object> invoke() {
		String service = apisixDiscoveryProperties.getService();

		Map<String, Object> apisixInfo = new HashMap<>(8);
		try {
			apisixInfo.put("apisixDiscoveryProperties", apisixDiscoveryProperties);
			apisixInfo.put("upstream", apisixClient.getUpstream(service));
			apisixInfo.put("route", apisixClient.getRoute(service));
			apisixInfo.put("instances", apisixClient.getAllInstance(service));
		}
		catch (Exception e) {
			log.error("apisix endpoint error: {}", e.getMessage());
		}
		return apisixInfo;
	}

}

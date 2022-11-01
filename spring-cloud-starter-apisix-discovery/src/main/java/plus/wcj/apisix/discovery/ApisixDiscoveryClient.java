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

package plus.wcj.apisix.discovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import plus.wcj.apisix.core.ApisixClient;
import plus.wcj.apisix.core.ApisixServiceInstance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * @author changjin wei(魏昌进)
 */
public class ApisixDiscoveryClient implements DiscoveryClient {

	private ApisixClient apisixClient;

	public ApisixDiscoveryClient(ApisixClient apisixClient) {
		this.apisixClient = apisixClient;
	}

	@Override
	public String description() {
		return "Spring Cloud Apisix Discovery Client";
	}

	@Override
	public List<ServiceInstance> getInstances(String serviceId) {
		JsonNode allInstance = apisixClient.getAllInstance(serviceId);
		ObjectNode upstream = apisixClient.getUpstream(serviceId);
		return toServiceInstance(allInstance, upstream, serviceId);
	}

	@Override
	public List<String> getServices() {
		return apisixClient.getAllServiceName();
	}

	protected List<ServiceInstance> toServiceInstance(JsonNode allInstance, ObjectNode upstream, String serviceId) {

		if (allInstance.isEmpty()) {
			return Collections.emptyList();
		}
		String scheme = upstream.get("node").get("value").get("scheme").asText();
		// JsonNode labels = upstream.get("labels")

		List<ServiceInstance> serviceInstances = new ArrayList<>(allInstance.size());
		for (int i = allInstance.size() - 1; i >= 0; i--) {
			JsonNode instance = allInstance.get(i);
			ApisixServiceInstance apisixServiceInstance = new ApisixServiceInstance();

			apisixServiceInstance.setInstanceId(
					serviceId + "@" + instance.get("host").asText() + ":" + instance.get("port").asText());
			apisixServiceInstance.setServiceId(serviceId);
			apisixServiceInstance.setScheme(scheme);
			apisixServiceInstance.setHost(instance.get("host").asText());
			apisixServiceInstance.setPort(instance.get("port").asInt());
			apisixServiceInstance.setWeight(instance.get("weight").asInt());
			// apisixServiceInstance.setMetadata(upstream.get("labels"));

			serviceInstances.add(apisixServiceInstance);
		}

		return serviceInstances;
	}

}

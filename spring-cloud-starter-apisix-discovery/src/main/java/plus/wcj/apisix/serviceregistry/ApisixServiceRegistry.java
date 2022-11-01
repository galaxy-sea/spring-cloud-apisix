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

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.core.ApisixClient;

import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.util.ReflectionUtils;

/**
 * @author changjin wei(魏昌进)
 */
public class ApisixServiceRegistry implements ServiceRegistry<ApisixRegistration> {

	private static final Logger log = LoggerFactory.getLogger(ApisixServiceRegistry.class);

	private ApisixClient apisixClient;

	public ApisixServiceRegistry(ApisixClient apisixClient) {
		this.apisixClient = apisixClient;
	}

	@Override
	public void register(ApisixRegistration registration) {
		try {
			apisixClient.register(registration);
			log.info("apisix registry, {} {}:{} register finished", registration.getServiceId(), registration.getHost(),
					registration.getPort());
		}
		catch (Exception e) {
			if (registration.getApisixDiscoveryProperties().isFailFast()) {
				log.error("apisix registry, {} register failed...{},", registration.getServiceId(), registration, e);
				ReflectionUtils.rethrowRuntimeException(e);
			}
			log.warn("Failfast is false. {} register failed...{},", registration.getServiceId(), registration, e);
		}

	}

	@Override
	public void deregister(ApisixRegistration registration) {
		apisixClient.deregister(registration);
	}

	@Override
	public void close() {

	}

	public void setStatus(ApisixRegistration registration, int status) {
		apisixClient.setStatus(registration, status);
	}

	@Override
	public void setStatus(ApisixRegistration registration, String status) {
		setStatus(registration, Integer.parseInt(status));
	}

	@Override
	public Object getStatus(ApisixRegistration registration) {
		JsonNode allHealthInstance = apisixClient.getAllInstance(registration);

		if (allHealthInstance.isEmpty()) {
			return -1;
		}

		for (int i = allHealthInstance.size() - 1; i >= 0; i--) {
			JsonNode healthInstance = allHealthInstance.get(i);

			if (registration.getPort() == healthInstance.get("port").asInt()
					&& registration.getHost().equals(healthInstance.get("host").asText())) {
				return healthInstance.get("weight").asText();
			}

		}
		return -1;
	}

}

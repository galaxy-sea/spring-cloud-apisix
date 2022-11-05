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

package plus.wcj.apisix.devtools.health.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.devtools.ApisixDevtoolsProperties;

import org.springframework.cloud.client.discovery.event.InstanceRegisteredEvent;
import org.springframework.context.event.EventListener;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/11/5
 */
public class AutoHealthTrigger {

	private static final Logger log = LoggerFactory.getLogger(AutoHealthTrigger.class);

	private HealthTriggerClient healthTriggerClient;

	private ApisixDevtoolsProperties apisixDevtoolsProperties;

	public AutoHealthTrigger(HealthTriggerClient healthTriggerClient,
			ApisixDevtoolsProperties apisixDevtoolsProperties) {
		this.healthTriggerClient = healthTriggerClient;
		this.apisixDevtoolsProperties = apisixDevtoolsProperties;
	}

	@EventListener
	public void healthTrigger(InstanceRegisteredEvent<?> instanceRegisteredEvent) {
		int healthTriggerQuantity = apisixDevtoolsProperties.getHealthTriggerQuantity();

		log.info("Apisix devtools healthcheck");

		for (int i = 0; i < healthTriggerQuantity; i++) {
			healthTriggerClient.healthTrigger();
		}
	}

}

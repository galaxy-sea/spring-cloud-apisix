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

package plus.wcj.apisix.core.customizer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import plus.wcj.apisix.core.ApisixDiscoveryProperties;
import plus.wcj.apisix.serviceregistry.ApisixRegistration;

/**
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public class SimpleHealthCheckCustomizer implements UpstreamCustomizer<ApisixRegistration> {

	@Override
	public void init(ApisixRegistration registration, ObjectNode requestBody) {
		ApisixDiscoveryProperties apisixDiscoveryProperties = registration.getApisixDiscoveryProperties();
		ObjectNode checks = requestBody.objectNode();

		if (apisixDiscoveryProperties.isActiveHealthCheck()) {
			activeHealthCheck(apisixDiscoveryProperties, checks);
		}
		if (apisixDiscoveryProperties.isPassiveHealthCheck()) {
			passiveHealthCheck(apisixDiscoveryProperties, checks);
		}

		if (!checks.isEmpty()) {
			requestBody.set("checks", checks);
		}

	}

	protected void activeHealthCheck(ApisixDiscoveryProperties apisixDiscoveryProperties, ObjectNode checks) {
		ObjectNode active = checks.putObject("active");
		// @formatter:off
		active.put("type", "http")
			.put("timeout", 10)
			.put("concurrency", 10)
			.put("http_path", apisixDiscoveryProperties.getActiveHealthCheckPath());

		active.putObject("healthy")
			.put("interval", 5)
			.put("successes", 1)
			.putArray("http_statuses")
			.add(200)
			.add(302);

		active.putObject("unhealthy")
			.put("timeouts", 3)
			.put("interval", 5)
			.put("http_failures", 5)
			.put("tcp_failures", 2)
			.putArray("http_statuses")
			.add(429)
			.add(404)
			.add(500)
			.add(501)
			.add(502)
			.add(503)
			.add(504)
			.add(505);
		// @formatter:on
	}

	protected void passiveHealthCheck(ApisixDiscoveryProperties apisixDiscoveryProperties, ObjectNode checks) {
		ObjectNode active = checks.putObject("passive");
		// @formatter:off
		active.put("type", "http");

		active.putObject("healthy")
			.put("successes", 5)
			.putArray("http_statuses")
			.add(200)
			.add(201)
			.add(202)
			.add(203)
			.add(204)
			.add(205)
			.add(206)
			.add(207)
			.add(208)
			.add(226)
			.add(300)
			.add(301)
			.add(302)
			.add(303)
			.add(304)
			.add(305)
			.add(306)
			.add(307)
			.add(308);

		active.putObject("unhealthy")
			.put("timeouts", 7)
			.put("tcp_failures", 2)
			.put("http_failures", 2)
			.putArray("http_statuses")
			.add(429)
			.add(500)
			.add(503);
		// @formatter:on
	}

}

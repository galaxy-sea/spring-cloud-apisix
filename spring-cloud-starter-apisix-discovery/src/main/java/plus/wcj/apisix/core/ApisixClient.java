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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.core.api.ControlApi;
import plus.wcj.apisix.core.api.RouteApi;
import plus.wcj.apisix.core.api.UpstreamApi;
import plus.wcj.apisix.core.customizer.RouteCustomizer;
import plus.wcj.apisix.core.customizer.UpstreamCustomizer;
import plus.wcj.apisix.serviceregistry.ApisixRegistration;

/**
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public class ApisixClient {

	private static final Logger log = LoggerFactory.getLogger(ApisixClient.class);

	private UpstreamApi upstreamApi;

	private RouteApi routeApi;

	private ControlApi controlApi;

	private List<RouteCustomizer<ApisixRegistration>> routeCustomizerList;

	private List<UpstreamCustomizer<ApisixRegistration>> upstreamCustomizerList;

	private ObjectMapper objectMapper;

	public ApisixClient(UpstreamApi upstreamApi, RouteApi routeApi, ControlApi controlApi,
			List<RouteCustomizer<ApisixRegistration>> routeCustomizerList,
			List<UpstreamCustomizer<ApisixRegistration>> upstreamCustomizerList, ObjectMapper objectMapper) {
		this.upstreamApi = upstreamApi;
		this.routeApi = routeApi;
		this.controlApi = controlApi;
		this.routeCustomizerList = routeCustomizerList;
		this.upstreamCustomizerList = upstreamCustomizerList;
		this.objectMapper = objectMapper;
	}

	public void register(ApisixRegistration registration) {
		try {
			upstreamApi.patch(registration.getServiceId(), registerUpstreamBody(registration));
		}
		catch (FeignException.NotFound e) {
			log.warn("init upstream and rout. failed to add upstream node: {}", e.getMessage());
			upstreamApi.put(registration.getServiceId(), initUpstreamBody(registration));
			routeApi.put(registration.getServiceId(), initRouteBody(registration));
		}
	}

	public void deregister(ApisixRegistration registration) {
		upstreamApi.patch(registration.getServiceId(), deregisterUpstreamBody(registration));
	}

	public void setStatus(ApisixRegistration registration, int status) {
		upstreamApi.put(registration.getServiceId(), setStatusUpstreamBody(registration, status));
	}

	public List<String> getAllServiceName() {
		ObjectNode allService = upstreamApi.get();
		return toServiceName(allService);
	}

	public JsonNode getAllInstance(ApisixRegistration registration) {
		String serviceId = registration.getServiceId();
		return getAllInstance(serviceId);
	}

	public JsonNode getAllInstance(String serviceId) {
		return controlApi.getHealthcheckUpstream(serviceId).get("healthy_nodes");
	}

	public ObjectNode getUpstream(String serviceId) {
		return upstreamApi.get(serviceId);
	}

	public ObjectNode getRoute(String routeId) {
		return routeApi.get(routeId);
	}

	private List<String> toServiceName(ObjectNode jsonNodes) {
		JsonNode allService = jsonNodes.get("node").get("nodes");
		if (allService.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> nameList = new ArrayList<>();
		for (int i = allService.size() - 1; i >= 0; i--) {
			String name = allService.get(i).get("value").get("id").asText();
			nameList.add(name);
		}
		return nameList;
	}

	private ObjectNode setStatusUpstreamBody(ApisixRegistration registration, int status) {
		ObjectNode body = objectMapper.createObjectNode();
		for (UpstreamCustomizer<ApisixRegistration> upstreamCustomizer : upstreamCustomizerList) {
			upstreamCustomizer.status(registration, status, body);
		}

		if (log.isDebugEnabled()) {
			log.debug("setStatusUpstreamBody: {}", body);
		}

		return body;
	}

	private ObjectNode deregisterUpstreamBody(ApisixRegistration registration) {
		ObjectNode body = objectMapper.createObjectNode();
		for (UpstreamCustomizer<ApisixRegistration> upstreamCustomizer : upstreamCustomizerList) {
			upstreamCustomizer.deregister(registration, body);
		}

		if (log.isDebugEnabled()) {
			log.debug("deregisterUpstreamBody: {}", body);
		}

		return body;
	}

	private ObjectNode initRouteBody(ApisixRegistration registration) {
		ObjectNode body = objectMapper.createObjectNode();
		for (RouteCustomizer<ApisixRegistration> routeCustomizer : routeCustomizerList) {
			routeCustomizer.init(registration, body);
		}

		if (log.isDebugEnabled()) {
			log.debug("initRouteBody: {}", body);
		}

		return body;
	}

	private ObjectNode initUpstreamBody(ApisixRegistration registration) {
		ObjectNode body = objectMapper.createObjectNode();
		for (UpstreamCustomizer<ApisixRegistration> upstreamCustomizer : upstreamCustomizerList) {
			upstreamCustomizer.init(registration, body);
		}

		if (log.isDebugEnabled()) {
			log.debug("initUpstreamBody: {}", body);
		}

		return body;
	}

	private ObjectNode registerUpstreamBody(ApisixRegistration registration) {
		ObjectNode body = objectMapper.createObjectNode();
		for (UpstreamCustomizer<ApisixRegistration> upstreamCustomizer : upstreamCustomizerList) {
			upstreamCustomizer.register(registration, body);
		}

		if (log.isDebugEnabled()) {
			log.debug("registerUpstreamBody: {}", body);
		}
		return body;
	}

}

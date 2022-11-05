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

package plus.wcj.apisix.devtools.port;

import com.fasterxml.jackson.databind.node.ObjectNode;
import plus.wcj.apisix.core.customizer.UpstreamCustomizer;
import plus.wcj.apisix.devtools.ApisixDevtoolsProperties;
import plus.wcj.apisix.serviceregistry.ApisixRegistration;

import org.springframework.core.annotation.Order;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/11/4
 */
@Order(UpstreamCustomizer.DEFAULT_ORDER + 1)
public class MultiplePortUpstreamCustomizer implements UpstreamCustomizer<ApisixRegistration> {

	private ApisixDevtoolsProperties apisixDevtoolsProperties;

	public MultiplePortUpstreamCustomizer(ApisixDevtoolsProperties apisixDevtoolsProperties) {
		this.apisixDevtoolsProperties = apisixDevtoolsProperties;
	}

	@Override
	public void init(ApisixRegistration registration, ObjectNode requestBody) {
		requestBodyAddNode(registration, requestBody, 1);
	}

	@Override
	public void deregister(ApisixRegistration registration, ObjectNode requestBody) {
		ObjectNode nodes = requestBody.with("nodes");
		Integer[] ports = apisixDevtoolsProperties.getPorts();
		if (ports != null) {
			for (Integer port : ports) {
				String node = registration.getHost() + ":" + port;
				nodes.putNull(node);
			}
		}
	}

	@Override
	public void register(ApisixRegistration registration, ObjectNode requestBody) {
		requestBodyAddNode(registration, requestBody, 1);
	}

	@Override
	public void status(ApisixRegistration registration, int status, ObjectNode requestBody) {
		requestBodyAddNode(registration, requestBody, status);
	}

	private void requestBodyAddNode(ApisixRegistration registration, ObjectNode requestBody, int status) {
		ObjectNode nodes = requestBody.with("nodes");
		Integer[] ports = apisixDevtoolsProperties.getPorts();
		if (ports != null) {
			for (Integer port : ports) {
				String node = registration.getHost() + ":" + port;
				nodes.put(node, status);
			}
		}
	}

}

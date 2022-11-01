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
import plus.wcj.apisix.serviceregistry.ApisixRegistration;

/**
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public class SimpleRouteCustomizer implements RouteCustomizer<ApisixRegistration> {

	@Override
	public void init(ApisixRegistration registration, ObjectNode requestBody) {
		String upstreamId = registration.getServiceId();
		// @formatter:off
		requestBody.put("name", upstreamId)
			.put("upstream_id", upstreamId)
			.putArray("uris")
			.add("/" + upstreamId + "/*");
		// @formatter:on
	}

}

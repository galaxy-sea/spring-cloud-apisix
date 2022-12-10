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

package plus.wcj.apisix.core.api;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <a href="https://apisix.apache.org/docs/apisix/control-api/">Control API.</a>
 * <p>
 * In Apache APISIX, the control API is used to:
 * <p>
 * Expose the internal state of APISIX. Control the behavior of a single, isolated APISIX
 * data plane.
 *
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public interface ControlApi {

	/**
	 * api path.
	 */
	String PATH = "/v1";

	/**
	 * Returns a health check of the APISIX instance.
	 * @param id upstream id
	 * @return respectBody
	 */
	@GetMapping("/healthcheck/upstreams/{id}")
	ObjectNode getHealthcheckUpstream(@PathVariable("id") String id);

}

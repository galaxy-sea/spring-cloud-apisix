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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <a href="https://apisix.apache.org/docs/apisix/admin-api/#upstream">Upstream</a>
 * <p>
 * API: /apisix/admin/upstreams/{id} .
 * <p>
 * Upstream is a virtual host abstraction that performs load balancing on a given set of
 * service nodes according to the configured rules.
 * <p>
 * An Upstream configuration can be directly bound to a Route or a Service, but the
 * configuration in Route has a higher priority. This behavior is consistent with priority
 * followed by the Plugin object.
 *
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public interface UpstreamApi {

	/**
	 * api path.
	 */
	String PATH = "/apisix/admin/upstreams";

	/**
	 * Fetch a list of all configured Upstreams.
	 * @return respectBody
	 */
	@GetMapping
	ObjectNode get();

	/**
	 * Fetches specified Upstream by id.
	 * @param id upstream id
	 * @return respectBody
	 */
	@GetMapping("/{id}")
	ObjectNode get(@PathVariable("id") String id);

	/**
	 * Creates an Upstream with the specified id.
	 * @param id upstream id
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PutMapping("/{id}")
	ObjectNode put(@PathVariable("id") String id, @RequestBody ObjectNode requestBody);

	/**
	 * Creates an Upstream and assigns a random id.
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PostMapping
	ObjectNode post(@RequestBody ObjectNode requestBody);

	/**
	 * Removes the Upstream with the specified id.
	 * @param id upstream id
	 * @return respectBody
	 */
	@DeleteMapping("/{id}")
	ObjectNode delete(@PathVariable("id") String id);

	/**
	 * Updates the selected attributes of the specified, existing Upstream. To delete an
	 * attribute, set value of attribute set to null.
	 * @param id upstream id
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PatchMapping("/{id}")
	ObjectNode patch(@PathVariable("id") String id, @RequestBody ObjectNode requestBody);

	/**
	 * Updates the attribute specified in the path. The values of other attributes remain
	 * unchanged.
	 * @param id upstream id
	 * @param path Updates the attribute specified in the path.
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PatchMapping("/{id}/{path}")
	ObjectNode patch(@PathVariable("id") String id, @PathVariable("path") String path,
			@RequestBody ObjectNode requestBody);

}

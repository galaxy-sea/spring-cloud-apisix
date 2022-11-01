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
 * <a href="https://apisix.apache.org/docs/apisix/admin-api/#route">Route.</a>
 * <p>
 * API: /apisix/admin/routes/{id}?ttl=0 .
 * <p>
 * Routes match the client's request based on defined rules, loads and executes the
 * corresponding plugins, and forwards the request to the specified Upstream.
 * <p>
 * Note: When the Admin API is enabled, to avoid conflicts with your design API, use a
 * different port for the Admin API. This can be set in your configuration file by
 * changing the admin_listen key.
 *
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
public interface RouteApi {

	/**
	 * api path.
	 */
	String PATH = "/apisix/admin/routes";

	/**
	 * Fetches a list of all configured Routes.
	 * @return respectBody
	 */
	@GetMapping
	ObjectNode get();

	/**
	 * Fetches specified Route by id.
	 * @param id route id
	 * @return respectBody
	 */
	@GetMapping("/{id}")
	ObjectNode get(@PathVariable("id") String id);

	/**
	 * Creates a Route with the specified id.
	 * @param id route id
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PutMapping("/{id}")
	ObjectNode put(@PathVariable("id") String id, @RequestBody ObjectNode requestBody);

	/**
	 * Creates a Route and assigns a random id.
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PostMapping
	ObjectNode post(@RequestBody ObjectNode requestBody);

	/**
	 * Removes the Route with the specified id.
	 * @param id route id
	 * @return respectBody
	 */
	@DeleteMapping("/{id}")
	ObjectNode delete(@PathVariable("id") String id);

	/**
	 * Updates the selected attributes of the specified, existing Route. To delete an
	 * attribute, set value of attribute set to null.
	 * @param id route id
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PatchMapping("/{id}")
	ObjectNode patch(@PathVariable("id") String id, @RequestBody ObjectNode requestBody);

	/**
	 * Updates the attribute specified in the path. The values of other attributes remain
	 * unchanged.
	 * @param id route id
	 * @param path Updates the attribute specified in the path
	 * @param requestBody requestBody
	 * @return respectBody
	 */
	@PatchMapping("/{id}/{path}")
	ObjectNode patch(@PathVariable("id") String id, @PathVariable("path") String path,
			@RequestBody ObjectNode requestBody);

}

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

package plus.wcj.apisix.core.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import plus.wcj.apisix.core.ApisixDiscoveryProperties;

/**
 * @author changjin wei(魏昌进)
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 */
public class ApisixTokenRequestInterceptor implements RequestInterceptor {

	private ApisixDiscoveryProperties apisixDiscoveryProperties;

	public ApisixTokenRequestInterceptor(ApisixDiscoveryProperties apisixDiscoveryProperties) {
		this.apisixDiscoveryProperties = apisixDiscoveryProperties;
	}

	@Override
	public void apply(RequestTemplate template) {
		template.header("X-API-KEY", apisixDiscoveryProperties.getToken());
	}

}

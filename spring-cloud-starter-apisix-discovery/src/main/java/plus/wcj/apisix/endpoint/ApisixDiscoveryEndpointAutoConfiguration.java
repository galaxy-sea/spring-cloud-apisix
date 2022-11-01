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

package plus.wcj.apisix.endpoint;

import plus.wcj.apisix.core.ApisixClient;
import plus.wcj.apisix.core.ApisixDiscoveryProperties;
import plus.wcj.apisix.core.ConditionalOnApisixEnabled;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Endpoint.class)
@ConditionalOnApisixEnabled
public class ApisixDiscoveryEndpointAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint
	public ApisixDiscoveryEndpoint apisixDiscoveryEndpoint(ApisixDiscoveryProperties apisixDiscoveryProperties,
			ApisixClient apisixClient) {
		return new ApisixDiscoveryEndpoint(apisixDiscoveryProperties, apisixClient);
	}

}

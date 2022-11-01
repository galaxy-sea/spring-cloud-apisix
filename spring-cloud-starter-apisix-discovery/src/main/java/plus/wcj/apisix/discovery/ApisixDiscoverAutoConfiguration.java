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

package plus.wcj.apisix.discovery;

import plus.wcj.apisix.core.ApisixClient;
import plus.wcj.apisix.core.ApisixDiscoveryProperties;
import plus.wcj.apisix.core.ConditionalOnApisixDiscoveryEnabled;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnApisixDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
public class ApisixDiscoverAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ApisixDiscoveryClient apisixDiscoveryClient(ApisixClient apisixClient) {
		return new ApisixDiscoveryClient(apisixClient);
	}

	@Bean
	@ConditionalOnMissingBean
	public ApisixDiscoveryProperties apisixDiscoveryProperties(InetUtils inetUtils) {
		return new ApisixDiscoveryProperties(inetUtils);
	}

}

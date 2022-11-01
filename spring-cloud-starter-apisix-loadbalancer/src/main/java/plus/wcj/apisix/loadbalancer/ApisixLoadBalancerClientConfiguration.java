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

package plus.wcj.apisix.loadbalancer;

import plus.wcj.apisix.core.ConditionalOnApisixDiscoveryEnabled;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnApisixDiscoveryEnabled
@ConditionalOnApisixLoadBalancerEnabled
public class ApisixLoadBalancerClientConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ReactorLoadBalancer<ServiceInstance> apisixLoadBalancer(Environment environment,
			LoadBalancerClientFactory loadBalancerClientFactory) {
		String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new ApisixLoadBalancer(
				loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
	}

}

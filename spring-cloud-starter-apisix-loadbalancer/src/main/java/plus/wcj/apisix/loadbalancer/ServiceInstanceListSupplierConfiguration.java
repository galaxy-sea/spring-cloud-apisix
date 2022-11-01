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

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnApisixLoadBalancerEnabled
public class ServiceInstanceListSupplierConfiguration {

	private static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 193827465;

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBlockingDiscoveryEnabled
	@Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER + 1)
	public static class PolarisBlockingSupportConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnBean(DiscoveryClient.class)
		@ConditionalOnApisixGatewayLoadbalancerEnabled
		public ServiceInstanceListSupplier polarisRouterDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				ApisixGatewayLoadbalancerProperties gatewayLoadbalancerProperties) {
			return new ApisixGatewayServiceInstanceListSupplier(
					ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient().build(context),
					gatewayLoadbalancerProperties);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnReactiveDiscoveryEnabled
	@Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
	public static class ApisixGatewayReactiveSupportConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnBean(ReactiveDiscoveryClient.class)
		@ConditionalOnApisixGatewayLoadbalancerEnabled
		public ServiceInstanceListSupplier polarisRouterDiscoveryClientServiceInstanceListSupplier(
				ConfigurableApplicationContext context,
				ApisixGatewayLoadbalancerProperties gatewayLoadbalancerProperties) {
			return new ApisixGatewayServiceInstanceListSupplier(
					ServiceInstanceListSupplier.builder().withDiscoveryClient().build(context),
					gatewayLoadbalancerProperties);
		}

	}

}

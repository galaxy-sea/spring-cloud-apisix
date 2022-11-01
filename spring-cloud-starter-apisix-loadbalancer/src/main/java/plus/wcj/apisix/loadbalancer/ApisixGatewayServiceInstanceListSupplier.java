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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import plus.wcj.apisix.core.ApisixServiceInstance;
import plus.wcj.apisix.core.SchemeEnum;
import reactor.core.publisher.Flux;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

/**
 * @author changjin wei(魏昌进)
 * @since 2022/10/27
 */
public class ApisixGatewayServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {

	private Map<String, ApisixGatewayConfig> apisixGatewayConfigs;

	public ApisixGatewayServiceInstanceListSupplier(ServiceInstanceListSupplier delegate,
			ApisixGatewayLoadbalancerProperties apisixGatewayLoadbalancerProperties) {
		super(delegate);
		this.apisixGatewayConfigs = apisixGatewayLoadbalancerProperties.getLoadbalancer();
	}

	@Override
	public Flux<List<ServiceInstance>> get() {
		ApisixGatewayConfig apisixGatewayConfig = this.apisixGatewayConfigs.get(getServiceId());
		if (apisixGatewayConfig != null && apisixGatewayConfig.isEnabled()) {
			return Flux.just(apisixGatewayConfig).map(this::toApisixGatewayServiceInstances);
		}
		return getDelegate().get();
	}

	private List<ServiceInstance> toApisixGatewayServiceInstances(ApisixGatewayConfig apisixGatewayConfig) {
		SchemeEnum scheme = apisixGatewayConfig.getScheme();
		String host = apisixGatewayConfig.getHost();
		int port = apisixGatewayConfig.getPort();

		ApisixServiceInstance apisixServiceInstance = new GatewayServiceInstance();
		apisixServiceInstance.setInstanceId(getServiceId() + "@" + host + ":" + port);
		apisixServiceInstance.setServiceId(getServiceId());
		apisixServiceInstance.setScheme(scheme.getScheme());
		apisixServiceInstance.setHost(host);
		apisixServiceInstance.setPort(port);
		apisixServiceInstance.setWeight(1);
		return Collections.singletonList(apisixServiceInstance);
	}

}

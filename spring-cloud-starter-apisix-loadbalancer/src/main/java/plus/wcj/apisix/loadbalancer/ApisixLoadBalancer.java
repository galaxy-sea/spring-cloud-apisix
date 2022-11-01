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

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.core.ApisixServiceInstance;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

/**
 * copy {@link org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer}.
 *
 * @author changjin wei(魏昌进)
 */
public class ApisixLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	private static final Logger log = LoggerFactory.getLogger(ApisixLoadBalancer.class);

	final AtomicInteger position;

	final String serviceId;

	ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	public ApisixLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			String serviceId) {
		this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
	}

	public ApisixLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
			String serviceId, int seedPosition) {
		this.serviceId = serviceId;
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.position = new AtomicInteger(seedPosition);
	}

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
				.getIfAvailable(NoopServiceInstanceListSupplier::new);
		return supplier.get(request).next()
				.map(serviceInstances -> processInstanceResponse(supplier, serviceInstances));
	}

	private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
			List<ServiceInstance> serviceInstances) {
		Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
		if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
			((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
		}
		return serviceInstanceResponse;
	}

	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
		if (instances.isEmpty()) {
			if (log.isWarnEnabled()) {
				log.warn("No servers available for service: " + serviceId);
			}
			return new EmptyResponse();
		}

		List<ServiceInstance> weightInstances = instances.stream()
				.map(serviceInstance -> (ApisixServiceInstance) serviceInstance)
				.filter(apisixServiceInstance -> apisixServiceInstance.getWeight() > 0).collect(Collectors.toList());

		if (weightInstances.isEmpty()) {
			weightInstances = instances;
		}

		int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;

		ServiceInstance instance = weightInstances.get(pos % weightInstances.size());

		return new DefaultResponse(instance);
	}

}

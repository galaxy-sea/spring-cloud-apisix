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

package plus.wcj.apisix.discovery.reactive;

import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.wcj.apisix.core.ApisixClient;
import plus.wcj.apisix.discovery.ApisixDiscoveryClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;

/**
 * @author changjin wei(魏昌进)
 */
public class ApisixReactiveDiscoveryClient implements ReactiveDiscoveryClient {

	private static final Logger log = LoggerFactory.getLogger(ApisixReactiveDiscoveryClient.class);

	private ApisixDiscoveryClient apisixDiscoveryClient;

	public ApisixReactiveDiscoveryClient(ApisixClient apisixClient) {
		this.apisixDiscoveryClient = new ApisixDiscoveryClient(apisixClient);
	}

	@Override
	public String description() {
		return "Spring Cloud APISIX Reactive Discovery Client";
	}

	@Override
	public Flux<ServiceInstance> getInstances(String serviceId) {
		return Mono.justOrEmpty(serviceId).flatMapMany(loadInstancesFromApisix())
				.subscribeOn(Schedulers.boundedElastic());
	}

	@Override
	public Flux<String> getServices() {
		return Flux.defer(() -> {
			try {
				return Flux.fromIterable(apisixDiscoveryClient.getServices());
			}
			catch (Exception e) {
				log.error("Error getting services from APISIX.", e);
				return Flux.empty();
			}
		}).subscribeOn(Schedulers.boundedElastic());
	}

	protected Function<String, Publisher<ServiceInstance>> loadInstancesFromApisix() {
		return serviceId -> {
			try {
				return Flux.fromIterable(apisixDiscoveryClient.getInstances(serviceId));
			}
			catch (Exception e) {
				log.error("Error getting instances from APISIX. Possibly, no service has registered.", e);
				return Flux.empty();
			}
		};
	}

}

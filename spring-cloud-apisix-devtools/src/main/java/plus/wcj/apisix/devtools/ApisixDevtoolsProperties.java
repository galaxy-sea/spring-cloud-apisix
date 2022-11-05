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

package plus.wcj.apisix.devtools;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author changjin wei(魏昌进)
 */
@ConfigurationProperties("spring.cloud.apisix.devtools")
public class ApisixDevtoolsProperties {

	private Integer[] ports = new Integer[] { ThreadLocalRandom.current().nextInt(10000, 65535),
			ThreadLocalRandom.current().nextInt(10000, 65535) };

	private String gatewayAddress = "http://127.0.0.1:9082";

	private int healthTriggerQuantity = 10;

	public Integer[] getPorts() {
		return ports;
	}

	public void setPorts(Integer[] ports) {
		this.ports = ports;
	}

	public int getHealthTriggerQuantity() {
		return healthTriggerQuantity;
	}

	public void setHealthTriggerQuantity(int healthTriggerQuantity) {
		this.healthTriggerQuantity = healthTriggerQuantity;
	}

	public String getGatewayAddress() {
		return gatewayAddress;
	}

	public void setGatewayAddress(String gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
	}

}

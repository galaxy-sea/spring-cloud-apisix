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

import feign.Feign;
import plus.wcj.apisix.core.ConditionalOnApisixEnabled;
import plus.wcj.apisix.core.api.ControlApi;
import plus.wcj.apisix.core.api.RouteApi;
import plus.wcj.apisix.core.api.UpstreamApi;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author changjin wei(魏昌进)
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnApisixEnabled
@ConditionalOnClass(Feign.class)
@ConditionalOnMissingBean({ ControlApi.class, RouteApi.class, UpstreamApi.class })
@EnableFeignClients("plus.wcj.apisix.core.client")
public class ApisixApiAutoConfiguration {

}

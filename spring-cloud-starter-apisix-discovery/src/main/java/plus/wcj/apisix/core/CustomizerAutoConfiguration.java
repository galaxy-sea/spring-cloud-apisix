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

package plus.wcj.apisix.core;

import plus.wcj.apisix.core.customizer.SimpleHealthCheckCustomizer;
import plus.wcj.apisix.core.customizer.SimpleProxyRewriteRouteCustomizer;
import plus.wcj.apisix.core.customizer.SimpleRouteCustomizer;
import plus.wcj.apisix.core.customizer.SimpleUpstreamCustomizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:naown@foxmail.com">chenjian</a>
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
public class CustomizerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SimpleProxyRewriteRouteCustomizer simpleProxyRewriteRouteCustomizer() {
		return new SimpleProxyRewriteRouteCustomizer();
	}

	@Bean
	@ConditionalOnMissingBean
	public SimpleRouteCustomizer simpleRouteCustomizer() {
		return new SimpleRouteCustomizer();
	}

	@Bean
	@ConditionalOnMissingBean
	public SimpleUpstreamCustomizer simpleUpstreamCustomizer() {
		return new SimpleUpstreamCustomizer();
	}

	@Bean
	@ConditionalOnMissingBean
	public SimpleHealthCheckCustomizer simpleHealthCheckCustomizer() {
		return new SimpleHealthCheckCustomizer();
	}

}

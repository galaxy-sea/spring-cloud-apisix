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

package plus.wcj.apisix.devtools.port;

import plus.wcj.apisix.core.ConditionalOnApisixDiscoveryEnabled;
import plus.wcj.apisix.devtools.ApisixDevtoolsProperties;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnApisixDiscoveryEnabled
@AutoConfigureBefore(ServletWebServerFactoryAutoConfiguration.class)
@EnableConfigurationProperties(ApisixDevtoolsProperties.class)
@Import({ MultiplePortServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		MultiplePortServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		MultiplePortServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class MultiplePortServletWebServerFactoryAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MultiplePortUpstreamCustomizer multiplePortUpstreamCustomizer(
			ApisixDevtoolsProperties apisixDevtoolsProperties) {
		return new MultiplePortUpstreamCustomizer(apisixDevtoolsProperties);
	}

}

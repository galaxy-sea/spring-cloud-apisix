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

import java.util.stream.Collectors;

import javax.servlet.Servlet;

import io.undertow.Undertow;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.UpgradeProtocol;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.xnio.SslClientAuthMode;
import plus.wcj.apisix.devtools.ApisixDevtoolsProperties;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.UndertowServletWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author changjin wei(魏昌进)
 */
@Configuration(proxyBeanMethods = false)
public class MultiplePortServletWebServerFactoryConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	@AutoConfigureBefore(
			name = "org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration")
	static class EmbeddedTomcat {

		@Bean
		TomcatServletWebServerFactory tomcatServletWebServerFactory(
				ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
				ObjectProvider<TomcatContextCustomizer> contextCustomizers,
				ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {
			TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			factory.getTomcatConnectorCustomizers()
					.addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
			factory.getTomcatContextCustomizers()
					.addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
			factory.getTomcatProtocolHandlerCustomizers()
					.addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));

			multiplePort(factory, apisixDevtoolsProperties);
			return factory;
		}

		private void multiplePort(TomcatServletWebServerFactory factory,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {

			Integer[] ports = apisixDevtoolsProperties.getPorts();
			if (ports != null) {
				for (Integer port : ports) {
					Connector connector = new Connector();
					connector.setScheme("http");
					connector.setPort(port);
					factory.addAdditionalTomcatConnectors(connector);
				}
			}
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	static class EmbeddedJetty {

		@Bean
		JettyServletWebServerFactory JettyServletWebServerFactory(
				ObjectProvider<JettyServerCustomizer> serverCustomizers,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {

			JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
			factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));

			multiplePort(factory, apisixDevtoolsProperties);
			return factory;
		}

		private void multiplePort(JettyServletWebServerFactory factory,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {

			Integer[] ports = apisixDevtoolsProperties.getPorts();
			if (ports != null) {
				factory.addServerCustomizers(server -> {
					for (Integer port : ports) {
						NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
						connector.setPort(port);
						server.addConnector(connector);
					}
				});
			}
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
	static class EmbeddedUndertow {

		@Bean
		UndertowServletWebServerFactory undertowServletWebServerFactory(
				ObjectProvider<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers,
				ObjectProvider<UndertowBuilderCustomizer> builderCustomizers,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {

			UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
			factory.getDeploymentInfoCustomizers()
					.addAll(deploymentInfoCustomizers.orderedStream().collect(Collectors.toList()));
			factory.getBuilderCustomizers().addAll(builderCustomizers.orderedStream().collect(Collectors.toList()));

			multiplePort(factory, apisixDevtoolsProperties);

			return factory;
		}

		@Bean
		UndertowServletWebServerFactoryCustomizer undertowServletWebServerFactoryCustomizer(
				ServerProperties serverProperties) {
			return new UndertowServletWebServerFactoryCustomizer(serverProperties);
		}

		private void multiplePort(UndertowServletWebServerFactory factory,
				ApisixDevtoolsProperties apisixDevtoolsProperties) {

			Integer[] ports = apisixDevtoolsProperties.getPorts();
			if (ports != null) {
				factory.addBuilderCustomizers(builder -> {
					for (Integer port : ports) {
						builder.addHttpListener(port, "127.0.0.1");
					}
				});
			}

		}

	}

}

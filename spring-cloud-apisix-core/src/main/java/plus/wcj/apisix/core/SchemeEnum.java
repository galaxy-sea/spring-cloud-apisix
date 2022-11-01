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

/**
 * @author changjin wei(魏昌进)
 */
public enum SchemeEnum {

	/**
	 * http.
	 */
	HTTP("http", false),

	/**
	 * https.
	 */
	HTTPS("https", true),

	/**
	 * grpc.
	 */
	GRPC("grpc", false),

	/**
	 * grpcs.
	 */
	GRPCS("grpcs", true);

	private final String scheme;

	private final boolean isSecure;

	SchemeEnum(String scheme, boolean isSecure) {
		this.scheme = scheme;
		this.isSecure = isSecure;
	}

	public String getScheme() {
		return scheme;
	}

	public boolean isSecure() {
		return isSecure;
	}

	public static boolean isScheme(String scheme) {
		for (SchemeEnum value : SchemeEnum.values()) {
			if (value.getScheme().equals(scheme)) {
				return value.isSecure();
			}
		}
		return false;
	}

	public static SchemeEnum of(String scheme) {
		for (SchemeEnum value : SchemeEnum.values()) {
			if (value.getScheme().equals(scheme)) {
				return value;
			}
		}
		return HTTP;
	}

	@Override
	public String toString() {
		return "SchemeEnum{" + "scheme='" + scheme + '\'' + ", isSecure=" + isSecure + '}';
	}

}

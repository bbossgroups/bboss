/**
 * Copyright 2026 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.frameworkset.yml.FlatYamlParser;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author biaoping.yin
 * @Date 2026/11/2
 */
public class YamlTest {
	public static void main(String[] args) throws IOException {
		System.out.println("-------------------------------------------------");
//		Map<String, Object> flat = FlatYamlParser.parseFlatFile("C:\\workspace\\ccc\\dddd\\grafana\\docker-compose.yml");
//		flat.forEach((k, v) -> System.out.println(k + " = " + v));
		System.out.println("-------------------------------------------------");
		Map<String, Object> flat = FlatYamlParser.parseFlatFile("docker-compose.yml");
		flat.forEach((k, v) -> System.out.println(k + " = " + v));
//// 打印结果：
//		server.port                                = "8080"
//		spring.application.name                    = "szop-gateway"
//		spring.profiles.active                     = "dev"
//		spring.cloud.nacos.discovery.server-addr   = "10.13.6.210:8848"   // 冒号原样保留
//		spring.cloud.nacos.discovery.username      = "nacos"
//		spring.cloud.nacos.discovery.password      = "nacos"
//		spring.cloud.sentinel.eager                = "true"                // 不再转 Boolean
//		spring.cloud.sentinel.transport.dashboard  = "127.0.0.1:8718"
//		spring.cloud.sentinel.datasource.ds1.nacos.data-type  = "json"
//		spring.cloud.sentinel.datasource.ds1.nacos.rule-type  = "gw-flow"
//		spring.config.file-extension               = "yml"
//		spring.config.import                       = List<String>[
//		"nacos:application-${spring.profiles.active}.${spring.config.file-extension}",
//				"nacos:${spring.application.name}-${spring.profiles.active}.${spring.config.file-extension}"
//]
	}
}

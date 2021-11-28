/**
 * Copyright 2020 bboss
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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 需要导入jre/lib下的tools.jar包</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2021/11/28 18:52
 * @author biaoping.yin
 * @version 1.0
 */
public class Test {

	public static void main(String[] args) throws Exception {
		// System.out.println(getPidFromWindows("javaw"));
		// 获取监控主机
        /*MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");
        // 取得所有在活动的虚拟机集合
        Set<Object> vmlist = new HashSet<Object>(local.activeVms());
        // 遍历集合，输出PID和进程名
        for (Object process : vmlist) {
            MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//" + process));
            // 获取类名
            String processname = MonitoredVmUtil.mainClass(vm, true);
            System.out.println(process + " ------> " + processname);
        }

        List<String> list = new ArrayList<String>();
        System.out.println(list.size());*/
		List<String> list = getPid();
		System.out.println(list);
//		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
	}

	public static List<String> getPid(){
		List<String> list = new ArrayList<String>();
//		try {
//			// 获取监控主机
//			MonitoredHost local;
//			local = MonitoredHost.getMonitoredHost("localhost");
//			// 取得所有在活动的虚拟机集合
//			Set<Object> vmlist = new HashSet<Object>(local.activeVms());
//			// 遍历集合，输出PID和进程名
//			for (Object process : vmlist) {
//				MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//" + process));
//				// 获取类名
//				String processname = MonitoredVmUtil.mainClass(vm, true);
//				System.out.println(process + " ------> " + processname);
////				if (processname.endsWith("stable.jar")) {
//					list.add(((Integer)process).toString());
////				}
//			}
//		} catch (MonitorException e) {
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
		return list;

	}

}
package org.frameworkset.runtime;
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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 *
 * @author biaoping.yin
 * @version 1.0
 * @Date 2021/4/13 9:37
 */
public class OSInfo {
	private static String OS ;
	private static OSInfo _instance = new OSInfo();
	private EPlatform platform;
	private static boolean isLinux;

	private static boolean isMacOS;

	private static boolean isMacOSX;

	private static boolean isWindows;
	private static boolean isOS2;

	private static boolean isSolaris;

	private static boolean isSunOS;

	private static boolean isMPEiX;

	private static boolean isHPUX;

	private static boolean isAix;

	private static boolean isOS390;

	private static boolean isFreeBSD;

	private static boolean isIrix;

	private static boolean isDigitalUnix;

	private static boolean isNetWare;

	private static boolean isOSF1;

	private static boolean isOpenVMS;
	static{
		OS = System.getProperty("os.name").toLowerCase();
		isLinux = OS.indexOf("linux") >= 0;

		isMacOS = OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;

		isMacOSX = OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;

		isWindows = OS.indexOf("windows") >= 0;
		isOS2 = OS.indexOf("os/2") >= 0;

		isSolaris = OS.indexOf("solaris") >= 0;

		isSunOS = OS.indexOf("sunos") >= 0;

		isMPEiX = OS.indexOf("mpe/ix") >= 0;

		isHPUX = OS.indexOf("hp-ux") >= 0;

		isAix = OS.indexOf("aix") >= 0;

		isOS390 = OS.indexOf("os/390") >= 0;

		isFreeBSD = OS.indexOf("freebsd") >= 0;

		isIrix = OS.indexOf("irix") >= 0;

		isDigitalUnix = OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;

		isNetWare = OS.indexOf("netware") >= 0;

		isOSF1 = OS.indexOf("osf1") >= 0;

		isOpenVMS = OS.indexOf("openvms") >= 0;
	}

	private OSInfo() {
	}

	public static String getOS(){
		return OS;
	}

	public static boolean isLinux() {
		return isLinux;
	}

	public static boolean isMacOS() {
		return isMacOS;
	}

	public static boolean isMacOSX() {
		return isMacOSX;
	}

	public static boolean isWindows() {
		return isWindows;
	}

	public static boolean isOS2() {
		return isOS2;
	}

	public static boolean isSolaris() {
		return isSolaris;
	}

	public static boolean isSunOS() {
		return isSunOS;
	}

	public static boolean isMPEiX() {
		return isMPEiX;
	}

	public static boolean isHPUX() {
		return isHPUX;
	}

	public static boolean isAix() {
		return isAix;
	}

	public static boolean isOS390() {
		return isOS390;
	}

	public static boolean isFreeBSD() {
		return isFreeBSD;
	}

	public static boolean isIrix() {
		return isIrix;
	}

	public static boolean isDigitalUnix() {
		return isDigitalUnix;
	}

	public static boolean isNetWare() {
		return isNetWare;
	}

	public static boolean isOSF1() {
		return isOSF1;
	}

	public static boolean isOpenVMS() {
		return isOpenVMS;
	}

	/**
	 * 获取操作系统名字
	 *
	 * @return 操作系统名
	 */
	public static EPlatform getOSname() {
		if(_instance.platform != null)
			return _instance.platform;
		synchronized (EPlatform.class) {
			if(_instance.platform != null)
				return _instance.platform;
			if (isAix()) {
				_instance.platform = EPlatform.AIX;
			} else if (isDigitalUnix()) {
				_instance.platform = EPlatform.Digital_Unix;
			} else if (isFreeBSD()) {
				_instance.platform = EPlatform.FreeBSD;
			} else if (isHPUX()) {
				_instance.platform = EPlatform.HP_UX;
			} else if (isIrix()) {
				_instance.platform = EPlatform.Irix;
			} else if (isLinux()) {
				_instance.platform = EPlatform.Linux;
			} else if (isMacOS()) {
				_instance.platform = EPlatform.Mac_OS;
			} else if (isMacOSX()) {
				_instance.platform = EPlatform.Mac_OS_X;
			} else if (isMPEiX()) {
				_instance.platform = EPlatform.MPEiX;
			} else if (isNetWare()) {
				_instance.platform = EPlatform.NetWare_411;
			} else if (isOpenVMS()) {
				_instance.platform = EPlatform.OpenVMS;
			} else if (isOS2()) {
				_instance.platform = EPlatform.OS2;
			} else if (isOS390()) {
				_instance.platform = EPlatform.OS390;
			} else if (isOSF1()) {
				_instance.platform = EPlatform.OSF1;
			} else if (isSolaris()) {
				_instance.platform = EPlatform.Solaris;
			} else if (isSunOS()) {
				_instance.platform = EPlatform.SunOS;
			} else if (isWindows()) {
				_instance.platform = EPlatform.Windows;
			} else {
				_instance.platform = EPlatform.Others;
			}
		}
		return _instance.platform;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(OSInfo.getOSname());
	}

	public enum EPlatform {
		Any("any"),
		Linux("Linux"),
		Mac_OS("Mac OS"),
		Mac_OS_X("Mac OS X"),
		Windows("Windows"),
		OS2("OS/2"),
		Solaris("Solaris"),
		SunOS("SunOS"),
		MPEiX("MPE/iX"),
		HP_UX("HP-UX"),
		AIX("AIX"),
		OS390("OS/390"),
		FreeBSD("FreeBSD"),
		Irix("Irix"),
		Digital_Unix("Digital Unix"),
		NetWare_411("NetWare"),
		OSF1("OSF1"),
		OpenVMS("OpenVMS"),
		Others("Others");

		private String description;

		private EPlatform(String desc) {
			this.description = desc;
		}

		public String toString() {
			return description;
		}
	}

}

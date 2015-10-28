/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.event;

import java.util.Date;

/**
 * 
 * <p>Title: Version.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-5-7 下午05:02:50
 * @author biaoping.yin
 * @version 1.0
 */
public class Version {
    public static final String description="1.0.1";
    public static final short version=100;
    public static final Date releaseDate = new Date("2009/05/17");
    

    /**
     * Prints the value of the description and fields to System.out.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\nVersion: \t" + description + "\nDate:\t" + releaseDate);        
    }

    /**
     * Returns the catenation of the description and cvs fields.
     * @return String with description
     */
    public static String printDescription() {
        return "bboss event " + description ;
    }

    /**
     * Returns the version field as a String.
     * @return String with version
     */
    public static String printVersion() {
        return Short.toString(version);
    }

    /**
     * Compares the specified version number against the current version number.
     * @param v short
     * @return Result of == operator.
     */
    public static boolean compareTo(short v) {
        return version == v;
    }

}

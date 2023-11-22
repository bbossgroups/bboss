package org.frameworkset.json;
/**
 * Copyright 2023 bboss
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

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.frameworkset.util.BeanUtils;
import org.frameworkset.util.ClassUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>Description: </p>
 * <p></p>
 *
 * @author biaoping.yin
 * @Date 2023/11/22
 */
public class JavaTimeModuleRegist {

    public static void javaTimeModuleRegist(ObjectMapper objectMapper){
        
        // Java 8 java.time package present?
        if (ClassUtils.isPresent("java.time.LocalDate", Jackson2ObjectMapper.moduleClassLoader)) {
            try {
//                Class<? extends Module> javaTimeModule = (Class<? extends Module>)
//                        ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", moduleClassLoader);
//                objectMapper.registerModule(BeanUtils.instantiate(javaTimeModule));
                JavaTimeModule javaTimeModule = new JavaTimeModule();
                LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'"));
                LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'"));

                javaTimeModule.addSerializer(LocalDateTime.class,localDateTimeSerializer);
                javaTimeModule.addDeserializer(LocalDateTime.class,localDateTimeDeserializer);


                objectMapper.registerModule(javaTimeModule);
            }
            catch (Exception ex) {
                // jackson-datatype-jsr310 not available or older than 2.6
                try {
                    Class<? extends Module> jsr310Module = (Class<? extends Module>)
                            ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JSR310Module", Jackson2ObjectMapper.moduleClassLoader);
                    objectMapper.registerModule(BeanUtils.instantiate(jsr310Module));
                }
                catch (ClassNotFoundException ex2) {
                    // OK, jackson-datatype-jsr310 not available at all...
                }
            }
        }

    }

}

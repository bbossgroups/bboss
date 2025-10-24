package org.frameworkset.util.concurrent;
/**
 * Copyright 2025 bboss
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

import java.util.List;

/**
 * list数据封装类
 * @author biaoping.yin
 * @Date 2025/10/24
 */
public class ListWrapper<T> {
    private List<T> datas;

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public List<T> getDatas() {
        return datas;
    }
}

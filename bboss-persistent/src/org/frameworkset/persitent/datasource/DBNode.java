package org.frameworkset.persitent.datasource;
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

import org.frameworkset.balance.Node;

/**
 * <p>Description: </p>
 * <p></p>
 *
 * @author biaoping.yin
 * @Date 2023/12/27
 */
public class DBNode implements Node {
    private String node;
    public DBNode(String node){
        this.node = node;
    }
    @Override
    public boolean ok() {
        return true;
    }

    @Override
    public boolean okOrFailed() {
        return true;
    }
    public String getNode(){
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
    @Override
    public String toString(){
        return node;
    }
}

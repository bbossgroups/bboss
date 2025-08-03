package org.frameworkset.persitent.datasource;
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
import java.util.Map;

/**
 * @author biaoping.yin
 * @Date 2025/8/2
 */
public class BalanceDatasourceConfig {
    private List<String> nodes;
    private Map<String,String> params;
    private boolean enabaleBalance;
    private String balance;
    private String normalParams;
    private String database;
    private String database2ndParams;

    public boolean isEnabaleBalance() {
        return enabaleBalance;
    }

    public void setEnabaleBalance(boolean enabaleBalance) {
        this.enabaleBalance = enabaleBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getNormalParams() {
        return normalParams;
    }

    public void setNormalParams(String normalParams) {
        this.normalParams = normalParams;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase2ndParams() {
        return database2ndParams;
    }

    public void setDatabase2ndParams(String database2ndParams) {
        this.database2ndParams = database2ndParams;
    }
}

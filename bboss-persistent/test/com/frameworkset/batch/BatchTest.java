package com.frameworkset.batch;

import com.frameworkset.common.poolman.BatchHandler;
import com.frameworkset.common.poolman.SQLExecutor;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 2017/5/14.
 */
public class BatchTest {
    @Test
    public void testBatch() throws SQLException {
        List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
        for(int i = 0; i < 25 ; i ++){
            Map<String,String> data = new HashMap<String, String>();
            if(i % 3 == 0)
                data.put("name","jack_"+i);
            else if(i % 3 == 1)
                data.put("name","brown_"+i);
            else if(i % 3 == 2)
                data.put("name","john_"+i);
            datas.add(data);
        }
        SQLExecutor.delete("delete from batchtest");
        SQLExecutor.executeBatch("insert into batchtest (name) values(?)", datas, 10,new BatchHandler<Map<String,String>>() {
            @Override
            public void handler(PreparedStatement stmt, Map<String,String> record, int i) throws SQLException {
                stmt.setString(1,record.get("name"));
            }
        });
    }
}

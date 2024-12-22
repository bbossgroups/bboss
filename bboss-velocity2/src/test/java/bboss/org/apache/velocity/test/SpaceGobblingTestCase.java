package bboss.org.apache.velocity.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import junit.framework.TestSuite;
import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.app.Velocity;
import bboss.org.apache.velocity.app.VelocityEngine;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeConstants.SpaceGobbling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Test case for conversion handler
 */
public class SpaceGobblingTestCase extends BaseTestCase
{
    private static final String RESULT_DIR = TEST_RESULT_DIR + "/gobbling";

    private static final String COMPARE_DIR = TEST_COMPARE_DIR + "/gobbling/compare";

    public SpaceGobblingTestCase(String name)
    {
        super(name);
    }

    @Override
    public void setUp()
            throws Exception
    {
        super.setUp();
    }

    /**
     * Test suite
     * @return test suite
     */
    public static junit.framework.Test suite()
    {
        return new TestSuite(SpaceGobblingTestCase.class);
    }

    /**
     * Return and initialize engine
     * @return
     */
    private VelocityEngine createEngine(SpaceGobbling mode)
            throws Exception
    {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(Velocity.RUNTIME_LOG_INSTANCE, log);
        ve.setProperty(RuntimeConstants.RESOURCE_LOADERS, "file");
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, TEST_COMPARE_DIR + "/gobbling");
        ve.setProperty(RuntimeConstants.SPACE_GOBBLING, mode.toString());
        ve.init();

        return ve;
    }

    public void testSpaceGobbling() throws Exception
    {
        for (SpaceGobbling mode : SpaceGobbling.values())
        {
            testMode(mode);
        }
    }

    private void testMode(SpaceGobbling mode) throws Exception
    {
        File dir = new File(TEST_COMPARE_DIR + "/gobbling");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                if (child.isFile())
                {
                    testTemplate(child.getName(), mode);
                }
            }
        }
        else
        {
            throw new Exception("cannot read input templates");
        }
    }

    private void testTemplate(String templateFile, SpaceGobbling mode) throws Exception
    {
        assureResultsDirectoryExists(RESULT_DIR);
        FileOutputStream fos = new FileOutputStream (getFileName(RESULT_DIR, templateFile, mode.toString()));
        VelocityContext context = new VelocityContext();
        Writer writer = new BufferedWriter(new OutputStreamWriter(fos));

        VelocityEngine ve = createEngine(mode);
        Template template = ve.getTemplate(templateFile);
        template.merge(context, writer);

        /*
         * Write to the file
         */
        writer.flush();
        writer.close();

        if (!isMatch(RESULT_DIR, COMPARE_DIR, templateFile, mode.toString(), mode.toString()))
        {
            String result = getFileContents(RESULT_DIR, templateFile, mode.toString());
            String compare = getFileContents(COMPARE_DIR, templateFile, mode.toString());

            String msg = "Processed template did not match expected output for template " + templateFile + " and mode " + mode + "\n"+
                    "-----Result-----\n"+ result +
                    "----Expected----\n"+ compare +
                    "----------------";

            fail(msg);
        }

    }
}


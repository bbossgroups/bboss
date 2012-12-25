/**
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
package org.frameworkset.web.wordpdf;

import javax.swing.event.EventListenerList;

import com.sun.star.awt.Point;
import com.sun.star.awt.Size;
import com.sun.star.beans.Property;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.chart.ChartDataChangeEvent;
import com.sun.star.chart.ChartDataChangeType;
import com.sun.star.chart.XAxisXSupplier;
import com.sun.star.chart.XAxisYSupplier;
import com.sun.star.chart.XChartDataArray;
import com.sun.star.chart.XChartDataChangeEventListener;
import com.sun.star.chart.XChartDocument;
import com.sun.star.chart.XDiagram;
import com.sun.star.drawing.XShape;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.text.HoriOrientation;
import com.sun.star.text.VertOrientation;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;

/**
 * An example of creating a chart for insertion into an OpenOffice Writer
 * document.
 * <p>
 * <b>Implementation Suggestions:</b>
 * <ul>
 * <li>This example has a single method named <code>setChartFeatures</code>
 *     that sets various chart parameters using locally hardcoded settings.
 *     In a real application, you would want to break this method up into
 *     multiple setters through which a caller could pass the desired values.
 *     I would recommend two setters for each parameter: a public one and a
 *     private one.  The public setter would receive a parameter value, save
 *     it locally, and then if the chart has already been instantiated, call
 *     the private setter.  The private setter, on the other hand, would
 *     simply apply the locally saved parameter to the chart.  This technique
 *     allows a caller to call the constructor, set all the desired parameters,
 *     and then call the <code>createChart</code> method to instantiate the
 *     chart and insert it into the document.  Furthermore, it enables
 *     on-the-fly parameter changes after the chart is instantiated.
 * <li>The enumeration classes <code>ChartInsertionModeEnum</code>,
 *     <code>ChartStyleEnum</code>, and <code>YAxisLabelOptionEnum</code>
 *     should be removed to their own class files.
 * </ul>
 * <p>
 * @author rdg
 */
public class Chart implements XChartDataArray {

    private XComponent          document;
    private XChartDocument      chartDoc;
    private XModel              chartDocModel;
    private XTextContent        embeddedObject;
    private XDiagram            diagram;
    private EventListenerList   chartDataChangeListeners = new EventListenerList();
    private static final String BAR_DIAGRAM_CLASS_ID =
        "12dcae26-281f-416f-a234-c3086127382e";

    // Test data.

    private int                 xAxisLabelRotation = 30;
    private double              yAxisLabelMin = 0.0;
    private double              yAxisLabelMax = 10.0;
    private double              yAxisLabelInc = 1.0;
    private double[][]          testData = {
            { 1 },
            { 10 },
            { 7 },
            { 4 },
            { 8 }
    };
    private String[]            testRowDescriptions =
            { "Row1", "Row2", "Row3", "Row4", "Row5" };
    private String[]            testColumnDescriptions =
            { "Column1" };

    /** Constructor. */
    public Chart() { }

    /**
     * Create a chart and insert it into the given document.
     * <p>
     * See "Implementation Suggestions" in the class comment header above.
     * <p>
     * @param document
     *      The Writer document into which the chart will be inserted.
     * @param textContent
     *      The text interface to the text unit into which the chart will
     *      be inserted.
     * @param textCursor
     *      The text cursor representing the text range at which to insert
     *      the chart within the document.
     * @param chartStyle
     *      The style of the chart.  See <code>Chart.ChartStyleEnum</code>.
     * @param insertionMode
     *      How the chart will be inserted into the document.
     *      See <code>Chart.ChartInsertionModeEnum</code>.
     */
    public void createChart(
            XComponent document,
            XText textContent,
            XTextCursor textCursor,
            ChartStyleEnum chartStyle,
            ChartInsertionModeEnum insertionMode) throws java.lang.Exception {

        this.document = document;
        XPropertySet pSet = null;

        System.out.println(
            "Chart: Create a TextEmbeddedObject within the document.");

        // Get the Writer document's model.
        XModel writerDocModel = (XModel) UnoRuntime.queryInterface(
                XModel.class, document);

        // Get the Writer document's service factory.
        XMultiServiceFactory writerDocServices =
            (XMultiServiceFactory) UnoRuntime.queryInterface(
                XMultiServiceFactory.class, writerDocModel);

        // Create a TextEmbeddedObject to provide accessors for embedding the chart.
        embeddedObject = (XTextContent) UnoRuntime.queryInterface(
                XTextContent.class,
                writerDocServices.createInstance(
                        "com.sun.star.text.TextEmbeddedObject"));

        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, embeddedObject);
        Any any = new Any(new Type(String.class), BAR_DIAGRAM_CLASS_ID);
        pSet.setPropertyValue("CLSID", any);
        pSet.setPropertyValue("VertOrient", new Short(VertOrientation.NONE));
        pSet.setPropertyValue("HoriOrient", new Short(HoriOrientation.NONE));
        Point coord = new Point(0,0);
        pSet.setPropertyValue("VertOrientPosition", new Integer(coord.Y));
        pSet.setPropertyValue("HoriOrientPosition", new Integer(coord.X));

        // Create and insert the chart document into the main Writer document body.

        if (insertionMode == ChartInsertionModeEnum.REPLACE_IN_TEXT_FIELD) {
            textContent.insertTextContent(textCursor, embeddedObject, true);
        }
        else if (insertionMode == ChartInsertionModeEnum.INSERT_IN_TEXT_FIELD) {
            textContent.insertTextContent(textCursor, embeddedObject, false);
        }
        else if (insertionMode == ChartInsertionModeEnum.REPLACE_IN_TEXT_BODY) {
            textContent.insertTextContent(textCursor, embeddedObject, true);
        }
        else if (insertionMode == ChartInsertionModeEnum.INSERT_IN_TEXT_BODY) {
            textContent.insertTextContent(textCursor, embeddedObject, false);
        }

        // Get the underlying XChartDocument.
        System.out.println("Chart: Get the underlying 'XChartDocument'.");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, embeddedObject);
        chartDoc = (XChartDocument) UnoRuntime.queryInterface(
                XChartDocument.class, pSet.getPropertyValue("Model"));

        // Attach the XChartDataArray containing the chart data.
        System.out.println("Chart: Attach the test data set to the chart.");
        chartDoc.attachData(this);

        // Create an instance of the desired type of diagram and insert it
        // into the chart document.
        System.out.println(
            "Chart: Create and insert the chart diagram into the XChartDocument.");

        XMultiServiceFactory chartDocServices =
                (XMultiServiceFactory) UnoRuntime.queryInterface(
                XMultiServiceFactory.class, chartDoc);
        diagram = (XDiagram) UnoRuntime.queryInterface(
                XDiagram.class,
                chartDocServices.createInstance(chartStyle.getDiagramClass()));
        chartDoc.setDiagram(diagram);

        // Set chart features.
        System.out.println("Chart: Set various chart parameters.");
        setChartFeatures();

        // Save the chart document model.
        chartDocModel = (XModel) UnoRuntime.queryInterface(XModel.class, chartDoc);

        System.out.println("Chart: Chart construction finished.");
    }

    /**
     * Set various chart parameters.
     * <p>
     * See "Implementation Suggestions" in the class comment header above.
     */
    private void setChartFeatures() throws java.lang.Exception {

        XShape shape = null;
        XPropertySet pSet = null;

        // Chart size in units of 1/100th millimeter.

        System.out.println("Chart: Set the chart size.");
        int width = 8000;
        int height = 5000;
        shape = (XShape) UnoRuntime.queryInterface(
                XShape.class, embeddedObject);
        shape.setSize(new Size(width, height));

        // Chart title.

        System.out.println("Chart: Set the title.");
        shape = chartDoc.getTitle();
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, shape);
        pSet.setPropertyValue("String", "My Chart Title");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, chartDoc);
        pSet.setPropertyValue("HasMainTitle", new Boolean(true));

        // Chart subtitle.

        System.out.println("Chart: Set the subtitle.");
        shape = chartDoc.getSubTitle();
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, shape);
        pSet.setPropertyValue("String", "My Chart SubTitle");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, chartDoc);
        pSet.setPropertyValue("HasSubTitle", new Boolean(true));

        // Enable legend view.

        System.out.println("Chart: Enable the legend view.");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, chartDoc);
        pSet.setPropertyValue("HasLegend", new Boolean(true));

        // Set the transparency of chart areas surrounding the diagram
        // to 100% transparent.

        System.out.println("Chart: Set the chart area transparency.");
        pSet = chartDoc.getArea();
        pSet.setPropertyValue("FillTransparence", new Integer(100));

        // Set the chart X-axis title.

        System.out.println("Chart: Set the X-axis title.");
        XAxisXSupplier xAxis = (XAxisXSupplier) UnoRuntime.queryInterface(
                XAxisXSupplier.class, diagram);
        shape = xAxis.getXAxisTitle();
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, shape);
        pSet.setPropertyValue("String", "My X-Axis Title");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, diagram);
        pSet.setPropertyValue("HasXAxisTitle", new Boolean(true));

        // Set the chart Y-axis title.

        System.out.println("Chart: Set the Y-axis title.");
        XAxisYSupplier yAxis = (XAxisYSupplier) UnoRuntime.queryInterface(
                XAxisYSupplier.class, diagram);
        shape = yAxis.getYAxisTitle();
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, shape);
        pSet.setPropertyValue("String", "My Y-Axis Title");
        pSet = (XPropertySet) UnoRuntime.queryInterface(
                XPropertySet.class, diagram);
        pSet.setPropertyValue("HasYAxisTitle", new Boolean(true));

        // Set the X axis label rotation in degrees.  0 = normal horizontal.

        System.out.println("Chart: Set the X-axis label rotation.");
        xAxis = (XAxisXSupplier) UnoRuntime.queryInterface(
                XAxisXSupplier.class, diagram);
        pSet = xAxis.getXAxis();
        // Convert degrees to 100ths of a degree and change polarity so
        // positive values rotate the text toward the right.
        Integer value = new Integer (xAxisLabelRotation * -100);
        pSet.setPropertyValue("TextRotation", value);

        // Set the Y axis tic label range.

        System.out.println("Chart: Set the Y-axis tic label range.");
        System.out.println("       Note - for some reason this takes quite a while.");
        yAxis = (XAxisYSupplier) UnoRuntime.queryInterface(
                XAxisYSupplier.class, diagram);
        pSet = yAxis.getYAxis();
        Boolean trueValue = new Boolean(true);
        Boolean falseValue = new Boolean(false);

        YAxisLabelOptionEnum yAxisLabelOption = YAxisLabelOptionEnum.FIXED_INTEGER;

        if (yAxisLabelOption == YAxisLabelOptionEnum.AUTO) {
            pSet.setPropertyValue("AutoMax", trueValue);
            pSet.setPropertyValue("AutoMin", trueValue);
            pSet.setPropertyValue("AutoStepHelp", trueValue);
            pSet.setPropertyValue("AutoStepMain", trueValue);
            pSet.setPropertyValue("Origin", new Double(0.0));
        }
        else if (yAxisLabelOption == YAxisLabelOptionEnum.FIXED_INTEGER) {
            pSet.setPropertyValue("AutoMax", falseValue);
            pSet.setPropertyValue("AutoMin", falseValue);
            pSet.setPropertyValue("AutoStepHelp", falseValue);
            pSet.setPropertyValue("AutoStepMain", falseValue);
            pSet.setPropertyValue("Origin", new Double(yAxisLabelMin));
            pSet.setPropertyValue("Min", new Double(yAxisLabelMin));
            pSet.setPropertyValue("Max", new Double(yAxisLabelMax));
            pSet.setPropertyValue("StepMain", new Double(yAxisLabelInc));
            pSet.setPropertyValue("StepHelp", new Double(yAxisLabelInc));
        }
        else if (yAxisLabelOption == YAxisLabelOptionEnum.FIXED_REAL) {
            pSet.setPropertyValue("AutoMax", falseValue);
            pSet.setPropertyValue("AutoMin", falseValue);
            pSet.setPropertyValue("AutoStepHelp", falseValue);
            pSet.setPropertyValue("AutoStepMain", falseValue);
            pSet.setPropertyValue("Origin", new Double(yAxisLabelMin));
            pSet.setPropertyValue("Min", new Double(yAxisLabelMin));
            pSet.setPropertyValue("Max", new Double(yAxisLabelMax));
            pSet.setPropertyValue("StepMain", new Double(yAxisLabelInc));
            pSet.setPropertyValue("StepHelp", new Double(yAxisLabelInc));
        }
    }

    /** @see com.sun.star.chart.XChartDataArray#getData() */
    public double[][] getData() { return testData; }
    /** @see com.sun.star.chart.XChartDataArray#setData(double[][]) */
    public void setData(double data[][]) { }

    /** @see com.sun.star.chart.XChartDataArray#getRowDescriptions() */
    public String[] getRowDescriptions() { return testRowDescriptions; }
    /** @see com.sun.star.chart.XChartDataArray#setRowDescriptions(String[]) */
    public void setRowDescriptions(String[] descriptions) {
    }

    /** @see com.sun.star.chart.XChartDataArray#getColumnDescriptions() */
    public String[] getColumnDescriptions() { return testColumnDescriptions; }
    /** @see com.sun.star.chart.XChartDataArray#setColumnDescriptions(String[]) */
    public void setColumnDescriptions(String[] descriptions) {
    }

    /** @see com.sun.star.chart.XChartDataArray#getNotANumber() */
    public double getNotANumber() { return -1; }
    /** @see com.sun.star.chart.XChartDataArray#setNotANumber(double) */
    public void setNotANumber(double value) {
    }
    /** @see com.sun.star.chart.XChartDataArray#isNotANumber() */
    public boolean isNotANumber(double value) { return false; }

    /** @see com.sun.star.chart.XChartData#addChartDataChangeEventListener(com.sun.star.chart.XChartDataChangeEventListener) */
    public void addChartDataChangeEventListener(XChartDataChangeEventListener listener) {
        chartDataChangeListeners.add(XChartDataChangeEventListener.class, listener);
    }

    /** @see com.sun.star.chart.XChartData#addChartDataChangeEventListener(com.sun.star.chart.XChartDataChangeEventListener) */
    public void removeChartDataChangeEventListener(XChartDataChangeEventListener listener) {
        chartDataChangeListeners.remove(XChartDataChangeEventListener.class, listener);
    }

    /** Notify chart data change listeners. */
    private void notifyChartDataChangeEventListeners() {
        if (chartDataChangeListeners.getListenerCount() > 0) {
            ChartDataChangeEvent ce = new ChartDataChangeEvent();
            ce.Type = ChartDataChangeType.ALL;
            Object[] listeners = chartDataChangeListeners.getListenerList();
            for (int i=0; i < listeners.length; i+=2) {
                if (listeners[i] == XChartDataChangeEventListener.class) {
                    XChartDataChangeEventListener listener =
                        (XChartDataChangeEventListener) listeners[i+1];
                    listener.chartDataChanged(ce);
                }
            }
        }
    }

    /** Enumeration for techniques of inserting the chart into a document. */
    public enum ChartInsertionModeEnum {
        INSERT_IN_TEXT_FIELD ("InsertInTextField", "Insert chart into a specific text field"),
        INSERT_IN_TEXT_BODY  ("InsertInTextBody", "Insert chart into the document body"),
        REPLACE_IN_TEXT_FIELD ("ReplaceInTextField", "Insert chart into a specific text field, replacing existing content"),
        REPLACE_IN_TEXT_BODY ("ReplaceInTextBody", "Insert chart into the document body, replacing existing content");
        private String name;
        private String desc;
        private static ChartInsertionModeEnum[] values = {
            INSERT_IN_TEXT_FIELD,
            INSERT_IN_TEXT_BODY,
            REPLACE_IN_TEXT_FIELD,
            REPLACE_IN_TEXT_BODY
        };

        private ChartInsertionModeEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
        public String getName() { return name; }
        public String getDesc() { return desc; }
        public static ChartInsertionModeEnum[] getValues() { return values; }
    }

    /** Enumeration for chart styles. */
    public enum ChartStyleEnum {
        BAR_DIAGRAM ("BarDiagram", "com.sun.star.chart.BarDiagram"),
        AREA_DIAGRAM ("AreaDiagram", "com.sun.star.chart.AreaDiagram"),
        PIE_DIAGRAM ("AreaDiagram", "com.sun.star.chart.PieDiagram");
        private String name;
        private String diagramClass;
        private static ChartStyleEnum[] values = {
            BAR_DIAGRAM, AREA_DIAGRAM, PIE_DIAGRAM
        };

        private ChartStyleEnum(String name, String diagramClass) {
            this.name = name;
            this.diagramClass = diagramClass;
        }
        public String getName() { return name; }
        public String getDiagramClass() { return diagramClass; }
        public static ChartStyleEnum[] getValues() { return values; }
    }

    /** Enumeration for Y-axis label options. */
    public enum YAxisLabelOptionEnum {
        AUTO ("Auto", "Automatic label formatting"),
        FIXED_INTEGER ("FixedInteger", "Display label values as fixed integers"),
        FIXED_REAL ("FixedReal", "Display label values as floating point values");
        private String name;
        private String desc;
        private static YAxisLabelOptionEnum[] values = {
            AUTO, FIXED_INTEGER, FIXED_REAL
        };

        private YAxisLabelOptionEnum(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
        public String getName() { return name; }
        public String getDesc() { return desc; }
        public static YAxisLabelOptionEnum[] getValues() { return values; }
    }

    /**
     * Dump a given <code>XPropertySet</code>.  This comes in handy because
     * it's otherwise hard to know all the properties available within a
     * given UNO component's property set.  You can use UnoRuntime.queryInterface
     * to fetch the component's property set, then call this method to dump it.
     */
    private void showProperties(String title, XPropertySet pSet) {
        System.out.println("\n" + title + "\n");
        XPropertySetInfo info = pSet.getPropertySetInfo();
        Property[] props = info.getProperties();
        if (props != null) {
            try {
                for (int i=0; i < props.length; i++) {
                    Property p = props[i];
                    String value = "<null>";
                    try {
                        Object o = (Object) pSet.getPropertyValue(p.Name);
                        if (o != null) { value = o.toString(); }
                    } catch (java.lang.Exception e) {
                        value = "<null>";
                    }
                    System.out.println(
                            "   Name = " + p.Name +
                            ", Type = " + p.Type +
                            ", Value = " + value);
                }
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
    }
} 

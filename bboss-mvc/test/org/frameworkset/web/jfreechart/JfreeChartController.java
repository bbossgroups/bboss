package org.frameworkset.web.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class JfreeChartController {

	public String showjfree() {

		return "/jfreechart/jfreechart";
	}

	public void getmaphot(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			int width = 500, height = 300;
			JFreeChart chart = (JFreeChart) request.getSession().getAttribute(
					"chart");
			ChartRenderingInfo info = (ChartRenderingInfo)request.getSession().getAttribute(
			"chartinfo");
//			chart.createBufferedImage(width, height, info);

			// 获取热点map
			String strimg = ChartUtilities.getImageMap("maphot", info);

			// 解析xml
			SAXReader reader = new SAXReader();
			StringReader r = new StringReader(strimg);

			Document document = reader.read(r);
			List<Node> nodes = document.selectNodes("/map/area");
			for (int t = 0; t < nodes.size(); t++) {
				Element ele1 = (Element) nodes.get(t);
				ele1.addAttribute("onmouseover", "showTooltip(this,event)");
				ele1.addAttribute("onmouseout", "UnTip(this)");

				ele1.addAttribute("spanid", "spanid");
			}

			strimg = document.asXML();
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(strimg);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void jfreechart(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		int series1Color = toIntHex(request.getParameter("s1c"), 0x9bd2fb);
		int series1OutlineColor = toIntHex(request.getParameter("s1o"),
				0x0665aa);
		int series2Color = toIntHex(request.getParameter("s2c"), 0xFF0606);
		int series2OutlineColor = toIntHex(request.getParameter("s2o"),
				0x9d0000);
		int backgroundColor = toIntHex(request.getParameter("bc"), 0xFFFFFF);
		int gridColor = toIntHex(request.getParameter("gc"), 0);

		response.setHeader("Content-type", "image/png");

		XYDataset dataset = this.createDateSet();// 建立数据集
		String fileName = null;
		// 建立JFreeChart
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"JFreeChart时间曲线序列图", "Date", "Price", dataset, true, false,
				false);
		// 设置JFreeChart的显示属性,对图形外部部分进行调整

		// chart.setBackgroundPaint(Color.pink);// 设置曲线图背景色 设置字体大小，形状
		Font font = new Font("宋体", Font.BOLD, 16);
		TextTitle title = new TextTitle("JFreeChart时间曲线序列图", font);
		chart.setTitle(title);
		// 副标题
		TextTitle subtitle = new TextTitle(" ", new Font("黑体", Font.BOLD, 12));
		chart.addSubtitle(subtitle);
		chart.setTitle(title); // 标题

		// 设置图示标题字符
		// TimeSeries s1 = new TimeSeries("历史曲线", Day.class);该中文字符
		LegendTitle legengTitle = chart.getLegend();
		legengTitle.setItemFont(font);

		// XYPlot plot = (XYPlot) chart.getPlot();// 获取图形的画布
		// plot.setBackgroundPaint(Color.lightGray);// 设置网格背景色
		// plot.setDomainGridlinePaint(Color.green);// 设置网格竖线(Domain轴)颜色
		// plot.setRangeGridlinePaint(Color.green);// 设置网格横线颜色
		// plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));//
		// 设置曲线图与xy轴的距离
		// plot.setDomainCrosshairVisible(true);
		// plot.setRangeCrosshairVisible(true);

		if (chart != null) {
			chart.setBackgroundPaint(new Color(backgroundColor));
			// ((XYAreaRenderer)
			// chart.getXYPlot().getRenderer()).setOutline(true);
			chart.getXYPlot().getRenderer().setSeriesPaint(0,
					Color.red);
			chart.getXYPlot().getRenderer().setSeriesOutlinePaint(0,
					new Color(series1OutlineColor));
			chart.getXYPlot().getRenderer().setSeriesPaint(1,
					new Color(series2Color));
			chart.getXYPlot().getRenderer().setSeriesOutlinePaint(1,
					new Color(series2OutlineColor));
			chart.getXYPlot().setDomainGridlinePaint(new Color(gridColor));
			chart.getXYPlot().setRangeGridlinePaint(new Color(gridColor));
			chart.getXYPlot().setDomainAxis(0, new DateAxis());
			chart.getXYPlot().setDomainAxis(1, new DateAxis());
			chart.getXYPlot().setInsets(new RectangleInsets(-15, 0, 0, 10));
		}

		XYItemRenderer r = chart.getXYPlot().getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setShapesVisible(true);// 设置曲线是否显示数据点
		}
		// 设置Y轴

		NumberAxis numAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
		NumberFormat numFormater = NumberFormat.getNumberInstance();
		numFormater.setMinimumFractionDigits(2);
		numAxis.setNumberFormatOverride(numFormater);

		// 设置提示信息
		StandardXYToolTipGenerator tipGenerator = new StandardXYToolTipGenerator(
				"历史信息({1} 16:00,{2})", new SimpleDateFormat("MM-dd"),
				numFormater);
		r.setToolTipGenerator(tipGenerator);

		// 设置X轴（日期轴）
		DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));
		ChartRenderingInfo info = new ChartRenderingInfo(
				new StandardEntityCollection());
		try {
			int width = 500, height = 300;
			response.getOutputStream().write(
					ChartUtilities.encodeAsPNG(chart.createBufferedImage(width,
							height, info)));
			// Write the image map to the PrintWriter
			session.setAttribute("chart", chart);
			session.setAttribute("chartinfo", info);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 建立生成图形所需的数据集
	 * 
	 * @return 返回数据集
	 */
	private XYDataset createDateSet() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();// 时间曲线数据集合
		TimeSeries s1 = new TimeSeries("历史曲线", Day.class);// 创建时间数据源，每一个//TimeSeries在图上是一条曲线
		// s1.add(new Day(day,month,year),value),添加数据点信息
		s1.add(new Day(1, 2, 2009), 123.51);
		s1.add(new Day(2, 2, 2009), 122.1);
		s1.add(new Day(3, 2, 2009), 120.86);
		s1.add(new Day(4, 2, 2009), 122.50);
		s1.add(new Day(5, 2, 2009), 123.12);
		s1.add(new Day(6, 2, 2009), 123.9);
		s1.add(new Day(7, 2, 2009), 124.47);
		s1.add(new Day(8, 2, 2009), 124.08);
		s1.add(new Day(9, 2, 2009), 123.55);
		s1.add(new Day(10, 2, 2009), 122.53);
		s1.add(new Day(11, 2, 2009), 123.43);
		s1.add(new Day(12, 2, 2009), 122.73);
		dataset.addSeries(s1);
		dataset.setDomainIsPointsInTime(true);
		return dataset;
	}

	public int toIntHex(String num, int defaultValue) {
		try {
			if (num != null && num.startsWith("#"))
				num = num.substring(1);
			return Integer.parseInt(num, 16);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	
	public void getbarmaphot(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			int width = 500, height = 300;
			JFreeChart chart = (JFreeChart) request.getSession().getAttribute(
					"chartbar");
			ChartRenderingInfo info = (ChartRenderingInfo) request.getSession().getAttribute("chartbarinfor");
			chart.createBufferedImage(width, height, info);

			// 获取热点map
			String strimg = ChartUtilities.getImageMap("mapbarhot", info);
System.out.println(strimg);
			// 解析xml
			SAXReader reader = new SAXReader();
			StringReader r = new StringReader(strimg);

			Document document = reader.read(r);
			List<Node> nodes = document.selectNodes("/map/area");
			for (int t = 0; t < nodes.size(); t++) {
				Element ele1 = (Element) nodes.get(t);
				ele1.addAttribute("onmouseover", "showTooltip(this,event)");
				ele1.addAttribute("onmouseout", "UnTip(this)");

				ele1.addAttribute("spanid", "spanid");
			}

			strimg = document.asXML();
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().print(strimg);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public void chartbar(HttpServletRequest request,
			HttpServletResponse response) {

		int series1Color = toIntHex(request.getParameter("s1c"), 0x9bd2fb);
		int series1OutlineColor = toIntHex(request.getParameter("s1o"),0x0665aa);
		int series2Color = toIntHex(request.getParameter("s2c"), 0xFF0606);
		int series2OutlineColor = toIntHex(request.getParameter("s2o"),0x9d0000);
		int backgroundColor = toIntHex(request.getParameter("bc"), 0xFFFFFF);
		int gridColor = toIntHex(request.getParameter("gc"), 0);
		response.setHeader("Content-type", "image/png");
		CategoryDataset dataset = createDataset();// 建立数据集

		JFreeChart chart = ChartFactory.createBarChart("多种产品出售统计", // 图片标题
				"category", // 横坐标标题
				"number", // 纵坐标标题
				dataset, // 用于出图的数据集
				PlotOrientation.VERTICAL, // 柱子方向
				true, // 是否包含Legend
				true, // 是否包含tooltips
				true); // 是否包含urls
//		 chart.setBackgroundPaint(Color.WHITE);

//		if (chart != null) {
			chart.setBackgroundPaint(new Color(backgroundColor));
//			// ((XYAreaRenderer)
//			// chart.getXYPlot().getRenderer()).setOutline(true);
//			chart.getXYPlot().getRenderer().setSeriesPaint(0,
//					new Color(series1Color));
//			chart.getXYPlot().getRenderer().setSeriesOutlinePaint(0,
//					new Color(series1OutlineColor));
//			chart.getXYPlot().getRenderer().setSeriesPaint(1,
//					new Color(series2Color));
//			chart.getXYPlot().getRenderer().setSeriesOutlinePaint(1,
//					new Color(series2OutlineColor));
//			chart.getXYPlot().setDomainGridlinePaint(new Color(gridColor));
//			chart.getXYPlot().setRangeGridlinePaint(new Color(gridColor));
//			chart.getXYPlot().setDomainAxis(0, new DateAxis());
//			chart.getXYPlot().setDomainAxis(1, new DateAxis());
//			chart.getXYPlot().setInsets(new RectangleInsets(-15, 0, 0, 10));
//		}
		// 设置标题字体
		chart.getTitle().setFont(new Font(" 宋体 ", Font.CENTER_BASELINE, 12));

		CategoryPlot plot = chart.getCategoryPlot();

		/**//*
			 * //没有数据时显示的消息 plot.setNoDataMessage("数据还未录入！");
			 * plot.setNoDataMessageFont(new Font("宋体", Font.CENTER_BASELINE,
			 * 15));
			 */

		// 横坐标设置
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置横坐标上显示各个业务子项的字体
		domainAxis.setTickLabelFont(new Font(" 宋体 ", Font.PLAIN, 9));
		plot.setDomainAxis(domainAxis);

		// 纵坐标设置
		ValueAxis rangeAxis = (ValueAxis) plot.getRangeAxis();
		// 设置最高的一个 Item 与图片顶端的距离
		rangeAxis.setUpperMargin(0.15);
		// 设置最低的一个 Item 与图片底端的距离
		rangeAxis.setLowerMargin(0.15);
		plot.setRangeAxis(rangeAxis);

		BarRenderer renderer = new BarRenderer();
		renderer.setBaseOutlinePaint(Color.BLACK);
		/**//*
			 * // 设置图上的文字 renderer.setSeriesItemLabelFont(0, font);
			 * renderer.setSeriesItemLabelFont(1, font);
			 */
		// 设置legend的字体
		renderer.setBaseLegendTextFont(new Font(" 宋体 ",
				Font.LAYOUT_RIGHT_TO_LEFT, 10));
		// 设置 Wall 的颜色
		// renderer.setWallPaint(Color.gray);
		// 设置每种柱的颜色
		renderer.setSeriesPaint(0, new Color(133, 210, 38));
		renderer.setSeriesPaint(1, new Color(0, 131, 249));
		// 设置柱的 Outline 颜色
		renderer.setSeriesOutlinePaint(0, Color.BLACK);
		renderer.setSeriesOutlinePaint(1, Color.BLACK);
		// 设置每个平行柱的之间距离
		renderer.setItemMargin(0.03);

		// 显示每个柱的数值，并修改该数值的字体属性
		renderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		// 注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		// 设置柱形图上的文字偏离值
		renderer.setItemLabelAnchorOffset(10D);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelFont(new Font("宋体", Font.PLAIN,
				9));

		plot.setRenderer(renderer);

		// 设置柱的透明度
		plot.setForegroundAlpha(0.6f);
		// 设置横坐标和纵坐标的显示位置
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		
		ChartRenderingInfo info = new ChartRenderingInfo(
				new StandardEntityCollection());
		try {
			int width = 500, height = 300;
			response.getOutputStream().write(
					ChartUtilities.encodeAsPNG(chart.createBufferedImage(width,
							height, info)));
			// Write the image map to the PrintWriter
			request.getSession().setAttribute("chartbar", chart);
			request.getSession().setAttribute("chartbarinfor", info);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private CategoryDataset createDataset() {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(25.0, "Series 1", "Category 1");
		dataset.addValue(34.0, "Series 1", "Category 2");
		dataset.addValue(19.0, "Series 2", "Category 1");
		dataset.addValue(29.0, "Series 2", "Category 2");
		dataset.addValue(41.0, "Series 3", "Category 1");
		dataset.addValue(33.0, "Series 3", "Category 2");
		return dataset;

	}

	
	
	
	
	 public  void piechart3D(HttpServletRequest request,HttpServletResponse response){   
	        PieDataset dataset = getDataSet();   
	        JFreeChart chart = ChartFactory.createPieChart3D(   
	                " 项目进度分布",// 图表标题   
	                dataset,// data   
	                true,// include legend   
	                true, false);   
	        PiePlot3D plot = (PiePlot3D) chart.getPlot();   
	        // 图片中显示百分比:默认方式   
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(   
	                StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));   
	        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值，   
	        //{2} 表示所占比例 ,小数点后两位   
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(   
	                "{0}={1}({2})", NumberFormat.getNumberInstance(),   
	                new DecimalFormat("0.00%")));   
	        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例   
	        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(   
	                        "{0}"));   
	        // 指定图片的透明度(0.0-1.0)   
	        plot.setForegroundAlpha(1.0f);   
	        // 指定显示的饼图上圆形(true)还椭圆形(false)   
	        plot.setCircular(true);   
	        // 设置图上分类标签label的字体，解决中文乱码   
	        plot.setLabelFont(new Font("黑体", Font.PLAIN, 12));   
	        // 设置图上分类标签label的最大宽度，相对与plot的百分比   
	        plot.setMaximumLabelWidth(0.2);   
	        // 设置3D饼图的Z轴高度（0.0～1.0）   
	        plot.setDepthFactor(0.07);   
	        //设置起始角度，默认值为90，当为0时，起始值在3点钟方向   
	        plot.setStartAngle(45);   
	  
	        // 设置图标题的字体，解决中文乱码   
	        TextTitle textTitle = chart.getTitle();   
	        textTitle.setFont(new Font("黑体", Font.PLAIN, 20));   
	  
	        // 设置背景色为白色   
	        chart.setBackgroundPaint(Color.white);   
	        // 设置Legend部分字体，解决中文乱码   
	        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));   
	        ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
	        // 输出图片到文件   
	        try {
				int width = 500, height = 300;
				response.getOutputStream().write(
						ChartUtilities.encodeAsPNG(chart.createBufferedImage(width,
								height, info)));
				// Write the image map to the PrintWriter
				request.getSession().setAttribute("chartpie3D", chart);
				request.getSession().setAttribute("chartpieinfo3D", info);

			} catch (IOException e) {
				e.printStackTrace();
			}
	    }   
	  
	 public void getpiemaphot3D(HttpServletRequest request,
				HttpServletResponse response) {

			try {
				int width = 500, height = 300;
				JFreeChart chart = (JFreeChart) request.getSession().getAttribute(
						"chartpie3D");
				ChartRenderingInfo info = (ChartRenderingInfo) request.getSession().getAttribute("chartpieinfo3D");
				chart.createBufferedImage(width, height, info);

				// 获取热点map
				String strimg = ChartUtilities.getImageMap("mappiehot3D", info);
	System.out.println(strimg);
				// 解析xml
				SAXReader reader = new SAXReader();
				StringReader r = new StringReader(strimg);

				Document document = reader.read(r);
				List<Node> nodes = document.selectNodes("/map/area");
				for (int t = 0; t < nodes.size(); t++) {
					Element ele1 = (Element) nodes.get(t);
					ele1.addAttribute("onmouseover", "showTooltip(this,event)");
					ele1.addAttribute("onmouseout", "UnTip(this)");

					ele1.addAttribute("spanid", "spanid");
				}

				strimg = document.asXML();
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(strimg);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	 
	 
	    public  void piechart(HttpServletRequest request,HttpServletResponse response){   
	        PieDataset dataset = getDataSet();   
	        JFreeChart chart = ChartFactory.createPieChart(" 项目进度分布",// 图表标题   
	                dataset,// data   
	                true,// include legend   
	                true, false);   
	        PiePlot plot = (PiePlot) chart.getPlot();   
	        // 图片中显示百分比:默认方式   
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(   
	                StandardPieToolTipGenerator.DEFAULT_TOOLTIP_FORMAT));   
	        // 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值，   
	        //{2} 表示所占比例 ,小数点后两位   
	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(   
	                "{0}={1}({2})", NumberFormat.getNumberInstance(),   
	                new DecimalFormat("0.00%")));   
	        // 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例   
	        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(   
	                        "{0}"));   
	        // 指定图片的透明度(0.0-1.0)   
	        plot.setForegroundAlpha(1.0f);   
	        // 指定显示的饼图上圆形(true)还椭圆形(false)   
	        plot.setCircular(true);   
	        // 设置图上分类标签label的字体，解决中文乱码   
	        plot.setLabelFont(new Font("黑体", Font.PLAIN, 12));   
	        // 设置图上分类标签label的最大宽度，相对与plot的百分比   
	        plot.setMaximumLabelWidth(0.2);   
	        //设置起始角度，默认值为90，当为0时，起始值在3点钟方向   
	        plot.setStartAngle(45);   
	  
	        /**  
	         * 设置某一块凸出，第一个参数为dataSet的key，此方法只在PiePlot中有效  
	         */  
	        plot.setExplodePercent(" 运维", 0.2);   
	  
	        // 设置图标题的字体，解决中文乱码   
	        TextTitle textTitle = chart.getTitle();   
	        textTitle.setFont(new Font("黑体", Font.PLAIN, 20));   
	  
	        // 设置背景色为白色   
//	        chart.setBackgroundPaint(Color.white);   
	        // 设置Legend部分字体，解决中文乱码   
	        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));   
	  
	        // 输出图片到文件   
	        ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			try {
				int width = 500, height = 300;
				response.getOutputStream().write(
						ChartUtilities.encodeAsPNG(chart.createBufferedImage(width,
								height, info)));
				// Write the image map to the PrintWriter
				request.getSession().setAttribute("chartpie", chart);
				request.getSession().setAttribute("chartpieinfo", info);

			} catch (IOException e) {
				e.printStackTrace();
			}
	    }   
	  
	    public void getpiemaphot(HttpServletRequest request,
				HttpServletResponse response) {

			try {
				int width = 500, height = 300;
				JFreeChart chart = (JFreeChart) request.getSession().getAttribute(
						"chartpie");
				ChartRenderingInfo info = (ChartRenderingInfo) request.getSession().getAttribute("chartpieinfo");
				chart.createBufferedImage(width, height, info);

				// 获取热点map
				String strimg = ChartUtilities.getImageMap("mappiehot", info);
	System.out.println(strimg);
				// 解析xml
				SAXReader reader = new SAXReader();
				StringReader r = new StringReader(strimg);

				Document document = reader.read(r);
				List<Node> nodes = document.selectNodes("/map/area");
				for (int t = 0; t < nodes.size(); t++) {
					Element ele1 = (Element) nodes.get(t);
					ele1.addAttribute("onmouseover", "showTooltip(this,event)");
					ele1.addAttribute("onmouseout", "UnTip(this)");

					ele1.addAttribute("spanid", "spanid");
				}

				strimg = document.asXML();
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(strimg);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	    
	    public  PieDataset getDataSet() {   
	        DefaultPieDataset dataset = new DefaultPieDataset();   
	        dataset.setValue(" 市场前期", new Double(10));   
	        dataset.setValue(" 立项", new Double(15));   
	        dataset.setValue(" 计划", new Double(10));   
	        dataset.setValue(" 需求与设计", new Double(10));   
	        dataset.setValue(" 执行控制", new Double(35));   
	        dataset.setValue(" 收尾", new Double(10));   
	        dataset.setValue(" 运维", new Double(10));   
	        return dataset;   
	    }  
	
	    
	    
	    
	
	  public void chartbar2(HttpServletRequest request,HttpServletResponse response){
		  JFreeChart chart=ChartFactory.createBarChart3D(
	                "图书销量统计图",
	                "图书",//目录轴的显示标签
	                "销量",//数值轴的显示标签
	                getDataSet1(),
	                PlotOrientation.VERTICAL,//设置图表方向
	                false,
	                false,
	                false        
	        );
	        
	        //设置标题
	        chart.setTitle(new TextTitle("图书销量统计图",new Font("黑体",Font.ITALIC,22)));
	        //设置图表部分
	        CategoryPlot plot=(CategoryPlot)chart.getPlot();
	        
	        CategoryAxis categoryAxis=plot.getDomainAxis();//取得横轴
	        categoryAxis.setLabelFont(new Font("宋体",Font.BOLD,22));//设置横轴显示标签的字体
	        categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);//分类标签以４５度倾斜
	        categoryAxis.setTickLabelFont(new Font("宋体",Font.BOLD,18));//分类标签字体
	        
	        NumberAxis numberAxis=(NumberAxis)plot.getRangeAxis();//取得纵轴
	        numberAxis.setLabelFont(new Font("宋体",Font.BOLD,42));//设置纵轴显示标签字体
	        ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			try {
				int width = 500, height = 300;
				response.getOutputStream().write(
						ChartUtilities.encodeAsPNG(chart.createBufferedImage(width,
								height, info)));
				// Write the image map to the PrintWriter
				request.getSession().setAttribute("chartbar1", chart);
				request.getSession().setAttribute("chartbarinfo1", info);

			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  public void getpiemaphot1(HttpServletRequest request,
				HttpServletResponse response) {

			try {
				int width = 500, height = 300;
				JFreeChart chart = (JFreeChart) request.getSession().getAttribute(
						"chartbar1");
				ChartRenderingInfo info = (ChartRenderingInfo) request.getSession().getAttribute("chartbarinfo1");
				chart.createBufferedImage(width, height, info);

				// 获取热点map
				String strimg = ChartUtilities.getImageMap("mapbarhot2", info);
	System.out.println(strimg);
				// 解析xml
				SAXReader reader = new SAXReader();
				StringReader r = new StringReader(strimg);

				Document document = reader.read(r);
				List<Node> nodes = document.selectNodes("/map/area");
				for (int t = 0; t < nodes.size(); t++) {
					Element ele1 = (Element) nodes.get(t);
					ele1.addAttribute("onmouseover", "showTooltip(this,event)");
					ele1.addAttribute("onmouseout", "UnTip(this)");

					ele1.addAttribute("spanid", "spanid");
				}

				strimg = document.asXML();
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(strimg);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	   public CategoryDataset getDataSet1(){
	        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
	        dataset.addValue(47000,"", "Spring2.0宝典");
	        dataset.addValue(38000,"","轻量级的J2EE");
	        dataset.addValue(31000, "", "JavaScript权威指南");
	        dataset.addValue(25000, "", "Ajax In Action");
	        return dataset;
	    }


	
	
}

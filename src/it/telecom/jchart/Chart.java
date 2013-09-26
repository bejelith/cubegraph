package it.telecom.jchart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

public class Chart extends JFrame {

  private static final long serialVersionUID = 1L;

  public Chart(String applicationTitle, String chartTitle, Date[] x, double[]...y) {
        super(applicationTitle);
        // This will create the dataset 
        XYDataset dataset = createDataset(x, y);
        // based on the dataset we create the chart
        JFreeChart chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        setContentPane(chartPanel);
        setMinimumSize(new Dimension(1200,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int xp = (int) ((dimension.getWidth() - getWidth()) / 2);
        int yp = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(xp, yp);
    }
    
    
/** * Creates a sample dataset */

	private XYDataset createDataset(Date[] x, double[][] y) {
		TimeSeries[] ts = new TimeSeries[y.length];
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		int serie = 0, i = 0;
		for(TimeSeries s : ts){
			if(serie == 0){
				s = new TimeSeries("Dati");				
			}else{
				s = new TimeSeries("Previsione");
			}
			
			for(i = 0; i < y[serie].length; i++){
				try{
					s.add(new Minute(x[i]), y[serie][i]);
				}catch(IndexOutOfBoundsException e){}
			}
			dataset.addSeries(s);
			serie++;
		}
		/*
		s1.add(new Month(2, 2001), 181.8);
		s1.add(new Month(3, 2001), 167.3);
		s1.add(new Month(4, 2001), 153.8);
		s1.add(new Month(5, 2001), 167.6);
		s1.add(new Month(6, 2001), 158.8);
		s1.add(new Month(7, 2001), 148.3);
		s1.add(new Month(8, 2001), 153.9);
		s1.add(new Month(9, 2001), 142.7);
		s1.add(new Month(10, 2001), 123.2);
		s1.add(new Month(11, 2001), 131.8);
		s1.add(new Month(12, 2001), 139.6);
		s1.add(new Month(1, 2002), 142.9);
		s1.add(new Month(2, 2002), 138.7);
		s1.add(new Month(3, 2002), 137.3);
		s1.add(new Month(4, 2002), 143.9);
		s1.add(new Month(5, 2002), 139.8);
		s1.add(new Month(6, 2002), 137.0);
		s1.add(new Month(7, 2002), 132.8);
		*/
		return dataset;

	}

    private JFreeChart createChart(XYDataset dataset, String title) {
        
        /*JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false);
		*/
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				this.getTitle(), // title
				"Ora", // x-axis label
				"Numero chiamate", // y-axis label
				dataset, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);

		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
			renderer.setDrawSeriesLineAsPath(true);
		}

		DateAxis axis = (DateAxis) plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));

		return chart;
        
    }
    /*
    public static void main(String[] args) {
    	Chart c = new Chart("AppTitle", "ChartTitle");
    	c.setMinimumSize(new Dimension(544,444));
    	c.setVisible(true);
    	
    }
    */
} 
package it.telecom.forecast;

import it.telecom.forecast.holtwinters.Forecast;
import it.telecom.jchart.Chart;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.ComponentOrientation;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;


public class Main {
	
	static boolean selected = false;
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		Date[] Xdata; 
		Date startdate = null;
		double[] dataValues;
		double[] prevision = null;
		boolean save = false;
		MetricThread metric = new MetricThread();
		//SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat datefile = new SimpleDateFormat("yyyyMMdd");
		/*
		 * Main Frame
		 */
		JFrame frame = new JFrame();
		/* 
		 * Panels
		 */
		JPanel panelback = new JPanel();
		JPanel paneltop = new JPanel();
		JPanel panelbot = new JPanel();
		
		JButton okbut = new JButton("OK");
		/*
		 * TextFields
		 */
		final JSpinner day = new JSpinner();
		day.setModel(new SpinnerNumberModel(cal.get(Calendar.DAY_OF_MONTH), 1, 31, 1));
		final JSpinner month = new JSpinner();
		month.setModel(new SpinnerNumberModel(cal.get(Calendar.MONTH)+1, 1, 12, 1));
		
		final JSpinner year = new JSpinner();
		year.setModel(new SpinnerNumberModel(cal.get(Calendar.YEAR), null, null, 1));
		year.setEditor(new JSpinner.NumberEditor(year, "#"));
		final JTextField lblExpression = new JTextField("sum(record_cdr_osp_3_1(1))");
		lblExpression.setPreferredSize(new Dimension(370,30));
		/*
		 * JLabels
		 */
		JLabel lblNewLabel = new JLabel();
		JLabel lblNewLabel_2 = new JLabel();
		JLabel lblNewLabel_3 = new JLabel();
		JCheckBox chckbxSaveToFile = new JCheckBox("Save to Desktop");
		
		day.setPreferredSize(new Dimension(50,20));
	
		frame.setSize(404, 237);
		okbut.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(lblExpression.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Insert all data");
				}else{
					selected = true;
				}
			}
		});
		
		frame.getContentPane().add(panelback);
		panelback.add(paneltop);
		
		JPanel panelconf = new JPanel();
		panelconf.setAlignmentY(0.0f);
		panelconf.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelconf.setPreferredSize(new Dimension(370, 65));
		panelback.add(panelconf);
		panelconf.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("Cube Hostname");
		lblNewLabel_1.setIconTextGap(0);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setBorder(null);
		lblNewLabel_1.setPreferredSize(new Dimension(90, 10));
		panelconf.add(lblNewLabel_1);
		
		JTextField txtHostname = new JTextField();
		txtHostname.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		txtHostname.setText("localhost");
		txtHostname.setToolTipText("hostname");
		txtHostname.setPreferredSize(new Dimension(270, 25));
		panelconf.add(txtHostname);
		
		JLabel lblNewLabel_4 = new JLabel("Cube Port");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_4.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_4.setPreferredSize(new Dimension(90, 10));
		panelconf.add(lblNewLabel_4);
		
		JFormattedTextField txtPort = new JFormattedTextField(NumberFormat.getIntegerInstance());
		txtPort.setValue(1081);
		txtPort.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		txtPort.setPreferredSize(new Dimension(270, 25));
		panelconf.add(txtPort);
		panelback.add(panelbot);
		
		paneltop.setLayout(new GridLayout(2, 3, 3, 3));
		paneltop.add(new JLabel("Day"));
		paneltop.add(new JLabel("Month"));
		paneltop.add(new JLabel("Year"));
		paneltop.add(day);
		paneltop.add(month);
		paneltop.add(year);
		
		panelbot.setLayout(new FlowLayout());
		panelbot.add(lblExpression);
		panelbot.add(lblNewLabel);
		panelbot.add(lblNewLabel_2);
		panelbot.add(lblNewLabel_3);
		panelbot.add(chckbxSaveToFile);
		panelbot.add(okbut);
		
		paneltop.setPreferredSize(new Dimension(370,50));
		panelbot.setPreferredSize(new Dimension(370, 70));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("CubeGraph");
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		while(true){
			while(!selected){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			selected = false;
			//cal.set((Integer)year.getValue(), (Integer)month.getValue() - 1, (Integer)day.getValue(), 0, 0, 0);
			
			cal.set((Integer)year.getValue(), (Integer)month.getValue() - 1, (Integer)day.getValue(), 7, 0);
			startdate = cal.getTime();
			cal.set((Integer)year.getValue(), (Integer)month.getValue() - 1, (Integer)day.getValue(), 20, 0);
			save = chckbxSaveToFile.isSelected();
			metric.setCubeHost(txtHostname.getText());
			metric.setCubePort(txtPort.getValue().toString());
			metric.chiamaMetrica(startdate, cal.getTime(), lblExpression.getText());
			Xdata = metric.keysToArray();
			dataValues = metric.toArray();
			Forecast f = new Forecast(
					Xdata, 
					dataValues, 
					datefile.format(startdate)
					);
			try {
				prevision = f.calc(save);
			} catch (IOException e) {
				System.err.println("Impossibile salvare i dati su file");
				e.printStackTrace();
			}
			
			Chart chart = new Chart("Dati e previsione " + metric.getCubeExpression(), "b", Xdata, dataValues, prevision);
	    	chart.setVisible(true);
	    	chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	
		}
		
	}
	

}

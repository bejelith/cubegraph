package it.telecom.forecast.holtwinters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;

public class Forecast {

	private FileWriter fileWritter;
	private double[] datasource;
	private String file;

	public Forecast(Date[] keys, double[] datasource, String file) {
		this.datasource = datasource;
		this.file = file;
		/*try {
			fileWritter = new FileWriter(new File(
					"c:/Users/bejelith/desktop/test/" + file + ".csv"), false);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}*/
	}

	public double[] calc(boolean save) throws IOException {
		int i = 0;
		new SimpleDateFormat("HH:mm:ss");
		DoubleExponentialSmoothingTest smooth = new DoubleExponentialSmoothingTest("uno", 0.8, 0.8);
		DataSet dataset = smooth.testDoubleExponentialSmoothing(datasource);
		Iterator<DataPoint> it = dataset.iterator();
		double prevision[] = new double[dataset.size()];
		while(it.hasNext()){
			DataPoint dp = it.next();
			prevision[i++] = dp.getDependentValue();
		}
		try {
			if(save){
				fileWritter = new FileWriter(new File(
					System.getProperty("user.home") + File.separator + "Desktop" + File.separator + file + ".csv"), false);
				fileWritter.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
/*		
		
		Iterator<DataPoint> it = dataset.iterator();
		while (it.hasNext()) {
			DataPoint dp = it.next();
			if (i < keys.length) {
				time = keys[i];
				//System.out.print(outformat.format(time) + ", ");
				fileWritter.append("\"" + outformat.format(time) + "\",");
				
			//	System.out.print("\"" + datasource[i] + "\",");
				fileWritter.append("\"" + String.format("%.2f", datasource[i]) + "\",");
				
			//	System.out.print("\"" + String.format("%.2f", dp.getDependentValue()) + "\"\n");
				fileWritter.append("\"" + String.format("%.2f", dp.getDependentValue()) + "\"\n");
				//result.add();
			} else {
				time = keys[i - 1];
				Calendar c = Calendar.getInstance();
				c.setTime(time);
				c.add(Calendar.SECOND, 10);
				time = c.getTime();
				//System.out.print(outformat.format(time) + ", ");
				fileWritter.append("\"" + outformat.format(time) + "\",");
				
				//System.out.print("\", ");
				fileWritter.append("\", ");
				
				//System.out.print("\"" + String.format("%.2f", dp.getDependentValue()) + "\"\n");
				fileWritter.append("\"" + String.format("%.2f", dp.getDependentValue()) + "\"\n");
			}
			i++;
		}
*/
		
    	return prevision;
	}
}

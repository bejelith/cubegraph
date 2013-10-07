package it.telecom.forecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import com.google.gson.Gson;

public class MetricThread {

	private InputStreamReader reader = null;
	private BufferedReader br = null;
	private ArrayList<Metric> outbuffer = null;
	private boolean debug = true;
	private String cubeHost = "172.16.3.210";
	private String cubePort = "1081";
	private String cubeExpression = "sum(record_cdr_osp_3_1(1))";
	
	private Date cubeStop = null;
	private Date cubeStart = null;
	private String cubeLimit = null;
	private String cubeStep = "3e5"; // 5minuti
	//private String cubeStep = "36e5"; // 60minuti
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
	
	
	public void chiamaMetrica(Date start, Date end, String expression) {
		HttpURLConnection conn = null;
		Calendar cal = Calendar.getInstance();
		URL url = null;
		StringBuilder sb = new StringBuilder();
		Gson gson = new Gson();
		if(expression != null && expression != ""){
			this.cubeExpression = expression;
		}
		this.setCubeStart(start);
		System.out.println("Start date: " + this.getCubeStart());
	/*	while(true) {
			if(this.getCubeStart().compareTo(end) == 0){
				System.out.println("LAST DAY");
				break;
			}
			*/
			cal.setTime(this.getCubeStart());
			cal.add(Calendar.DATE, 1);// ADD one day
			this.setCubeStop(cal.getTime());
			System.out.println("Stop date: " + this.getCubeStop());
			dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
			sb.append("http://" + cubeHost + ":" + cubePort	+ "/1.0/metric/get?expression=" + cubeExpression.replace("+", "%2B"));
			if (this.cubeStart != null) {
				sb.append("&start=" + dateformat.format(cubeStart));
			}
			if (this.cubeStop != null) {
				sb.append("&stop=" + dateformat.format(cubeStop));
			}
			
			if (this.cubeLimit != null) {
				sb.append("&limit=" + cubeLimit);
			}
			if (this.cubeStep != null) {
				sb.append("&step=" + cubeStep);
			}
			
			try {
				if(debug) {
					WriteLog.scriviRigaLog("Cube URL: " + sb.toString());
				}
				url = new URL(sb.toString());
				sb.delete(0, sb.length());
				/*
				 * Open the connection to Cube
				 */
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				reader = new InputStreamReader(conn.getInputStream());
				br = new BufferedReader(reader);
				while (reader.ready()) {
					sb.append(br.readLine());
				}
				br.close();
				reader.close();
				conn.disconnect();
				
				this.outbuffer = new ArrayList<Metric>(Arrays.asList(gson.fromJson(
						sb.toString(), Metric[].class)));
				sb.delete(0, sb.length());
				/*
				if(outbuffer.size() > 0 ){
					Forecast f = new Forecast(
										this.keysToArray(), 
										this.toArray(), 
										datefile.format(getCubeStart())
										);
					f.calc();
				}else{
					System.err.println("Empty result from cube");
				}
				*/
				cal.setTime(this.getCubeStart());
				cal.add(Calendar.DATE, 1);// ADD one day
				this.setCubeStart(cal.getTime());
			} catch (ConnectException e){
				System.err.println("Impossibile connettersi a Cube");
				System.exit(-1);
			} catch (IOException e) {
				System.err.println("IOException: " + e.getMessage());
				System.exit(-2);
			}
		//}
	} 
	
	public ArrayList<Metric> getOutbuffer() {
		return outbuffer;
	}

	public String getCubeHost() {
		return cubeHost;
	}

	public void setCubeHost(String cubeHost) {
		this.cubeHost = cubeHost;
	}

	public String getCubeExpression() {
		return cubeExpression;
	}

	public void setCubeExpression(String cubeExpression) {
		this.cubeExpression = cubeExpression;
	}

	public Date getCubeStop() {
		return cubeStop;
	}

	public void setCubeStop(Date cubeStop) {
		this.cubeStop = cubeStop;
	}

	public Date getCubeStart() {
		return cubeStart;
	}

	public void setCubeStart(Date cubeStart) {
		this.cubeStart = cubeStart;
	}

	public String getCubeLimit() {
		return cubeLimit;
	}

	public void setCubeLimit(String cubeLimit) {
		this.cubeLimit = cubeLimit;
	}

	public String getCubeStep() {
		return cubeStep;
	}

	public void setCubeStep(String cubeStep) {
		this.cubeStep = cubeStep;
	}

	public String getCubePort() {
		return cubePort;
	}

	public void setCubePort(String cubePort) {
		if(cubePort != "0" && cubePort != null)
			this.cubePort = cubePort;
	}
	
	public double[] toArray(){
		double[] out = new double[outbuffer.size()];
		Iterator<Metric> it = outbuffer.iterator();
		int i = 0;
		while(it.hasNext()){
			out[i++] = Double.valueOf(it.next().getValue());
		}
		return out;
	}
	
	public Date[] keysToArray(){
		Date[] out = new Date[outbuffer.size()];
		Iterator<Metric> it = outbuffer.iterator();
		int i = 0;
		while(it.hasNext()){
			try {
				out[i++] = dateformat.parse(it.next().getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return out;
	}
}

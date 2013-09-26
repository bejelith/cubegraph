package it.telecom.forecast;
/**
 * Progetto    : Pascalplug <br>
 * Descrizione : Plugin Nagios tra CTS e Pascal <br>
 * Package     : it.telecom.cts.pascalplug.utl <br> 
 * Modulo      : WriteLog
 *
 * 
 * @version 21-giu-2013
 * @author Martino Marangi
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteLog {

	private static String nomeFile = null;

	/** Nome del file di LOG */
	// private static String nomeFile = "d:\\prova.log";
	/**
	 * Metodo per valorizzare il nome del file di log
	 * 
	 * @param path
	 *            il nome del file comprensivo di PATH
	 */
	public static void setNomeFile(String path) {
		nomeFile = path;
	}

	/**
	 * Metodo di comodo per la scrittura del file di log.<br>
	 * il metodo non puo' essere invocato dall'esterno in quanto il logging
	 * dell'applicazione segue un determinato tracciato del file di LOG
	 * 
	 * @param log
	 */
	private static void scriviLog(String log) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(nomeFile,
					true));
			bw.write(log);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo per scrivere una singola riga di LOG
	 * 
	 * @param messaggio
	 */
	public static synchronized void scriviRigaLog(String messaggio) {

		System.err.println(messaggio);
		if (null == nomeFile)
			return;
		String log = "";
		Date d = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		log += "INFO " + dt.format(d) + " " + messaggio + "\n";
		scriviLog(log);
	}

	public static synchronized void errore(String messaggio,Exception a) {

		System.out.println(messaggio);
		if (null == nomeFile)
			return;
		String log = "";
		Date d = new Date();
		SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		log += "ERRO " + dt.format(d) + " " + messaggio + "\n";
		log += "classe " + a.getClass() + "\n";
		log += "" + a.getCause() + "\n";
		scriviLog(log);
	}
}
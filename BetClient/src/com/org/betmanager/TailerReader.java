package com.org.betmanager;

import java.io.File;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;

public class TailerReader {
	PCTailListener listener;

	public PCTailListener getListener() {
		return listener;
	}

	public void setListener(PCTailListener listener) {
		this.listener = listener;
	}

	public static class PCTailListener extends TailerListenerAdapter {
		StringBuffer string_buffer;

		public StringBuffer getString_buffer() {
			return string_buffer;
		}

		public void setString_buffer(StringBuffer string_buffer) {
			this.string_buffer = string_buffer;
		}

		public PCTailListener() {
			super();
			this.string_buffer = new StringBuffer();
		}

		public void handle(String line) {
			if (string_buffer.length() > 100000)
				string_buffer = new StringBuffer();
			else {
				string_buffer.append(line);
				string_buffer.append("<br>");
			}
		}
	}

	public static class PCTailListener1 extends TailerListenerAdapter {
		public void handle(String line) {
			System.out.println(line);
		}
	}

	public TailerReader() {
		super();
		// TODO Auto-generated constructor stub
		this.listener = new TailerReader.PCTailListener();
	}

	public void getdata() {
		// TODO code application logic here
		File pcounter_log = new File(".\\log\\client.log");

		try {

			Tailer tailer = new Tailer(pcounter_log, this.listener, 500, true);
			Thread thread = new Thread(tailer);
			thread.start();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		File pcounter_log = new File(".\\log\\client.log");

		try {
			TailerListener listener = new TailerReader.PCTailListener1();
			Tailer tailer = new Tailer(pcounter_log, listener, 1000, true);
			tailer.run();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}

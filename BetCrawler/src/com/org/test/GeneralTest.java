package com.org.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class GeneralTest {
	@Test
	public void testJson() {
		try {
			File f = new File("G:\\Users\\Thai Son\\Desktop\\odd_data.txt");
			FileInputStream ip = new FileInputStream(f);
			String content = IOUtils.toString(ip, "utf-8");
			JSONObject obj = new JSONObject(content);
			JSONArray array = obj.getJSONArray("today");
			System.out.println(array.getJSONArray(2));
			for (int i = 0; i < array.length(); i++) {
				System.out.println(array.getJSONArray(i));
			}
			System.out.println();
			
			  
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

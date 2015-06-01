package com.zhangdong.JiShi.Tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XmlAndJson {

	
	
	public static String cc(String xml) {
		InputStream is = null;
		String a=null;
		try {
			is = new ByteArrayInputStream(xml.getBytes());
 
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "utf-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				if (event == XmlPullParser.START_TAG) {
					a= parser.nextText();
					
					/*Toast.makeText(MainActivity.this, parser.nextText(), 0)
							.show();*/
				}
				event = parser.next();

			}
			return a;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	
}

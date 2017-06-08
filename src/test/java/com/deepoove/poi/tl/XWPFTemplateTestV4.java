package com.deepoove.poi.tl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.tl.datasource.DataSourceTest;

/**
 * @author Sayi
 * @version 0.0.4
 */
public class XWPFTemplateTestV4 {

	@Test
	public void testV4() throws Exception {
		Map<String, Object> datas = new HashMap<String, Object>(){{
			put("header_version", "ver 0.0.3");
			put("hello", "ver 0.0.3");
			put("logo", new PictureRenderData(100, 120, "src/test/resources/logo.png"));
			put("title", new TextRenderData("9d55b8", "Deeply in love with the things you love,\\n just deepoove."));
			put("changeLog", new TableRenderData(new ArrayList<RenderData>(){{
				add(new TextRenderData("d0d0d0", ""));
				add(new TextRenderData("d0d0d0", "introduce"));
			}},new ArrayList<Object>(){{
				add("1;add new # gramer");
				add("2;support insert table");
				add("3;support more style");
			}}, "no datas", 10600));
			put("website", "http://www.deepoove.com/poi-tl");
		}};

		
		XWPFTemplate template = XWPFTemplate.compile("src/test/resources/PB.docx").render(datas);;

		FileOutputStream out = new FileOutputStream("out.docx");
		template.write(out);
		out.flush();
		out.close();
		template.close();
	}
	
	@Test
	public void testRenderObject() throws Exception {
		DataSourceTest obj = new DataSourceTest();
		obj.setHeader_version("v0.0.3");
		obj.setHello("v0.0.3");
		obj.setWebsite("http://www.deepoove.com/poi-tl");
		obj.setLogo(new PictureRenderData(100, 120, "src/test/resources/logo.png"));
		obj.setTitle(new TextRenderData("9d55b8",
				"Deeply in love with the things you love,\\n just deepoove."));
		obj.setChangeLog(new TableRenderData(new ArrayList<RenderData>() {
			{
				add(new TextRenderData("d0d0d0", ""));
				add(new TextRenderData("d0d0d0", "introduce"));
			}
		}, new ArrayList<Object>() {
			{
				add("1;add new # gramer");
				add("2;support insert table");
				add("3;support more style");
			}
		}, "no datas", 10600));
		obj.setBaseProp("1232456");
		
		
		XWPFTemplate template = XWPFTemplate.compile("src/test/resources/PB.docx").render(obj);;

		FileOutputStream out = new FileOutputStream("out.docx");
		template.write(out);
		template.close();
		out.flush();
		out.close();
		
		
		
		
		
	}
	
	

}

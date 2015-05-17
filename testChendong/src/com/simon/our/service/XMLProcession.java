package com.simon.our.service;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 读xml文档进行读取处理，得到该xml文档的根元素
 * @author SSS
 *
 */
public class XMLProcession {

	public Element getRootElement(String filePath) {
		Element rootElement = null;
		// 获得工厂实例
		DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document document = null;
		try {
			dBuilder = dBuilderFactory.newDocumentBuilder();
			document = dBuilder.parse(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		rootElement = document.getDocumentElement();
		return rootElement;
	}
}

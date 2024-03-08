package org.hermione.minis.web;


import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Slf4j
public class XmlScanComponentHelper {
    public static List<String> getNodeValue(URL xmlPath) {
        List<String> packages = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(xmlPath); //加载配置文件
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        Element root = Objects.requireNonNull(document).getRootElement();
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) { //得到XML中所有的base-package节点
            Element element = it.next();
            packages.add(element.attributeValue("base-package"));
        }
        return packages;
    }
}

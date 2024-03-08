package org.hermione.minis.web;


import java.net.URL;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

@Slf4j
public class ClassPathXmlResource implements Resource {
    Document document;
    Element rootElement;
    Iterator<Element> elementIterator;

    public ClassPathXmlResource(URL xmlPath) {
        SAXReader saxReader=new SAXReader();
        try {
            this.document = saxReader.read(xmlPath);
            this.rootElement=document.getRootElement();
            this.elementIterator=this.rootElement.elementIterator();
        } catch (DocumentException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }
    @Override
    public boolean hasNext() {
        return this.elementIterator.hasNext();
    }
    @Override
    public Object next() {
        return this.elementIterator.next();
    }
}

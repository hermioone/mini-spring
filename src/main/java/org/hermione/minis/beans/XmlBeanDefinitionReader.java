package org.hermione.minis.beans;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.hermione.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        Element element = (Element) resource.next();
        String beanID = element.attributeValue("id");
        String beanClassName = element.attributeValue("class");

        BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

        // 解析 构造器 注入参数
        List<Element> constructorElements = element.elements("constructor-arg");
        ArgumentValues AVS = new ArgumentValues();
        for (Element e : constructorElements) {
            String pType = e.attributeValue("type");
            String pName = e.attributeValue("name");
            String pValue = e.attributeValue("value");
            AVS.addArgumentValue(new ArgumentValue(pType, pName, pValue));
        }
        beanDefinition.setConstructorArgumentValues(AVS);
        //end of handle constructor

        // 解析 setter 注入参数
        List<Element> propertyElements = element.elements("property");
        PropertyValues PVS = new PropertyValues();
        List<String> refs = new ArrayList<>();
        for (Element e : propertyElements) {
            String pType = e.attributeValue("type");
            String pName = e.attributeValue("name");
            String pValue = e.attributeValue("value");
            String pRef = e.attributeValue("ref");
            String pV = "";
            boolean isRef = false;
            if (StringUtils.isNotBlank(pValue)) {
                pV = pValue;
            } else if (StringUtils.isNotBlank(pRef)) {
                isRef = true;
                pV = pRef;
                refs.add(pRef);
            }
            PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
        }
        beanDefinition.setPropertyValues(PVS);
        String[] refArray = refs.toArray(new String[0]);
        beanDefinition.setDependsOn(refArray);
        //end of handle properties

        this.simpleBeanFactory.registerBeanDefinition(beanID, beanDefinition);
    }
}


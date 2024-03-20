package org.hermione.minis.beans.factory.xml;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.hermione.minis.beans.factory.config.ConstructorArgumentValue;
import org.hermione.minis.beans.factory.config.ConstructorArgumentValues;
import org.hermione.minis.beans.factory.config.BeanDefinition;
import org.hermione.minis.beans.PropertyValue;
import org.hermione.minis.beans.PropertyValues;
import org.hermione.minis.beans.factory.support.AbstractBeanFactory;
import org.hermione.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    AbstractBeanFactory bf;

    public XmlBeanDefinitionReader(AbstractBeanFactory bf) {
        this.bf = bf;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            String initMethod = element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            if (StringUtils.isNotBlank(initMethod)) {
                beanDefinition.setInitMethodName(initMethod);
            }

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(pType, pName, pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            //handle properties
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
                if (pValue != null && !pValue.isEmpty()) {
                    pV = pValue;
                } else if (pRef != null && !pRef.isEmpty()) {
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

            this.bf.registerBeanDefinition(beanID, beanDefinition);
        }
    }
}


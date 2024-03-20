package org.hermione.minis.batis;


import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.jdbc.core.JdbcTemplate;

@Slf4j
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    @Autowired(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Getter
    @Setter
    private String mapperLocations;

    private final Map<String, MapperNode> mapperNodeMap = new HashMap<>();

    public DefaultSqlSessionFactory() {
    }

    /**
     * 在 applicationContext.xml 中使用 init-method 方法初始化
     */
    public void init() {
        scanLocation(this.mapperLocations);
    }

    private void scanLocation(String location) {
        String sLocationPath = Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath() + location;
        File dir = new File(sLocationPath);
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanLocation(location + "/" + file.getName());
            } else {
                buildMapperNodes(location + "/" + file.getName());
            }
        }
    }

    private void buildMapperNodes(String filePath) {
        log.info("buildMapperNodes: " + filePath);
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();

            String namespace = rootElement.attributeValue("namespace");

            Iterator<Element> nodes = rootElement.elementIterator();
            while (nodes.hasNext()) {
                Element node = nodes.next();
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode selectNode = new MapperNode();
                selectNode.setNamespace(namespace);
                selectNode.setId(id);
                selectNode.setParameterType(parameterType);
                selectNode.setResultType(resultType);
                selectNode.setSql(sql);
                selectNode.setParameter("");

                this.mapperNodeMap.put(namespace + "." + id, selectNode);
            }
        } catch (Exception ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

    @Override
    public SqlSession openSession() {
        SqlSession newSqlSession = new DefaultSqlSession();
        newSqlSession.setJdbcTemplate(jdbcTemplate);
        newSqlSession.setSqlSessionFactory(this);

        return newSqlSession;
    }
}

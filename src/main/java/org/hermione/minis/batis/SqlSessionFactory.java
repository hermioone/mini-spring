package org.hermione.minis.batis;


public interface SqlSessionFactory {
    SqlSession openSession();

    MapperNode getMapperNode(String name);
}

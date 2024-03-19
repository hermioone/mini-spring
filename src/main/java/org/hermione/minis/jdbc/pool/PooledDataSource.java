package org.hermione.minis.jdbc.pool;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@Slf4j
@Getter
public class PooledDataSource implements DataSource {

    // 没有做并发控制
    private List<PooledConnection> connections = null;
    @Setter
    private String driverClassName;
    @Setter
    private String url;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private int initialSize = 2;
    private Properties connectionProperties;

    public PooledDataSource() {
    }

    private void initPool() {
        try {
            this.connections = new ArrayList<>(initialSize);
            for(int i = 0; i < initialSize; i++){
                Connection connect = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connect, false);
                this.connections.add(pooledConnection);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver();
    }

    protected Connection getConnectionFromDriver() throws SQLException {
        if (this.connections == null) {
            initPool();
        }

        PooledConnection pooledConnection= getAvailableConnection();

        while(pooledConnection == null){
            pooledConnection = getAvailableConnection();

            if(pooledConnection == null){
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException ignored) {
                }
            }
        }

        return pooledConnection;
    }

    private PooledConnection getAvailableConnection() throws SQLException{
        for(PooledConnection pooledConnection : this.connections){
            if (!pooledConnection.isActive()){
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }

        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}

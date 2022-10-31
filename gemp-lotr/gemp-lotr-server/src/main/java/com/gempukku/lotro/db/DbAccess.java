package com.gempukku.lotro.db;

import com.gempukku.lotro.common.AppConfig;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.util.Properties;

public class DbAccess {
    private final DataSource _dataSource;

    public DbAccess() {
        this(AppConfig.getProperty("db.connection.url"), AppConfig.getProperty("db.connection.username"), AppConfig.getProperty("db.connection.password"), false);
    }

    public DbAccess(String url, String user, String pass, boolean batch) {
        try {
            Class.forName(AppConfig.getProperty("db.connection.class"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find the DB driver", e);
        }

        _dataSource = setupDataSource(url, user, pass, batch);
    }

    public DataSource getDataSource() {
        return _dataSource;
    }

    private DataSource setupDataSource(String connectURI, String user, String pass, Boolean batch) {
        //
        // First, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
        Properties props = new Properties() {{
            setProperty("user", user);
            setProperty("password", pass);
            setProperty("rewriteBatchedStatements", batch.toString().toLowerCase());
            setProperty("innodb_autoinc_lock_mode", "2");
        }};
        ConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(connectURI, props);

        //
        // Now we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
        GenericObjectPool connectionPool =
                new GenericObjectPool();
        connectionPool.setTestOnBorrow(true);

        //
        // Next we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(connectionFactory, connectionPool, null, AppConfig.getProperty("db.connection.validateQuery"), false, true);

        connectionPool.setFactory(poolableConnectionFactory);

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //

        return new PoolingDataSource(connectionPool);
    }
}

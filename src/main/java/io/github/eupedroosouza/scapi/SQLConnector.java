package io.github.eupedroosouza.scapi;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.github.eupedroosouza.scapi.config.DefaultHikariConfig;
import io.github.eupedroosouza.scapi.type.DriverType;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Simple SQL connector.
 * @author eupedroosouza
 * @project sql-connector-api
 * @created 20/01/2023 - 13:06
 */
@Getter
public class SQLConnector implements HikariConfigMXBean {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 3306;

    private final HikariConfig config;
    private HikariDataSource dataSource;

    public SQLConnector(HikariConfig config){
        this.config = config;
    }

    public SQLConnector(Properties properties){
        this.config = new HikariConfig(properties);
    }

    public SQLConnector(File propertiesFile){
        try(InputStream inputStream = Files.newInputStream(propertiesFile.toPath())){
            Properties properties = new Properties();
            properties.load(inputStream);
            this.config = new HikariConfig(properties);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SQLConnector(String urlName, String driverClassName, String host, int port, String username, String password, String database){
        this.config = new DefaultHikariConfig();

        this.config.setDriverClassName(driverClassName);
        this.config.setJdbcUrl(getUrl(urlName, host, port, database));

        this.config.setUsername(username);
        this.config.setPassword(password);

    }

    public SQLConnector(String urlName, String driverClassName, String username, String password, String database){
        this(urlName, driverClassName, DEFAULT_HOST, DEFAULT_PORT, username, password, database);
    }

    public SQLConnector(String driverClassName, String username, String password, String database){
        this(DriverType.getFromDefaultDriverClassName(driverClassName).getUrlName(), driverClassName, DEFAULT_HOST,
                DEFAULT_PORT, username, password, database);
    }

    public SQLConnector(DriverType type, String username, String password, String database){
        this(type.getUrlName(), DriverType.getDriverClassNameOfDriverType(type), DEFAULT_HOST, DEFAULT_PORT, username, password, database);
    }

    public SQLConnector(String driverClassName, String host, int port, String username, String password, String database) {
        this(DriverType.getFromDefaultDriverClassName(driverClassName).getUrlName(), driverClassName, host, port, username, password, database);
    }

    public SQLConnector(DriverType type, String host, int port, String username, String password, String database){
        this(type.getUrlName(), DriverType.getDriverClassNameOfDriverType(type), host, port, username, password, database);
    }

    public SQLConnector(DriverType type, String host, int port, String database){
        this(type.getUrlName(), DriverType.getDriverClassNameOfDriverType(type), host, port, null, null, database);
    }

    public SQLConnector(String driverClassName, String host, int port, String database){
        this(DriverType.getFromDefaultDriverClassName(driverClassName).getUrlName(), driverClassName, host, port, null, null, database);
    }

    private String getUrl(String urlName, String host, int port, String database){
        return "jdbc:" + urlName + "://" + host + ":" + port + "/" + database;
    }

    public Properties getDataSourceProperties(){
        return this.config.getDataSourceProperties();
    }

    public void addDataSourceProperty(String propertyName, Object value){
        this.config.addDataSourceProperty(propertyName, value);
    }

    public void setDataSourceProperties(Properties dsProperties){
        this.config.setDataSourceProperties(dsProperties);
    }

    @Override
    public long getConnectionTimeout() {
        return this.config.getConnectionTimeout();
    }

    @Override
    public void setConnectionTimeout(long connectionTimeoutMs) {
        this.config.setConnectionTimeout(connectionTimeoutMs);
    }

    @Override
    public long getValidationTimeout() {
        return this.config.getValidationTimeout();
    }

    @Override
    public void setValidationTimeout(long validationTimeoutMs) {
        this.config.setValidationTimeout(validationTimeoutMs);
    }

    @Override
    public long getIdleTimeout() {
        return this.config.getIdleTimeout();
    }

    @Override
    public void setIdleTimeout(long idleTimeoutMs) {
        this.config.setIdleTimeout(idleTimeoutMs);
    }

    @Override
    public long getLeakDetectionThreshold() {
        return this.config.getLeakDetectionThreshold();
    }

    @Override
    public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
        this.config.setLeakDetectionThreshold(leakDetectionThresholdMs);
    }

    @Override
    public long getMaxLifetime() {
        return this.config.getMaxLifetime();
    }

    @Override
    public void setMaxLifetime(long maxLifetimeMs) {
        this.config.setMaxLifetime(maxLifetimeMs);
    }

    @Override
    public int getMinimumIdle() {
        return this.config.getMinimumIdle();
    }

    @Override
    public void setMinimumIdle(int minIdle) {
        this.config.setMinimumIdle(minIdle);
    }

    @Override
    public int getMaximumPoolSize() {
        return this.config.getMaximumPoolSize();
    }

    @Override
    public void setMaximumPoolSize(int maximumPoolSize){
        this.config.setMaximumPoolSize(maximumPoolSize);
    }

    @Override
    public void setPassword(String password) {
        this.config.setPassword(password);
    }

    @Override
    public void setUsername(String username) {
        this.config.setUsername(username);
    }

    @Override
    public String getPoolName() {
        return this.config.getPoolName();
    }

    @Override
    public String getCatalog() {
        return this.config.getCatalog();
    }

    @Override
    public void setCatalog(String catalog) {
        this.config.setCatalog(catalog);
    }

    /*
    public void useSSL(){
        this.config.addDataSourceProperty("useSSL", "true");
    }*/

    /**
     * Start the Hikari pool.
     */
    public void connect(){
        try {
            dataSource = new HikariDataSource(config);
        }catch(Exception e){
            throw new RuntimeException("Error when trying to open database connection: " + e.getMessage(), e);
        }
    }

    /**
     * Get the JDBC Connection (The pool must be started first with {@link #connect()})
     * @return JDBC Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


}

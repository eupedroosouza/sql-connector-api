package io.github.eupedroosouza.scapi.config;

import com.zaxxer.hikari.HikariConfig;

import java.util.Properties;

/**
 * @author Pedro
 * @project sql-connector-api
 * @created 21/01/2023 - 07:37
 */
public class DefaultHikariConfig extends HikariConfig {

    public DefaultHikariConfig(){
        this.addDataSourceProperty("autoReconnect", "true");
        this.addDataSourceProperty("charsetEncoding", "UTF-8");
        this.addDataSourceProperty("enconding", "UTF-8");
        this.addDataSourceProperty("useUnicode", "true");

        this.addDataSourceProperty("cachePrepStmts", "true");
        this.addDataSourceProperty("prepStmtCacheSize", "250");
        this.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }


}

package io.github.eupedroosouza.scapi.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pedro
 * @project sql-connector-api
 * @created 21/01/2023 - 16:35
 */
@Getter
@AllArgsConstructor
public enum DriverType {

    H2("h2", "org.h2.Driver"),
    MS_SQL_SERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    POSTGRESQL("postgresql", "org.postgresql.Driver"),
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    MARIADB("mariadb", "org.mariadb.jdbc.Driver"),
    SQLITE("sqlite", "org.sqlite.JDBC");


    private final String urlName;
    private final String defaultDriverClassName;

    public static DriverType getFromDefaultDriverClassName(String className){
        for(DriverType dt : values()){
            if(dt.getDefaultDriverClassName().equals(className))
                return dt;
        }
        throw new NullPointerException("DriverType not found for class " + className);
    }

    public static String getDriverClassNameOfDriverType(DriverType dt){
        try{
            Class<?> driverClass = Class.forName(dt.getDefaultDriverClassName());
            return driverClass.getName();
        }catch(ClassNotFoundException e){
            throw new RuntimeException("Driver class not found ", e);
        }
    }


}

package io.github.eupedroosouza.scapi.test;

import io.github.eupedroosouza.scapi.SQLConnector;
import io.github.eupedroosouza.scapi.type.DriverType;

import java.sql.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Pedro
 * @project sql-connector-api
 * @created 21/01/2023 - 17:16
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World - SQL-Connector-API");
        System.out.println("Executing test program...");

        /* with driverClassName
        SQLConnector sqlConnector = new SQLConnector("org.mariadb.jdbc.Driver", "root", "", "plugins");
        sqlConnector.connect();*/

        /* with driverType
        SQLConnector sqlConnector = new SQLConnector(DriverType.MARIADB, "root", "", "plugins");
        sqlConnector.connect();*/

        /* with driver type and driverClassName
        SQLConnector sqlConnector = new SQLConnector("mariadb", "org.mariadb.jdbc.Driver", "root", "", "plugins");
        sqlConnector.connect();*/

         /* with driver type, driverClassName, host and port */
        SQLConnector sqlConnector = new SQLConnector("mariadb", "org.mariadb.jdbc.Driver", "localhost", 3306,
                "root", "", "plugins");
        sqlConnector.connect();

        try(Connection connection = sqlConnector.getConnection();
                PreparedStatement table = connection.prepareStatement("create table if not exists `players`(`name` varchar(16) primary key, `level` int);")) {
            table.execute();

            int id = ThreadLocalRandom.current().nextInt(1000, 1999);

            try(PreparedStatement checkExists = connection.prepareStatement("select `name` from `players` where `name` = 'pedro-" + id + "';");
                    ResultSet result = checkExists.executeQuery()){
                if(!result.next()){
                    try(PreparedStatement insert = connection.prepareStatement("insert into `players`(`name`, `level`) values ('pedro-" + id + "', '1');")){
                        insert.executeUpdate();
                    }
                }
            }

            System.out.println("Listing all...");
            try(PreparedStatement listAll = connection.prepareStatement("select * from `players`;");
                    ResultSet result = listAll.executeQuery()) {
                while(result.next()){
                    String name = result.getString("name");
                    int level = result.getInt("level");
                    System.out.println("name: " + name + " - level: " + level);
                }
            }
        }

        sqlConnector.close();

        System.out.println("Finish, thank you!");
    }
}

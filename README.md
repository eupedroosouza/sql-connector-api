# SQL Connector Api
[![](https://jitpack.io/v/eupedroosouza/sql-connector-api.svg)](https://jitpack.io/#eupedroosouza/sql-connector-api)

Simple SQL Connector API using HikariCP.

Example using MariaDB:
```java
//Create SQLConnector instance and configure
SQLConnector sqlConnector = new SQLConnector("mariadb", "org.mariadb.jdbc.Driver", "localhost", 3306,
        "root", "", "plugins");
//Start Hikari pool
sqlConnector.connect();

//Queries
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

```
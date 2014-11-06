package com.kaltiz.cc.storage;

public enum DatabaseType
{
    H2("org.h2.Driver"),
    MYSQL("com.mysql.jdbc.Driver"),
    POSTGRE("org.postgresql.Driver");

    public final String driver;

    DatabaseType(String driver) {
        this.driver = driver;
    }

    public static DatabaseType match(String driver) {
        for (DatabaseType type : DatabaseType.values()) {
            if (type.name().equalsIgnoreCase(driver)) {
                return type;
            }
        }
        return null;
    }
}
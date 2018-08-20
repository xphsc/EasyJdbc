package com.xphsc.easyjdbc.config;


public class DatasourceProperties {
    private static  String url="jdbc:mysql://localhost:3306/easy_jdbc?useUnicode=true&useSSL=false&characterEncoding=UTF-8";
    private static String username="root";
    private static String password="root";
    private static String driverClass="com.mysql.jdbc.Driver";
    private static int     maxActive;
    private static int     minIdle;
    private static int     initialSize;
    private static boolean testOnBorrow;
    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        DatasourceProperties.url = url;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DatasourceProperties.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DatasourceProperties.password = password;
    }

    public static String getDriverClass() {
        return driverClass;
    }

    public static void setDriverClass(String driverClass) {
        DatasourceProperties.driverClass = driverClass;
    }

    public static int getMaxActive() {
        return maxActive;
    }

    public static void setMaxActive(int maxActive) {
        DatasourceProperties.maxActive = maxActive;
    }

    public static int getMinIdle() {
        return minIdle;
    }

    public static void setMinIdle(int minIdle) {
        DatasourceProperties.minIdle = minIdle;
    }

    public static int getInitialSize() {
        return initialSize;
    }

    public static void setInitialSize(int initialSize) {
        DatasourceProperties.initialSize = initialSize;
    }

    public static boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public static void setTestOnBorrow(boolean testOnBorrow) {
        DatasourceProperties.testOnBorrow = testOnBorrow;
    }
}

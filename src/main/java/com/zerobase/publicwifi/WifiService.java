package com.zerobase.publicwifi;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WifiService {
    private static final String AUTH_KEY = "7948437a6c74686438344872706241";
    private static final String BASE_URL = "http://openapi.seoul.go.kr:8088/";
    private static final String DB_URL = "jdbc:sqlite:wifiDB.db";
    public int loadWifi(){
        Connection dbConnection = null;
        Statement statement = null;
        PreparedStatement prepared = null;
        String sql;
        ResultSet rs = null;
        int totalCount = 0;
        Integer startIdx = 1;
        String code = "INFO-000";

        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(DB_URL);
            statement = dbConnection.createStatement();

            statement.executeUpdate(" drop table if exists wifi ");
            statement.executeUpdate(" CREATE TABLE WIFI ( " +
                    "    MGR_NO VARCHAR(9) PRIMARY KEY NOT NULL, DISTRICT VARCHAR(10), NAME VARCHAR(20), " +
                    " ROAD_ADDR VARCHAR(30), DETAIL_ADDR VARACHAR(50), FLOOR VARCHAR(5), " +
                    " INST_TYPE VARCHAR(20), INST_AGENCY VARCHAR(20), SERVICE VARCHAR(10), " +
                    " NETWORK VARCHAR(10), YEAR VARCHAR(4), INDOOR BOOLEAN, CONNECT_ENV VARCHAR(10), " +
                    " X_COORD REAL NOT NULL, Y_COORD REAL NOT NULL, WORK_AT DATE ) ");

            //statement.executeUpdate(" insert into WIFI (MGR_NO, X_COORD, Y_COORD) values ('TEST',12.34,123.412) " );

        } catch (SQLException e) {
            e.printStackTrace();
            return totalCount;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            URLConnection connection;
            String result;
            BufferedReader bf;
            JsonObject object;
            JsonArray rows;
            JsonObject row;
            sql = " insert into wifi ( mgr_no, district, name, road_addr, detail_addr, " +
                    " floor, inst_type, inst_agency, service, network, " +
                    " year, indoor, connect_env, x_coord, y_coord, work_at ) " +
                    " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";


            while ("INFO-000".equals(code)){

                connection = new URL(BASE_URL + AUTH_KEY + "/json/TbPublicWifiInfo/" + Integer.toString(startIdx) + "/" + Integer.toString(startIdx+999) + "/").openConnection();
                connection.setRequestProperty("Content-type", "application/xml");
                bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                result = bf.readLine();
                object = JsonParser.parseString(result).getAsJsonObject().getAsJsonObject("TbPublicWifiInfo");

                if(object == null) {
                    break;
                }

                code = object.getAsJsonObject("RESULT").get("CODE").getAsString();
                totalCount = object.get("list_total_count").getAsInt();
                rows = object.getAsJsonArray("row");
                prepared = dbConnection.prepareStatement(sql);

                for (int i = 0; i < rows.size(); i++) {
                    row = rows.get(i).getAsJsonObject();
                    prepared.setString(1,row.get("X_SWIFI_MGR_NO").getAsString());
                    prepared.setString(2,row.get("X_SWIFI_WRDOFC").getAsString());
                    prepared.setString(3,row.get("X_SWIFI_MAIN_NM").getAsString());
                    prepared.setString(4,row.get("X_SWIFI_ADRES1").getAsString());
                    prepared.setString(5,row.get("X_SWIFI_ADRES2").getAsString());
                    prepared.setString(6,row.get("X_SWIFI_INSTL_FLOOR").getAsString());
                    prepared.setString(7,row.get("X_SWIFI_INSTL_TY").getAsString());
                    prepared.setString(8,row.get("X_SWIFI_INSTL_MBY").getAsString());
                    prepared.setString(9,row.get("X_SWIFI_SVC_SE").getAsString());
                    prepared.setString(10,row.get("X_SWIFI_CMCWR").getAsString());
                    prepared.setString(11,row.get("X_SWIFI_CNSTC_YEAR").getAsString());
                    prepared.setBoolean(12,("실내".equals(row.get("X_SWIFI_INOUT_DOOR").getAsString()))?true:false); //1:0
                    prepared.setString(13,row.get("X_SWIFI_REMARS3").getAsString());
                    prepared.setFloat(14,row.get("LNT").getAsFloat()); //json에 따라 변경
                    prepared.setFloat(15,row.get("LAT").getAsFloat());
                    prepared.setString(16,row.get("WORK_DTTM").getAsString());
                    prepared.executeUpdate();
                    //System.out.println(prepared.executeUpdate());
                }

                startIdx += 1000;

            }

            /*
            rs = statement.executeQuery(" select * from wifi limit 10");

            while (rs.next()){
                System.out.println(rs.getString("MGR_NO"));
            }
             */


            return totalCount;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if(prepared!= null && !prepared.isClosed()){
                    prepared.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(statement!= null && !statement.isClosed()){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //커넥션 객체가 제대로 안닫힐 수도 있다, 안닫히면 서버가 죽기도 함
            try {
                if(dbConnection!= null && !dbConnection.isClosed()){
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return totalCount;
    }

    public List<Wifi> getCloseList(Float lat, Float lnt){
        List<Wifi> wifis = new ArrayList<>();
        Connection dbConnection = null;
        PreparedStatement prepared = null;
        String sql;
        ResultSet rs = null;
        Wifi wifi = null;

        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(DB_URL);
            sql =  " select mgr_no, district, name, road_addr, detail_addr, floor, " +
                    "       inst_type, inst_agency, service, network, year, indoor, " +
                    "       connect_env, x_coord, y_coord, work_at, " +
                    "       ( 6371 * acos( cos( radians(x_coord) ) * cos( radians( ? ) )* cos( radians( ? ) - radians(y_coord) )+ sin( radians(x_coord) ) * sin( radians( ? ) ) ) ) as distance " +
                    " from wifi " +
                    " order by distance limit 20 ";

            prepared = dbConnection.prepareStatement(sql);
            prepared.setFloat(1, lat);
            prepared.setFloat(2, lnt);
            prepared.setFloat(3, lat);

            rs = prepared.executeQuery();

            while (rs.next()){
                wifi = new Wifi();
                wifi.setDistance(rs.getFloat("distance"));
                wifi.setDistrict(rs.getString("district"));
                wifi.setManageId(rs.getString("mgr_no"));
                wifi.setName(rs.getString("name"));
                wifi.setRoadAddress(rs.getString("road_addr"));
                wifi.setDetailAddress(rs.getString("detail_addr"));
                wifi.setFloor(rs.getString("floor"));
                wifi.setInstallType(rs.getString("inst_type"));
                wifi.setInstallAgency(rs.getString("inst_agency"));
                wifi.setService(rs.getString("service"));
                wifi.setNetwork(rs.getString("network"));
                wifi.setYear(rs.getString("year"));
                wifi.setIndoor(rs.getBoolean("indoor"));
                wifi.setEnvironment(rs.getString("connect_env"));
                wifi.setxCoord(rs.getFloat("x_coord"));
                wifi.setyCoord(rs.getFloat("y_coord"));
                wifi.setWorkAt(rs.getDate("work_at"));

                wifis.add(wifi);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(prepared!= null && !prepared.isClosed()){
                    prepared.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(dbConnection!= null && !dbConnection.isClosed()){
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


        return wifis;
    }

    public void addHistory(Float lat, Float lnt){
        Connection dbConnection = null;
        PreparedStatement prepared = null;
        String sql;

        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(DB_URL);
            sql =  " insert into history ( x_coord, y_coord, created_at ) " +
                    "values (?,?,?) ";
            prepared = dbConnection.prepareStatement(sql);
            prepared.setFloat(1,lat);
            prepared.setFloat(2,lnt);
            prepared.setDate(3, Date.valueOf(LocalDateTime.now().toLocalDate()));
            prepared.executeUpdate();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {

            try {
                if(prepared!= null && !prepared.isClosed()){
                    prepared.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(dbConnection!= null && !dbConnection.isClosed()){
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public List<History> getHistory(Float lat, Float lnt){
        Connection dbConnection = null;
        PreparedStatement prepared = null;
        String sql;
        ResultSet rs = null;
        List<History> histories = new ArrayList<>();
        History history = null;

        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(DB_URL);
            sql =  " select * from history where x_coord = ? and y_coord = ? ";
            prepared = dbConnection.prepareStatement(sql);
            prepared.setFloat(1,lat);
            prepared.setFloat(2,lnt);
            rs = prepared.executeQuery();

            while (rs.next()){
                history = new History();
                history.setId(rs.getInt("hist_id"));
                history.setxCoord(rs.getFloat("x_coord"));
                history.setyCoord(rs.getFloat("y_coord"));
                history.setCreatedAt(rs.getDate("created_at"));
                histories.add(history);
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {

            try {
                if(prepared!= null && !prepared.isClosed()){
                    prepared.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(rs!= null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(dbConnection!= null && !dbConnection.isClosed()){
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return histories;

    }

    public void deleteHistory(int id){
        Connection dbConnection = null;
        PreparedStatement prepared = null;
        String sql;

        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection(DB_URL);
            sql =  " delete from history where hist_id = ? ";
            prepared = dbConnection.prepareStatement(sql);
            prepared.setInt(1,id);
            prepared.executeQuery();


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {

            try {
                if(prepared!= null && !prepared.isClosed()){
                    prepared.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                if(dbConnection!= null && !dbConnection.isClosed()){
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }


}

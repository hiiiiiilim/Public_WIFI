package db;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbTest {
	public String dbCount() {
		String db_url = "jdbc:mariadb://localhost:3306/public_wifi_db";
		String dbUserId="wifi";
		String dbPassword = "1234";
		String count = null;
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(db_url, dbUserId, dbPassword);
			
			String sql = "select count(*) as count from wifi";
			
            preparedStatement = connection.prepareStatement(sql);

    
            rs = preparedStatement.executeQuery();
         
            
            while (rs.next()){
                count = rs.getString("count");
                System.out.println(count);
            }
            
            
        	
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            //6. 객체 연결 해제(close)
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null&& !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
		return count;
	
	}
	
	public void dbInsert() throws IOException {
		for(int i = 1; i <= 24601; i += 1000) {	
    		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
    		urlBuilder.append("/").append(URLEncoder.encode("694b69416d6d616d3130334a6c706574", "UTF-8"));
    		urlBuilder.append("/").append(URLEncoder.encode("json", "UTF-8"));
    		urlBuilder.append("/").append(URLEncoder.encode("TbPublicWifiInfo", "UTF-8"));
    		urlBuilder.append("/" + i);
            urlBuilder.append("/" + (i + 999)); // 1000개씩 가져옴 
    		
    		URL url = new URL(urlBuilder.toString());
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestMethod("GET");
    		conn.setRequestProperty("Content-type", "application/json");
    		
    		BufferedReader rd;
    		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
    			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    		} else {
    			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    		}
    		
    		StringBuilder response = new StringBuilder();
    		String line;
    		while ((line = rd.readLine()) != null) {
    			response.append(line);
    		}
    		rd.close();
    		conn.disconnect();
    		
    		System.out.println(response);
    		
    		Gson gson = new Gson();
    		
    		Wifi wifi = gson.fromJson(response.toString(), Wifi.class);    
    		List<Wifi.TbPublicWifiInfo.Row> rows = wifi.getTbPublicWifiInfo().getRow();
    		    		
    		String db_url = "jdbc:mariadb://localhost:3306/public_wifi_db";
    		String dbUserId="wifi";
    		String dbPassword = "1234";
    		
    		Connection connection = null;
    		PreparedStatement preparedStatement = null;
    		Statement statement = null;
    		ResultSet rs = null;
    		
    		try {
    			Class.forName("org.mariadb.jdbc.Driver");
    		} catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		try {
    			connection = DriverManager.getConnection(db_url, dbUserId, dbPassword);
    			
    			String sql = "INSERT INTO wifi"
    					+ "(X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM) "
    					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    			
    			preparedStatement = connection.prepareStatement(sql);
    			for (Wifi.TbPublicWifiInfo.Row row : rows) {
					preparedStatement.setString(1, row.getX_SWIFI_MGR_NO());
					preparedStatement.setString(2, row.getX_SWIFI_WRDOFC());
					preparedStatement.setString(3, row.getX_SWIFI_MAIN_NM());				
					preparedStatement.setString(4, row.getX_SWIFI_ADRES1());				
					preparedStatement.setString(5, row.getX_SWIFI_ADRES2());				
					preparedStatement.setString(6, row.getX_SWIFI_INSTL_FLOOR());				
					preparedStatement.setString(7, row.getX_SWIFI_INSTL_TY());				
					preparedStatement.setString(8, row.getX_SWIFI_INSTL_MBY());				
					preparedStatement.setString(9, row.getX_SWIFI_SVC_SE());				
					preparedStatement.setString(10, row.getX_SWIFI_CMCWR());				
					preparedStatement.setString(11, row.getX_SWIFI_CNSTC_YEAR());				
					preparedStatement.setString(12, row.getX_SWIFI_INOUT_DOOR());				
					preparedStatement.setString(13, row.getX_SWIFI_REMARS3());				
					preparedStatement.setString(14, row.getLAT());				
					preparedStatement.setString(15, row.getLNT());				
					preparedStatement.setString(16, row.getWORK_DTTM());				
					int affected = preparedStatement.executeUpdate();
    			}
				
    			System.out.println("저장성공");
    
 
    			
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}finally {
                //6. 객체 연결 해제(close)
                try {
                    if(rs != null && !rs.isClosed()){
                        rs.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    if (preparedStatement != null && !preparedStatement.isClosed()){
                        preparedStatement.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    if (connection != null&& !connection.isClosed()){
                        connection.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
    		
    		
    		
     	}
	}
	
	public List<HistoryData> historySelect() {
		String db_url = "jdbc:mariadb://localhost:3306/public_wifi_db";
		String dbUserId="wifi";
		String dbPassword = "1234";
				
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;
		
		List<HistoryData> historyList = new ArrayList<>();
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(db_url, dbUserId, dbPassword);
			
			String sql = "select * from history order by history_id desc";
			
            preparedStatement = connection.prepareStatement(sql);

    
            rs = preparedStatement.executeQuery();
         
            
            while (rs.next()){
            	String history_id = rs.getString("history_id");
            	String x = rs.getString("x");
            	String y = rs.getString("y");
            	String search_DT = rs.getString("search_DT");
            	
            	HistoryData historyData = new HistoryData(history_id, x, y, search_DT);
                historyList.add(historyData);
            }
            
            
        	
    			
    		} catch (SQLException e) {
    			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            //6. 객체 연결 해제(close)
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null&& !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
		
		return historyList;
	}
	
	public void historyInsert(String lat, String lnt) {
		String db_url = "jdbc:mariadb://localhost:3306/public_wifi_db";
		String dbUserId="wifi";
		String dbPassword = "1234";
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(db_url, dbUserId, dbPassword);
			
			String sql = "insert into history (x,y, search_DT) values(?, ?, now())";
			System.out.println(lat);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, lat);
			preparedStatement.setString(2, lnt);	
			System.out.println("-"+lat);
			System.out.println(lnt);
	
			
			int affected = preparedStatement.executeUpdate();

            if (affected > 0){
                System.out.println("저장성공");
            }else {
                System.out.println("저장실패");
            }


			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            //6. 객체 연결 해제(close)
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null&& !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
	}
	
	}
	
	
	 public List<Wifi2> getWifiList(String lat, String lnt) {
	        String db_url = "jdbc:mariadb://localhost:3306/public_wifi_db";
	        String dbUserId = "wifi";
	        String dbPassword = "1234";
	        
	        double latV = Double.parseDouble(lat);
	        double lntV = Double.parseDouble(lnt);

	        List<Wifi> wifiList = new ArrayList<>();
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet rs = null;
	        
	        List<Wifi2> wifi2List = new ArrayList<>();

	        try {
	            Class.forName("org.mariadb.jdbc.Driver");
	            connection = DriverManager.getConnection(db_url, dbUserId, dbPassword);
	            String sql = "SELECT *, round(6371*acos(cos(radians(?))*cos(radians(LAT))*cos(radians(LNT)-radians(?))+sin(radians(?))*sin(radians(LAT))), 4) as distance FROM wifi order by distance";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setDouble(1, latV);
	            preparedStatement.setDouble(2, lntV);
	            preparedStatement.setDouble(3, latV);
	            rs = preparedStatement.executeQuery();
	            

	            while (rs.next()) {
	            	double dis = rs.getDouble("distance");
	            	String X_SWIFI_MGR_NO = rs.getString("X_SWIFI_MGR_NO");
	            	String X_SWIFI_WRDOFC = rs.getString("X_SWIFI_WRDOFC");
	            	String X_SWIFI_MAIN_NM = rs.getString("X_SWIFI_MAIN_NM");
	            	String X_SWIFI_ADRES1 = rs.getString("X_SWIFI_ADRES1");
	            	String X_SWIFI_ADRES2 = rs.getString("X_SWIFI_ADRES2");
	            	String X_SWIFI_INSTL_FLOOR = rs.getString("X_SWIFI_INSTL_FLOOR");
	            	String X_SWIFI_INSTL_TY = rs.getString("X_SWIFI_INSTL_TY");
	            	String X_SWIFI_INSTL_MBY = rs.getString("X_SWIFI_INSTL_MBY");
	            	String X_SWIFI_SVC_SE = rs.getString("X_SWIFI_SVC_SE");
	            	String X_SWIFI_CMCWR = rs.getString("X_SWIFI_CMCWR");
	            	String X_SWIFI_CNSTC_YEAR = rs.getString("X_SWIFI_CNSTC_YEAR");
	            	String X_SWIFI_INOUT_DOOR = rs.getString("X_SWIFI_INOUT_DOOR");
	            	String X_SWIFI_REMARS3 = rs.getString("X_SWIFI_REMARS3");
	            	String LAT = rs.getString("LAT");
	            	String LNT = rs.getString("LNT");
	            	String WORK_DTTM = rs.getString("WORK_DTTM");
	                
	                Wifi2 wifi2 = new Wifi2();
	                wifi2.setDis(dis);
	                wifi2.setX_SWIFI_MGR_NO(X_SWIFI_MGR_NO);
	                wifi2.setX_SWIFI_WRDOFC(X_SWIFI_WRDOFC);
	                wifi2.setX_SWIFI_MAIN_NM(X_SWIFI_MAIN_NM);
	                wifi2.setX_SWIFI_ADRES1(X_SWIFI_ADRES1);
	                wifi2.setX_SWIFI_ADRES2(X_SWIFI_ADRES2);
	                wifi2.setX_SWIFI_INSTL_FLOOR(X_SWIFI_INSTL_FLOOR);
	                wifi2.setX_SWIFI_INSTL_TY(X_SWIFI_INSTL_TY);
	                wifi2.setX_SWIFI_INSTL_MBY(X_SWIFI_INSTL_MBY);
	                wifi2.setX_SWIFI_SVC_SE(X_SWIFI_SVC_SE);
	                wifi2.setX_SWIFI_CMCWR(X_SWIFI_CMCWR);
	                wifi2.setX_SWIFI_CNSTC_YEAR(X_SWIFI_CNSTC_YEAR);
	                wifi2.setX_SWIFI_REMARS3(X_SWIFI_REMARS3);
	                wifi2.setLAT(LAT);
	                wifi2.setLNT(LNT);
	                wifi2.setWORK_DTTM(WORK_DTTM);
	                
	                wifi2List.add(wifi2);

	            }
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                if (rs != null && !rs.isClosed()) {
	                    rs.close();
	                }
	                if (preparedStatement != null && !preparedStatement.isClosed()) {
	                    preparedStatement.close();
	                }
	                if (connection != null && !connection.isClosed()) {
	                    connection.close();
	                }
	            } catch (SQLException e) {
	                throw new RuntimeException(e);
	            }
	        }

	        return wifi2List;
	    }
	}


	



package ch07_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/*
 *  Web에서 DB 액세스하는 방법 : DBCP(Database Connection Pool)
 *  	1. webapp/WEB-INF/lib 에 database library (.jar) 추가
 *  	2. Tomcat server의 context.xml 수정
 */
public class CityDao {

	public Connection getConnection() {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/" + "jdbc/world");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public City getCity(int id) {
		Connection conn = getConnection();
		String sql = "select * from kcity where id=?";
		City city = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				city = new City(rs.getInt(1), rs.getString(2), 
						rs.getString(3), rs.getString(4), rs.getInt(5));		
			}
			rs.close(); pstmt.close(); conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return city;
	}
	
	public List<City> getCityList(String district, int num, int offset) {
		Connection conn = getConnection();
		String sql = "select * from kcity where district=? limit ? offset ?";
		List<City> list = new ArrayList<City>();	
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, district);
			pstmt.setInt(2, num);
			pstmt.setInt(3, offset);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				City city = new City(rs.getInt(1), rs.getString(2), rs.getString(3), 
						rs.getString(4), rs.getInt(5));
				list.add(city);
			}
			rs.close(); pstmt.close(); conn.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}

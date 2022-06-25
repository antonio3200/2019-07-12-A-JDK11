package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getFoodByPorzioni(int porzioni,Map<Integer,Food> idMap){
		String sql="SELECT f.food_code AS codice,f.display_name AS descrizione "
				+ "FROM food f,`portion`p "
				+ "WHERE f.food_code=p.food_code "
				+ "GROUP BY codice,descrizione "
				+ "HAVING COUNT(DISTINCT p.portion_id)<=?";
		List<Food> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1, porzioni);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int codice= rs.getInt("codice");
				String descrizione = rs.getString("descrizione");
				Food f = new Food(codice,descrizione);
				result.add(f);
				idMap.put(codice, f);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
	
	public List<Arco> getArco(Map<Integer,Food> idMap){
		String sql="SELECT f1.food_code AS c1,f2.food_code AS c2, AVG(DISTINCT c.condiment_calories) AS cal "
				+ "FROM food_condiment f1, food_condiment f2, condiment c "
				+ "WHERE f1.food_code<f2.food_code "
				+ "AND f1.condiment_code=f2.condiment_code "
				+ "AND f1.condiment_code=c.condiment_code "
				+ "GROUP BY c1,c2";
		List<Arco> result= new ArrayList<>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				int c1= rs.getInt("c1");
				int c2= rs.getInt("c2");
				if(idMap.containsKey(c1) && idMap.containsKey(c2)) {
				Food f1= idMap.get(c1);
				Food f2= idMap.get(c2);
				double peso=rs.getDouble("cal");
				Arco a = new Arco(f1,f2,peso);
				result.add(a);
		}
	}
			conn.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
	}
}

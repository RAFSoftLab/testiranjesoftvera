package trgovina.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProizvodiTableManager {
	
	private Connection conn;
	
	public ProizvodiTableManager(Connection conn) {
		this.conn = conn;		
	}	
	
	
	public void createProizvodiTable() {		
		String sql = "CREATE TABLE proizvodi (naziv varchar(255), kolicina int, cena float);";		
		executeStatement(sql);	
		
	}
	
	public void dropTable() {
		String sql = "DROP TABLE IF EXISTS proizvodi;";
		executeStatement(sql);
	}
	
	private void executeStatement(String sql) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			ps.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	

}

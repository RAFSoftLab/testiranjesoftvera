package trgovina.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import trgovina.repositories.ProizvodRepository;

public class ProizvodiJDBCRepository implements ProizvodRepository {	
	
	private Connection conn;
	
	public ProizvodiJDBCRepository (Connection conn) {
		this.conn = conn;
	}
	

	@Override
	public int vratiBrojRazlicitihProizvoda() {
		String sql = "SELECT COUNT (DISTINCT naziv) FROM proizvodi";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt(1);			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}		
		
	}

	@Override
	public List<String> vratiSveRazliciteProizvode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int vratiStanjeZaProizvod(String proizvod) {
		
		return 0;
	}


	@Override
	public boolean sacuvajProizvod(String naziv, int kolicina, float cena) {
		
		// todo ako vec postoji azurirati kolicinu
		
		String sql = "INSERT INTO proizvodi (naziv,kolicina,cena) VALUES (?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, naziv);
			pstmt.setInt(2, kolicina);
			pstmt.setFloat(3, cena);	
			int num = pstmt.executeUpdate();			
			return num == 1;					
		}catch(SQLException e) {
			e.printStackTrace();
		}	
		return false;
		
	}

}

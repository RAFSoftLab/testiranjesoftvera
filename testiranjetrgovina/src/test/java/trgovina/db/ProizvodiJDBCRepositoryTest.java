package trgovina.db;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import trgovina.db.connection.JDBCConnectionManager;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProizvodiJDBCRepositoryTest {
	
	private static ProizvodiTableManager tableManager;
	private static Connection conn;
	private static ProizvodiJDBCRepository repo;
	
	@BeforeAll
	public static void prepareDatabase() {
		conn = JDBCConnectionManager.openConnection();
		tableManager = new ProizvodiTableManager(conn);
		repo = new ProizvodiJDBCRepository(conn);
		tableManager.createProizvodiTable();	
		populateDatabase();
	}	
	
	// testovi treba da budu nezavisni
	
	@Test
	void testBrojRazlicitih() {
		int brojRazlicitih = repo.vratiBrojRazlicitihProizvoda();		
		assertEquals(4,brojRazlicitih);	
	}
	
	@Test
	@Disabled
	void testSacuvajProizvod() {
		boolean ok = repo.sacuvajProizvod("sampon", 30, (float)100.0);		
		assertTrue(ok);	
	}
	
	@AfterAll
	public static void deleteTables() {
		tableManager.dropTable();
	}
	
	private static void populateDatabase() {
		repo.sacuvajProizvod("sampon", 30, (float)100.0);	
		repo.sacuvajProizvod("sapun", 20, (float)50.0);	
		repo.sacuvajProizvod("pasta za zube", 40, (float)40.0);	
		repo.sacuvajProizvod("deterdzent", 30, (float)70.0);	
	}
	

}

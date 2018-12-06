import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.app.Application;
import com.cognizant.config.HibernateConfiguration;
import com.cognizant.datasource.IDataSource;
import com.cognizant.datasource.JDBCDataSource;
import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;
import com.cognizant.service.BookService;

/**
 * 
 */

/**
 * @author gaura
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JDBCDataSourcdDAOTest {

	@TestConfiguration
	static class JDBCDataSourceTestContextConfiguration {

		@Autowired
		HibernateConfiguration hibernateConfiguration = null;

		@Bean
		public JDBCDataSource jdbcDataSource() throws IOException {
			return new JDBCDataSource(hibernateConfiguration);
		}
	}

	@Autowired
	private JDBCDataSource jdbcDataSource;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void addBookTest() {
		try {
			Book b = new Book();
			b.setPrice(12.3);
			b.setPublishDate(new Date(2012, 12, 12));
			b.setTitle("Hello World");
			b.setVolume(100);

			this.jdbcDataSource.writeBook(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}
	
	@Test
	public void serchBookTest() {
		try {
			Book b = new Book();
			b.setPrice(12.3);
			b.setPublishDate(new Date(2012, 12, 12));
			b.setTitle("Hello World");
			b.setVolume(100);
			
			Map<Long, Book> bookMap = this.jdbcDataSource.searchBook(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}
	
	
	@Test
	public void removeBookTest() {
		try {
			Book b = new Book();
			b.setPrice(12.3);
			b.setPublishDate(new Date(2012, 12, 12));
			b.setTitle("Hello World");
			b.setVolume(100);
			
			this.jdbcDataSource.removeBook(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}

	@Test
	public void addSubjectTest() {
		try {
			Subject b = new Subject();
			b.setDurationInHours(10);
			b.setSubTitle("Hello");
			b.setReference(new HashSet<Book>());
			this.jdbcDataSource.writeSubject(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}
	
	@Test
	public void serchSubjectTest() {
		try {
			Subject b = new Subject();
			b.setDurationInHours(10);
			b.setSubTitle("Hello");
			b.setReference(new HashSet<Book>());
			Map<Long, Subject> bookMap = this.jdbcDataSource.searchSubject(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}
	
	
	@Test
	public void removeSubjectTest() {
		try {
			Subject b = new Subject();
			b.setDurationInHours(10);
			b.setSubTitle("Hello");
			this.jdbcDataSource.removeSubject(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}



}

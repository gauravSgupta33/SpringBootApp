import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
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
import com.cognizant.datasource.IDataSource;
import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;
import com.cognizant.service.BookService;
import com.cognizant.service.SubjectService;

/**
 * 
 */

/**
 * @author gaura
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SubjectServiceTest {

	@TestConfiguration
	static class SubjectServiceImplTestContextConfiguration {

		@MockBean
		private IDataSource dataSource;

		@Bean
		public SubjectService SubjectService() throws IOException {
			return new SubjectService(dataSource);
		}
		

		@Bean
		public BookService bookService() throws IOException {
			return new BookService(dataSource);
		}
	}

	@Autowired
	private SubjectService subjectService;
	@Autowired
	private BookService bookService;

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
	public void addSubjectTest() {
		try {
			Subject b = new Subject();
			b.setDurationInHours(10);
			b.setSubTitle("Hello");
			b.setReference(new HashSet<Book>());
			this.subjectService.addSubject(b);
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
			Map<Long, Subject> bookMap = this.subjectService.searchSubjects(b);
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
			this.subjectService.removeSubject(b);
			assertTrue(true);
		} catch (Exception e) {
			fail("Faied to add book");
		}
	}


}

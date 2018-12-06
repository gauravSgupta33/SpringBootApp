package com.cognizant.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.config.HibernateConfiguration;
import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;

@Service("DataSource")
public class JDBCDataSource implements IDataSource {
	private Connection jdbcConnection = null;
	private List<Long> ids = new ArrayList<>();
	
	@Autowired
	HibernateConfiguration hibernateConfiguration = null;
	

	@Autowired
	public JDBCDataSource(HibernateConfiguration hibernateConfiguration) {
		this.hibernateConfiguration = hibernateConfiguration;
		try {
		jdbcConnection = this.hibernateConfiguration.dataSource().getConnection();
		initializeDb();
		} catch(Exception e) {
			
		}
	}

	private void initializeDb() throws IOException {
		List<Book> bookList = new ArrayList<Book>();
		for (int i = 0; i < 10; i++) {
			Book book = new Book();
			book.setTitle("BookTitle-" + i);
			book.setVolume(new Random().nextInt(300));
			java.util.Date date = new java.util.Date(1980 + i, (new Random().nextInt(11) + 1),
					(new Random().nextInt(20) + 1));
			date.setYear(1980 + i);
			book.setPublishDate(date);
			book.setPrice(new Random().nextDouble() * 100);
			writeBook(book);
			bookList.add(book);
		}

		for (int i = 0; i < 20; i++) {
			Subject subject = new Subject();
			subject.setSubTitle("Subject-" + i);
			subject.setDurationInHours((new Random().nextInt(30) + 1));
			int randValue = new Random().nextInt(5);
			Set<Book> ref = new LinkedHashSet<Book>();
			for (int j = 0; j <= randValue; j++) {
				ref.add(bookList.get(j));
			}
			subject.setReference(ref);
			writeSubject(subject);
		}

	}

	private boolean isBookExists(Book book) {
		try {
			PreparedStatement prepStmt = jdbcConnection
					.prepareStatement("select count(*), bookId  from Book where LOWER(title) like LOWER(?)");
			prepStmt.setString(1, book.getTitle().trim());
			ResultSet resultSet = prepStmt.executeQuery();
			int count = 0;
			while (resultSet.next()) {
				count = resultSet.getInt(1);
				if (count > 0) {
					book.setBookId(resultSet.getInt(2));
				}
			}
			resultSet.close();
			prepStmt.close();
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	private boolean isSubjectExists(Subject subject, int reference) {
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(
					"select count(*) from Subject where LOWER(subTitle) like LOWER(?) and reference = ?");
			prepStmt.setString(1, subject.getSubTitle().trim());
			prepStmt.setInt(2, reference);
			ResultSet resultSet = prepStmt.executeQuery();
			int count = 0;
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			resultSet.close();
			prepStmt.close();
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	private int getMaxSubjectID() {
		int count = 0;
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement("select max(subjectId)  from Subject");
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			resultSet.close();
			prepStmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return count;
	}

	@Override
	public void writeBook(Book book) throws IOException {
		String insertBookRecord = "INSERT INTO Book (`title`, `price`, `volume`, `localDate`) VALUES (?,?,?,?)";

		String updateBookRecord = "UPDATE Book SET `price` = ? , `volume` = ? , `localDate` = ? where LOWER(title) like LOWER(?)";
		try {
			PreparedStatement prepStmt = null;

			if (isBookExists(book)) {
				prepStmt = jdbcConnection.prepareStatement(updateBookRecord);
				prepStmt.setDouble(1, book.getPrice());
				prepStmt.setInt(2, book.getVolume());
				java.sql.Date sqlDate = new java.sql.Date(book.getPublishDate().getTime());
				prepStmt.setDate(3, sqlDate);
				prepStmt.setString(4, book.getTitle().trim());
				prepStmt.executeUpdate();
				// System.out.println("Book Updated Successfully");

			} else {
				prepStmt = jdbcConnection.prepareStatement(insertBookRecord);
				prepStmt.setString(1, book.getTitle());
				prepStmt.setDouble(2, book.getPrice());
				prepStmt.setInt(3, book.getVolume());
				java.sql.Date sqlDate = new java.sql.Date(book.getPublishDate().getTime());
				prepStmt.setDate(4, sqlDate);

				prepStmt.executeUpdate();
				// prepStatement.close();
				String getLastValue = "SELECT @last := LAST_INSERT_ID()";
				Statement stmt = jdbcConnection.createStatement();
				ResultSet resultSet = stmt.executeQuery(getLastValue);
				long lastId = 0;
				while (resultSet.next()) {
					lastId = resultSet.getInt(1);
				}
				book.setBookId(lastId);
				ids.add(lastId);
				resultSet.close();
				// System.out.println("Book Added Successfully");
			}
			prepStmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<Long, Book> readBook() throws IOException {
		Map<Long, Book> bookMap = new LinkedHashMap<Long, Book>();
		try {
			
			PreparedStatement prepStmt = jdbcConnection
					.prepareStatement("select bookId , title, price, volume, localDate  from Book");
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Book book = new Book();
				book.setBookId(resultSet.getInt(1));
				book.setTitle(resultSet.getString(2));
				book.setPrice(resultSet.getDouble(3));
				book.setVolume(resultSet.getInt(4));

				Date date = resultSet.getDate(5);
				if (date != null) {
					book.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookMap.put(book.getBookId(), book);

				// System.out.println(book.toString());
			}
			resultSet.close();
			prepStmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return bookMap;
	}

	@Override
	public void writeSubject(Subject subject) throws IOException {
		String insertsubjectRecord = "INSERT INTO Subject (`subjectId`, `subTitle`, `durationInHours`, `reference`) VALUES (?,?,?,?)";

		String updatesubjectRecord = "UPDATE Subject SET `durationInHours` = ? , `reference` = ?  where LOWER(subTitle) like LOWER(?)";
		try {
			PreparedStatement prepStmt = null;
			int index = getMaxSubjectID();
			for (Book book : subject.getReference()) {
				if (isSubjectExists(subject, Integer.valueOf(book.getBookId() + ""))) {
					prepStmt = jdbcConnection.prepareStatement(updatesubjectRecord);
					prepStmt.setInt(1, subject.getDurationInHours());
					prepStmt.setInt(2, Integer.valueOf(book.getBookId() + ""));
					prepStmt.setString(3, subject.getSubTitle().trim());
					prepStmt.executeUpdate();
					System.out.println("Subject Updated Successfully");

				} else {
					prepStmt = jdbcConnection.prepareStatement(insertsubjectRecord);
					prepStmt.setInt(1, ++index);
					prepStmt.setString(2, subject.getSubTitle());
					prepStmt.setInt(3, subject.getDurationInHours());
					prepStmt.setInt(4, Integer.valueOf(book.getBookId() + ""));

					prepStmt.executeUpdate();
					// System.out.println("subject Added Successfully");
				}
				prepStmt.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<Long, Subject> readSubject() throws IOException {
		String searchQuery = "SELECT s.subjectId, s.subTitle, s.durationInHours, b.bookId, b.title, b.price, b.volume, "
				+ "b.localDate FROM Book b INNER JOIN Subject s  ON s.reference = b.bookId";
		Map<Long, Subject> subjectMap = new LinkedHashMap<Long, Subject>();
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(searchQuery);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Subject tempSubject = null;
				Set<Book> bookSet = null;
				long subjectId = resultSet.getInt(1);
				if (subjectMap.containsKey(subjectId)) {
					tempSubject = subjectMap.get(subjectId);
					bookSet = subjectMap.get(subjectId).getReference();
				} else {
					tempSubject = new Subject();
					bookSet = new LinkedHashSet<Book>();
				}
				tempSubject.setSubjectId(subjectId);
				tempSubject.setSubTitle(resultSet.getString(2));
				tempSubject.setDurationInHours(resultSet.getInt(3));

				Book tempBook = new Book();

				tempBook.setBookId(resultSet.getInt(4));
				tempBook.setTitle(resultSet.getString(5));
				tempBook.setPrice(resultSet.getDouble(6));
				tempBook.setVolume(resultSet.getInt(7));

				Date date = resultSet.getDate(8);
				if (date != null) {
					tempBook.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookSet.add(tempBook);
				tempSubject.setReference(bookSet);
				subjectMap.put(subjectId, tempSubject);
			}
			resultSet.close();
			prepStmt.close();

		} catch (Exception e) {

		}
		return subjectMap;

	}

	@Override
	public void removeSubject(Subject subject) throws IOException {
		if (subject.getSubjectId() > 0) {
			String deleteQuery = "delete from Subject where subjectId = ?";

			try {
				PreparedStatement prepStmt = jdbcConnection.prepareStatement(deleteQuery);
				prepStmt.setLong(1, subject.getSubjectId());
				prepStmt.executeUpdate();
				prepStmt.close();
				System.out.println("Subject Deleted Successfully");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			String deleteQuery = "delete from Subject where subTitle like ?";

			try {
				PreparedStatement prepStmt = jdbcConnection.prepareStatement(deleteQuery);
				prepStmt.setString(1, subject.getSubTitle());
				prepStmt.executeUpdate();
				prepStmt.close();
				System.out.println("Subject Deleted Successfully");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Map<Long, Subject> searchSubject(Subject subject) throws IOException {
		String searchQuery = "SELECT s.subjectId, s.subTitle, s.durationInHours, b.bookId, b.title, b.price, b.volume, "
				+ "b.localDate FROM Book b INNER JOIN Subject s  ON s.reference = b.bookId where s.subTitle like ?";
		Map<Long, Subject> subjectMap = new LinkedHashMap<Long, Subject>();
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(searchQuery);
			prepStmt.setString(1, "%" + subject.getSubTitle() + "%");
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Subject tempSubject = null;
				Set<Book> bookSet = null;
				long subjectId = resultSet.getInt(1);
				if (subjectMap.containsKey(subjectId)) {
					tempSubject = subjectMap.get(subjectId);
					bookSet = subjectMap.get(subjectId).getReference();
				} else {
					tempSubject = new Subject();
					bookSet = new LinkedHashSet<Book>();
				}
				tempSubject.setSubjectId(subjectId);
				tempSubject.setSubTitle(resultSet.getString(2));
				tempSubject.setDurationInHours(resultSet.getInt(3));

				Book tempBook = new Book();

				tempBook.setBookId(resultSet.getInt(4));
				tempBook.setTitle(resultSet.getString(5));
				tempBook.setPrice(resultSet.getDouble(6));
				tempBook.setVolume(resultSet.getInt(7));

				Date date = resultSet.getDate(8);
				if (date != null) {
					tempBook.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookSet.add(tempBook);
				tempSubject.setReference(bookSet);
				subjectMap.put(subjectId, tempSubject);
			}
			resultSet.close();
			prepStmt.close();

		} catch (Exception e) {

		}
		return subjectMap;
	}

	@Override
	public Map<Long, Book> searchBook(Book book) throws IOException {
		Map<Long, Book> bookMap = new LinkedHashMap<Long, Book>();
		try {
			PreparedStatement prepStmt = jdbcConnection
					.prepareStatement("select bookId , title, price, volume, localDate  from Book where title like ?");
			prepStmt.setString(1, "%" + book.getTitle() + "%");
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Book tempBook = new Book();
				tempBook.setBookId(resultSet.getInt(1));
				tempBook.setTitle(resultSet.getString(2));
				tempBook.setPrice(resultSet.getDouble(3));
				tempBook.setVolume(resultSet.getInt(4));

				Date date = resultSet.getDate(5);
				if (date != null) {
					tempBook.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookMap.put(tempBook.getBookId(), tempBook);
			}
			resultSet.close();
			prepStmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return bookMap;
	}

	@Override
	public void removeBook(Book book) throws IOException {
		String deleteQuery = "delete from Book where title like ?";
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(deleteQuery);
			prepStmt.setString(1, book.getTitle());
			prepStmt.executeUpdate();
			prepStmt.close();
			System.out.println("Book Deleted Successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<Long, Book> sortBook(String fieldName) throws IOException {
		Map<Long, Book> bookMap = new LinkedHashMap<Long, Book>();
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(
					"select bookId , title, price, volume, localDate  from Book order by " + fieldName);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Book tempBook = new Book();
				tempBook.setBookId(resultSet.getInt(1));
				tempBook.setTitle(resultSet.getString(2));
				tempBook.setPrice(resultSet.getDouble(3));
				tempBook.setVolume(resultSet.getInt(4));

				Date date = resultSet.getDate(5);
				if (date != null) {
					tempBook.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookMap.put(tempBook.getBookId(), tempBook);
			}
			resultSet.close();
			prepStmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return bookMap;
	}

	@Override
	public Map<Long, Subject> sortSubject(String fieldName) throws IOException {
		String searchQuery = "SELECT s.subjectId, s.subTitle, s.durationInHours, b.bookId, b.title, b.price, b.volume, "
				+ "b.localDate FROM Book b INNER JOIN Subject s  ON s.reference = b.bookId order by " + fieldName;
		Map<Long, Subject> subjectMap = new LinkedHashMap<Long, Subject>();
		try {
			PreparedStatement prepStmt = jdbcConnection.prepareStatement(searchQuery);
			ResultSet resultSet = prepStmt.executeQuery();
			while (resultSet.next()) {
				Subject tempSubject = null;
				Set<Book> bookSet = null;
				long subjectId = resultSet.getInt(1);
				if (subjectMap.containsKey(subjectId)) {
					tempSubject = subjectMap.get(subjectId);
					bookSet = subjectMap.get(subjectId).getReference();
				} else {
					tempSubject = new Subject();
					bookSet = new LinkedHashSet<Book>();
				}
				tempSubject.setSubjectId(subjectId);
				tempSubject.setSubTitle(resultSet.getString(2));
				tempSubject.setDurationInHours(resultSet.getInt(3));

				Book tempBook = new Book();

				tempBook.setBookId(resultSet.getInt(4));
				tempBook.setTitle(resultSet.getString(5));
				tempBook.setPrice(resultSet.getDouble(6));
				tempBook.setVolume(resultSet.getInt(7));

				Date date = resultSet.getDate(8);
				if (date != null) {
					tempBook.setPublishDate(new java.util.Date(date.getTime()));
				}
				bookSet.add(tempBook);
				tempSubject.setReference(bookSet);
				subjectMap.put(subjectId, tempSubject);
			}
			resultSet.close();
			prepStmt.close();

		} catch (Exception e) {

		}
		return subjectMap;
	}

}

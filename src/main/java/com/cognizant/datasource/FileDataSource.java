package com.cognizant.datasource;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;

//@Service("DataSource")
public class FileDataSource implements IDataSource {
	private String bookFileName = "Book.ser";
	Map<Long, Book> bookMap = new ConcurrentHashMap<Long, Book>();
	private OutputStream outputStream = null;
	private ObjectOutputStream objectOutputStream = null;
	private InputStream inputStream = null;
	private ObjectInputStream objectInputStream = null;
	private static long maxBookId = 0;
	
	private String subjectFileName = "Subject.ser";
	Map<Long, Subject> subjectMap = new ConcurrentHashMap<Long, Subject>();
	private OutputStream subjectOutputStream = null;
	private ObjectOutputStream subjectObjectOutputStream = null;
	private InputStream subjectInputStream = null;
	private ObjectInputStream subjectObjectInputStream = null;
	private static long maxSubjectId = 0;


	
	public void writeBook(Book book) throws IOException {
		try {
			if (outputStream == null) {
				outputStream = new FileOutputStream(bookFileName);
				objectOutputStream = new ObjectOutputStream(outputStream);
			}
			book.setBookId(++maxBookId);
			bookMap.put(book.getBookId(), book);
			for(Entry<Long, Book> entry : bookMap.entrySet()) {
				objectOutputStream.writeObject(entry.getValue());	
			}
			objectOutputStream.flush();
		} finally {

		}
	}

	public Map<Long, Book> readBook() {
		try {
			if (inputStream == null) {
				inputStream = new FileInputStream(bookFileName);
				objectInputStream = new ObjectInputStream(inputStream);
			}
			Book book = null;

			while (true) {
				try {
					book = (Book) objectInputStream.readObject();
					bookMap.put(book.getBookId(), book);
					if(book.getBookId() > maxBookId) {
						maxBookId = book.getBookId();
					}
					maxBookId = book.getBookId();
					//System.out.println(book.toString());
				} catch (EOFException ex) {
					break;
				}
			}
		}

		catch (IOException ex) {
			System.out.println("Read initial book records");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
		return bookMap;
	}

	
	public void writeSubject(Subject subject) throws IOException {
		try {
			if (subjectOutputStream == null) {
				subjectOutputStream = new FileOutputStream(subjectFileName);
				subjectObjectOutputStream = new ObjectOutputStream(subjectOutputStream);
			}
			subject.setSubjectId(++maxSubjectId);
			subjectMap.put(subject.getSubjectId(), subject);
			for(Entry<Long, Subject> entry : subjectMap.entrySet()) {
				subjectObjectOutputStream.writeObject(entry.getValue());
			}
			subjectObjectOutputStream.flush();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {

		} 
	}

	public Map<Long, Subject> readSubject() {
		// Deserialization
		if(subjectMap.size() > 0) {
			return subjectMap;
		}
		try {
			// Reading the object from a file
			if (subjectInputStream == null) {
				subjectInputStream = new FileInputStream(subjectFileName);
				subjectObjectInputStream = new ObjectInputStream(subjectInputStream);
			}

			// Method for deserialization of object
			Subject subject = null;

			while (true) {
				try {
					subject = (Subject) subjectObjectInputStream.readObject();
					subjectMap.put(subject.getSubjectId(), subject);
					if (subject.getSubjectId() > maxSubjectId) {
						maxSubjectId = subject.getSubjectId();
					}
					//System.out.println(subject.toString());
				} catch (EOFException ex) {
					break;
				}
			}
		}

		catch (IOException ex) {
			System.out.println("Read initial subject records");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
		return subjectMap;
	}
	
	public Map<Long, Book> searchBook(Book book) throws IOException {
		Map<Long, Book> resultMap = new LinkedHashMap<Long, Book>();
		for (Entry<Long, Book> entry : bookMap.entrySet()) {
			if (entry.getValue().getTitle().contains(book.getTitle())) {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		return resultMap;
	}


	public void removeSubject(Subject subject) throws IOException {
		long id = 0;
		for (Entry<Long, Subject> entry : subjectMap.entrySet()) {
			if (entry.getValue().getSubTitle().equals(subject.getSubTitle())) {
				id = entry.getValue().getSubjectId();
				break;
			}
		}
		
		if(id != 0) {
			subjectMap.remove(id);
		}
		if(subjectMap.isEmpty()) {
			emptyFile(subjectFileName);
		}

		for (Entry<Long, Subject> entry : subjectMap.entrySet()) {
			writeSubject(entry.getValue());
		}
	}
	
	public Map<Long, Subject> searchSubject(Subject subject) throws IOException {
		Map<Long, Subject> resultMap = new LinkedHashMap<Long, Subject>();
		for (Entry<Long, Subject> entry : subjectMap.entrySet()) {
			if (entry.getValue().getSubTitle().contains(subject.getSubTitle())) {
				resultMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		return resultMap;
	}

	
	private void emptyFile(String filename) throws IOException {
	    OutputStream os = null;
	    try {
	      os = new FileOutputStream(filename);
	    } finally {
	      if (os != null) {
	        os.close();
	      }
	    }
	  }

	
	public void closeStream() {
		closeReadStream();
		closeWriteStream();
	}

	private void closeReadStream() {

	}

	private void closeWriteStream() {

	}

	@Override
	public void removeBook(Book book) throws IOException {
		long id = 0;
		for (Entry<Long, Book> entry : bookMap.entrySet()) {
			if (entry.getValue().getTitle().equals(book.getTitle())) {
				id = entry.getValue().getBookId();
				break;
			}
		}
		
		if(id != 0) {
			bookMap.remove(id);
		}
		if(bookMap.isEmpty()) {
			emptyFile(bookFileName);
		}

		for (Entry<Long, Book> entry : bookMap.entrySet()) {
			writeBook(entry.getValue());
		}

		
	}

	@Override
	public Map<Long, Book> sortBook(String fieldName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Subject> sortSubject(String fieldName) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
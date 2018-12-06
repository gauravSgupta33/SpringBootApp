package com.cognizant.datasource;

import java.io.IOException;
import java.util.Map;

import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;

public interface IDataSource {
	
	public void writeBook(Book book) throws IOException;
	public Map<Long, Book> readBook() throws IOException;
	public void writeSubject(Subject subject) throws IOException;
	public Map<Long, Subject> readSubject() throws IOException;
	public void removeSubject(Subject subject) throws IOException;
	public Map<Long, Subject> searchSubject(Subject subject) throws IOException;
	public Map<Long, Book> searchBook(Book book) throws IOException;
	public void removeBook(Book book) throws IOException;
	public Map<Long, Book> sortBook(String fieldName) throws IOException;
	public Map<Long, Subject> sortSubject(String fieldName) throws IOException;
}

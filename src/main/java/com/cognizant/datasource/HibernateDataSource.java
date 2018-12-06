package com.cognizant.datasource;

import java.io.IOException;
import java.util.Map;

import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;

public class HibernateDataSource implements IDataSource {

	@Override
	public void writeBook(Book book) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Long, Book> readBook() throws IOException {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public void writeSubject(Subject subject) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Long, Subject> readSubject() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSubject(Subject subject) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Long, Subject> searchSubject(Subject subject) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Long, Book> searchBook(Book book) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeBook(Book book) throws IOException {
		// TODO Auto-generated method stub
		
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

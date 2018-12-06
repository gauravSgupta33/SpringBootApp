package com.cognizant.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.datasource.IDataSource;
import com.cognizant.entity.Book;
import com.cognizant.utilities.CustomAppContext;
import com.cognizant.utilities.TableUtil;

@Service("BookService")
public class BookService {
	@Autowired
	private IDataSource dataSource = null;

	@Autowired
	public BookService(IDataSource dataSource) throws IOException {
		//this.dataSource = (IDataSource) CustomAppContext.getContext().getBean("DataSource");
		this.dataSource = dataSource;
		System.out.println("Reading Book");
		this.dataSource.readBook();
	}
	
//	public void setDataSource(IDataSource dataSource) {
//		this.dataSource = dataSource;
//	}

	public Map<Long, Book> listAllBooks() {
		try {
			return this.dataSource.readBook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void closeStream() {
		closeReadStream();
		closeWriteStream();
	}

	private void closeReadStream() {

	}

	private void closeWriteStream() {

	}

	public Set<Book> addBook(Book book) {
		Set<Book> bookSet = new LinkedHashSet<Book>();
		try {
//			do {
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				System.out.println("Add Book");
//				System.out.println("");
//				System.out.print("Enter Title -> ");
//				String bookTitle = br.readLine();
//				System.out.println("");
//				System.out.print("Enter Price -> ");
//				double bookPrice = Double.parseDouble(br.readLine());
//				System.out.println("");
//				System.out.print("Enter Volume -> ");
//				int bookVolume = Integer.parseInt(br.readLine());
//				System.out.println("");
//				System.out.print("Enter Publish date(YYYY/MM/DD) -> ");
//				String bookPublishDate = br.readLine();
//				String[] parseDate = bookPublishDate.split("/");
//				LocalDate date = LocalDate.of(Integer.valueOf(parseDate[0]), Integer.valueOf(parseDate[1]),
//						Integer.valueOf(parseDate[2]));
//
//				Book book = (Book) CustomAppContext.getContext().getBean("Book");
//				book.setPrice(bookPrice);
//				book.setTitle(bookTitle);
//				book.setVolume(bookVolume);
//				book.setPublishDate(date);
				dataSource.writeBook(book);
				bookSet.add(book);
				System.out.println("Book Added successfully");
				
//				System.out.println("");
//				System.out.print("Add more (Y/N)\r\n");
//				String more = br.readLine();
//
//
//				if (more.equalsIgnoreCase("N")) {
//					break;
//				}
//			} while (true);
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your input!\r\n");
			System.exit(1);
		}
		return bookSet;
	}
	

	public void removeBook(Book book) throws IOException {
		dataSource.removeBook(book);
	}
	
	
	public void searchBook(Book book) throws IOException {
		
		Map<Long, Book> resultMap = dataSource.searchBook(book);
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No serach result found for book -> " + book.getTitle());
			return;
		}
		
		TableUtil tableUtil = new TableUtil();
        tableUtil.setShowVerticalLines(true);
        tableUtil.setHeaders("Id", "Title", "Price", "Volume", "Publish Date");

		for (Entry<Long, Book> entry : resultMap.entrySet()) {
				Book tempBook = entry.getValue();
				tableUtil.addRow(tempBook.getBookId()  + "", tempBook.getTitle(), tempBook.getPrice() + "", tempBook.getVolume() + "", tempBook.getPublishDate()  + "" );
		}
        tableUtil.print();
	}

	public Map<Long, Book> searchBooks(Book book) throws IOException {
		
		Map<Long, Book> resultMap = dataSource.searchBook(book);
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No serach result found for book -> " + book.getTitle());
			return new HashMap<Long, Book>();
		}
		return resultMap;
//		
//		TableUtil tableUtil = new TableUtil();
//        tableUtil.setShowVerticalLines(true);
//        tableUtil.setHeaders("Id", "Title", "Price", "Volume", "Publish Date");
//
//		for (Entry<Long, Book> entry : resultMap.entrySet()) {
//				Book tempBook = entry.getValue();
//				tableUtil.addRow(tempBook.getBookId()  + "", tempBook.getTitle(), tempBook.getPrice() + "", tempBook.getVolume() + "", tempBook.getPublishDate()  + "" );
//		}
//        tableUtil.print();
		
	}


	

	public void removeBook() {
		try {
			do {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Remove Book");
				System.out.println("");
				System.out.print("Enter Title -> ");
				String subTitle = br.readLine();
				Book book = (Book) CustomAppContext.getContext().getBean("Book");
				book.setTitle(subTitle);
				removeBook(book);
				
				System.out.println("");
				System.out.print("Remove more book (Y/N)\r\n");

				String more = br.readLine();
				if (more.equalsIgnoreCase("N")) {
					break;
				}
			} while (true);
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your input!\r\n");
			System.exit(1);
		}

	}
	
	public void searchBook() {
		try {
			do {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Search Book");
				System.out.println("");
				System.out.print("Enter Title -> ");
				String subTitle = br.readLine();
				Book book = (Book) CustomAppContext.getContext().getBean("Book");
				book.setTitle(subTitle);
				searchBook(book);
				
				System.out.println("");
				System.out.print("Search more book (Y/N)\r\n");

				String more = br.readLine();
				if (more.equalsIgnoreCase("N")) {
					break;
				}
			} while (true);
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your input!\r\n");
			System.exit(1);
		}
	}
	
	public void sortBook(String fieldName) {
		try {
		Map<Long, Book> resultMap = dataSource.sortBook(fieldName);
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No result found for book");
			return;
		}
		
		TableUtil tableUtil = new TableUtil();
        tableUtil.setShowVerticalLines(true);
        tableUtil.setHeaders("Id", "Title", "Price", "Volume", "Publish Date");

		for (Entry<Long, Book> entry : resultMap.entrySet()) {
				Book tempBook = entry.getValue();
				tableUtil.addRow(tempBook.getBookId()  + "", tempBook.getTitle(), tempBook.getPrice() + "", tempBook.getVolume() + "", tempBook.getPublishDate()  + "" );
		}
        tableUtil.print();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



//	public static void main(String[] args) throws IOException {
//		BookService service = BookService.getInstance();
//		service.addBook();
//	}
}

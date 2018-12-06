package com.cognizant.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.datasource.IDataSource;
import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;
import com.cognizant.utilities.CustomAppContext;
import com.cognizant.utilities.TableUtil;

@Service("SubjectService")
public class SubjectService {

	@Autowired
	private IDataSource dataSource = null;

private BookService bookService = null;
	
	@Autowired
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}
	
	@Autowired
	public SubjectService(IDataSource dataSource) throws IOException {
		//dataSource = (IDataSource) CustomAppContext.getContext().getBean("DataSource");
		this.dataSource = dataSource;
		System.out.println("Reading subject");
		this.dataSource.readSubject();
	}
	
	
//	public void setDataSource(IDataSource dataSource) {
//		this.dataSource = dataSource;
//	}
	
	

	public void removeSubject(Subject subject) throws IOException {
		dataSource.removeSubject(subject);
	}
	
	public Map<Long, Subject> listAll() throws IOException {
		return this.dataSource.readSubject();
	}
	
	public void removeSubject() {
		try {
			do {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Remove Subject");
				System.out.println("");
				System.out.print("Enter Title -> ");
				String subTitle = br.readLine();
				System.out.println("");
				System.out.print("Remove more subject (Y/N)\r\n");
				Subject subject = (Subject) CustomAppContext.getContext().getBean("Subject");
				subject.setSubTitle(subTitle);
				dataSource.removeSubject(subject);
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
	
	private void searchSubject(Subject subject) throws IOException {
		
		Map<Long, Subject> resultMap = dataSource.searchSubject(subject);
		
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No serach result found for subject -> " + subject.getSubTitle());
			return;
		}

		
		TableUtil tableUtil = new TableUtil();
        tableUtil.setShowVerticalLines(true);
        tableUtil.setHeaders("Id", "Title", "Duration", "Referance(s)");//optional - if not used then there will be no header and horizontal lines

		for (Entry<Long, Subject> entry : resultMap.entrySet()) {
				Subject tempSubject = entry.getValue();
				StringBuffer ref = new StringBuffer();
				if(!tempSubject.getReference().isEmpty()) {
					for(Book book : tempSubject.getReference()) {
						ref.append(book.getTitle() + ",");
					}
				}
				tableUtil.addRow(tempSubject.getSubjectId()  + "", tempSubject.getSubTitle(), tempSubject.getDurationInHours() + "", ref.toString());
		}
		tableUtil.print();

	}
	
	public Map<Long, Subject> searchSubjects(Subject subject) throws IOException {
		
		Map<Long, Subject> resultMap = dataSource.searchSubject(subject);
		
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No serach result found for subject -> " + subject.getSubTitle());
			return new HashMap<Long, Subject>();
		}
		return resultMap;

	}
	
	public Map<Long, Subject> searchSubjectsByDuration(Subject subject) throws IOException {
		
		Map<Long, Subject> resultMap = listAll();
		
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No serach result found for subject -> " + subject.getSubTitle());
			return new HashMap<Long, Subject>();
		}
		
		Map<Long, Subject> map = new LinkedHashMap<Long, Subject>();
		for (Entry<Long, Subject> entry : resultMap.entrySet()) {
			if (entry.getValue().getDurationInHours() == subject.getDurationInHours()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;

	}


	public void searchSubject() {
		try {
			do {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Search Subject");
				System.out.print("Enter Title -> ");
				String subTitle = br.readLine();
				Subject subject = (Subject) CustomAppContext.getContext().getBean("Subject");
				subject.setSubTitle(subTitle);
				searchSubject(subject);
				System.out.print("Serach more subject (Y/N)\r\n");
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


	public void addSubject(Subject subject) {
		try {
//			do {
//				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//				System.out.println("Add Subject");
//				System.out.println("");
//				System.out.print("Enter Title -> ");
//				String subjectTitle = br.readLine();
//				System.out.println("");
//				System.out.print("Enter duration -> ");
//				int subjectDuration = Integer.parseInt(br.readLine());
//				System.out.println("");
//				System.out.print("Add reference(Y/N) ");
//				String ref = br.readLine();
//				Set<Book> refBookSet = new LinkedHashSet<Book>();
//				if (ref.equalsIgnoreCase("Y")) {
//					refBookSet = ((BookService)CustomAppContext.getContext().getBean("BookService")).addBook();//BookService.getInstance().addBook();
//				}
//				System.out.println("");
//				System.out.print("Add more subject (Y/N)\r\n");
//
//				String more = br.readLine();
//				Subject subject = (Subject) CustomAppContext.getContext().getBean("Subject");
//				//subject.setSubjectId(++maxSubjectId);
//				subject.setSubTitle(subjectTitle);
//				subject.setDurationInHours(subjectDuration);
//				subject.setReference(refBookSet);
				dataSource.writeSubject(subject);
				for(Book book : subject.getReference()) {
				bookService.addBook(book);
				}
//				if (more.equalsIgnoreCase("N")) {
//					break;
//				}
//			} while (true);
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your input!\r\n");
			System.exit(1);
		}

	}

	public void sortSubject(String fieldName) {
		try {
		Map<Long, Subject> resultMap = dataSource.sortSubject(fieldName);
		
		if(resultMap == null || resultMap.isEmpty()) {
			System.out.println("No result found for subject");
			return;
		}

		
		TableUtil tableUtil = new TableUtil();
        tableUtil.setShowVerticalLines(true);
        tableUtil.setHeaders("Id", "Title", "Duration", "Referance(s)");//optional - if not used then there will be no header and horizontal lines

		for (Entry<Long, Subject> entry : resultMap.entrySet()) {
				Subject tempSubject = entry.getValue();
				StringBuffer ref = new StringBuffer();
				if(!tempSubject.getReference().isEmpty()) {
					for(Book book : tempSubject.getReference()) {
						ref.append(book.getTitle() + ",");
					}
				}
				tableUtil.addRow(tempSubject.getSubjectId()  + "", tempSubject.getSubTitle(), tempSubject.getDurationInHours() + "", ref.toString());
		}
		tableUtil.print();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
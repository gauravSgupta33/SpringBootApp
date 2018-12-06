package com.cognizant.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.entity.Book;
import com.cognizant.entity.Subject;
import com.cognizant.service.SubjectService;

@RestController
@CrossOrigin
public class SubjectController {
	private SubjectService subjectService = null;
	
	@Autowired
	public void setSubjectService(SubjectService subjectService) {
		this.subjectService = subjectService;
	}
	
	
	/*
	 * This method will be called on form submission, handling POST request for
	 * saving employee in database. It also validates the user input
	 */
	@PostMapping("/newsubject")
	public String saveSubject(@RequestBody Subject subject) {
////		Set<Book> ref = new HashSet<Book>();
////		ref.add(book);
//		subject.setReference(ref);
		subjectService.addSubject(subject);
		return "\"Subject " + subject.getSubTitle() + " added successfully\"";
	}

	@DeleteMapping("/deletesubject")
	public boolean deleteSubject(@RequestParam long id) {
		Subject subject = new Subject();
		try {
			Map<Long, Subject> subjectMap = this.subjectService.listAll();
			for (Entry<Long, Subject> entry : subjectMap.entrySet()) {
				if (entry.getValue().getSubjectId() == id) {
					subject.setSubTitle(entry.getValue().getSubTitle());
					subject.setSubjectId(id);
					this.subjectService.removeSubject(subject);
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	

    /**
     * This method searches our database for books based on the given {@link BookSearchCriteria}. 
     * Only books matching the criteria are returned.
     * 
     * @param criteria the criteria used for searching
     * @return the found books
     * @throws IOException 
     * 
     * @see com.example.bookstore.repository.BookRepository#findBooks(BookSearchCriteria)
     */
    @GetMapping("/subjectsearch")
    public Collection<Subject> list(@RequestParam String title) throws IOException {
    	Subject subject = new Subject();
    	subject.setSubTitle(title);
    	Map<Long, Subject> subjectMap = this.subjectService.searchSubjects(subject);
    	List<Subject> subjects = new ArrayList<Subject>();
    	for(Entry<Long, Subject> entry : subjectMap.entrySet()) {
				subjects.add(entry.getValue());
		}
    	return subjects;
    }
    
    
    /**
     * This method searches our database for books based on the given {@link BookSearchCriteria}. 
     * Only books matching the criteria are returned.
     * 
     * @param criteria the criteria used for searching
     * @return the found books
     * 
     * @see com.example.bookstore.repository.BookRepository#findBooks(BookSearchCriteria)
     */
    @GetMapping("/subjectsearchbyduration")
    public Collection<Subject> listByDuration(@RequestParam int duration) {
    	List<Subject> subjects = new ArrayList<Subject>();
    	if(duration <= 0) {
    		return subjects;
    	}
 		
		Map<Long, Subject> subjectMap;
		try {
			Subject subject = new Subject();
			subject.setDurationInHours(duration);
			subjectMap = subjectService.searchSubjectsByDuration(subject);
			for(Entry<Long, Subject> entry : subjectMap.entrySet()) {
				subjects.add(entry.getValue());
			}
	        return subjects;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return subjects;
    }
}

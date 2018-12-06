package com.cognizant.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.entity.Book;
import com.cognizant.service.BookService;

@RestController
@CrossOrigin
public class BookController {
	private BookService bookService = null;
	
	@Autowired
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}


    @GetMapping("/listAllBook")
    public List<Book> listAllBook(@RequestParam(value="name", defaultValue="World") String name) {
		List<Book> books = new ArrayList<Book>();
		Map<Long, Book> bookMap = bookService.listAllBooks();
		for(Entry<Long, Book> entry : bookMap.entrySet()) {
			books.add(entry.getValue());
		}
		
		return books;
    }
    
    @GetMapping("/searchBook")
    public List<Book> searchBook(@RequestParam String title) {
		List<Book> books = new ArrayList<Book>();
		Map<Long, Book> bookMap = bookService.listAllBooks();
		for(Entry<Long, Book> entry : bookMap.entrySet()) {
			if(entry.getValue().getTitle().equalsIgnoreCase(title)) {
				books.add(entry.getValue());
			}
		}
		
		return books;
    }
    
   @PostMapping("/addbook")
   public String addBook(@RequestBody Book book) {
	   this.bookService.addBook(book);
	   return "\"Successfully added the book details\"";
   }
   
   @PutMapping("/updatebook")
   public String updateBook(@RequestBody Book book) {
		Map<Long, Book> bookMap = bookService.listAllBooks();
		for(Entry<Long, Book> entry : bookMap.entrySet()) {
			if(book.getTitle().equals(entry.getValue().getTitle())) {
				 this.bookService.addBook(book);
				 
				return "\"Successfully updated the book details\"";
			}
		}
		return "\"Failed to updated the book details.\"";
   }
   
   @DeleteMapping("/deleteBook")
	public boolean deleteBook(@RequestParam long id) {
		Book book = new Book();

		Map<Long, Book> bookMap = bookService.listAllBooks();
		try {
			for (Entry<Long, Book> entry : bookMap.entrySet()) {
				if (entry.getValue().getBookId() == id) {
					book.setTitle(entry.getValue().getTitle());
					this.bookService.removeBook(book);
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		}
		return false;
	}
}

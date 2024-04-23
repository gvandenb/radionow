package com.radionow.stream.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.Book;
import com.radionow.stream.model.Station;
import com.radionow.stream.model.Statistic;
import com.radionow.stream.model.Statistic.StatisticType;
import com.radionow.stream.service.BookService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BookController {

	@Autowired
	BookService bookService;
	
	
	@GetMapping("/books")
	public ResponseEntity<List<Book>> getBooks(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "20") int size) {
		
		try {
			
			//Pageable pageable = PageRequest.of(page, size, Sort.by("language").ascending());
			Pageable pageable = PageRequest.of(page, size, Sort.by("statistic.rbVotes").descending());


			//List<Book> bookList = bookService.findAll(pageable).getContent();
			List<Book> bookList = bookService.findAllByLanguage("eng", pageable).getContent();

			 
			return new ResponseEntity<>(bookList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/books/{bid}")
	public ResponseEntity<Book> getBookById(@PathVariable("bid") Long bid) {
		
		try {
			
			Book book = bookService.findById(bid);

			 
			return new ResponseEntity<>(book, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/books/{id}/views")
	public ResponseEntity<Long> fetchBookViews(@PathVariable("id") long id) {
		Book bookData = bookService.findById(id);
		Long viewCount = 1L;
		if (bookData != null) {
			Book book = bookData;
			if (book.getStatistic() != null) {
				viewCount = book.getStatistic().getViews();
			}
			
			return new ResponseEntity<>(viewCount, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/books/{id}/views")
	public ResponseEntity<String> updateBookViews(@PathVariable("id") long id) {
		Book bookData = bookService.findById(id);

		if (bookData != null) {
			Book book = bookData;
			Statistic stat = book.getStatistic();
			if (stat != null) {
				if (stat.getViews() == null) {
					stat.setViews(1L);
				}
				else {
					stat.setViews(stat.getViews() + 1);
				}
				stat.setStatisticType(StatisticType.AUDIOBOOK);

			}
			else {
				stat = new Statistic(1L, StatisticType.AUDIOBOOK);
			}
			book.setStatistic(stat);
			System.out.println(book.getStatistic());
			bookService.save(book);
			
			return new ResponseEntity<>("Ok", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}

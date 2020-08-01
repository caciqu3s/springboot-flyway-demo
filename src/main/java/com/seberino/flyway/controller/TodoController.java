package com.seberino.flyway.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seberino.flyway.entity.Todo;
import com.seberino.flyway.repository.TodoRepository;
import com.seberino.flyway.request.CreateTodoRequest;

@RestController
@RequestMapping("api/v1/todos")
public class TodoController {
	@Autowired
	private TodoRepository repository;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Object> createTodo(@RequestBody CreateTodoRequest request) {
		Todo todo = this.repository.save(Todo.builder().title(request.getTitle()).build());
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.getId())
                .toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping
	public ResponseEntity<List<Todo>> getTodos() {
		return ResponseEntity.ok(this.repository.findAll());
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
		return ResponseEntity.ok(this.repository.getOne(id));
	}
	
	@PutMapping("{id}")
	@Transactional
	public ResponseEntity<Todo> editTodo(@PathVariable Long id, @RequestBody CreateTodoRequest request) {
		Todo todo = this.repository.getOne(id);
		todo.setTitle(request.getTitle());
		
		return ResponseEntity.ok(todo);
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<Object> deleteTodo(@PathVariable Long id) {
		this.repository.deleteById(id);
		
		return ResponseEntity.accepted().build();
	}
}

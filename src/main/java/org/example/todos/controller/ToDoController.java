package org.example.todos.controller;

import org.example.todos.model.ToDo;
import org.example.todos.repository.ToDoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
public class ToDoController {

    private final ToDoRepository toDoRepository;

    public ToDoController(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    // Список всех задач
    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", toDoRepository.findAll());
        return "todos/list";
    }

    // Форма для добавления новой задачи
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("todo", new ToDo());
        return "todos/add";
    }

    // Добавление новой задачи
    @PostMapping
    public String addTodo(@ModelAttribute ToDo todo) {
        toDoRepository.save(todo);
        return "redirect:/todos";
    }

    // Форма для редактирования задачи
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        toDoRepository.findById(id).ifPresent(todo -> model.addAttribute("todo", todo));
        return "todos/edit";
    }

    // Обновление задачи
    @PostMapping("/{id}")
    public String updateTodo(@PathVariable("id") Long id, @ModelAttribute ToDo updatedTodo) {
        toDoRepository.findById(id).ifPresent(todo -> {
            todo.setTitle(updatedTodo.getTitle());
            todo.setDescription(updatedTodo.getDescription());
            todo.setCompleted(updatedTodo.isCompleted());
            toDoRepository.save(todo);
        });
        return "redirect:/todos";
    }

    // Удаление задачи
    @GetMapping("/{id}/delete")
    public String deleteTodo(@PathVariable("id") Long id) {
        toDoRepository.deleteById(id);
        return "redirect:/todos";
    }
}

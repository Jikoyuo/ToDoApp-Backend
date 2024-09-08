package com.belajar.belajartodolist.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.belajar.belajartodolist.models.Task;
import com.belajar.belajartodolist.models.Users;
import com.belajar.belajartodolist.repositories.TaskRepository;
import com.belajar.belajartodolist.repositories.UserRepository;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add/{username}")
    public ResponseEntity<Map<String, Object>> addTask(@PathVariable String username, @RequestBody Task task) {
        Map<String, Object> response = new HashMap<>();
        try {
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                throw new Exception("User not found");
            }

            task.setUser(user);
            Task savedTask = taskRepository.save(task);
            response.put("message", "Task added successfully");
            response.put("task", savedTask);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Error adding task: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param username
     * @return
     */
    @GetMapping("/get/{username}")
    public ResponseEntity<ArrayList<Map<String, Object>>> getTasks(@PathVariable String username) {
        try {
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<Task> tasks = taskRepository.findAllByUser(user);
            ArrayList<Map<String, Object>> testRespon = new ArrayList<>();

            for (int i = 0; i < tasks.size(); i++) {
                Map<String, Object>temp = new HashMap<>();
                temp.put("id", tasks.get(i).getId());
                temp.put("Task", tasks.get(i).getTask());
                testRespon.add(temp);
            }
            System.out.println("Respon : "+testRespon);
            return new ResponseEntity<>(testRespon, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{taskId}")
    public ResponseEntity<Map<String, Object>> editTask(@PathVariable Integer taskId, @RequestBody Task updatedTask) {
        Map<String, Object> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task not found"));
            task.setTask(updatedTask.getTask()); // Update task details
            Task savedTask = taskRepository.save(task);
            response.put("message", "Task updated successfully");
            response.put("task", savedTask);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error updating task: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Integer taskId) {
        Map<String, String> response = new HashMap<>();
        try {
            taskRepository.deleteById(taskId);
            response.put("message", "Task deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error deleting task: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

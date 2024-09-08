package com.belajar.belajartodolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.belajar.belajartodolist.models.Task;
import com.belajar.belajartodolist.models.Users;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByUser(Users user);
}
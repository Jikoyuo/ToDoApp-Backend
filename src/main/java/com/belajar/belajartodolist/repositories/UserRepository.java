package com.belajar.belajartodolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.belajar.belajartodolist.models.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username); // Pastikan ini mengembalikan satu pengguna
}

package com.lib.api.repository;

import com.lib.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // JpaRepository gives us findAll(), save(), and deleteById() automatically.
}
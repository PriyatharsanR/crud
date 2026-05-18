package com.tasks.crud.Repository;

import com.tasks.crud.Entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Books, Integer> {

}

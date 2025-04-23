package com.sensevoca.backend.repository;

import com.sensevoca.backend.entity.MyWordbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyWordbookRepository extends JpaRepository<MyWordbook, Long> {

}

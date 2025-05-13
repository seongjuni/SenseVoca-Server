package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.Basic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BasicRepository extends JpaRepository<Basic, Long> {

}

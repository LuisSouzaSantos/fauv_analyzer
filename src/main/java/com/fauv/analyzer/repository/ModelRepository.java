package com.fauv.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fauv.analyzer.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

}

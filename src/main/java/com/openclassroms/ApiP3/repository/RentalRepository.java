package com.openclassroms.ApiP3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroms.ApiP3.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

}

package com.openclassroms.ApiP3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassroms.ApiP3.model.Message;
import com.openclassroms.ApiP3.model.AppUser;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByUser(AppUser user);
}

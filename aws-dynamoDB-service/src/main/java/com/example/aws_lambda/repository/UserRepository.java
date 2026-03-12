package com.example.aws_lambda.repository;

import com.example.aws_lambda.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByName(String name);

    List<User> findByAgeGreaterThan(int age);
}

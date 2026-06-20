package com.example.crud.domain.model;

import java.util.Set;

public record User(String username, String password, Set<String> roles) {}
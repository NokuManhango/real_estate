package app.model;

public record User(String id, String name, Role role, String email, String password) {}

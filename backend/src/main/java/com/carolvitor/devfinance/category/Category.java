package com.carolvitor.devfinance.category;

import jakarta.persistence.*;

@Entity
public class Category {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false, unique = true) private String name;
  @Column(nullable = false) private String color;
  protected Category() {}
  public Category(String name, String color) { this.name = name; this.color = color; }
  public Long getId() { return id; }
  public String getName() { return name; }
  public String getColor() { return color; }
}

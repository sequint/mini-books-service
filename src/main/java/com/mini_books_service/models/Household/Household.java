package com.mini_books_service.models.Household;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Household {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String address;

    public String getName() {
      return name;
    }
    public String getAddress() {
      return address;
    }

    public void setName(String name) {
      this.name = name;
    }
    public void setAddress(String address) {
      this.address = address;
    }
}

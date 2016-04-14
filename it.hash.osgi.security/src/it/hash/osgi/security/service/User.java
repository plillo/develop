package it.hash.osgi.security.service;

import java.security.Principal;

public class User implements Principal {

  private final String name;

  public User( String name ) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
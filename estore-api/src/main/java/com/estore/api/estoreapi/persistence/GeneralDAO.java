package com.estore.api.estoreapi.persistence;

public interface GeneralDAO<Key, Value> {
  Value[] getAll() throws Exception;

  Value[] find(String containsText) throws Exception;

  Value get(Key id) throws Exception;

  Value create(Value t) throws Exception;

  Value update(Value t) throws Exception;

  boolean delete(Key id) throws Exception;
}

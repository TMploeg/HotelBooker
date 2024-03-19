package com.tmploeg.hotelbooker.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ValueResult<TValue> {
  private final List<String> errors;
  private TValue value;

  private ValueResult(List<String> errors) {
    this.errors = errors;
  }

  private ValueResult(TValue value) {
    this.value = value;
    errors = new ArrayList<>();
  }

  public boolean succeeded() {
    return errors == null || errors.isEmpty();
  }

  public static <TValue> ValueResult<TValue> succesResult(TValue booking) {
    return new ValueResult<TValue>(booking);
  }

  public static <TValue> ValueResult<TValue> errorResult(List<String> errors) {
    return new ValueResult<TValue>(errors);
  }
}

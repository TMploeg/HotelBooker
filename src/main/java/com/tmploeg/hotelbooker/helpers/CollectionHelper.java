package com.tmploeg.hotelbooker.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class CollectionHelper {
  private CollectionHelper() {}

  public static <T> boolean hasDuplicates(T[] array) {
    if (array == null) {
      throw new NullPointerException("array is null");
    }

    if (array.length <= 1) {
      return false;
    }

    for (int i = 0; i < array.length - 1; i++) {
      for (int ii = i + 1; ii < array.length; ii++) {
        if (Objects.equals(array[i], array[ii])) {
          return true;
        }
      }
    }

    return false;
  }

  public static <T> boolean hasDuplicates(Collection<T> collection) {
    if (collection == null) {
      throw new NullPointerException("collection is null");
    }

    if (collection.size() <= 1) {
      return false;
    }

    List<T> list = new ArrayList<T>(collection);

    for (int i = 0; i < list.size() - 1; i++) {
      for (int ii = i + 1; ii < list.size(); ii++) {
        if (Objects.equals(list.get(i), list.get(ii))) {
          return true;
        }
      }
    }

    return false;
  }
}

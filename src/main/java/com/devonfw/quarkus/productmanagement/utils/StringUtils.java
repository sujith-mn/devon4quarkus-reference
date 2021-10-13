package com.devonfw.quarkus.productmanagement.utils;

import org.springframework.lang.Nullable;

public class StringUtils {
  public static boolean isEmpty(@Nullable String str) {

    return (str == null || "".equals(str));
  }
}

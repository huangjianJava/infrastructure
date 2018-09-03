package org.infrastructure.common.web.exception;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.io.PrintWriter;
import java.io.StringWriter;

/** 
 * Utility functions when interacting with {@link Throwables}. 
 * 
 *
 */

public final class MoreThrowables {
  /**
   * Throws {code t} if it is an instance of {@link RuntimeException} or {@link Error}.
   *
   * <p>This is intended to mimic Guava's method by the same name,
   *  but which is unavailable to us due to compatibility with older Guava versions
   *  (upgraded to Guava 20 or later).
   */
  public static void throwIfUnchecked(Throwable t) {
    Preconditions.checkNotNull(t);
    if (t instanceof RuntimeException) {
      throw (RuntimeException) t;
    }
    if (t instanceof Error) {
      throw (Error) t;
    }
  }

  /**
   * 将ErrorStack转化为String.
   */
  public static String getStackTraceAsString(Throwable e) {
    if (e == null) {
      return "";
    }
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
  
  /**
   * Get root cause stack trace as string.
   * @param cause Throwable
   * @return the string of root cause stack trace
   */
  public static String getRootCauseStackTraceAsString(Throwable cause) {
    return getStackTraceAsString(ExceptionUtils.getRootCause(cause));
  }
  
  /**
   * Get Root Cause Message.
   * @param cause Throwable
   * @return the root cause message
   */
  public static String getRootCauseMessage(Throwable cause) {
    return ExceptionUtils.getRootCauseMessage(cause);
  }

  /**
   * 判断异常是否由某些底层的异常引起.
   */
  @SuppressWarnings("unchecked")
  public static boolean isCausedBy(Exception ex,
      Class<? extends Exception>... causeExceptionClasses) {
    Throwable cause = ex.getCause();
    while (cause != null) {
      for (Class<? extends Exception> causeClass : causeExceptionClasses) {
        if (causeClass.isInstance(cause)) {
          return true;
        }
      }
      cause = cause.getCause();
    }
    return false;
  }

  // Prevent instantiation
  private MoreThrowables() {}
}

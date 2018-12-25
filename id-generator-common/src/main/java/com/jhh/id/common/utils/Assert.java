package com.jhh.id.common.utils;

import com.jhh.id.common.exception.ArgumentsException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public abstract class Assert {

  private Assert() {
    throw new IllegalStateException("不允许此操作.");
  }

  /**
   * Assert a boolean expression, throwing an {@code ArgumentsException} if the
   * expression evaluates to {@code false}.
   * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than
   * zero");</pre>
   *
   * @param expression a boolean expression
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if {@code expression} is {@code false}
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert a boolean expression, throwing an {@code ArgumentsException} if the
   * expression evaluates to {@code false}.
   * <pre class="code">
   * Assert.isTrue(i &gt; 0, () -&gt; "The value '" + i + "' must be greater than zero");
   * </pre>
   *
   * @param expression a boolean expression
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if {@code expression} is {@code false}
   * @since 5.0
   */
  public static void isTrue(boolean expression,
      Supplier<String> messageSupplier) {
    if (!expression) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }

  /**
   * Assert that an object is {@code null}.
   * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
   *
   * @param object the object to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the object is not {@code null}
   */
  public static void isNull(Object object, String message) {
    if (object != null) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that an object is {@code null}.
   * <pre class="code">
   * Assert.isNull(value, () -&gt; "The value '" + value + "' must be null");
   * </pre>
   *
   * @param object the object to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the object is not {@code null}
   * @since 5.0
   */
  public static void isNull(Object object,
      Supplier<String> messageSupplier) {
    if (object != null) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that an object is not {@code null}.
   * <pre class="code">Assert.notNull(clazz, "The class must not be
   * null");</pre>
   *
   * @param object the object to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the object is {@code null}
   */
  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that an object is not {@code null}.
   * <pre class="code">
   * Assert.notNull(clazz, () -&gt; "The class '" + clazz.getName() + "' must not be null");
   * </pre>
   *
   * @param object the object to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the object is {@code null}
   * @since 5.0
   */
  public static void notNull(Object object,
      Supplier<String> messageSupplier) {
    if (object == null) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that the given String is not empty; that is, it must not be {@code
   * null} and not the empty String.
   * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
   *
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the text is empty
   * @see StringUtils#hasLength
   */
  public static void hasLength(String text, String message) {
    if (!StringUtils.hasLength(text)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that the given String is not empty; that is, it must not be {@code
   * null} and not the empty String.
   * <pre class="code">
   * Assert.hasLength(name, () -&gt; "Name for account '" + account.getId() + "' must not be empty");
   * </pre>
   *
   * @param text the String to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the text is empty
   * @see StringUtils#hasLength
   * @since 5.0
   */
  public static void hasLength(String text,
      Supplier<String> messageSupplier) {
    if (!StringUtils.hasLength(text)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that the given String contains valid text content; that is, it must
   * not be {@code null} and must contain at least one non-whitespace
   * character.
   * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
   *
   * @param text the String to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the text does not contain valid text content
   * @see StringUtils#hasText
   */
  public static void hasText(String text, String message) {
    if (!StringUtils.hasText(text)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that the given String contains valid text content; that is, it must
   * not be {@code null} and must contain at least one non-whitespace
   * character.
   * <pre class="code">
   * Assert.hasText(name, () -&gt; "Name for account '" + account.getId() + "' must not be empty");
   * </pre>
   *
   * @param text the String to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the text does not contain valid text content
   * @see StringUtils#hasText
   * @since 5.0
   */
  public static void hasText(String text,
      Supplier<String> messageSupplier) {
    if (!StringUtils.hasText(text)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that the given text does not contain the given substring.
   * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain
   * 'rod'");</pre>
   *
   * @param textToSearch the text to search
   * @param substring the substring to find within the text
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the text contains the substring
   */
  public static void doesNotContain(String textToSearch,
      String substring, String message) {
    if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
        &&
        textToSearch.contains(substring)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that the given text does not contain the given substring.
   * <pre class="code">
   * Assert.doesNotContain(name, forbidden, () -&gt; "Name must not contain '" + forbidden + "'");
   * </pre>
   *
   * @param textToSearch the text to search
   * @param substring the substring to find within the text
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the text contains the substring
   * @since 5.0
   */
  public static void doesNotContain(String textToSearch,
      String substring, Supplier<String> messageSupplier) {
    if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
        &&
        textToSearch.contains(substring)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that an array contains elements; that is, it must not be {@code
   * null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(array, "The array must contain
   * elements");</pre>
   *
   * @param array the array to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the object array is {@code null} or contains
   * no elements
   */
  public static void notEmpty(Object[] array, String message) {
    if (ObjectUtils.isEmpty(array)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that an array contains elements; that is, it must not be {@code
   * null} and must contain at least one element.
   * <pre class="code">
   * Assert.notEmpty(array, () -&gt; "The " + arrayType + " array must contain elements");
   * </pre>
   *
   * @param array the array to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the object array is {@code null} or contains
   * no elements
   * @since 5.0
   */
  public static void notEmpty(Object[] array,
      Supplier<String> messageSupplier) {
    if (ObjectUtils.isEmpty(array)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that an array contains no {@code null} elements.
   * <p>Note: Does not complain if the array is empty!
   * <pre class="code">Assert.noNullElements(array, "The array must contain
   * non-null elements");</pre>
   *
   * @param array the array to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the object array contains a {@code null}
   * element
   */
  public static void noNullElements(Object[] array, String message) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw new ArgumentsException(message);
        }
      }
    }
  }

  /**
   * Assert that an array contains no {@code null} elements.
   * <p>Note: Does not complain if the array is empty!
   * <pre class="code">
   * Assert.noNullElements(array, () -&gt; "The " + arrayType + " array must contain non-null elements");
   * </pre>
   *
   * @param array the array to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the object array contains a {@code null}
   * element
   * @since 5.0
   */
  public static void noNullElements(Object[] array,
      Supplier<String> messageSupplier) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          throw new ArgumentsException(nullSafeGet(messageSupplier));
        }
      }
    }
  }


  /**
   * Assert that a collection contains elements; that is, it must not be {@code
   * null} and must contain at least one element.
   * <pre class="code">Assert.notEmpty(collection, "Collection must contain
   * elements");</pre>
   *
   * @param collection the collection to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the collection is {@code null} or contains no
   * elements
   */
  public static void notEmpty(Collection<?> collection,
      String message) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that a collection contains elements; that is, it must not be {@code
   * null} and must contain at least one element.
   * <pre class="code">
   * Assert.notEmpty(collection, () -&gt; "The " + collectionType + " collection must contain elements");
   * </pre>
   *
   * @param collection the collection to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the collection is {@code null} or contains no
   * elements
   * @since 5.0
   */
  public static void notEmpty(Collection<?> collection,
      Supplier<String> messageSupplier) {
    if (CollectionUtils.isEmpty(collection)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }

  /**
   * Assert that a Map contains entries; that is, it must not be {@code null}
   * and must contain at least one entry.
   * <pre class="code">Assert.notEmpty(map, "Map must contain entries");</pre>
   *
   * @param map the map to check
   * @param message the exception message to use if the assertion fails
   * @throws ArgumentsException if the map is {@code null} or contains no
   * entries
   */
  public static void notEmpty(Map<?, ?> map, String message) {
    if (CollectionUtils.isEmpty(map)) {
      throw new ArgumentsException(message);
    }
  }

  /**
   * Assert that a Map contains entries; that is, it must not be {@code null}
   * and must contain at least one entry.
   * <pre class="code">
   * Assert.notEmpty(map, () -&gt; "The " + mapType + " map must contain entries");
   * </pre>
   *
   * @param map the map to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails
   * @throws ArgumentsException if the map is {@code null} or contains no
   * entries
   * @since 5.0
   */
  public static void notEmpty(Map<?, ?> map,
      Supplier<String> messageSupplier) {
    if (CollectionUtils.isEmpty(map)) {
      throw new ArgumentsException(nullSafeGet(messageSupplier));
    }
  }


  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">Assert.instanceOf(Foo.class, foo, "Foo expected");</pre>
   *
   * @param type the type to check against
   * @param obj the object to check
   * @param message a message which will be prepended to provide further
   * context. If it is empty or ends in ":" or ";" or "," or ".", a full
   * exception message will be appended. If it ends in a space, the name of the
   * offending object's type will be appended. In any other case, a ":" with a
   * space and the name of the offending object's type will be appended.
   * @throws ArgumentsException if the object is not an instance of type
   */
  public static void isInstanceOf(Class<?> type, Object obj,
      String message) {
    notNull(type, "Type to check against must not be null");
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, message);
    }
  }

  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">
   * Assert.instanceOf(Foo.class, foo, () -&gt; "Processing " + Foo.class.getSimpleName() + ":");
   * </pre>
   *
   * @param type the type to check against
   * @param obj the object to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails. See {@link #isInstanceOf(Class, Object, String)} for
   * details.
   * @throws ArgumentsException if the object is not an instance of type
   * @since 5.0
   */
  public static void isInstanceOf(Class<?> type, Object obj,
      Supplier<String> messageSupplier) {
    notNull(type, "Type to check against must not be null");
    if (!type.isInstance(obj)) {
      instanceCheckFailed(type, obj, nullSafeGet(messageSupplier));
    }
  }

  /**
   * Assert that the provided object is an instance of the provided class.
   * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
   *
   * @param type the type to check against
   * @param obj the object to check
   * @throws ArgumentsException if the object is not an instance of type
   */
  public static void isInstanceOf(Class<?> type, Object obj) {
    isInstanceOf(type, obj, "");
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">Assert.isAssignable(Number.class, myClass, "Number
   * expected");</pre>
   *
   * @param superType the super type to check against
   * @param subType the sub type to check
   * @param message a message which will be prepended to provide further
   * context. If it is empty or ends in ":" or ";" or "," or ".", a full
   * exception message will be appended. If it ends in a space, the name of the
   * offending sub type will be appended. In any other case, a ":" with a space
   * and the name of the offending sub type will be appended.
   * @throws ArgumentsException if the classes are not assignable
   */
  public static void isAssignable(Class<?> superType,
      Class<?> subType, String message) {
    notNull(superType, "Super type to check against must not be null");
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, message);
    }
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">
   * Assert.isAssignable(Number.class, myClass, () -&gt; "Processing " + myAttributeName + ":");
   * </pre>
   *
   * @param superType the super type to check against
   * @param subType the sub type to check
   * @param messageSupplier a supplier for the exception message to use if the
   * assertion fails. See {@link #isAssignable(Class, Class, String)} for
   * details.
   * @throws ArgumentsException if the classes are not assignable
   * @since 5.0
   */
  public static void isAssignable(Class<?> superType,
      Class<?> subType, Supplier<String> messageSupplier) {
    notNull(superType, "Super type to check against must not be null");
    if (subType == null || !superType.isAssignableFrom(subType)) {
      assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
    }
  }

  /**
   * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
   * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
   *
   * @param superType the super type to check
   * @param subType the sub type to check
   * @throws ArgumentsException if the classes are not assignable
   */
  public static void isAssignable(Class<?> superType, Class<?> subType) {
    isAssignable(superType, subType, "");
  }


  private static void instanceCheckFailed(Class<?> type, Object obj,
      String msg) {
    String className = (obj != null ? obj.getClass().getName() : "null");
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.hasLength(msg)) {
      if (endsWithSeparator(msg)) {
        result = msg + " ";
      } else {
        result = messageWithTypeName(msg, className);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + ("Object of class [" + className
          + "] must be an instance of " + type);
    }
    throw new ArgumentsException(result);
  }

  private static void assignableCheckFailed(Class<?> superType,
      Class<?> subType, String msg) {
    String result = "";
    boolean defaultMessage = true;
    if (StringUtils.hasLength(msg)) {
      if (endsWithSeparator(msg)) {
        result = msg + " ";
      } else {
        result = messageWithTypeName(msg, subType);
        defaultMessage = false;
      }
    }
    if (defaultMessage) {
      result = result + (subType + " is not assignable to " + superType);
    }
    throw new ArgumentsException(result);
  }

  private static boolean endsWithSeparator(String msg) {
    return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg
        .endsWith("."));
  }

  private static String messageWithTypeName(String msg,
      Object typeName) {
    return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
  }

  private static String nullSafeGet(
      Supplier<String> messageSupplier) {
    return (messageSupplier != null ? messageSupplier.get() : null);
  }

}

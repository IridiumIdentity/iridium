/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package software.iridium.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.iridium.api.util.AttributeValidator;

public class AttributeValidatorTest {

  private AttributeValidator subject;

  @BeforeEach
  public void setupForEachTestCase() {
    subject = new AttributeValidator();
  }

  @Test
  public void isZeroOrGreater_AllGood_LongIsGreater() {
    final var candidate = 10L;
    final var isGreater = subject.isZeroOrGreater(candidate);

    assertTrue(isGreater);
  }

  @Test
  public void isNotBlank_CandidateContainsNullValueString_ReturnsFalse() {
    final var candidate = "null";

    assertFalse(subject.isNotBlank(candidate));
  }

  @Test
  public void isZeroOrGreater_AllGood_IntegerIsGreater() {
    final var candidate = 10;
    final var isGreater = subject.isZeroOrGreater(candidate);

    assertTrue(isGreater);
  }

  @Test
  public void isPositive_AllGood_IsPositive() {
    final var candidate = 10;
    final var isPositive = subject.isPositive(candidate);

    assertTrue(isPositive);
  }

  @Test
  public void isValidSubdomain_AllGood_IsValidSubdomain() {
    final var candidate = "Ab0-zZ3";
    final var isValidSubdomain = subject.isValidSubdomain(candidate);

    assertTrue(isValidSubdomain);
  }

  @Test
  public void isValidSubdomain_ContainsDot_IsNotValidSubdomain() {
    final var candidate = "abc.def";
    final var isValidSubdomain = subject.isValidSubdomain(candidate);

    assertFalse(isValidSubdomain);
  }

  @Test
  public void isNotBlankAndNoLongerThan_AllGood_IsNotBlankAndNoLongerThanFive() {
    final var candidate = "abcde";
    final var maxLength = 5;
    final var isNotBlankAndNoLongerThanFive =
        subject.isNotBlankAndNoLongerThan(candidate, maxLength);

    assertTrue(isNotBlankAndNoLongerThanFive);
  }

  @Test
  public void isNotBlankAndNoLongerThan_ExceedsMaxLength_IsNotBlankButLongerThanFive() {
    final var candidate = "abcdef";
    final var maxLength = 5;
    final var isNotBlankAndNoLongerThanFive =
        subject.isNotBlankAndNoLongerThan(candidate, maxLength);

    assertFalse(isNotBlankAndNoLongerThanFive);
  }

  @Test
  public void isNotBlankAndNoLongerThan_Blank_IsBlank() {
    final var candidate = " ";
    final var maxLength = 5;
    final var isNotBlankAndNoLongerThanFive =
        subject.isNotBlankAndNoLongerThan(candidate, maxLength);

    assertFalse(isNotBlankAndNoLongerThanFive);
  }

  @Test
  public void
      ifPresentAndIsNotBlankAndNoLongerThan_AllGood_isPresentAndIsNotBlankAndNoLongerThanFive() {
    final var candidate = "abc";
    final var maxLength = 5;
    final var ifPresentAndIsNotBlankAndNoLongerThan =
        subject.ifPresentAndIsNotBlankAndNoLongerThan(candidate, maxLength);

    assertTrue(ifPresentAndIsNotBlankAndNoLongerThan);
  }

  @Test
  public void ifPresentAndIsNotBlankAndNoLongerThan_Empty_IsNotPresent() {
    final var candidate = "";
    final var maxLength = 5;
    final var ifPresentAndIsNotBlankAndNoLongerThan =
        subject.ifPresentAndIsNotBlankAndNoLongerThan(candidate, maxLength);

    assertTrue(ifPresentAndIsNotBlankAndNoLongerThan);
  }

  @Test
  public void ifPresentAndIsNotBlankAndNoLongerThan_Null_IsNotPresent() {
    final String candidate = null;
    final var maxLength = 5;
    final var ifPresentAndIsNotBlankAndNoLongerThan =
        subject.ifPresentAndIsNotBlankAndNoLongerThan(candidate, maxLength);

    assertTrue(ifPresentAndIsNotBlankAndNoLongerThan);
  }

  @Test
  public void isBlank_AllGood_isBlank() {
    final var candidate = "   ";
    final var isBlank = subject.isBlank(candidate);

    assertTrue(isBlank);
  }

  @Test
  public void isBlank_NotBlank_isNotBlank() {
    final var candidate = "abc";
    final var isBlank = subject.isBlank(candidate);

    assertFalse(isBlank);
  }

  @Test
  public void isNotBlank_AllGood_isBlank() {
    final var candidate = "   ";
    final var isNotBlank = subject.isNotBlank(candidate);

    assertFalse(isNotBlank);
  }

  @Test
  public void isNotBlank_NotBlank_isNotBlank() {
    final var candidate = "abc";
    final var isNotBlank = subject.isNotBlank(candidate);

    assertTrue(isNotBlank);
  }

  @Test
  public void isNotNull_Null_IsNull() {
    final Object candidate = null;
    final var isNotNull = subject.isNotNull(candidate);

    assertFalse(isNotNull);
  }

  @Test
  public void isNotNull_NotNull_IsNotNull() {
    final var candidate = "abc";
    final var isNotNull = subject.isNotNull(candidate);

    assertTrue(isNotNull);
  }

  @Test
  public void isValidUrl_AllGood_IsValidUrl() {
    final var candidate = "https://www.some-url.com";
    final var isValidUrl = subject.isValidUrl(candidate);

    assertTrue(isValidUrl);
  }

  @Test
  public void isValidUrl_Null_IsNotValidUrl() {
    final String candidate = null;
    final var isValidUrl = subject.isValidUrl(candidate);

    assertFalse(isValidUrl);
  }

  @Test
  public void isValidUrl_InvalidUrl_IsNotValidUrl() {
    final var candidate = "abc";
    final var isValidUrl = subject.isValidUrl(candidate);

    assertFalse(isValidUrl);
  }

  @Test
  public void isUuid_AllGood_IsUuid() {
    final var candidate = "d7088dc7-e7b0-462e-b200-24b5a15f122b";
    final var isUuid = subject.isUuid(candidate);

    assertTrue(isUuid);
  }

  @Test
  public void isUuid_Blank_IsNotUuid() {
    final var candidate = "   ";
    final var isUuid = subject.isUuid(candidate);

    assertFalse(isUuid);
  }

  @Test
  public void isUuid_InvalidUuid_IsNotUuid() {
    final var candidate = "abc";
    final var isUuid = subject.isUuid(candidate);

    assertFalse(isUuid);
  }

  @Test
  public void doesNotEqual_AllGood_doesNotEqual() {
    final var key = "abc";
    final var candidate = "def";
    final var doesNotEqual = subject.doesNotEqual(key, candidate);

    assertTrue(doesNotEqual);
  }

  @Test
  public void doesNotEqual_EqualStrings_equals() {
    final var key = "abc";
    final var candidate = "abc";
    final var doesNotEqual = subject.doesNotEqual(key, candidate);

    assertFalse(doesNotEqual);
  }

  @Test
  public void equals_AllGood_equals() {
    final var key = "abc";
    final var candidate = "abc";
    final var equals = subject.equals(key, candidate);

    assertTrue(equals);
  }

  @Test
  public void equals_UnequalStrings_doesNotEqual() {
    final var key = "abc";
    final var candidate = "def";
    final var equals = subject.equals(key, candidate);

    assertFalse(equals);
  }
}

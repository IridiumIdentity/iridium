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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.iridium.api.util.DateUtils;

@ExtendWith(MockitoExtension.class)
public class DateUtilsTest {

  @Mock private Calendar calendarInstanceMock;

  @Test
  public void addHoursToCurrentTime_AllGood_FiveHoursAdded() {
    try (MockedStatic<Calendar> calendarClassMock = Mockito.mockStatic(Calendar.class)) {
      when(calendarInstanceMock.getTime()).thenReturn(new Date());
      calendarClassMock.when(Calendar::getInstance).thenReturn(calendarInstanceMock);

      final var hours = 5;
      Date dateAfterAddingFiveHoursToCurrentTime = DateUtils.addHoursToCurrentTime(hours);

      verify(calendarInstanceMock).add(Calendar.HOUR_OF_DAY, hours);
      assertNotNull(dateAfterAddingFiveHoursToCurrentTime);
    }
  }

  @Test
  public void addMinutesToCurrentTime_AllGood_FiveMinutesAdded() {
    try (MockedStatic<Calendar> calendarClassMock = Mockito.mockStatic(Calendar.class)) {
      when(calendarInstanceMock.getTime()).thenReturn(new Date());
      calendarClassMock.when(Calendar::getInstance).thenReturn(calendarInstanceMock);

      final var minutes = 5;
      Date dateAfterAddingFiveMinutesToCurrentTime = DateUtils.addMinutesToCurrentTime(minutes);

      verify(calendarInstanceMock).add(Calendar.MINUTE, minutes);
      assertNotNull(dateAfterAddingFiveMinutesToCurrentTime);
    }
  }

  @Test
  public void addDaysToCurrentTime_AllGood_FiveDaysAdded() {
    try (MockedStatic<Calendar> calendarClassMock = Mockito.mockStatic(Calendar.class)) {
      when(calendarInstanceMock.getTime()).thenReturn(new Date());
      calendarClassMock.when(Calendar::getInstance).thenReturn(calendarInstanceMock);

      final var days = 5;
      Date dateAfterAddingFiveMinutesToCurrentTime = DateUtils.addDaysToCurrentTime(days);

      verify(calendarInstanceMock).add(Calendar.DATE, days);
      assertNotNull(dateAfterAddingFiveMinutesToCurrentTime);
    }
  }
}

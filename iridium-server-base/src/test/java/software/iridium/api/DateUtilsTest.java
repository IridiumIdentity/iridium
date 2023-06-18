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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.Test;
import software.iridium.api.util.DateUtils;

public class DateUtilsTest {

  @Test
  public void addHoursToCurrentTime_AllGood_FiveHoursAdded() {
    final var hours = 5;

    final var calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.HOUR_OF_DAY, hours);
    final var dateFiveHoursAhead = calendar.getTime();

    Date dateAfterAddingFiveHoursToCurrentTime = DateUtils.addHoursToCurrentTime(hours);

    assertTrue(dateAfterAddingFiveHoursToCurrentTime.getTime() >= dateFiveHoursAhead.getTime());
  }

  @Test
  public void addMinutesToCurrentTime_AllGood_FiveMinutesAdded() {
    final var minutes = 5;

    final var calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.MINUTE, minutes);
    final var dateFiveMinutesAhead = calendar.getTime();

    Date dateAfterAddingFiveMinutesToCurrentTime = DateUtils.addMinutesToCurrentTime(minutes);

    assertTrue(dateAfterAddingFiveMinutesToCurrentTime.getTime() >= dateFiveMinutesAhead.getTime());
  }
}

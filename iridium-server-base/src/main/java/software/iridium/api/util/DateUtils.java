/*
 *  Copyright (C) Josh Fischer - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Josh Fischer <josh@joshfischer.io>, 2023.
 */
package software.iridium.api.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

  public static Date addHoursToCurrentTime(final Integer hours) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR_OF_DAY, hours);
    return cal.getTime();
  }

  public static Date addMinutesToCurrentTime(final Integer minutes) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.MINUTE, minutes);
    return cal.getTime();
  }
}

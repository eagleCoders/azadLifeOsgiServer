/**
 * 
 */
package infrastructure.core.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import infrastructure.core.data.ApiParameterError;
import infrastructure.core.exception.PlatformApiDataValidationException;

/**
 * @author anees-ur-rehman
 *
 */
public class DateUtils {

	private DateUtils() {

    }

    public static ZoneId getDateTimeZoneOfTenant() {
//        final FineractPlatformTenant tenant = ThreadLocalContextUtil.getTenant();
        ZoneId zone = ZoneId.systemDefault();
//        if (tenant != null) {
//            zone = ZoneId.of(tenant.getTimezoneId());
//        }
        return zone;
    }

    public static TimeZone getTimeZoneOfTenant() {
//        final FineractPlatformTenant tenant = ThreadLocalContextUtil.getTenant();
//        TimeZone zone = null;
//        if (tenant != null) {
//            zone = TimeZone.getTimeZone(tenant.getTimezoneId());
//        }
//        return zone;
    	return null;
    }

    public static Date getDateOfTenant() {
        return Date.from(getLocalDateOfTenant().atStartOfDay(getDateTimeZoneOfTenant()).toInstant());
    }

    public static LocalDate getLocalDateOfTenant() {
        final ZoneId zone = getDateTimeZoneOfTenant();
        LocalDate today = LocalDate.now(zone);
        return today;
    }

    public static LocalDateTime getLocalDateTimeOfTenant() {
        final ZoneId zone = getDateTimeZoneOfTenant();
        LocalDateTime today = LocalDateTime.now(zone).truncatedTo(ChronoUnit.SECONDS);
        return today;
    }

    public static LocalDate parseLocalDate(final String stringDate, final String pattern) {

        try {
            final DateTimeFormatter dateStringFormat = DateTimeFormatter.ofPattern(pattern).withZone(getDateTimeZoneOfTenant());
            final ZonedDateTime dateTime = ZonedDateTime.parse(stringDate, dateStringFormat);
            return dateTime.toLocalDate();
        } catch (final IllegalArgumentException e) {
            final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
            final ApiParameterError error = ApiParameterError.parameterError("validation.msg.invalid.date.pattern",
                    "The parameter date (" + stringDate + ") is invalid w.r.t. pattern " + pattern, "date", stringDate, pattern);
            dataValidationErrors.add(error);
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors, e);
        }
    }

    public static String formatToSqlDate(final Date date) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(getTimeZoneOfTenant());
        final String formattedSqlDate = df.format(date);
        return formattedSqlDate;
    }

    public static boolean isDateInTheFuture(final LocalDate localDate) {
        return localDate.isAfter(getLocalDateOfTenant());
    }
}

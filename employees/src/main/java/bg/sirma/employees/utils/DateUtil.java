package bg.sirma.employees.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

	private static final String DATE_FORMAT_BASE_STRING = "dd/MM/yyyy";
	private static final String DATE_FORMAT_BASE_REVERSE_STRING = "MM/dd/yyyy";
	private static final String DATE_FORMAT_HYPEN_STRING = "yyyy-MM-dd";
	private static final String DATE_FORMAT_MONTH_DAY_YEAR_STRING = "MMM dd, yyyy";
	private static final String DATE_FORMAT_DATE_TIME_STRING = "dd/MM/yyyy HH:mm";
	private static final String DATE_FORMAT_COMPACT_STRING = "yyyyMMddHHmmss";

	public static List<DateTimeFormatter> getDateFormatters() {
		List<DateTimeFormatter> dateFormats = new ArrayList<>();
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_BASE_STRING));
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_HYPEN_STRING));
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_DAY_YEAR_STRING));
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_DATE_TIME_STRING));
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_COMPACT_STRING));
		dateFormats.add(DateTimeFormatter.ofPattern(DATE_FORMAT_BASE_REVERSE_STRING));
		return dateFormats;
	}

	private DateUtil() {
	}

	public static Date parseDate(String dateString) throws ParseException {
		 // Attempt to parse the date using each format
	    for (DateTimeFormatter formatter : getDateFormatters()) {
	        try {
	            LocalDate localDate = LocalDate.parse(dateString, formatter);
	            return java.sql.Date.valueOf(localDate);
	        } catch (DateTimeParseException ignored) {
	            // Try the next formatter if parsing fails
	        }
	    }

	    // If none of the formats work, throw an exception
	    throw new ParseException("Unable to parse the date: " + dateString, 0);
	}

}
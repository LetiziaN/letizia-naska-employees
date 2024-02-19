package bg.sirma.employees.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility functions for handling dates
 */
public class DateUtil {

	private static final String DATE_FORMAT_BASE_STRING = "dd/MM/yyyy";
	private static final String DATE_FORMAT_BASE_REVERSE_STRING = "MM/dd/yyyy";
	private static final String DATE_FORMAT_HYPEN_STRING = "yyyy-MM-dd";
	private static final String DATE_FORMAT_MONTH_DAY_YEAR_STRING = "MMM dd, yyyy";
	private static final String DATE_FORMAT_DATE_TIME_STRING = "dd/MM/yyyy HH:mm";
	private static final String DATE_FORMAT_COMPACT_STRING = "yyyyMMddHHmmss";

	private DateUtil() {
	}

	 public static Date parseDate(String dateString) throws ParseException {
	        // Define multiple date formats to handle different formats
	        SimpleDateFormat[] dateFormats = {
	                new SimpleDateFormat(DATE_FORMAT_BASE_STRING),
	                new SimpleDateFormat(DATE_FORMAT_HYPEN_STRING),
	                new SimpleDateFormat(DATE_FORMAT_MONTH_DAY_YEAR_STRING),
	                new SimpleDateFormat(DATE_FORMAT_DATE_TIME_STRING),
	                new SimpleDateFormat(DATE_FORMAT_COMPACT_STRING),
	                new SimpleDateFormat(DATE_FORMAT_BASE_REVERSE_STRING)

	        };

	        // Attempt to parse the date using each format21
	        for (SimpleDateFormat dateFormat : dateFormats) {
	            try {
	                return dateFormat.parse(dateString);
	            } catch (ParseException ignored) {
	                // If parsing fails with the current format, try the next one
	            }
	        }

	        // If none of the formats work, throw an exception
	        throw new ParseException("Unable to parse the date: " + dateString, 0);
	    }

}
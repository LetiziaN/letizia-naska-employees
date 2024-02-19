package bg.sirma.employees.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class FileUploadConstraints {
	public static final String ALLOWED_FILE_TYPES = "text/csv";
	public static final Integer MAX_FILE_SIZE = 50;
	public static final Integer FILE_SIZE_MULTIPLIER = 1024 * 1024;
	public static final String DATE_FORMAT_BASE_STRING = "dd/MM/yyyy";
	public static final String DATE_FORMAT_BASE_REVERSE_STRING = "MM/dd/yyyy";
	public static final String DATE_FORMAT_HYPEN_STRING = "yyyy-mm-dd";
	public static final String DATE_FORMAT_MONTH_DAY_YEAR_STRING = "MMM dd, yyyy";
	public static final String DATE_FORMAT_DATE_TIME_STRING = "dd/MM/yyyy HH:mm";
	public static final String DATE_FORMAT_COMPACT_STRING = "yyyyMMddHHmmss";

}

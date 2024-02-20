package bg.sirma.employees.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import bg.sirma.employees.beans.EmployeeData;
import bg.sirma.employees.beans.EmployeeResponse;

public interface EmployeeService {

	/**
	 * Reads employee data from a CSV file and returns a list of EmployeeData objects.
	 *
	 * @param file 	- The CSV file containing employee data.
	 * @return A list of EmployeeData objects parsed from the CSV file.
	 * @throws Exception If there is an error reading or parsing the CSV file.
	 */
	List<EmployeeData> readDataFromFile(MultipartFile file) throws Exception;
	
	/**
	 * Finds the longest working pair of employees on common projects.
	 *
	 * @param projects 	- A list of EmployeeData objects representing details related to projects worked on by employees.
	 * @return An EmployeeResponse object representing the longest working pair of employees and the number of days worked together.
	 * @throws Exception If there is an error while finding the longest working pair.
	 */
	EmployeeResponse findLongestWorkingPairs(List<EmployeeData> projects) throws Exception;

}

package bg.sirma.employees.services.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import bg.sirma.employees.beans.EmployeeData;
import bg.sirma.employees.beans.EmployeeResponse;
import bg.sirma.employees.constants.FileUploadConstraints;
import bg.sirma.employees.services.EmployeeService;
import bg.sirma.employees.utils.DateUtil;
import bg.sirma.employees.utils.FileUtil;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Override
	public List<EmployeeData> readDataFromFile(MultipartFile file) throws Exception {

		if (!FileUploadConstraints.ALLOWED_FILE_TYPES.equals(file.getContentType())) {
			throw new Exception("file type not allowed");
		}
		try {
			FileUtil.validateFile(file.getBytes(), FileUploadConstraints.MAX_FILE_SIZE,
					FileUploadConstraints.FILE_SIZE_MULTIPLIER, FileUploadConstraints.ALLOWED_FILE_TYPES);
		} catch (Exception ex) {
			throw new Exception("error uploading file ", ex);
		}
		List<EmployeeData> employeeDataList = new ArrayList<>();

		try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
			List<String[]> records = csvReader.readAll();
			if (!records.isEmpty()) {
				// Remove the first record, the headers rows
				records = records.subList(1, records.size());
			}

			for (String[] record : records) {
				EmployeeData employeeData = new EmployeeData();
				employeeData.setEmpId(Integer.parseInt(record[0]));
				employeeData.setProjectId(Integer.parseInt(record[1]));
				employeeData.setDateFrom(DateUtil.parseDate(record[2]));

				if (record[3] != null && !record[3].isBlank() && !record[3].isEmpty() && record[3] != "") {
					employeeData.setDateTo(DateUtil.parseDate(record[3]));
				} else {
					// Set DateTo as today if it is NULL
					employeeData.setDateTo(new Date());
				}

				employeeDataList.add(employeeData);
			}
		} catch (IOException | CsvException e) {
			throw new Exception("unable to map correctly data from csv");
		}

		return employeeDataList;
	}

	@Override
	public EmployeeResponse findLongestWorkingPairs(List<EmployeeData> empProjectList) throws Exception {
		Map<Integer, Map<Integer, Integer>> employeeProjects = new HashMap<>();

		for (EmployeeData projectData : empProjectList) {
			if (!employeeProjects.containsKey(projectData.getEmpId())) {
				// Create a new entry in the map with an empty HashMap
				employeeProjects.put(projectData.getEmpId(), new HashMap<>());
			}

			// Get the projects map for the current employee ID
			Map<Integer, Integer> projects = employeeProjects.get(projectData.getEmpId());

			if (!projects.containsKey(projectData.getProjectId())) {
				// Add the project ID to the map with 0 common days
				projects.put(projectData.getProjectId(), 0);
			}

			// Calculate the common days between the current project and existing projects for the employee
			int commonDays = calculateCommonDays(projects.get(projectData.getProjectId()), projectData);

			// Update the projects map with the maximum common days for the project ID
			projects.put(projectData.getProjectId(), Math.max(commonDays, projects.get(projectData.getProjectId())));
		}

		EmployeeResponse longestWorkingPair = null;
		int maxDaysWorked = 0;

		for (Integer empID1 : employeeProjects.keySet()) {
			for (Integer empID2 : employeeProjects.keySet()) {
				if (!empID1.equals(empID2)) {
					// Find the set of common project IDs between the two employees
					Set<Integer> commonProjects = new HashSet<>(employeeProjects.get(empID1).keySet());
					commonProjects.retainAll(employeeProjects.get(empID2).keySet());

					// Iterate over each common project ID
					for (Integer projectID : commonProjects) {
						// Calculate the common days worked on the project for both employees
						int commonDays = Math.min(employeeProjects.get(empID1).get(projectID),
								employeeProjects.get(empID2).get(projectID));

						// Check if the common days worked on the project is greater than the current
						// maximum
						if (commonDays > maxDaysWorked) {
							// If so, update the longest working pair and the maximum days worked
							maxDaysWorked = commonDays;
							longestWorkingPair = new EmployeeResponse(empID1, empID2, commonDays);
						}
					}
				}
			}
		}

		return longestWorkingPair;
	}

	/**
	 * Calculate the common days worked on a project by an employee.
	 *
	 * @param currentCommonDays The current total common days worked on all projects by the employee.
	 * @param newProjectData    The EmployeeData object representing the new project.
	 * @return The total common days worked on all projects by the employee including the new project.
	 */
	private int calculateCommonDays(int currentCommonDays, EmployeeData newProjectData) {
		// Determine the start date of the overlap period between the current project and the new project
	    Date overlapStart = newProjectData.getDateFrom().after(new Date()) ? new Date() : newProjectData.getDateFrom();

	    // Determine the end date of the overlap period (default to current date)
	    Date overlapEnd = new Date();

	    // If the new project has an end date and it is before the default end date, update the end date
	    if (newProjectData.getDateTo() != null && newProjectData.getDateTo().before(overlapEnd)) {
	        overlapEnd = newProjectData.getDateTo();
	    }

	    // Calculate the number of days in the overlap period
	    long overlapDays = Math.max(0, (overlapEnd.getTime() - overlapStart.getTime()) / (24 * 60 * 60 * 1000));

	    // Return the total common days worked on all projects by the employee including the new project
	    return currentCommonDays + (int) overlapDays;
	}
}

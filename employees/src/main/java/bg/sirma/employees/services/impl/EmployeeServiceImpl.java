package bg.sirma.employees.services.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	            employeeProjects.put(projectData.getEmpId(), new HashMap<>());
	        }

	        Map<Integer, Integer> projects = employeeProjects.get(projectData.getEmpId());

	        if (!projects.containsKey(projectData.getProjectId())) {
	            projects.put(projectData.getProjectId(), 0);
	        }

	        int commonDays = calculateCommonDays(projects.get(projectData.getProjectId()), projectData);
	        projects.put(projectData.getProjectId(), Math.max(commonDays, projects.get(projectData.getProjectId())));
	    }

	    EmployeeResponse longestWorkingPair = null;
	    int maxDaysWorked = 0;

	    for (Integer empID1 : employeeProjects.keySet()) {
	        for (Integer empID2 : employeeProjects.keySet()) {
	            if (!empID1.equals(empID2)) {
	                Set<Integer> commonProjects = new HashSet<>(employeeProjects.get(empID1).keySet());
	                commonProjects.retainAll(employeeProjects.get(empID2).keySet());

	                for (Integer projectID : commonProjects) {
	                    int commonDays = Math.min(employeeProjects.get(empID1).get(projectID),
	                            employeeProjects.get(empID2).get(projectID));

	                    if (commonDays > maxDaysWorked) {
	                        maxDaysWorked = commonDays;
	                        longestWorkingPair = new EmployeeResponse(empID1, empID2, commonDays);
	                    }
	                }
	            }
	        }
	    }

	    return longestWorkingPair;
	}

	private int calculateCommonDays(int currentCommonDays, EmployeeData newProjectData) {
		Date overlapStart = newProjectData.getDateFrom().after(new Date()) ? new Date() : newProjectData.getDateFrom();
		Date overlapEnd = new Date();

		if (newProjectData.getDateTo() != null && newProjectData.getDateTo().before(overlapEnd)) {
			overlapEnd = newProjectData.getDateTo();
		}

		long overlapDays = Math.max(0, (overlapEnd.getTime() - overlapStart.getTime()) / (24 * 60 * 60 * 1000));

		return currentCommonDays + (int) overlapDays;
	}
}

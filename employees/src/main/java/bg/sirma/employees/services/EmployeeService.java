package bg.sirma.employees.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import bg.sirma.employees.beans.EmployeeData;
import bg.sirma.employees.beans.EmployeeResponse;

public interface EmployeeService {

	List<EmployeeData> readDataFromFile(MultipartFile file) throws Exception;

	EmployeeResponse findLongestWorkingPairs(List<EmployeeData> projects) throws Exception;

}

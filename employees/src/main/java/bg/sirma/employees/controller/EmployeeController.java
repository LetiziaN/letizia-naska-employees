package bg.sirma.employees.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bg.sirma.employees.beans.EmployeeData;
import bg.sirma.employees.beans.EmployeeResponse;
import bg.sirma.employees.services.EmployeeService;

@CrossOrigin
@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EmployeeResponse> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
		 try {
	            List<EmployeeData> employeeDataList = employeeService.readDataFromFile(file);
	            EmployeeResponse longestWorkingPair = employeeService.findLongestWorkingPairs(employeeDataList);
	            return ResponseEntity.ok(longestWorkingPair);
	        } catch (Exception e) {
	            throw new Exception("Failed to process file: " + e.getMessage());
	        }
	}
}

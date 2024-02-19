package bg.sirma.employees.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeResponse {

	private int firstEmpId;
	private int secondEmpId;
	private int daysWorked;
}

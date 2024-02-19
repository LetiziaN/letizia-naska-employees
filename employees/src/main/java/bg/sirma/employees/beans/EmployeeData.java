package bg.sirma.employees.beans;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeData {
	private int empId;
    private int projectId;
    private Date dateFrom;
    private Date dateTo;
}

package za.co.zensar.cloud.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.zensar.cloud.employee.model.Employee;

import java.util.List;

/**
 * Created by abhishek9 on 12/09/2018.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List findByDepartmentId(Long departmentId);
    List findByOrganizationId(Long organizationId);
}

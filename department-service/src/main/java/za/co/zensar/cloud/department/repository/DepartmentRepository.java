package za.co.zensar.cloud.department.repository;

import org.springframework.stereotype.Repository;
import za.co.zensar.cloud.department.model.Department;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek9 on 13/09/2018.
 */
@Repository
public class DepartmentRepository {

    private static final List<Department> departments = new ArrayList<>();

    static {
        departments.add(new Department(1001L, 101L, "Zensar Development"));
        departments.add(new Department(1002L, 101L, "Zensar Testing"));
        departments.add(new Department(1003L, 101L, "Zensar DevOps"));
    }

    public Department findById(Long id) {
        for(Department department : departments){
            if(department.getId().equals(id)){
                return department;
            }
        }
        return null;
    }

    public List<Department> findAll() {
        return departments;
    }

    public List<Department> findByOrganization(Long organizationId) {
        List<Department> departmentss = new ArrayList<>();
        for(Department department : departments){
            if(department.getOrganizationId().equals(organizationId)){
                departmentss.add(department);
            }
        }
        return departmentss;
    }

    public Department add(Department department) {
        department.setId((long)departments.size() + 1);
        departments.add(department);
        return department;
    }
}

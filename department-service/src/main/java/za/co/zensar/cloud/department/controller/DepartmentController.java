package za.co.zensar.cloud.department.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.zensar.cloud.department.client.EmployeeClient;
import za.co.zensar.cloud.department.model.Department;
import za.co.zensar.cloud.department.repository.DepartmentRepository;

import java.io.IOException;
import java.util.List;

/**
 * Created by abhishek9 on 13/09/2018.
 */
@RestController
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    DepartmentRepository repository;

    @Autowired
    EmployeeClient employeeClient;

    @PostMapping("/")
    public Department add(@RequestBody Department department) {
        LOGGER.info("Department add: {}", department);
        return repository.add(department);
    }

    @GetMapping("/{id}")
    public Department findById(@PathVariable("id") Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GetMapping("/")
    public List<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @GetMapping("/organization/{organizationId}")
    public List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @GetMapping("/organization/{organizationId}/with-employees")
    public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) throws IOException {
        LOGGER.info("Department find: organizationId={}", organizationId);
        List<Department> departments = repository.findByOrganization(organizationId);
        for(Department department : departments){
            department.setEmployees(employeeClient.findByDepartmentFeign(department.getId()));
        }

        return departments;
    }

    @RequestMapping(value = "/message")
    public String getMessage(){
        String s = "Hey Dude, I am running as a Micro service and calling another Micro service.... Awesome.. haaaa... :)";
        return s;
    }


}

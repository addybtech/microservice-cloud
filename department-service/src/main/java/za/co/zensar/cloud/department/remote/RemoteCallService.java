package za.co.zensar.cloud.department.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.zensar.cloud.department.model.Employee;

import java.util.List;

/**
 * Created by abhishek9 on 14/09/2018.
 */
//@FeignClient(name="employee-service")
@FeignClient(name="zuul-service")
public interface RemoteCallService {

    @RequestMapping(method = RequestMethod.GET, value = "employeeProducer/department/{departmentId}")
    public List<Employee> getData(@PathVariable("departmentId") Long departmentId);

}

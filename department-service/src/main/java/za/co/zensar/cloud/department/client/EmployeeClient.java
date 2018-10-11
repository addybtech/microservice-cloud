package za.co.zensar.cloud.department.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import za.co.zensar.cloud.department.model.Employee;
import za.co.zensar.cloud.department.remote.RemoteCallService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek9 on 13/09/2018.
 */
@Component
public class EmployeeClient {

    // For Direct call to employee without eureka server
    //@Value("${employee.service}")
    @Value("${employee.application.name}")
    private String employeeServiceUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //Direct call to employee without eureka server
    public List<Employee> findByDepartment(Long departmentId) throws RestClientException, IOException {

        List<Employee> employees = new ArrayList<>();

        employeeServiceUrl = employeeServiceUrl + "department/" + departmentId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(employeeServiceUrl, HttpMethod.GET, getHeaders(),String.class);
        }catch (Exception ex){
            System.out.println(ex);
        }

        if (response != null){
            String body = response.getBody();

            try {
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
                employees = objectMapper.readValue(body, new TypeReference<ArrayList<Employee>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return employees;
    }

    private static HttpEntity<?> getHeaders() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }


    //---------------------------------------------------------------------------------------------

//    with Discovery client means single instance of employee producer
    //@Autowired
    private DiscoveryClient discoveryClient;

//    with Load balancer client means multiple instance of employee producer on different-2 port
//    @Autowired
//    private LoadBalancerClient loadBalancer;

    public List<Employee> findByDepartmentEureka(Long departmentId) throws RestClientException, IOException {

//        with Load balancer client means multiple instance of employee producer on different-2 port
//        ServiceInstance serviceInstance=loadBalancer.choose(employeeServiceUrl);
//        System.out.println(serviceInstance.getUri());
//        String employeeServiceUrl = serviceInstance.getUri().toString();

//        with Discovery client means single instance of employee producer
        List<ServiceInstance> instances=discoveryClient.getInstances(employeeServiceUrl);
        ServiceInstance serviceInstance = instances.get(0);
        String employeeServiceUrl = serviceInstance.getUri().toString();

        employeeServiceUrl = employeeServiceUrl + "department/" + departmentId;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try{
            response = restTemplate.exchange(employeeServiceUrl, HttpMethod.GET, getHeaders(),String.class);
        }catch (Exception ex){
            System.out.println(ex);
        }

        List<Employee> employees = new ArrayList<>();

        if (response != null){
            String body = response.getBody();

            try {
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
                employees = objectMapper.readValue(body, new TypeReference<ArrayList<Employee>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return employees;
    }


    //-----------------------------------------------------------------------------------------------------------

    //for feign client - used for short coding
    @Autowired
    private RemoteCallService loadBalancer;


    public List<Employee> findByDepartmentFeign(Long departmentId) throws RestClientException, IOException {
        List<Employee> employees = new ArrayList<>();
        try{
            employees = loadBalancer.getData(departmentId);
            return employees;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

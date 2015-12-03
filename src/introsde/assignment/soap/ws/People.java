package introsde.assignment.soap.ws;
import introsde.assignment.soap.model.HealthProfile;
import introsde.assignment.soap.model.HealthProfileHistory;
import introsde.assignment.soap.model.MeasureType;
import introsde.assignment.soap.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface People {
    
	// Method #1
	@WebMethod(operationName="readPersonList")
    @WebResult(name="people") 
    public List<Person> readPersonList();
	
	// Method #2
	@WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="id") Long id);
	
	// Method #3
	@WebMethod(operationName="updatePerson")
    @WebResult(name="id") 
    public Long updatePerson(@WebParam(name="person") Person person);

	// Method #4
    @WebMethod(operationName="createPerson")
    @WebResult(name="id") 
    public Person createPerson(@WebParam(name="person") Person person);

    // Method #5
    @WebMethod(operationName="deletePerson")
    @WebResult(name="id") 
    public boolean deletePerson(@WebParam(name="id") Long id);

    // Method #6
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="history") 
    public List<HealthProfileHistory> readPersonHistory(@WebParam(name="id") Long id, @WebParam(name="measureType") String measureType);
    
    // Method #7
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes") 
    public List<MeasureType> readMeasureTypes();
    
    // Method #8
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure") 
    public String readPersonMeasure(@WebParam(name="id") Long id, @WebParam(name="measureType") String measureType, @WebParam(name="mid") Long mid);
    
    // Method #9
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="measure") 
    public HealthProfile savePersonMeasure(@WebParam(name="id") Long id, @WebParam(name="healthProfile") HealthProfile newMeasure); 
    
    // Method #10
    @WebMethod(operationName="updatePersonMeasure")
    @WebResult(name="measureId") 
    public Long updatePersonMeasure(@WebParam(name="id") Long id, @WebParam(name="healthProfile") HealthProfileHistory measure);
}
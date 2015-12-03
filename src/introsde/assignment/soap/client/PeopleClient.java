package introsde.assignment.soap.client;

import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import introsde.assignment.soap.ws.People;
import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.model.HealthProfile;
import introsde.assignment.soap.model.HealthProfileHistory;
import introsde.assignment.soap.model.MeasureType;

public class PeopleClient {
	
	static PrintWriter logWriter;

    public static void main(String[] args) throws Exception {
    	// Define writer
    	logWriter = new PrintWriter("client.log", "UTF-8");

    	
    	String wsdl = "http://10.196.212.126:6900/ws/people?wsdl";
    	printAndLog(wsdl);

    	// New people service
    	URL url = new URL(wsdl);
        QName qname = new QName("http://ws.soap.assignment.introsde/", "PeopleService");
        Service service = Service.create(url, qname);

        People people = service.getPort(People.class);
        
        // METHOD #1
        printAndLog("\nMethod #1: readPerson(56)");
        Person person = people.readPerson(new Long(56));
        printAndLog("Result ==> Person: " + personToString(person));
        
        // METHOD #2
        printAndLog("\nMethod #2: readPersonList()");
        List<Person> pList = people.readPersonList();
        printAndLog("Result ==> Found " + pList.size() + " people:");
        for (Person p : pList) {
        	printAndLog("\t" + personToString(p));
        }
        
        // METHOD #3
        printAndLog("\nMethod #3: updatePerson(person) [firstname: TEST NEW NAME]");
        person.setFirstname("TEST NEW NAME");
        Long personId = people.updatePerson(person);
        Person testPerson = people.readPerson(personId);
        printAndLog("Result ==> Person: " + personToString(testPerson));
        
        // METHOD #4
        printAndLog("\nMethod #4: createPerson(newPerson) [firstname: Chuck]");
        
        // Create new person
        Person newPerson = new Person();
        newPerson.setFirstname("Chuck");
        newPerson.setLastname("Norris");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        newPerson.setBirthdate(sdf.parse("1960/01/01"));
        newPerson.setEmail("chucknorris@gmail.com");
        
        // Add health profile
        HealthProfile measure = new HealthProfile();
        measure.setMeasureType("height");
        measure.setMeasureValue("175");
        measure.setMeasureValueType("cm");
        List<HealthProfile> hp = new ArrayList<HealthProfile>();
        hp.add(measure);
        newPerson.setHealthProfile(hp);
        
        newPerson = people.createPerson(newPerson);
        printAndLog("Result ==> Person: " + personToString(newPerson));
        
        // METHOD #5
        personId = newPerson.getId();
        printAndLog("\nMethod #5: deletePerson(" + personId + ")");
        people.deletePerson(personId);
        Person deletedPerson = people.readPerson(personId);
        printAndLog("Result ==> Person with id " + personId +": " + personToString(deletedPerson));
        
        // METHOD #6
        printAndLog("\nMethod #6: readPersonHistory(" + person.getId() + ", 'weight')");
        List<HealthProfileHistory> hpHistory = people.readPersonHistory(person.getId(), "weight");
        printAndLog("Result ==> Found " + hpHistory.size() + " measures:");
        HealthProfileHistory measureExample = new HealthProfileHistory();
        for (HealthProfileHistory hMeasure : hpHistory) {
        	printAndLog("\t" + healthProfileHistoryToString(hMeasure));
        	// Save measure for method 8
        	measureExample = hMeasure;
        }
        
        // METHOD #7
        printAndLog("\nMethod #7: readMeasureTypes()");
        List<MeasureType> measures = people.readMeasureTypes();
        printAndLog("Result ==> Found " + measures.size() + " measure types:");
        for (MeasureType m : measures) {
        	printAndLog("\t" + m.getName());
        }
        
        // METHOD #8
        printAndLog("\nMethod #8: readPersonMeasure(" + person.getId() + ", weight, " + measureExample.getId() + ")");
        String value = people.readPersonMeasure(person.getId(), "weight", measureExample.getId());
        printAndLog("Result ==> MeasureID = " + measureExample.getId() + ", weight: " + value);
        
        // METHOD #9
        printAndLog("\nMethod #9: savePersonMeasure(" + person.getId() + ", newMeasure) [height: 180 cm]");
        HealthProfile newMeasure = new HealthProfile();
        newMeasure.setMeasureType("height");
        newMeasure.setMeasureValue("180");
        newMeasure.setMeasureValueType("cm");
        newMeasure.setCreated(new Date());
        
        HealthProfile returnedMeasure = people.savePersonMeasure(person.getId(), newMeasure);
        printAndLog("Result ==> MeasureID = " + returnedMeasure.getId()
        	+ ", " + returnedMeasure.getMeasureType() + ": " + returnedMeasure.getMeasureValue());
        
        // METHOD #10
        printAndLog("\nMethod #10: updatePersonMeasure(" + person.getId() + ", measure) [value: 100]");
        measureExample.setMeasureValue("100");
        Long measureId = people.updatePersonMeasure(person.getId(), measureExample);
        String measureValue = people.readPersonMeasure(person.getId(), "weight", measureId);
        printAndLog("Result ==> MeasureID = " + measureId + ", weight: " + measureValue);
    }
    
    /**
     * Helper method to stringify person
     * @param p
     * @return
     */
    private static String personToString(Person p) {
    	if (p == null) {
    		return "NULL";
    	}

    	return p.getId() + " " + p.getFirstname() + " " + p.getLastname();
    }
    
    /**
     * Helper method to stringify health profile history
     * @param measure
     * @return
     */
    private static String healthProfileHistoryToString(HealthProfileHistory measure) {
    	if (measure == null) {
    		return "NULL";
    	}
    	
    	return measure.getMeasureType() + ": " + measure.getMeasureValue() + " " + measure.getMeasureValueType();
    }
    
    /**
     * Helper method to print to console and write to log
     * @param message
     */
    private static void printAndLog(String message) {
    	System.out.println(message);
    	logWriter.println(message);
    }
    
}
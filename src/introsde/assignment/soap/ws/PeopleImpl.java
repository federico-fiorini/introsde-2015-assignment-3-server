package introsde.assignment.soap.ws;
import introsde.assignment.soap.model.HealthProfile;
import introsde.assignment.soap.model.HealthProfileHistory;
import introsde.assignment.soap.model.MeasureType;
import introsde.assignment.soap.model.Person;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "introsde.assignment.soap.ws.People",
    serviceName="PeopleService")
public class PeopleImpl implements People {

	@Override
    public List<Person> readPersonList() {
		System.out.println("---> Reading all Persons");

		List<Person> pList = Person.getAll();
		System.out.println("---> Found " + pList.size() + " Persons");
		
		return pList;
    }

    @Override
    public Person readPerson(Long id) {
        System.out.println("---> Reading Person by id = "+id);

        return Person.getPersonById(id);
    }
    
    @Override
    public Long updatePerson(Person person) {
    	System.out.println("---> Update Person by id = " + person.getId());
    	
    	// Update only person info
    	person.setHealthProfile(null);
    	person.updatePerson(person);
    
        return person.getId();
    }   

    @Override
    public Person createPerson(Person person) {
    	System.out.println("---> Create new Person");

        return Person.savePerson(person);
    }

    @Override
    public boolean deletePerson(Long id) {
    	System.out.println("---> Deleting Person by id = " + id);

        Person p = Person.getPersonById(id);
        
        if (p != null) {
            Person.deletePerson(p);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<HealthProfileHistory> readPersonHistory(Long id, String measureType) {
    	System.out.println("---> Reading person " + id + " " + measureType + " history");

    	// Get person
    	Person person = Person.getPersonById(id);

    	// Return history
    	return HealthProfileHistory.getHistoryByPersonAndMeasureType(person, measureType);
    }
    
    @Override
    public List<MeasureType> readMeasureTypes() {
    	System.out.println("---> Reading all measure types");
    	
    	return MeasureType.getAll();
    }
    
    @Override
    public String readPersonMeasure(Long id, String measureType, Long mid) {
    	System.out.println("---> Reading person " + id + " " + measureType + " measure by id = " + mid);
    	
    	// Get person
    	Person person = Person.getPersonById(id);
    	HealthProfileHistory hph = HealthProfileHistory.getMeasureById(person, measureType, mid);
    	
    	// Return measure value
    	return hph.getMeasureValue();
    	
    }
    
    @Override
    public HealthProfile savePersonMeasure(Long id, HealthProfile newMeasure) {
    	// Get person
    	Person person = Person.getPersonById(id);
    	
        newMeasure.setPerson(person);
        
        System.out.println("---> Creating/updating measure for person " + id);

        HealthProfile current = new HealthProfile();
        Long currentId = null;

        // Get current measure
        current = HealthProfile.getMeasureByType(person, newMeasure.getMeasureType());
        if (current != null)
        	currentId = current.getId();

        // Update current measure or create new one
        current = HealthProfile.updateHealthProfile(currentId, newMeasure);
        
        // Copy updated measure to history
        HealthProfileHistory.copyHealthProfileToHistory(current);

    	return current;
    }
    
    @Override
    public Long updatePersonMeasure(Long id, HealthProfileHistory measure) {
    	// Get person
    	Person person = Person.getPersonById(id);

    	// Get current measure
    	HealthProfileHistory hp = HealthProfileHistory.getMeasureById(person, measure.getMeasureType(), measure.getId());
    	
    	// Set new value
    	hp.setMeasureValue(measure.getMeasureValue());
    	HealthProfileHistory.updateHealthProfileHistory(hp);
    	
    	return hp.getId();
    }

}
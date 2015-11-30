package introsde.assignment.soap.ws;
import introsde.assignment.soap.model.HealthProfile;
import introsde.assignment.soap.model.Person;

import java.util.List;

import javax.jws.WebService;

//Service Implementation

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

        Person p = Person.getPersonById(id);
        if (p != null) {
            System.out.println("---> Found Person by id = "+id+" => "+p.getFirstname());
        } else {
            System.out.println("---> Person not found ");
        }

        return p;
    }
    
    @Override
    public Long updatePerson(Person person) {
    	if (person == null) {
    		System.out.println("Person is null");
    	}
    	person.updatePerson(person);
        return person.getId();
    }   

    @Override
    public Person createPerson(Person person) {
        return Person.savePerson(person);
    }

    @Override
    public boolean deletePerson(Long id) {
        Person p = Person.getPersonById(id);
        
        if (p != null) {
            Person.deletePerson(p);
            return true;
        } else {
            return false;
        }
    }

//    @Override
//    public int updatePersonMeasure(int id, HealthProfile hp) {
//    	HealthProfile ls = LifeStatus.getLifeStatusById(hp.getIdMeasure());
//        if (ls.getPerson().getIdPerson() == id) {
//            LifeStatus.updateLifeStatus(hp);
//            return hp.getIdMeasure();
//        } else {
//            return -1;
//        }
//    }

}
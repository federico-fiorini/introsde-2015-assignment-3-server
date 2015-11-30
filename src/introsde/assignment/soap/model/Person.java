package introsde.assignment.soap.model;

import introsde.assignment.soap.dao.LifeStyleDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name="person") 
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement(name="person")
@XmlType(propOrder={"id","firstname","lastname", "email", "birthdate", "healthProfile"})
public class Person implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="person")
    @Column(name="id")
    private Long id;

    @Column(name="firstname")
    private String firstname;
    

    @Column(name="lastname")
    private String lastname;

    @Temporal(TemporalType.DATE)
    @Column(name="birthdate")
    private Date birthdate; 

    @Column(name="email")
    private String email;
    
    @OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<HealthProfile> healthProfile;

    @OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<HealthProfileHistory> healthProfileHistory;
    
    /**
     * Get person id
     * @return
     */
    public Long getId(){
        return id;
    }

    /**
     * Get firstname
     */
    public String getFirstname(){
        return firstname;
    }

    /**
     * Get lastname
     * @return
     */
    public String getLastname(){
        return lastname;
    }

    /**
     * Get birthdate
     * @return
     */
    public Date getBirthdate(){
        return birthdate;
    }

    /**
     * Get email
     * @return
     */
    public String getEmail(){
        return email;
    }

    @XmlElementWrapper(name="healthProfile")
    @XmlElement(name="measure")
    public List<HealthProfile> getHealthProfile() {
        return this.healthProfile;
    }

    @XmlTransient
    public List<HealthProfileHistory> getMeasureHistory() {
        return this.healthProfileHistory;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * Set firstname
     * @param firstname
     */
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    /**
     * Set lastname
     * @param lastname
     */
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    
    /**
     * Set birthdate
     * @param birthdate
     */
    public void setBirthdate(Date birthdate){
        this.birthdate = birthdate;
    }

    /**
     * Set email
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Set healthProfile
     * @param healthProfile
     */
    public void setHealthProfile(List<HealthProfile> healthProfile) {
        this.healthProfile = healthProfile;
    }

    /**
     * Set healthProfileHistory
     * @param healthProfileHistory
     */
    public void setHealthProfileHistory(List<HealthProfileHistory> healthProfileHistory) {
        this.healthProfileHistory = healthProfileHistory;
    }

    /**
     * Find person by id
     * @param personId
     * @return
     */
    public static Person getPersonById(Long personId) {
        EntityManager em = LifeStyleDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        if (p != null)
        	em.refresh(p);

        LifeStyleDao.instance.closeConnections(em);

        return p;
    }

    /**
     * Find all people
     * @return
     */
    public static List<Person> getAll() {
        EntityManager em = LifeStyleDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
        LifeStyleDao.instance.closeConnections(em);

        return list;
    }

    /**
     * Save new person
     * @param p
     * @return
     */
    public static Person savePerson(Person p) {

    	if (p.healthProfile != null) {
    		// Cascade health profile and add to history
    		Person.cascadeHealthProfile(p);
    		Person.copyHealthProfileToHistory(p);
    	}

        EntityManager em = LifeStyleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeStyleDao.instance.closeConnections(em);
        return p;
    } 

    /**
     * Update person
     * @param p
     * @return
     */
    public Person updatePerson(Person p) {
    	
    	// Call setters
    	if (p.birthdate != null) {
    		this.setBirthdate(p.birthdate);
    	}
    	if (p.email != null) {
    		this.setEmail(p.email);
    	}
    	if (p.firstname != null) {
    		this.setFirstname(p.getFirstname());
    	}
    	if (p.lastname != null) {
    		this.setLastname(p.getLastname());
    	}

        EntityManager em = LifeStyleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p = em.merge(this);
        tx.commit();
        LifeStyleDao.instance.closeConnections(em);

        return p;
    }

    /**
     * Delete person
     * @param p
     */
    public static void deletePerson(Person p) {
        EntityManager em = LifeStyleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p = em.merge(p);
        em.remove(p);
        tx.commit();
        LifeStyleDao.instance.closeConnections(em);
    }

    /**
     * Helper method to cascade person to health profile
     */
    private static Person cascadeHealthProfile(Person p) {
    	// Get current timestamp
    	java.util.Date date = new java.util.Date();

    	// For each health profile measure: set person id and current timestamp
    	for (HealthProfile healthProfile : p.healthProfile) {
    		healthProfile.setPerson(p);
    		healthProfile.setCreated(date);
    	}

    	return p;
    }

    private static Person copyHealthProfileToHistory(Person p) {
    	// Create new history list
    	List<HealthProfileHistory> history = new ArrayList<HealthProfileHistory>();
    	
    	for (HealthProfile healthProfile : p.healthProfile) {
    		// Create new history object
    		HealthProfileHistory h = new HealthProfileHistory();
    		h.setMeasureType(healthProfile.getMeasureType());
    		h.setMeasureValue(healthProfile.getMeasureValue());
    		h.setPerson(p);
    		h.setTimestamp(healthProfile.getTimestamp());

    		// Add to history list
    		history.add(h);
    	}
    	
    	p.setHealthProfileHistory(history);
    	return p;
    }
}
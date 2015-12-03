package introsde.assignment.soap.model;

import introsde.assignment.soap.dao.LifeStyleDao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="health_profile") 
@NamedQuery(name="HealthProfile.findByPersonAndType",
			query="SELECT h FROM HealthProfile h "
				+ "WHERE h.person = :person AND h.measureType = :measureType")
public class HealthProfile implements Serializable {

	private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(generator="sqlite_healthProfile")
	@TableGenerator(name="sqlite_healthProfile", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="health_profile")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name="person_id", referencedColumnName="id")
	private Person person;

	@Column(name = "measure_type")
	private String measureType;

	@Column(name="measure_value")
	private String measureValue;
	
	@Column(name="measure_value_type")
	private String measureValueType;

	@Temporal(TemporalType.DATE)
    @Column(name="timestamp")
    private Date timestamp; 
	
	/**
	 * Get id
	 * @return
	 */
	@XmlElement(name="mid")
	public Long getId() {
		return this.id;
	}

	/**
	 * Get person
	 * @return
	 */
	@XmlTransient
	public Person getPerson() {
		return this.person;
	}

	/**
	 * Get measureType
	 * @return
	 */
	@XmlElement(name="type")
	public String getMeasureType() {
		return this.measureType;
	}

	/**
	 * Get measureValue
	 * @return
	 */
	@XmlElement(name="value")
	public String getMeasureValue() {
		return this.measureValue;
	}
	
	/**
	 * Get measureValueType
	 * @return
	 */
	@XmlElement(name="valueType")
	public String getMeasureValueType() {
		return this.measureValueType;
	}

	/**
	 * Get timestamp
	 * @return
	 */
	@XmlElement(name="created")
	public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Set id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Set person
	 * @param person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * Set measureType
	 * @param measureType
	 */
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}

	/**
	 * Set measureValue
	 * @param measureValue
	 */
	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}
	
	/**
	 * Set measureValueType
	 * @param measureValue
	 */
	public void setMeasureValueType(String measureValueType) {
		this.measureValueType = measureValueType;
	}

	/**
	 * Set timestamp
	 * @param timestamp
	 */
	public void setCreated(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
     * Find measure by person id and measure type
     * @return
     */
    public static HealthProfile getMeasureByType(Person p, String measureType) {
        EntityManager em = LifeStyleDao.instance.createEntityManager();

        HealthProfile measure = new HealthProfile();
        try {
		    measure = em.createNamedQuery(
		    		"HealthProfile.findByPersonAndType",
		    		HealthProfile.class)
		    	.setParameter("person", p)
		    	.setParameter("measureType", measureType)
		    	.getSingleResult();
        } catch (Exception e) {
        	return null;
        }
        
        LifeStyleDao.instance.closeConnections(em);

        return measure;
    }

    /**
     * Update health profile
     * @param h
     * @return
     */
    public static HealthProfile updateHealthProfile(Long currentId, HealthProfile newMeasure) {

    	// Get current timestamp
    	java.util.Date date = new java.util.Date();

    	EntityManager em = LifeStyleDao.instance.createEntityManager();
    	EntityTransaction tx = em.getTransaction();
    	HealthProfile newHealthProfile = new HealthProfile();
    	
    	if (currentId == null) { // Persist new measure
    		
    		newMeasure.setCreated(date);
    		tx.begin();
    		newHealthProfile = em.merge(newMeasure);
            tx.commit();

    	} else { // Update current measure
    		
    		HealthProfile healthProfile = em.find(HealthProfile.class, currentId);
            tx.begin();
            healthProfile.setMeasureValue(newMeasure.getMeasureValue());
            healthProfile.setCreated(date);
            tx.commit();
            newHealthProfile = healthProfile;
    	}
    	
        LifeStyleDao.instance.closeConnections(em);
        
        return newHealthProfile;
    }
}

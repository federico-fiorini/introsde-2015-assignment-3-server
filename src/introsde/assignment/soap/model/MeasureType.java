package introsde.assignment.soap.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlTransient;

import introsde.assignment.soap.dao.LifeStyleDao;

@Entity
@Table(name="measure_type") 
@NamedQuery(name="MeasureType.findAll", query="SELECT m FROM MeasureType m")
@XmlRootElement(name="measureType")
public class MeasureType implements Serializable {

	private static final long serialVersionUID = 4L;
	
	@Id
	@GeneratedValue(generator="sqlite_measure_type")
	@TableGenerator(name="sqlite_measure_type", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="measure_type")
	@Column(name = "id")
	private int id;

	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;

	/**
	 * Get name
	 * @return
	 */
	@XmlValue
	public String getName() {
		return this.name;
	}

	/**
	 * Set name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get description
	 * @return
	 */
	@XmlTransient
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * Find all supported measure
     * @return
     */
    public static List<MeasureType> getAll() {
    	
        EntityManager em = LifeStyleDao.instance.createEntityManager();
        List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class).getResultList();
        LifeStyleDao.instance.closeConnections(em);
    
        return list;
    }
}

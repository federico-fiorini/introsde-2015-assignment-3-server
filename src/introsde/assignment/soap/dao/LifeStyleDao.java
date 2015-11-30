package introsde.assignment.soap.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public enum LifeStyleDao {

    instance;

    private EntityManagerFactory emf;

    /**
     * Create entity manager factory
     */
    private LifeStyleDao() {

        if (emf != null) {
            emf.close();
        }
        
        try {
        	emf = Persistence.createEntityManagerFactory("lifestyle-jpa");
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	e.printStackTrace();
        	emf = null;
        }
    }

    /**
     * Create entity manager
     * @return
     */
    public EntityManager createEntityManager() {
    	
        try {
            return emf.createEntityManager();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return null;    
    }

    /**
     * Close connection
     * @param em
     */
    public void closeConnections(EntityManager em) {
        em.close();
    }

    /**
     * Return entity transaction
     * @param em
     * @return
     */
    public EntityTransaction getTransaction(EntityManager em) {
        return em.getTransaction();
    }

    /**
     * Return entity manager factory
     * @return
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }  
}
/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.session;

import com.mycompany.entity.UserTable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author cloud
 */
@Stateless
public class UserTableFacade extends AbstractFacade<UserTable> {

    @PersistenceContext(unitName = "com.mycompany_Ryde_war_1.0PU")
    private final EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_Ryde_war_1.0PU").createEntityManager();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTableFacade() {
        super(UserTable.class);
    }
    
    public UserTable findByName(String firstName, String lastName) {
        if (em.createQuery("SELECT u FROM UserTable u WHERE u.firstName = :firstName AND u.lastName = :lastName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return em.createQuery("SELECT u FROM UserTable u WHERE u.firstName = :firstName AND u.lastName = :lastName", UserTable.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
        }
    }
    
}

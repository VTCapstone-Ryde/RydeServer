/*
 * Created by Joe Fletcher on 2016.04.02  * 
 * Copyright © 2016 Joe Fletcher. All rights reserved. * 
 */
package com.mycompany.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author cloud
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mycompany.service.AuthenticationFilter.class);
        resources.add(com.mycompany.service.EventFacadeREST.class);
        resources.add(com.mycompany.service.GroupTableFacadeREST.class);
        resources.add(com.mycompany.service.GroupTimeslotFacadeREST.class);
        resources.add(com.mycompany.service.GroupUserFacadeREST.class);
        resources.add(com.mycompany.service.RideFacadeREST.class);
        resources.add(com.mycompany.service.TimeslotTableFacadeREST.class);
        resources.add(com.mycompany.service.TimeslotUserFacadeREST.class);
        resources.add(com.mycompany.service.UserTableFacadeREST.class);
    }
    
}

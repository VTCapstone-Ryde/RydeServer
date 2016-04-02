/*
 * Created by Shawn Amjad on 2016.04.02  * 
 * Copyright © 2016 Shawn Amjad. All rights reserved. * 
 */
package com.mycompany.service;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

/**
 *
 * @author Shawn_000
 */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured { }

/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2004 - 2011 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.security;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Bernabei Angelo
 * 
 */
public class AuthorizationsBusinessMapper {
    static private Logger logger = Logger.getLogger(AuthorizationsBusinessMapper.class);

    private static AuthorizationsBusinessMapper instance = null;
    private HashMap _mapActions = null;
    private HashMap _mapPages = null;
    private HashMap _mapPagesModules = null;

    /**
     * Instantiates a new authorizations business mapper.
     */
    public AuthorizationsBusinessMapper() {
	logger.debug("IN");
	ConfigSingleton config = ConfigSingleton.getInstance();
	_mapActions = new HashMap();
	List actions =config.getAttributeAsList("BUSINESS_MAP.MAP_ACTIONS");
	Iterator it = actions.iterator();
	while (it.hasNext()) {
	    SourceBean mapActions = (SourceBean) it.next();
	    List actionsList =mapActions.getAttributeAsList("MAP_ACTION");
	    Iterator actionListIt = actionsList.iterator();
	    while (actionListIt.hasNext()) {
		SourceBean mapAction = (SourceBean) actionListIt.next();
    	    	String actionName = (String) mapAction.getAttribute("actionName");
    	    	String businessProcessName = (String) mapAction.getAttribute("businessProcess");
    	    	String actStr = "ACTION[" + actionName + "]";
    	    	logger.debug("PUT:actStr"+actStr);
    	    	_mapActions.put(actStr.toUpperCase(), businessProcessName);
	    }
	}
	_mapPages = new HashMap();
	_mapPagesModules = new HashMap();
	List pageModules = config.getAttributeAsList("BUSINESS_MAP.MAP_PAGE_MODULES");
	it = pageModules.iterator();
	while (it.hasNext()) {
	    SourceBean mapPageModules = (SourceBean) it.next();
	    List mapPageModuleList =mapPageModules.getAttributeAsList("MAP_PAGE_MODULE");
	    Iterator mapPageModuleListIt = mapPageModuleList.iterator();
	    while (mapPageModuleListIt.hasNext()) {
		SourceBean mapModules = (SourceBean) mapPageModuleListIt.next();
		String pageName = (String) mapModules.getAttribute("pageName");
		String moduleName = (String) mapModules.getAttribute("moduleName");
		String businessProcessName = (String) mapModules.getAttribute("businessProcess");
		if (moduleName == null) {
		    String pgStr = "PAGE[" + pageName + "]";
		    logger.debug("PUT:pgStr"+pgStr);
		    _mapPages.put(pgStr.toUpperCase(), businessProcessName);
		} else {
		    String pgMdlStr = "PAGE[" + pageName + "]MODULE[" + moduleName + "]";
		    logger.debug("PUT:pgMdlStr"+pgMdlStr);
		    _mapPagesModules.put(pgMdlStr.toUpperCase(), businessProcessName);
	    	}
	    }
	}
	logger.debug("OUT");
    }

    /**
     * Gets the single instance of AuthorizationsBusinessMapper.
     * 
     * @return single instance of AuthorizationsBusinessMapper
     */
    public static AuthorizationsBusinessMapper getInstance() {
	if (instance == null) {
	    synchronized (AuthorizationsBusinessMapper.class) {
		if (instance == null) {
		    try {
			instance = new AuthorizationsBusinessMapper();
		    } catch (Exception ex) {
			logger.error("Exception", ex);
		    }
		}
	    }
	}
	return instance;
    }

    /**
     * Map action to business process.
     * 
     * @param actionName the action name
     * 
     * @return the string
     */
    public String mapActionToBusinessProcess(String actionName) {
	logger.debug("IN. actionName="+actionName);
	String actStr = "ACTION[" + actionName + "]";
	String businessProcessName = (String) _mapActions.get(actStr.toUpperCase());
	if (businessProcessName == null) {
	    logger.warn("mapping per action [" + actionName + "] non trovato");
	}
	logger.debug("OUT,businessProcessName="+businessProcessName);
	return businessProcessName;
    }

    /**
     * Map page module to business process.
     * 
     * @param pageName the page name
     * @param moduleName the module name
     * 
     * @return the string
     */
    public String mapPageModuleToBusinessProcess(String pageName, String moduleName) {
	logger.debug("IN. pageName="+pageName+" moduleName="+moduleName);
	String pgMdlStr = "PAGE[" + pageName + "]MODULE[" + moduleName + "]";
	String businessProcessName = (String) _mapPagesModules.get(pgMdlStr.toUpperCase());
	if (businessProcessName == null) {
	    logger.warn("mapping per page [" + pageName + "] e module [" + moduleName + "] non trovato");
	    String pgStr = "PAGE[" + pageName + "]";
	    businessProcessName = (String) _mapPages.get(pgStr.toUpperCase());
	    if (businessProcessName == null) {
		logger.warn(" mapping per page [" + pageName + "] non trovato");
	    }
	}
	logger.debug("OUT,businessProcessName="+businessProcessName);
	return businessProcessName;
    }
}

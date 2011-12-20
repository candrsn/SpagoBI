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
package it.eng.spagobi.analiticalmodel.document.metadata;

import it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata.SbiParameters;
import it.eng.spagobi.commons.metadata.SbiCommonInfo;
import it.eng.spagobi.commons.metadata.SbiHibernateModel;


/**
 * SbiObjPar generated by hbm2java
 */
public class SbiObjPar  extends SbiHibernateModel {

    // Fields    

	private Integer objParId;
     private SbiObjects sbiObject;
     private SbiParameters sbiParameter;
     private Short reqFl;
     private Short modFl;
     private Short viewFl;
     private Short multFl;
     private String label;
     private String parurlNm;
     private Integer prog;
     private Integer priority;


    // Constructors

    /**
     * default constructor.
     */
    public SbiObjPar() {
		this.objParId = -1;

    }
    
    /**
     * constructor with id.
     * 
     * @param objParId the obj par id
     */
    public SbiObjPar(Integer objParId) {
        this.objParId = objParId;
    }


    // Property accessors
    
	/**
     * Gets the label.
     * 
     * @return the label
     */
    public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Gets the mod fl.
	 * 
	 * @return the mod fl
	 */
	public Short getModFl() {
		return modFl;
	}

	/**
	 * Sets the mod fl.
	 * 
	 * @param modFl the new mod fl
	 */
	public void setModFl(Short modFl) {
		this.modFl = modFl;
	}

	/**
	 * Gets the mult fl.
	 * 
	 * @return the mult fl
	 */
	public Short getMultFl() {
		return multFl;
	}

	/**
	 * Sets the mult fl.
	 * 
	 * @param multFl the new mult fl
	 */
	public void setMultFl(Short multFl) {
		this.multFl = multFl;
	}

	/**
	 * Gets the obj par id.
	 * 
	 * @return the obj par id
	 */
	public Integer getObjParId() {
		return objParId;
	}

	/**
	 * Sets the obj par id.
	 * 
	 * @param objParId the new obj par id
	 */
	public void setObjParId(Integer objParId) {
		this.objParId = objParId;
	}

	/**
	 * Gets the parurl nm.
	 * 
	 * @return the parurl nm
	 */
	public String getParurlNm() {
		return parurlNm;
	}

	/**
	 * Sets the parurl nm.
	 * 
	 * @param parurlNm the new parurl nm
	 */
	public void setParurlNm(String parurlNm) {
		this.parurlNm = parurlNm;
	}

	/**
	 * Gets the prog.
	 * 
	 * @return the prog
	 */
	public Integer getProg() {
		return prog;
	}

	/**
	 * Sets the prog.
	 * 
	 * @param prog the new prog
	 */
	public void setProg(Integer prog) {
		this.prog = prog;
	}

	/**
	 * Gets the req fl.
	 * 
	 * @return the req fl
	 */
	public Short getReqFl() {
		return reqFl;
	}

	/**
	 * Sets the req fl.
	 * 
	 * @param reqFl the new req fl
	 */
	public void setReqFl(Short reqFl) {
		this.reqFl = reqFl;
	}

	/**
	 * Gets the sbi object.
	 * 
	 * @return the sbi object
	 */
	public SbiObjects getSbiObject() {
		return sbiObject;
	}

	/**
	 * Sets the sbi object.
	 * 
	 * @param sbiObject the new sbi object
	 */
	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}

	/**
	 * Gets the sbi parameter.
	 * 
	 * @return the sbi parameter
	 */
	public SbiParameters getSbiParameter() {
		return sbiParameter;
	}

	/**
	 * Sets the sbi parameter.
	 * 
	 * @param sbiParameter the new sbi parameter
	 */
	public void setSbiParameter(SbiParameters sbiParameter) {
		this.sbiParameter = sbiParameter;
	}

	/**
	 * Gets the view fl.
	 * 
	 * @return the view fl
	 */
	public Short getViewFl() {
		return viewFl;
	}

	/**
	 * Sets the view fl.
	 * 
	 * @param viewFl the new view fl
	 */
	public void setViewFl(Short viewFl) {
		this.viewFl = viewFl;
	}

	/**
	 * Gets the priority.
	 * 
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * Sets the priority.
	 * 
	 * @param priority the new priority
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
   
}
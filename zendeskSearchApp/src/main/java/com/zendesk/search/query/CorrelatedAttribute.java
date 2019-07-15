package com.zendesk.search.query;

public class CorrelatedAttribute {

	String correlatedGroupName;
	String attribute;
	String correlatedAttribute;
	String targetAttribute;
	String targetAlias;
	
	public CorrelatedAttribute(String correlatedGroupName, String attribute, String correlatedAttribute, String targetAttribute, String targetAlias) {
		this.correlatedGroupName=correlatedGroupName;
		this.attribute=attribute;
		this.correlatedAttribute=correlatedAttribute;
		this.targetAttribute=targetAttribute;
		this.targetAlias=targetAlias;
	}

	public String getCorrelatedGroupName() {
		return correlatedGroupName;
	}

	public void setCorrelatedGroupName(String correlatedGroupName) {
		this.correlatedGroupName = correlatedGroupName;
	}
	
	public String getCorrelatedAttribute() {
		return correlatedAttribute;
	}

	public void setCorrelatedAttribute(String correlatedAttribute) {
		this.correlatedAttribute = correlatedAttribute;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getTargetAttribute() {
		return targetAttribute;
	}

	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}

	public String getTargetAlias() {
		return targetAlias;
	}

	public void setTargetAlias(String targetAlias) {
		this.targetAlias = targetAlias;
	}

}

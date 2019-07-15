package com.zendesk.search.components;

import java.util.LinkedList;
import java.util.List;

import com.zendesk.search.interfaces.Correlation;
import com.zendesk.search.query.CorrelatedAttribute;

public class CorrelationImp implements Correlation{

	private List<CorrelatedAttribute> attributes=new LinkedList<>();
	
	@Override
	public void addAttribute(CorrelatedAttribute attribute) {
		attributes.add(attribute);
	}

	@Override
	public List<CorrelatedAttribute> getCorrelations() {
		return attributes;
	}

}

package com.zendesk.search.interfaces;

import java.util.List;

import com.zendesk.search.query.CorrelatedAttribute;

public interface Correlation {
	
	public void addAttribute(CorrelatedAttribute attribute);
	
	public List<CorrelatedAttribute> getCorrelations();
}

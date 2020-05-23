package acs.logic;

import java.util.List;

import acs.boundaries.ElementBoundary;

public interface ElementService {
	
	public ElementBoundary create(String managerEmail,ElementBoundary element);
	public ElementBoundary update(String mangerEmail,String elementid,ElementBoundary update);
	public List<ElementBoundary> getAll(String userEmail);
	public ElementBoundary getSpecificElement(String userEmail,String elementId);
	public void deleteAllElements(String adminEmail);

}

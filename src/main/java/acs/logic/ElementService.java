package acs.logic;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import acs.boundaries.ElementBoundary;

public interface ElementService {
	public ElementBoundary create(String managerEmail,ElementBoundary element);
	public ElementBoundary update(String mangerEmail,String elementid,ElementBoundary update);
	public List<ElementBoundary> getAll(String userEmail);
	public ElementBoundary getSpecificElement(String userEmail,String elementId);
	public void deleteAllElements(String adminEmail);
	public void addElementToParent(String parentId, String childrenId,String managerEmail);
	public Set<ElementBoundary> getChildrens(String parentId,String userEmail);
	public Collection<ElementBoundary> getParents(String childrenId,String userEmail);
}

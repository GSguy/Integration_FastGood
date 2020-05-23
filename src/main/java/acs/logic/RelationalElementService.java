package acs.logic;

import java.util.Collection;
import java.util.Set;

import acs.boundaries.ElementBoundary;

public interface RelationalElementService extends ElementService {
	
	public void addElementToParent(String parentId, String childrenId,String managerEmail);
	public Set<ElementBoundary> getChildrens(String parentId,String userEmail);
	public Collection<ElementBoundary> getParents(String childrenId,String userEmail);
	
}
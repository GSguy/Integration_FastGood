package acs.logic;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import acs.boundaries.ElementBoundary;

public interface ElementServiceRelational extends ElementService {
	
	public void addElementToParent(String parentId, String childrenId,String managerEmail);
	public List<ElementBoundary> getChildrens(String parentId,String userEmail, int size, int page);
	public List<ElementBoundary> getParents(String childrenId,String userEmail, int size, int page);
	public List<ElementBoundary> getAll(String userEmail, int size, int page);
	public List<ElementBoundary> getElementsByType(String type, String userEmail, int size, int page);
	public List<ElementBoundary> getElementsByName(String name, String userEmail, int size, int page);
	Set<ElementBoundary> getChildrens(String parentId, String userEmail);
	Collection<ElementBoundary> getParents(String childrenId, String userEmail);
	
	
}
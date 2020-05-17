package acs.logic;

import java.util.List;

import acs.boundaries.ActionBoundary;

public interface ActionServiceUpgraded extends ActionService{
	public List<ActionBoundary> getAllActions(String adminEmail, int size, int page);

}

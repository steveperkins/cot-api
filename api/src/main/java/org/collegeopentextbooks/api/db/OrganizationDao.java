package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.Organization;

public interface OrganizationDao {

	List<Organization> getOrganizations();

	Organization getById(int organizationId);

	/**
	 * Creates or updates an organization
	 * @param organization the editor to create or update
	 * @return
	 * @author steve.perkins
	 */
	Organization save(Organization organization);

}
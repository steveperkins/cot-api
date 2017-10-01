package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Organization;

public interface OrganizationService {

	/**
	 * Retrieves ALL organizations
	 * @return
	 * @author steve.perkins
	 */
	List<Organization> getOrganizations();

	/**
	 * Retrieves an organization by it's ID
	 * @param organizationId
	 * @return
	 * @author steve.perkins
	 */
	Organization getRepository(Integer organizationId);

	/**
	 * Creates or updates the given organization's scalar values.
	 * @param repository the organization to create or update
	 * @return the updated organization. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name, URL, or logo URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	Organization save(Organization organization) throws RequiredValueEmptyException, ValueTooLongException;

}
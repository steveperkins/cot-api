package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.OrganizationDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	private static final Integer LOGO_URL_MAX_LENGTH = 255;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	/**
	 * Retrieves ALL organizations
	 * @return
	 * @author steve.perkins
	 */
	public List<Organization> getOrganizations() {
		List<Organization> organizations = organizationDao.getOrganizations();
		return organizations;
	}
	
	/**
	 * Retrieves an organization by it's ID
	 * @param organizationId
	 * @return
	 * @author steve.perkins
	 */
	public Organization getRepository(Integer organizationId) {
		Organization organization = organizationDao.getById(organizationId);
		return organization;
	}
	
	/**
	 * Creates or updates the given organization's scalar values.
	 * @param repository the organization to create or update
	 * @return the updated organization. If this is a create operation, the new object's ID is populated on both the returned object and the given object.
	 * @throws RequiredValueEmptyException if the provided name is missing or blank
	 * @throws ValueTooLongException if the provided name, URL, or logo URL is longer than their respective max lengths
	 * @author steve.perkins
	 */
	public Organization save(Organization organization) throws RequiredValueEmptyException, ValueTooLongException {
		if(null == organization)
			return null;
		
		if(StringUtils.isBlank(organization.getName()))
			throw new RequiredValueEmptyException("Name cannot be blank");
		
		if(organization.getName().length() > NAME_MAX_LENGTH)
			throw new ValueTooLongException("Name exceeds max length (" + NAME_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(organization.getUrl()) 
				&& organization.getUrl().length() > URL_MAX_LENGTH)
			throw new ValueTooLongException("URL exceeds max length (" + URL_MAX_LENGTH + ")");
		
		if(StringUtils.isNotBlank(organization.getLogoUrl()) 
				&& organization.getLogoUrl().length() > LOGO_URL_MAX_LENGTH)
			throw new ValueTooLongException("Logo URL exceeds max length (" + LOGO_URL_MAX_LENGTH + ")");
		
		return organizationDao.save(organization);
	}
	
}

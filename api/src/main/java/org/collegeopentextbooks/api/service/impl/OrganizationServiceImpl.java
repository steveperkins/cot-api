package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.OrganizationDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.Organization;
import org.collegeopentextbooks.api.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	private static final Integer NAME_MAX_LENGTH = 255;
	private static final Integer URL_MAX_LENGTH = 255;
	private static final Integer LOGO_URL_MAX_LENGTH = 255;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.OrganizationService#getOrganizations()
	 */
	@Override
	public List<Organization> getOrganizations() {
		List<Organization> organizations = organizationDao.getOrganizations();
		return organizations;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.OrganizationService#getRepository(java.lang.Integer)
	 */
	@Override
	public Organization getRepository(Integer organizationId) {
		Organization organization = organizationDao.getById(organizationId);
		return organization;
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.OrganizationService#save(org.collegeopentextbooks.api.model.Organization)
	 */
	@Override
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

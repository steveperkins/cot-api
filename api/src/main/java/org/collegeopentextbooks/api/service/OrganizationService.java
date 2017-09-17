package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.db.OrganizationDaoImpl;
import org.collegeopentextbooks.api.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
	
	@Autowired
	private OrganizationDaoImpl organizationDao;
	
	/**
	 * Retrieves ALL organizations
	 * @return
	 */
	public List<Organization> getOrganizations() {
		List<Organization> organizations = organizationDao.getOrganizations();
		return organizations;
	}
	
	/**
	 * Retrieves an organization by it's ID
	 * @param organizationId
	 * @return
	 */
	public Organization getRepository(Integer organizationId) {
		Organization organization = organizationDao.getById(organizationId);
		return organization;
	}
	
	public Organization save(Organization organization) {
		return organizationDao.save(organization);
	}
	
}

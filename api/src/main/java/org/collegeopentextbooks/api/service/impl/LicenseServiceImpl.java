package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;
import org.collegeopentextbooks.api.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {

	private static final Integer ID_MAX_LENGTH = 2;
	private static final Integer DESCRIPTION_MAX_LENGTH = 255;
	
	@Autowired
	private LicenseDao licenseDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#getAll()
	 */
	@Override
	public List<License> getAll() {
		return licenseDao.getLicenses();
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#getById(java.lang.String)
	 */
	@Override
	public License getById(String licenseId) {
		return licenseDao.getById(licenseId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#getByResource(java.lang.Integer)
	 */
	@Override
	public List<License> getByResource(Integer resourceId) {
		return licenseDao.getLicensesByResourceId(resourceId);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#insert(org.collegeopentextbooks.api.model.License)
	 */
	@Override
	public License insert(License license) {
		if(null == license)
			return null;
		
		if(StringUtils.isBlank(license.getId()))
			throw new RequiredValueEmptyException("License Code is required");
		
		if(StringUtils.isBlank(license.getDescription()))
			throw new RequiredValueEmptyException("Description cannot be blank");
		
		if(license.getId().length() > ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + ID_MAX_LENGTH + ")");
		
		if(license.getDescription().length() > DESCRIPTION_MAX_LENGTH)
			throw new ValueTooLongException("Description exceeds max length (" + DESCRIPTION_MAX_LENGTH + ")");
		
		return licenseDao.insert(license);
	}
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#update(org.collegeopentextbooks.api.model.License)
	 */
	@Override
	public License update(License license) {
		if(null == license)
			return null;
		
		if(StringUtils.isBlank(license.getId()))
			throw new RequiredValueEmptyException("License Code is required");
		
		if(StringUtils.isBlank(license.getDescription()))
			throw new RequiredValueEmptyException("Description cannot be blank");
		
		if(license.getId().length() > ID_MAX_LENGTH)
			throw new ValueTooLongException("License Code exceeds max length (" + ID_MAX_LENGTH + ")");
		
		if(license.getDescription().length() > DESCRIPTION_MAX_LENGTH)
			throw new ValueTooLongException("Description exceeds max length (" + DESCRIPTION_MAX_LENGTH + ")");
		
		return licenseDao.update(license);
	}

	@Override
	public void addLicenseToResource(Resource resource, String licenseId) {
		licenseDao.addLicenseToResource(resource.getId(), licenseId);
	}

	@Override
	public void deleteLicenseFromResource(Resource resource, String licenseId) {
		licenseDao.deleteLicenseFromResource(resource.getId(), licenseId);
	}
}

package org.collegeopentextbooks.api.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.collegeopentextbooks.api.db.LicenseDaoImpl;
import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {

	private static final Integer ID_MAX_LENGTH = 2;
	private static final Integer DESCRIPTION_MAX_LENGTH = 255;
	
	@Autowired
	private LicenseDaoImpl licenseDao;
	
	public List<License> getAll() {
		return licenseDao.getLicenses();
	}
	
	public License getById(String licenseId) {
		return licenseDao.getById(licenseId);
	}
	
	public List<License> getByResource(Integer resourceId) {
		return licenseDao.getLicensesByResourceId(resourceId);
	}
	
	/**
	 * Create the given license
	 * @param license the license to create or update
	 * @return the updated license
	 * @throws RequiredValueEmptyException if the provided license ID or description is missing or blank
	 * @throws ValueTooLongException if the provided license ID or description is longer than its respective max length
	 * @author steve.perkins
	 */
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
	
	/**
	 * Updates the given license
	 * @param license the license to create or update
	 * @return the updated license
	 * @throws RequiredValueEmptyException if the provided license ID or description is missing or blank
	 * @throws ValueTooLongException if the provided license ID or description is longer than its respective max length
	 * @author steve.perkins
	 */
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
}

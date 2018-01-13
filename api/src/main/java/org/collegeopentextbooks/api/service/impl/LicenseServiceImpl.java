package org.collegeopentextbooks.api.service.impl;

import java.util.List;

import org.collegeopentextbooks.api.db.LicenseDao;
import org.collegeopentextbooks.api.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl implements LicenseService {

	@Autowired
	private LicenseDao licenseDao;
	
	/* (non-Javadoc)
	 * @see org.collegeopentextbooks.api.service.LicenseService#getAll()
	 */
	@Override
	public List<String> getAll() {
		return licenseDao.getLicenses();
	}
	
}

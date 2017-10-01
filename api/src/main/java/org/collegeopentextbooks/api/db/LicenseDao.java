package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.License;

public interface LicenseDao {
	public static Integer LICENSE_ID_MAX_SIZE = 2;
	
	List<License> getLicenses();

	List<License> getLicensesByResourceId(Integer resourceId);

	License getById(String id);

	/**
	 * Associates an existing resource with an existing license
	 * @param resourceId
	 * @param licenseId
	 * @author steve.perkins
	 */
	void addLicenseToResource(Integer resourceId, String licenseId);

	/**
	 * Removes an existing association between a resource and a license
	 * @param resourceId
	 * @param licenseId
	 * @author steve.perkins
	 */
	void deleteLicenseFromResource(Integer resourceId, String licenseId);

	/**
	 * Creates a new license
	 * @param license the license to create
	 * @return
	 * @author steve.perkins
	 */
	License insert(License license);

	/**
	 * Updates a license code or description
	 * @param license the license to update
	 * @return
	 * @author steve.perkins
	 */
	License update(License license);

}
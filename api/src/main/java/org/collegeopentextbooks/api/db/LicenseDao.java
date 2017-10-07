package org.collegeopentextbooks.api.db;

import java.util.List;

import org.collegeopentextbooks.api.model.License;
import org.collegeopentextbooks.api.model.Resource;

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
	 * Merges <code>resource</code>'s licenses into the data store. Creates and associates any licenses that do not exist. Associates any licenses that exist but are not associated with <code>resource</code>. Disassociates any current licenses that are not in <code>resource</code>'s license list.
	 * @param resource an existing resources with a set of licenses in the "desired" state (i.e. the final list of the resource's licenses regardless of whether they currently exist or are associated)
	 * @return
	 * @license steve.perkins
	 */
	List<License> merge(Resource resource, List<License> licenses);
	
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
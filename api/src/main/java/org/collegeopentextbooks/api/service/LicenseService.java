package org.collegeopentextbooks.api.service;

import java.util.List;

import org.collegeopentextbooks.api.exception.RequiredValueEmptyException;
import org.collegeopentextbooks.api.exception.ValueTooLongException;
import org.collegeopentextbooks.api.model.License;

public interface LicenseService {

	List<License> getAll();

	License getById(String licenseId);

	List<License> getByResource(Integer resourceId);

	/**
	 * Create the given license
	 * @param license the license to create or update
	 * @return the updated license
	 * @throws RequiredValueEmptyException if the provided license ID or description is missing or blank
	 * @throws ValueTooLongException if the provided license ID or description is longer than its respective max length
	 * @author steve.perkins
	 */
	License insert(License license);

	/**
	 * Updates the given license
	 * @param license the license to create or update
	 * @return the updated license
	 * @throws RequiredValueEmptyException if the provided license ID or description is missing or blank
	 * @throws ValueTooLongException if the provided license ID or description is longer than its respective max length
	 * @author steve.perkins
	 */
	License update(License license);

}
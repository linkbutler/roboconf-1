/**
 * Copyright 2014 Linagora, Université Joseph Fourier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.roboconf.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import net.roboconf.core.Constants;
import net.roboconf.core.model.runtime.Instance;

/**
 * @author Vincent Zurczak - Linagora
 */
public final class ResourceUtils {

	/**
	 * Private empty constructor.
	 */
	private ResourceUtils() {
		// nothing
	}


	/**
	 * Stores the instance resources into a map.
	 * @param applicationFilesDirectory the application's directory
	 * @param instance an instance (not null)
	 * @return a non-null map (key = the file location, relative to the instance's directory, value = file content)
	 * @throws IOException if something went wrong while reading a file
	 */
	public static Map<String,byte[]> storeInstanceResources( File applicationFilesDirectory, Instance instance ) throws IOException {

		Map<String,byte[]> result;
		File instanceResourcesDirectory = findInstanceResourcesDirectory( applicationFilesDirectory, instance );
		if( instanceResourcesDirectory.exists()
				&& instanceResourcesDirectory.isDirectory())
			result = Utils.storeDirectoryResourcesAsBytes( instanceResourcesDirectory );
		else
			result = Collections.emptyMap();

		return result;
	}


	/**
	 * Finds the resource directory for an instance.
	 * @param applicationFilesDirectory the application's directory
	 * @param instance an instance
	 * @return a non-null file (that may not exist)
	 */
	public static File findInstanceResourcesDirectory( File applicationFilesDirectory, Instance instance ) {
		return findInstanceResourcesDirectory( applicationFilesDirectory, instance.getComponent().getName());
	}


	/**
	 * Finds the resource directory for an instance.
	 * @param applicationFilesDirectory the application's directory
	 * @param componentName the component name
	 * @return a non-null file (that may not exist)
	 */
	public static File findInstanceResourcesDirectory( File applicationFilesDirectory, String componentName ) {

		File result = new File( applicationFilesDirectory, Constants.PROJECT_DIR_GRAPH );
		result = new File( result, componentName);

		return result;
	}
}

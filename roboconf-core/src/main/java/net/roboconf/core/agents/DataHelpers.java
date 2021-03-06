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

package net.roboconf.core.agents;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

/**
 * A set of helpers to write and read data for agents.
 * @author Vincent Zurczak - Linagora
 */
public final class DataHelpers {

	public static final String MESSAGING_IP = "messaging.ip";
	public static final String MESSAGING_USERNAME = "messaging.username";
	public static final String MESSAGING_PASSWORD = "messaging.password";
	public static final String ROOT_INSTANCE_NAME = "root.instance.name";
	public static final String APPLICATION_NAME = "application.name";


	/**
	 * Constructor.
	 */
	private DataHelpers() {
		// nothing
	}


	/**
	 * Writes data to store in IaaS as a string.
	 * @param messagingServerIp the IP of the messaging server
	 * @param messagingUsername the user name to connect to the messaging server
	 * @param messagingPassword the password to connect to the messaging server
	 * @param applicationName the application name
	 * @param rootInstanceName the root instance name
	 * @return a non-null string
	 * @throws IOException if something went wrong
	 */
	public static String writeIaasDataAsString(
			String messagingServerIp,
			String messagingUsername,
			String messagingPassword,
			String applicationName,
			String rootInstanceName ) throws IOException {

		Properties props = writeIaasDataAsProperties( messagingServerIp, messagingUsername, messagingPassword, applicationName, rootInstanceName );
		StringWriter writer = new StringWriter();
		props.store( writer, "" );

		return writer.toString();
	}


	/**
	 * Writes data to store in IaaS as properties.
	 * @param messagingServerIp the IP of the messaging server
	 * @param messagingUsername the user name to connect to the messaging server
	 * @param messagingPassword the password to connect to the messaging server
	 * @param applicationName the application name
	 * @param rootInstanceName the root instance name
	 * @return a non-null object
	 */
	public static Properties writeIaasDataAsProperties(
			String messagingServerIp,
			String messagingUsername,
			String messagingPassword,
			String applicationName,
			String rootInstanceName ) {

		Properties result = new Properties();
		if( applicationName != null )
			result.setProperty( APPLICATION_NAME, applicationName );

		if( rootInstanceName != null )
			result.setProperty( ROOT_INSTANCE_NAME, rootInstanceName );

		if( messagingServerIp != null )
			result.setProperty( MESSAGING_IP, messagingServerIp );

		if( messagingUsername != null )
			result.setProperty( MESSAGING_USERNAME, messagingUsername );

		if( messagingPassword != null )
			result.setProperty( MESSAGING_PASSWORD, messagingPassword );

		return result;
	}


	/**
	 * Reads IaaS data.
	 * @param rawProperties the IaaS data as a string
	 * @return a non-null object
	 * @throws IOException if something went wrong
	 */
	public static Properties readIaasData( String rawProperties ) throws IOException {

		Properties result = new Properties();
		StringReader reader = new StringReader( rawProperties );
		result.load( reader );

		return result;
	}
}

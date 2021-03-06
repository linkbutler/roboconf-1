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

package net.roboconf.dm.rest.client;

import net.roboconf.dm.rest.client.delegates.ApplicationWsDelegate;
import net.roboconf.dm.rest.client.delegates.ManagementWsDelegate;
import net.roboconf.dm.rest.json.ObjectMapperProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * A client for the REST API of the Deployment Manager.
 * <p>
 * This client is configured for the REST implementations that use the predefined
 * PATH constants. Users should call {@link #destroy()} to release the connection.
 * </p>
 * <p>
 * The following rules apply to the client implementation.
 * </p>
 * <ul>
 * 		<li>Methods that return a list do not throw exceptions.</li>
 * 		<li>Methods that return a list never return null.</li>
 * 		<li>Methods that return a list only expected 2xx responses from the server.</li>
 * 		<li>Methods that return an object throw an exception if they did not receive a 2xx response.</li>
 * 		<li>Methods that return an object return null if they did not receive a 200 response.</li>
 * 		<li>Methods that do not return anything throw an exception if they did not receive a 2xx response.</li>
 * </ul>
 * <p>
 * Thrown exceptions contain the error code and the error message.
 * </p>
 * <p>
 * About the logging policy...<br />
 * Every client method logs an entry when it is invoked.
 * It logs a second entry once the REST invocation has completed, and provided a runtime
 * exception was not thrown by the REST library.
 * </p>
 * <p>
 * The second log entry either logs the response code (success), or logs the response
 * code and the response message (error in the result).
 * </p>
 *
 * @author Vincent Zurczak - Linagora
 */
public class WsClient {

	private final ApplicationWsDelegate applicationDelegate;
	private final ManagementWsDelegate managementDelegate;

	private final Client client;


	/**
	 * Constructor.
	 * @param rootUrl the root URL (example: http://192.168.1.18:9007/dm/)
	 */
	public WsClient( String rootUrl ) {

		ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add( ObjectMapperProvider.class );

		this.client = Client.create( cc );
		this.client.setFollowRedirects( true );
		WebResource resource = this.client.resource( rootUrl );

		this.applicationDelegate = new ApplicationWsDelegate( resource );
		this.managementDelegate = new ManagementWsDelegate( resource );

		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				WsClient.this.client.destroy();
			}
		});
	}


	/**
	 * Force the destruction of the JAX-RS client.
	 */
	public void destroy() {
		this.client.destroy();
	}


	/**
	 * @return the applicationDelegate
	 */
	public ApplicationWsDelegate getApplicationDelegate() {
		return this.applicationDelegate;
	}


	/**
	 * @return the appManagementDelegate
	 */
	public ManagementWsDelegate getManagementDelegate() {
		return this.managementDelegate;
	}
}

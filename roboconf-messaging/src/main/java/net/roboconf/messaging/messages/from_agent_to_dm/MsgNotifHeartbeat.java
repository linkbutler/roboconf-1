/**
 * Copyright 2013-2014 Linagora, Université Joseph Fourier
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

package net.roboconf.messaging.messages.from_agent_to_dm;

import net.roboconf.core.model.helpers.InstanceHelpers;
import net.roboconf.core.model.runtime.Instance;
import net.roboconf.messaging.messages.Message;

/**
 * @author Noël - LIG
 */
public class MsgNotifHeartbeat extends Message {

	private static final long serialVersionUID = -5112964737944073719L;
	private final String rootInstanceName;
	private final String applicationName;


	/**
	 * Constructor.
	 * @param applicationName the application name
	 * @param rootInstanceName the root instance (machine) name
	 */
	public MsgNotifHeartbeat( String applicationName, String rootInstanceName ) {
		super();
		this.rootInstanceName = rootInstanceName;
		this.applicationName = applicationName;
	}

	/**
	 * Constructor.
	 * @param applicationName the application name
	 * @param rootInstance the root instance
	 */
	public MsgNotifHeartbeat( String applicationName, Instance rootInstance ) {
		this( applicationName, InstanceHelpers.findRootInstance( rootInstance ).getName());
	}

	/**
	 * @return the rootInstanceName
	 */
	public String getRootInstanceName() {
		return this.rootInstanceName;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return this.applicationName;
	}
}

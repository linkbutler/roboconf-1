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

package net.roboconf.dm.environment.messaging;

import junit.framework.Assert;
import net.roboconf.core.model.helpers.InstanceHelpers;
import net.roboconf.core.model.runtime.Instance;
import net.roboconf.core.model.runtime.Instance.InstanceStatus;
import net.roboconf.dm.internal.TestApplication;
import net.roboconf.dm.internal.TestIaasResolver;
import net.roboconf.dm.internal.TestMessageServerClient.DmMessageServerClientFactory;
import net.roboconf.dm.management.ManagedApplication;
import net.roboconf.dm.management.Manager;
import net.roboconf.messaging.messages.from_agent_to_dm.MsgNotifHeartbeat;
import net.roboconf.messaging.messages.from_agent_to_dm.MsgNotifInstanceChanged;
import net.roboconf.messaging.messages.from_agent_to_dm.MsgNotifInstanceRemoved;
import net.roboconf.messaging.messages.from_agent_to_dm.MsgNotifMachineDown;
import net.roboconf.messaging.messages.from_agent_to_dm.MsgNotifMachineUp;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Zurczak - Linagora
 */
public class DmMessageProcessorTest {

	private TestApplication app;
	private DmMessageProcessor processor;
	private TestIaasResolver iaasResolver;



	@Before
	public void resetManager() {

		this.app = new TestApplication();
		this.processor = new DmMessageProcessor();

		Manager.INSTANCE.getAppNameToManagedApplication().clear();
		Manager.INSTANCE.getAppNameToManagedApplication().put( this.app.getName(), new ManagedApplication( this.app, null ));

		this.iaasResolver = new TestIaasResolver();
		Manager.INSTANCE.setIaasResolver( this.iaasResolver );
		Manager.INSTANCE.setMessagingClientFactory( new DmMessageServerClientFactory());
	}


	@Test
	public void testProcessMsgNotifMachineUp_success() {

		final String ip = "192.13.1.23";
		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());

		MsgNotifMachineUp msg = new MsgNotifMachineUp( this.app.getName(), this.app.getMySqlVm().getName(), ip );
		this.processor.processMessage( msg );

		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
		String value = this.app.getMySqlVm().getData().get( Instance.IP_ADDRESS );
		Assert.assertNotNull( value );
		Assert.assertEquals( ip, value );
	}


	@Test
	public void testProcessMsgNotifMachineUp_invalidApplication() {

		final String ip = "192.13.1.23";
		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());

		MsgNotifMachineUp msg = new MsgNotifMachineUp( "app-32", this.app.getMySqlVm().getName(), ip );
		this.processor.processMessage( msg );

		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());
		String value = this.app.getMySqlVm().getData().get( Instance.IP_ADDRESS );
		Assert.assertNull( value );
	}


	@Test
	public void testProcessMsgNotifMachineUp_invalidInstance() {

		final String ip = "192.13.1.23";
		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());

		MsgNotifMachineUp msg = new MsgNotifMachineUp( this.app.getName(), "invalid-machine", ip );
		this.processor.processMessage( msg );

		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());
		String value = this.app.getMySqlVm().getData().get( Instance.IP_ADDRESS );
		Assert.assertNull( value );
	}


	@Test
	public void testProcessMsgNotifMachineDown_success() {

		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );
		MsgNotifMachineDown msg = new MsgNotifMachineDown( this.app.getName(), this.app.getMySqlVm().getName());

		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.NOT_DEPLOYED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifMachineDown_invalidApplication() {

		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );
		MsgNotifMachineDown msg = new MsgNotifMachineDown( "app-51", this.app.getMySqlVm());

		// The application name is invalid, no update should have been performed
		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifMachineDown_invalidInstance() {

		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );
		MsgNotifMachineDown msg = new MsgNotifMachineDown( this.app.getName(), "invalid-path" );

		// The application name is invalid, no update should have been performed
		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifInstanceChanged_success() {

		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );
		MsgNotifInstanceChanged msg = new MsgNotifInstanceChanged( this.app.getName(), this.app.getMySqlVm());
		msg.setNewStatus( InstanceStatus.STOPPING );

		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.STOPPING, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifInstanceChanged_invalidApplication() {

		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );
		MsgNotifInstanceChanged msg = new MsgNotifInstanceChanged( "app-53", this.app.getMySqlVm());
		msg.setNewStatus( InstanceStatus.STOPPING );

		// The application name is invalid, no update should have been performed
		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifInstanceChanged_invalidInstance() {

		Manager.INSTANCE.getAppNameToManagedApplication().put( this.app.getName(), new ManagedApplication( this.app, null ));
		this.app.getMySqlVm().setStatus( InstanceStatus.DEPLOYED_STARTED );

		MsgNotifInstanceChanged msg = new MsgNotifInstanceChanged( this.app.getName(), new Instance( "invalid instance" ));
		msg.setNewStatus( InstanceStatus.STOPPING );

		// The application name is invalid, no update should have been performed
		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testProcessMsgNotifInstanceRemoved_success() {

		String instancePath = InstanceHelpers.computeInstancePath( this.app.getMySql());
		int instancesCount = InstanceHelpers.getAllInstances( this.app ).size();

		MsgNotifInstanceRemoved msg = new MsgNotifInstanceRemoved( this.app.getName(), this.app.getMySql());
		Assert.assertNotNull( InstanceHelpers.findInstanceByPath( this.app, instancePath ));

		this.processor.processMessage( msg );
		Assert.assertNull( InstanceHelpers.findInstanceByPath( this.app, instancePath ));
		Assert.assertEquals( instancesCount - 1, InstanceHelpers.getAllInstances( this.app ).size());
	}


	@Test
	public void testProcessMsgNotifInstanceRemoved_invalidApplication() {

		String instancePath = InstanceHelpers.computeInstancePath( this.app.getMySql());
		int instancesCount = InstanceHelpers.getAllInstances( this.app ).size();

		MsgNotifInstanceRemoved msg = new MsgNotifInstanceRemoved( "app-98", this.app.getMySql());
		Assert.assertNotNull( InstanceHelpers.findInstanceByPath( this.app, instancePath ));

		this.processor.processMessage( msg );
		Assert.assertNotNull( InstanceHelpers.findInstanceByPath( this.app, instancePath ));
		Assert.assertEquals( instancesCount, InstanceHelpers.getAllInstances( this.app ).size());
	}


	@Test
	public void testProcessMsgNotifInstanceRemoved_invalidInstance() {

		MsgNotifInstanceRemoved msg = new MsgNotifInstanceRemoved( this.app.getName(), new Instance( "whatever" ));
		int instancesCount = InstanceHelpers.getAllInstances( this.app ).size();

		this.processor.processMessage( msg );
		Assert.assertEquals( instancesCount, InstanceHelpers.getAllInstances( this.app ).size());
	}


	@Test
	public void testMsgNotifHeartbeat_success() {

		this.app.getMySqlVm().setStatus( InstanceStatus.PROBLEM );
		MsgNotifHeartbeat msg = new MsgNotifHeartbeat( this.app.getName(), this.app.getMySqlVm());

		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.DEPLOYED_STARTED, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testMsgNotifHeartbeat_invalidApplication() {

		this.app.getMySqlVm().setStatus( InstanceStatus.PROBLEM );
		MsgNotifHeartbeat msg = new MsgNotifHeartbeat( "app-98", this.app.getMySqlVm());

		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.PROBLEM, this.app.getMySqlVm().getStatus());
	}


	@Test
	public void testMsgNotifHeartbeat_invalidInstance() {

		this.app.getMySqlVm().setStatus( InstanceStatus.PROBLEM );
		MsgNotifHeartbeat msg = new MsgNotifHeartbeat( this.app.getName(), new Instance( "unknown" ));

		this.processor.processMessage( msg );
		Assert.assertEquals( InstanceStatus.PROBLEM, this.app.getMySqlVm().getStatus());
	}
}

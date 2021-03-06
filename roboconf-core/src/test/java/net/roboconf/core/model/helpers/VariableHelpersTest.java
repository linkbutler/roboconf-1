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

package net.roboconf.core.model.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import net.roboconf.core.model.runtime.Component;
import net.roboconf.core.model.runtime.Instance;

import org.junit.Test;

/**
 * @author Vincent Zurczak - Linagora
 */
public class VariableHelpersTest {

	@Test
	public void testParseVariableName() {

		String[][] values = {
				{ "facet", "variable" },
				{ "FacetName", "complex.variable.name" },
				{ "facet-Name", "" },
				{ "", "some_variable" }
		};

		for( String[] value : values ) {
			String s = value[ 0 ] + "." + value[ 1 ];
			Map.Entry<String,String> entry = VariableHelpers.parseVariableName( s );
			Assert.assertEquals( "Invalid component or facet name for " + s, value[ 0 ], entry.getKey());
			Assert.assertEquals( "Invalid simple name for " + s, value[ 1 ], entry.getValue());
		}

		Map.Entry<String,String> entry = VariableHelpers.parseVariableName( "noPrefix" );
		Assert.assertEquals( "noPrefix", entry.getValue());
		Assert.assertEquals( "", entry.getKey());
	}


	@Test
	public void testParseExportedVariable() {

		String[][] values = {
				{ "variableName", "" },
				{ "variableName", "default value" },
				{ "complex.variable.name", "51" },
				{ "", "oops" }
		};

		for( String[] value : values ) {
			String s = value[ 0 ] + " = " + value[ 1 ];
			Map.Entry<String,String> entry = VariableHelpers.parseExportedVariable( s );
			Assert.assertEquals( "Invalid variable name for " + s, value[ 0 ], entry.getKey());
			Assert.assertEquals( "Invalid default value for " + s, value[ 1 ], entry.getValue());
		}
	}


	@Test
	public void testFindPrefixesForExportedVariables_withComponentVariables() {

		Component component = new Component( "comp" );
		component.getExportedVariables().put( "comp.ip", "" );
		component.getExportedVariables().put( "comp.split.property", "" );
		component.getExportedVariables().put( "comp.port", "8000" );
		component.getExportedVariables().put( "facet.desc", "some description" );

		Instance instance = new Instance( "inst" ).component( component );

		Set<String> prefixes = VariableHelpers.findPrefixesForExportedVariables( instance );
		Assert.assertEquals( 2, prefixes.size());
		Assert.assertTrue( prefixes.contains( "comp" ));
		Assert.assertTrue( prefixes.contains( "facet" ));
	}


	@Test
	public void testFindPrefixesForExportedVariables_withVariable() {

		Instance instance = new Instance( "inst" ).component( new Component( "comp" ));
		Set<String> prefixes = VariableHelpers.findPrefixesForExportedVariables( instance );
		Assert.assertEquals( 0, prefixes.size());
	}


	@Test
	public void testFindPrefixesForExportedVariables_withInstanceVariablesOnly() {

		Instance instance = new Instance( "inst" ).component( new Component( "comp" ));
		instance.getOverriddenExports().put( "comp.ip", "" );
		instance.getOverriddenExports().put( "comp.split.property", "" );
		instance.getOverriddenExports().put( "comp.port", "8000" );
		instance.getOverriddenExports().put( "facet.desc", "some description" );

		Set<String> prefixes = VariableHelpers.findPrefixesForExportedVariables( instance );
		Assert.assertEquals( 2, prefixes.size());
		Assert.assertTrue( prefixes.contains( "comp" ));
		Assert.assertTrue( prefixes.contains( "facet" ));
	}


	@Test
	public void testFindPrefixesForImportedVariables() {

		Component component = new Component( "comp" );
		component.getImportedVariables().put( "comp.ip", Boolean.FALSE );
		component.getImportedVariables().put( "comp.split.property", Boolean.FALSE );
		component.getImportedVariables().put( "comp.port", Boolean.FALSE );
		component.getImportedVariables().put( "facet.desc", Boolean.FALSE );

		Instance instance = new Instance( "inst" ).component( component );

		Set<String> prefixes = VariableHelpers.findPrefixesForImportedVariables( instance );
		Assert.assertEquals( 2, prefixes.size());
		Assert.assertTrue( prefixes.contains( "comp" ));
		Assert.assertTrue( prefixes.contains( "facet" ));

		component.getImportedVariables().clear();
		prefixes = VariableHelpers.findPrefixesForImportedVariables( instance );
		Assert.assertEquals( 0, prefixes.size());
	}


	@Test
	public void testFindPrefixesForMandatoryImportedVariables() {

		Component component = new Component( "comp" );
		component.getImportedVariables().put( "comp.ip", Boolean.FALSE );
		component.getImportedVariables().put( "comp.split.property", Boolean.FALSE );
		component.getImportedVariables().put( "comp.port", Boolean.FALSE );
		component.getImportedVariables().put( "facet.desc", Boolean.TRUE );
		component.getImportedVariables().put( "facet-n.prop1", Boolean.TRUE );
		component.getImportedVariables().put( "facet-n.prop2", Boolean.FALSE );

		Instance instance = new Instance( "inst" ).component( component );

		Set<String> prefixes = VariableHelpers.findPrefixesForMandatoryImportedVariables( instance );
		Assert.assertEquals( 2, prefixes.size());
		Assert.assertTrue( prefixes.contains( "comp" ));
		Assert.assertTrue( prefixes.contains( "facet-n" ));

		component.getImportedVariables().clear();
		prefixes = VariableHelpers.findPrefixesForMandatoryImportedVariables( instance );
		Assert.assertEquals( 0, prefixes.size());
	}


	@Test
	public void testUpdateNetworkVariables() {

		Map<String,String> map = new HashMap<String,String> ();
		map.put( "comp.ip", "" );
		map.put( "ip", "" );
		map.put( "not-ip", "" );

		final String ip = "127.0.0.1";
		VariableHelpers.updateNetworkVariables( map, ip );
		Assert.assertEquals( ip, map.get( "comp.ip" ));
		Assert.assertEquals( ip, map.get( "ip" ));
		Assert.assertEquals( "", map.get( "not-ip" ));
	}
}

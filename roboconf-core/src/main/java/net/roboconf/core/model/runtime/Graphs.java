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

package net.roboconf.core.model.runtime;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * This object contains sets of related components.
 * @author Vincent Zurczak - Linagora
 */
public class Graphs implements Serializable {

	private static final long serialVersionUID = 2918281424743945139L;
	private final Collection<Component> rootsComponents = new HashSet<Component> ();

	/**
	 * @return a non-null list of root components
	 */
	public Collection<Component> getRootComponents() {
		return this.rootsComponents;
	}
}

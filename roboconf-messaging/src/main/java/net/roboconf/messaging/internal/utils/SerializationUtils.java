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

package net.roboconf.messaging.internal.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import net.roboconf.messaging.messages.Message;

/**
 * @author Noël - LIG
 */
public final class SerializationUtils {

	/**
	 * Empty private constructor.
	 */
	private SerializationUtils() {
		// nothing
	}


	/**
	 * Serializes an object.
	 * @param object a serializable object
	 * @return a non-null array of bytes
	 * @throws IOException
	 */
	public static <T extends Serializable> byte[] serializeObject( T object ) throws IOException {

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream( os );
		out.writeObject( object );

		return os.toByteArray();
	}


	/**
	 * Deserializes an object.
	 * @param bytes a non-null array of bytes
	 * @param clazz the class of the expected object
	 * @return the deserialized object, or null if it failed
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T extends Serializable> T deserializeObject( byte[] bytes, Class<T> clazz )
	throws IOException, ClassNotFoundException {

		ByteArrayInputStream is = new ByteArrayInputStream( bytes );
		ObjectInputStream deserializer = new ObjectInputStream( is );
		return clazz.cast( deserializer.readObject());
	}


	/**
	 * Deserializes a message.
	 * @param bytes a non-null array of bytes
	 * @return the deserialized message, or null if it failed
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Message deserializeObject( byte[] bytes )
	throws IOException, ClassNotFoundException {
		return deserializeObject( bytes, Message.class );
	}
}

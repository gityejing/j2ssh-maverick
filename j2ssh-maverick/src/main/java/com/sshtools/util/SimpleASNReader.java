/**
 * Copyright 2003-2014 SSHTOOLS Limited. All Rights Reserved.
 *
 * For product documentation visit https://www.sshtools.com/
 *
 * This file is part of J2SSH Maverick.
 *
 * J2SSH Maverick is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * J2SSH Maverick is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with J2SSH Maverick.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sshtools.util;

import java.io.IOException;

/**
 * 
 * 
 * @author $author$
 */
public class SimpleASNReader {
	private byte[] data;
	private int offset;

	/**
	 * Creates a new SimpleASNReader object.
	 * 
	 * @param data
	 */
	public SimpleASNReader(byte[] data) {
		this.data = data;
		this.offset = 0;
	}

	/**
	 * 
	 * 
	 * @param b
	 * 
	 * @throws IOException
	 */
	public void assertByte(int b) throws IOException {
		int x = getByte();

		if (x != b) {
			throw new IOException("Assertion failed, next byte value is "
					+ Integer.toHexString(x) + " instead of asserted "
					+ Integer.toHexString(b));
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getByte() {
		return data[offset++] & 0xff;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public byte[] getData() {
		int length = getLength();

		return getData(length);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getLength() {
		int b = data[offset++] & 0xff;

		if ((b & 0x80) != 0) {
			int length = 0;

			for (int bytes = b & 0x7f; bytes > 0; bytes--) {
				length <<= 8;
				length |= (data[offset++] & 0xff);
			}

			return length;
		}

		return b;
	}

	private byte[] getData(int length) {
		byte[] result = new byte[length];
		System.arraycopy(data, offset, result, 0, length);
		offset += length;

		return result;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean hasMoreData() {
		return offset < data.length;
	}
}

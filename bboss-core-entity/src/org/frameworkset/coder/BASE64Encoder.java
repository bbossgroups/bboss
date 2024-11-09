package org.frameworkset.coder;
/**
 * Copyright 2024 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.io.OutputStream;

public class BASE64Encoder extends CharacterEncoder {
	private static final char[] pem_array = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
	
	public BASE64Encoder() {
	}
	
	protected int bytesPerAtom() {
		return 3;
	}
	
	protected int bytesPerLine() {
		return 57;
	}
	
	protected void encodeAtom(OutputStream var1, byte[] var2, int var3, int var4) throws IOException {
		byte var5;
		if (var4 == 1) {
			var5 = var2[var3];
			byte var6 = 0;
			boolean var7 = false;
			var1.write(pem_array[var5 >>> 2 & 63]);
			var1.write(pem_array[(var5 << 4 & 48) + (var6 >>> 4 & 15)]);
			var1.write(61);
			var1.write(61);
		} else {
			byte var8;
			if (var4 == 2) {
				var5 = var2[var3];
				var8 = var2[var3 + 1];
				byte var9 = 0;
				var1.write(pem_array[var5 >>> 2 & 63]);
				var1.write(pem_array[(var5 << 4 & 48) + (var8 >>> 4 & 15)]);
				var1.write(pem_array[(var8 << 2 & 60) + (var9 >>> 6 & 3)]);
				var1.write(61);
			} else {
				var5 = var2[var3];
				var8 = var2[var3 + 1];
				byte var10 = var2[var3 + 2];
				var1.write(pem_array[var5 >>> 2 & 63]);
				var1.write(pem_array[(var5 << 4 & 48) + (var8 >>> 4 & 15)]);
				var1.write(pem_array[(var8 << 2 & 60) + (var10 >>> 6 & 3)]);
				var1.write(pem_array[var10 & 63]);
			}
		}
		
	}
}

// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.

package bboss.org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Map;

/**
 * A {@link Remapper} using a {@link Map} to define its mapping.
 *
 * @author Eugene Kuleshov
 */
public class SimpleRemapper extends Remapper {

  private final Map<String, String> mapping;

  /**
   * Constructs a new {@link SimpleRemapper} with the given mapping.
   *
   * @param mapping a map specifying a remapping as follows:
   *     <ul>
   *       <li>for method names, the key is the owner, name and descriptor of the method (in the
   *           form &lt;owner&gt;.&lt;name&gt;&lt;descriptor&gt;), and the value is the new method
   *           name.
   *       <li>for invokedynamic method names, the key is the name and descriptor of the method (in
   *           the form .&lt;name&gt;&lt;descriptor&gt;), and the value is the new method name.
   *       <li>for field names, the key is the owner and name of the field or attribute (in the form
   *           &lt;owner&gt;.&lt;name&gt;), and the value is the new field name.
   *       <li>for attribute names, the key is the annotation descriptor and the name of the
   *           attribute (in the form &lt;descriptor&gt;.&lt;name&gt;), and the value is the new
   *           attribute name.
   *       <li>for internal names, the key is the old internal name, and the value is the new
   *           internal name (see {@link bboss.org.objectweb.asm.Type#getInternalName()}).
   *     </ul>
   */
  public SimpleRemapper(final Map<String, String> mapping) {
    this.mapping = mapping;
  }

  /**
   * Constructs a new {@link SimpleRemapper} with the given mapping.
   *
   * @param oldName the key corresponding to a method, field or internal name (see {@link
   *     #SimpleRemapper(Map)} for the format of these keys).
   * @param newName the new method, field or internal name (see {@link
   *     bboss.org.objectweb.asm.Type#getInternalName()}).
   */
  public SimpleRemapper(final String oldName, final String newName) {
    this.mapping = Collections.singletonMap(oldName, newName);
  }

  @Override
  public String mapMethodName(final String owner, final String name, final String descriptor) {
    String remappedName = map(owner + '.' + name + descriptor);
    return remappedName == null ? name : remappedName;
  }

  @Override
  public String mapInvokeDynamicMethodName(final String name, final String descriptor) {
    String remappedName = map('.' + name + descriptor);
    return remappedName == null ? name : remappedName;
  }

  @Override
  public String mapAnnotationAttributeName(final String descriptor, final String name) {
    String remappedName = map(descriptor + '.' + name);
    return remappedName == null ? name : remappedName;
  }

  @Override
  public String mapFieldName(final String owner, final String name, final String descriptor) {
    String remappedName = map(owner + '.' + name);
    return remappedName == null ? name : remappedName;
  }

  @Override
  public String map(final String key) {
    return mapping.get(key);
  }
}

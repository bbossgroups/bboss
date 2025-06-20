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

import bboss.org.objectweb.asm.ModuleVisitor;
import bboss.org.objectweb.asm.Opcodes;

/**
 * A {@link ModuleVisitor} that remaps types with a {@link Remapper}.
 *
 * @author Remi Forax
 */
public class ModuleRemapper extends ModuleVisitor {

  /** The remapper used to remap the types in the visited module. */
  protected final Remapper remapper;

  /**
   * Constructs a new {@link ModuleRemapper}. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #ModuleRemapper(int,ModuleVisitor,Remapper)} version.
   *
   * @param moduleVisitor the module visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited module.
   */
  public ModuleRemapper(final ModuleVisitor moduleVisitor, final Remapper remapper) {
    this(/* latest api = */ Opcodes.ASM9, moduleVisitor, remapper);
  }

  /**
   * Constructs a new {@link ModuleRemapper}.
   *
   * @param api the ASM API version supported by this remapper. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param moduleVisitor the module visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited module.
   */
  protected ModuleRemapper(
      final int api, final ModuleVisitor moduleVisitor, final Remapper remapper) {
    super(api, moduleVisitor);
    this.remapper = remapper;
  }

  @Override
  public void visitMainClass(final String mainClass) {
    super.visitMainClass(remapper.mapType(mainClass));
  }

  @Override
  public void visitPackage(final String packaze) {
    super.visitPackage(remapper.mapPackageName(packaze));
  }

  @Override
  public void visitRequire(final String module, final int access, final String version) {
    super.visitRequire(remapper.mapModuleName(module), access, version);
  }

  @Override
  public void visitExport(final String packaze, final int access, final String... modules) {
    String[] remappedModules = null;
    if (modules != null) {
      remappedModules = new String[modules.length];
      for (int i = 0; i < modules.length; ++i) {
        remappedModules[i] = remapper.mapModuleName(modules[i]);
      }
    }
    super.visitExport(remapper.mapPackageName(packaze), access, remappedModules);
  }

  @Override
  public void visitOpen(final String packaze, final int access, final String... modules) {
    String[] remappedModules = null;
    if (modules != null) {
      remappedModules = new String[modules.length];
      for (int i = 0; i < modules.length; ++i) {
        remappedModules[i] = remapper.mapModuleName(modules[i]);
      }
    }
    super.visitOpen(remapper.mapPackageName(packaze), access, remappedModules);
  }

  @Override
  public void visitUse(final String service) {
    super.visitUse(remapper.mapType(service));
  }

  @Override
  public void visitProvide(final String service, final String... providers) {
    String[] remappedProviders = new String[providers.length];
    for (int i = 0; i < providers.length; ++i) {
      remappedProviders[i] = remapper.mapType(providers[i]);
    }
    super.visitProvide(remapper.mapType(service), remappedProviders);
  }
}

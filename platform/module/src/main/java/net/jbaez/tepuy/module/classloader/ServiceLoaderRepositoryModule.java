/*******************************************************************************
 * Copyright (C) 2017 Jesus Baez
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package net.jbaez.tepuy.module.classloader;

import java.util.Collections;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.Set;
import java.util.stream.Collectors;

import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.module.RequiredVersion;
import net.jbaez.tepuy.module.engine.ClassLoaderSupplier;
import net.jbaez.tepuy.module.engine.RepositoryModules;

public class ServiceLoaderRepositoryModule implements RepositoryModules {

  private Set<PlatformModule> modules;
  private ClassLoaderSupplier classLoaderSupplier;
  
  public ServiceLoaderRepositoryModule(ClassLoaderSupplier supplier)
  {
    this.classLoaderSupplier = supplier;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<PlatformModule> findAll() {
    if(modules == null)
    {
      modules = readModules();
    }
    
    return Collections.unmodifiableSet(modules);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<PlatformModule> findOne(String moduleId, RequiredVersion version) throws IllegalStateException {
    if(modules == null)
    {
      modules = readModules();
    }
    
    return modules.parallelStream().filter(module -> {
      return module.getModuleId().equals(moduleId) &&
             version.isValid(module.getVersion());
    }).findAny();
  }
  
  private Set<PlatformModule> readModules()
  {
    ClassLoader moduleClassLoader = classLoaderSupplier.get();
    ServiceLoader<PlatformModule> serviceLoader = ServiceLoader.load(PlatformModule.class, moduleClassLoader);
    return serviceLoader.stream().map(Provider::get).collect(Collectors.toSet());
  }

}

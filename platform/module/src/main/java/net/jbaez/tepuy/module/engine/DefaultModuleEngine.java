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

package net.jbaez.tepuy.module.engine;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import net.jbaez.tepuy.module.PlatformModule;

/**
 * <p> Implementacion por defecto del motor 
 * de modulos
 * @author Jesus Baez
 */
public class DefaultModuleEngine implements ModuleEngine {

  private MainContextSupplier mainContextSupplier;
  private ClassLoaderSupplier classLoaderSupplier;
  private ContextLayout contextLayout;
  
  private boolean initiated;
  private AbstractApplicationContext mainContext;
  
  public DefaultModuleEngine(MainContextSupplier mainContextSupplier, 
      ClassLoaderSupplier classLoaderSupplier, ContextLayout contextLayout) {
    this.mainContextSupplier = mainContextSupplier;
    this.classLoaderSupplier = classLoaderSupplier;
    this.contextLayout = contextLayout;
  }

  @Override
  public void start() {
    if(this.initiated)
    {
      throw new IllegalStateException("The engine is already started");
    }
    
    this.mainContext = mainContextSupplier.get();
    configureMainContext(mainContext);
    
    Set<PlatformModule> modules = loadModules(classLoaderSupplier);
    configureModules(modules, mainContext);
    this.initiated = true;
  }

  @Override
  public void stop() {
    if(this.initiated)
    {
      throw new IllegalStateException("The engine is not started");
    }
    
    this.mainContext.close();
  }
  
  protected Set<PlatformModule> loadModules(ClassLoaderSupplier classLoaderSupplier)
  {
    ClassLoader moduleClassLoader = classLoaderSupplier.get();
    ServiceLoader<PlatformModule> serviceLoader = ServiceLoader.load(PlatformModule.class, moduleClassLoader);
    return serviceLoader.stream().map(Provider::get).collect(Collectors.toSet());
  }
  
  protected void configureModules(Set<PlatformModule> modules, ApplicationContext context)
  {
    this.contextLayout.initialize(context);
    modules.forEach(module -> this.contextLayout.addModule(module));
  }
  
  protected void configureMainContext(AbstractApplicationContext ctx)
  {
    //
  }

}

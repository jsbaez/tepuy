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

package net.jbaez.tepuy.module.context;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.module.engine.ContextLayout;

public class HierarchicalContextLayout implements ContextLayout {

  private ApplicationContext mainContext;
  
  @Override
  public void initialize(ApplicationContext ctx) {
    this.mainContext = ctx;
  }

  public void addModule(PlatformModule module) {
    
    AnnotationConfigApplicationContext moduleContext = createApplicationContext(module);
    moduleContext.refresh();
  }
  
  protected AnnotationConfigApplicationContext createApplicationContext(PlatformModule module)
  {
    Set<Class<?>> configClass = module.getConfigClass();
    AnnotationConfigApplicationContext moduleContext = new AnnotationConfigApplicationContext();
    moduleContext.setParent(mainContext);
    moduleContext.register(configClass.toArray(new Class[] {}));
    return moduleContext;
  }

}

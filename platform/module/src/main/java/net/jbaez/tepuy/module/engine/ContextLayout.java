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

import org.springframework.context.ApplicationContext;

import net.jbaez.tepuy.module.PlatformModule;

/**
 * <p> Define la manera como se debe organizar
 * los contextos de los modulos
 * @author Jesus Baez
 */
public interface ContextLayout 
{

  /**
   * <p>Inicializa el layout 
   * @param ctx Context raiz de la aplicacion
   */
  void initialize(ApplicationContext ctx);
  /**
   * <p> Anade el modulo en el layout 
   * @param module {@link PlatformModule} a anadir
   */
  void addModule(PlatformModule module);
}

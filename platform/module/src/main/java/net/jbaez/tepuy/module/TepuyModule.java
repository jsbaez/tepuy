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
package net.jbaez.tepuy.module;

import java.util.List;
import java.util.Set;

/**
 * <p> Interfaz que declara los metodos comunes a 
 * un modulo
 * @author Jesus Baez
 */
public interface TepuyModule 
{
  /**
   * @return Id del modulo
   */
  String getModuleId();
  
  /**
   * @return Version de la plataforma compatible
   */
  RequiredVersion getPlatform();

  /**
   * @return Listado de los modulos requerido 
   * por este modulo
   */
  List<Dependency> getDependencies();
  
  /**
   * <p> Retorna las clases configuracion de contextos 
   * @return Lista con las clases de configuracion
   */
  Set<Class<?>> getConfigClass();
}

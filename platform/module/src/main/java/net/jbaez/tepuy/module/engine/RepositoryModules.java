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

import java.util.Optional;
import java.util.Set;

import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.module.RequiredVersion;

/**
 * <p> Interfaz que suministra los metodos
 * de un repositorio de {@link PlatformModule}
 * @author Jesus Baez
 */
public interface RepositoryModules 
{
  /**
   * @return Todos los modulos que se encuentran
   * en el repositorio
   */
  Set<PlatformModule> findAll();
  /**
   * <p> Realiza la busqueda de un modulo
   * que tenga el id igual al pasado como parametro
   * y cumpla con el {@link RequiredVersion}
   * @param moduleId Id del modulo a buscar
   * @param version {@link RequiredVersion} a buscar
   * @return El {@link Optional} que contiene el resultado
   * @throws IllegalStateException Si existe mas de un modulo
   * que satisface 
   */
  Optional<PlatformModule> findOne(String moduleId, RequiredVersion version) throws IllegalStateException;
}

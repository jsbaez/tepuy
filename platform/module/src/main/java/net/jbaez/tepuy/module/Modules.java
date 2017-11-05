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

import java.util.regex.Pattern;

import net.jbaez.tepuy.version.Version;
import net.jbaez.tepuy.version.Versions;

/**
 * <p> Clase de ayuda para el trabajo con {@link Module}'s,
 * {@link RequiredVersion} y otros
 * @author Jesus Baez
 */
public class Modules 
{
  private Modules() {}
  
  /**
   * <p>Expresion regular para comprobar una version exacta
   */
  public static final Pattern EXACTLY_VERSION = Versions.VERSION_PATTERN;
  
  /**
   * <p> Expresion regular para comprobar un version mayor o
   * igual a la informada
   */
  public static final Pattern GREATER_THAN_EQUAL_VERSION = Pattern.compile(">=[0-9]+\\.[0-9]+\\.[0-9]+");
  
  /**
   * <p> Expresion regular para comprobar un version menor o
   * igual a la informada
   */
  public static final Pattern LESS_THAN_EQUAL_VERSION = Pattern.compile("<=[0-9]+\\.[0-9]+\\.[0-9]+");
  
  /**
   * <p> Expresion regular para comprobar un rango de versiones
   */
  public static final Pattern RANGE_VERSION = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+\\-[0-9]+\\.[0-9]+\\.[0-9]+");
  
  /**
   * <p> Parsea una cadena de texto y la transforma en 
   * un {@link RequiredVersion}
   * @param value Valor a parsear
   * @return El {@link RequiredVersion} de la cadena
   * @throws IllegalArgumentException si la cadena no posee un texto valido
   */
  public static RequiredVersion parseRequiredVersion(String value) throws IllegalArgumentException
  {
    if(EXACTLY_VERSION.matcher(value).matches())
    {
      return new ExactlyVersion(Versions.parseVersion(value));
    }
    
    if(GREATER_THAN_EQUAL_VERSION.matcher(value).matches())
    {
      String version = value.replaceAll(Pattern.quote(">="), "");
      return new GreaterThanEqualVersion(Versions.parseVersion(version));
    }
    
    if(LESS_THAN_EQUAL_VERSION.matcher(value).matches())
    {
      String version = value.replaceAll(Pattern.quote("<="), "");
      return new LessThanEqualVersion(Versions.parseVersion(version));
    }
    
    if(RANGE_VERSION.matcher(value).matches())
    {
      String[] versions = value.split(Pattern.quote("-"));
      return new RangeVersion(Versions.parseVersion(versions[0]), 
                              Versions.parseVersion(versions[1]));
    }
    
    throw new IllegalArgumentException("The text does not contain a required version");
  }
  
  /**
   * <p> Valida una {@link Version} exacta
   * @author Jesus Baez
   */
  static class ExactlyVersion implements RequiredVersion
  {
    private Version requiredVersion;
    
    ExactlyVersion(Version requiredVersion)
    {
      this.requiredVersion = requiredVersion;
    }
    

    @Override
    public boolean isValid(Version version) 
    {
      if(version == null)
      {
        return false;
      }
      
      return version.compareTo(requiredVersion) == 0;
    }
    
  }
  
  /**
   * <p> Valida que la {@link Version} sea mayor o igual
   * @author Jesus Baez
   */
  static class GreaterThanEqualVersion implements RequiredVersion
  {
    private Version requiredVersion;

    GreaterThanEqualVersion(Version requiredVersion)
    {
      this.requiredVersion = requiredVersion;
    }
    
    @Override
    public boolean isValid(Version version) 
    {
      if(version == null)
      {
        return false;
      }
      
      return version.compareTo(requiredVersion) >= 0;
    }
    
  }
  
  /**
   * <p> Valida que la {@link Version} sea menor o igual
   * @author Jesus Baez
   */
  static class LessThanEqualVersion implements RequiredVersion
  {
    private Version requiredVersion;

    LessThanEqualVersion(Version requiredVersion)
    {
      this.requiredVersion = requiredVersion;
    }

    @Override
    public boolean isValid(Version version) {
      if(version == null)
      {
        return false;
      }
      
      return version.compareTo(requiredVersion) <= 0;
    }
    
  }
  
  /**
   * <p> Valida un rango de version
   * @author Jesus Baez
   */
  static class RangeVersion implements RequiredVersion
  {
    private Version startVersion,
                    endVersion;
    
    RangeVersion(Version startVersion, Version endVersion)
    {
      this.startVersion = startVersion;
      this.endVersion = endVersion;
    }

    @Override
    public boolean isValid(Version version) {
      if(version == null)
      {
        return false;
      }
      
      return version.compareTo(startVersion) >= 0
             && version.compareTo(endVersion) <= 0;
    }

  }

  
}

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

package net.jbaez.tepuy.version;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * <p>Clase de utilidad para el trabajo con versiones
 * <p>El sisteam de verisones utilizado es el modelo semantico
 * @see http://semver.org/
 * @author Jesus Baez
 */
public final class Versions {

  private Versions() {}
  
  /**
   * <p>Expresion regular para comprobar que un texto posee
   * un numero de version valido
   */
  public static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+");
  
  /**
   * <p> Crea una version con la informacion suministrada
   * @param major Numero major
   * @param minor Numero minor
   * @param patch Numero patch
   * @return La {@link Version} creada
   */
  public static Version createVersion(int major, int minor, int patch)
  {
    return new SimpleVersion(major, minor, patch);
  }
  
  /**
   * <p> Parseara la cadena y creara una version con la informacion
   * contenida en el texto
   * @param expre Cadena que debe tener el formato 
   * <numero_major>.<numero_minor>.<numero.patch>
   * @return La {@link Version} creada
   * @throws Si la cadena no cumple con el formato esperado
   */
  public static Version parseVersion(String expre) throws IllegalArgumentException
  {
    if(!VERSION_PATTERN.matcher(expre).matches())
    {
      throw new IllegalArgumentException("The text does not contain a version");
    }
    
    String[] parts = expre.split("\\.");
    
    try {
      return new SimpleVersion(Integer.parseInt(parts[0]), 
                               Integer.parseInt(parts[1]), 
                               Integer.parseInt(parts[2]));
    }catch(NumberFormatException nfe) {
      throw new IllegalArgumentException("One of the parts is not a number");
    }
  }

  static class SimpleVersion implements Version 
  {
    private int major = 0,
                minor = 0,
                patch = 0;

    private SimpleVersion(int major, int minor, int patch) {
      this.major = major;
      this.minor = minor;
      this.patch = patch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Version other) 
    {
      int otherMajor = other.getMajor(),
          otherMinor = other.getMinor(),
          otherPatch = other.getPatch();
      
      if(this.major != otherMajor)
      {
        return this.major - otherMajor;
      }
      
      if(this.minor != otherMinor)
      {
        return this.minor - otherMinor;
      }
      
      if(this.patch != otherPatch)
      {
        return this.patch - otherPatch;
      }
      
      return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMajor() 
    {
      return major;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinor() 
    {
      return minor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPatch() 
    {
      return patch;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
      String template = "{0,number,#}.{1,number,#}.{2,number,#}";
      return MessageFormat.format(template, getMajor(), getMinor(), getPatch());
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + major;
      result = prime * result + minor;
      result = prime * result + patch;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      SimpleVersion other = (SimpleVersion) obj;
      if (major != other.major)
        return false;
      if (minor != other.minor)
        return false;
      if (patch != other.patch)
        return false;
      return true;
    }
    
  }
}

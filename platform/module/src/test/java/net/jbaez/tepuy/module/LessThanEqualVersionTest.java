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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.jbaez.tepuy.version.Version;
import net.jbaez.tepuy.version.Versions;

public class LessThanEqualVersionTest 
{

  private String versionString = "1.0.1";
  
  private Version versionEquals = Versions.parseVersion(versionString),
                  versionMajor = Versions.parseVersion("1.0.2"),
                  versionMinor = Versions.parseVersion("1.0.0");
  
  @Test
  public void test_valid()
  {
    RequiredVersion requiredVersion = Modules.parseRequiredVersion("<=" + versionString);
    Assertions.assertTrue(requiredVersion.isValid(versionEquals));
    Assertions.assertTrue(requiredVersion.isValid(versionMinor));
  }
  
  @Test
  public void test_not_valid()
  {
    RequiredVersion requiredVersion = Modules.parseRequiredVersion("<=" + versionString);
    Assertions.assertFalse(requiredVersion.isValid(versionMajor));
  }
}

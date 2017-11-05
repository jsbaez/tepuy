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

public class RangeVersionTest 
{
  
  private String startVersion = "0.1.0",
                 endVersion = "2.1.0";
  
  private String requiredVersionString = startVersion + "-" + endVersion;
  
  @Test
  public void test_valid()
  {
    RequiredVersion requiredVersion = Modules.parseRequiredVersion(requiredVersionString);
    
    Version startVersion = Versions.parseVersion(this.startVersion),
            endVersion = Versions.parseVersion(this.endVersion),
            otherVersion = Versions.parseVersion("0.5.0"),
            otherVersion2 = Versions.parseVersion("2.0.1");
    
    Assertions.assertTrue(requiredVersion.isValid(startVersion));
    Assertions.assertTrue(requiredVersion.isValid(endVersion));
    Assertions.assertTrue(requiredVersion.isValid(otherVersion2));
    Assertions.assertTrue(requiredVersion.isValid(otherVersion));
  }
  
  @Test
  public void test_not_valid()
  {
    RequiredVersion requiredVersion = Modules.parseRequiredVersion(requiredVersionString);
    
    Version otherVersion = Versions.parseVersion("0.0.1"),
            otherVersion2 = Versions.parseVersion("2.1.1");

    Assertions.assertFalse(requiredVersion.isValid(otherVersion));
    Assertions.assertFalse(requiredVersion.isValid(otherVersion2));
  }

}

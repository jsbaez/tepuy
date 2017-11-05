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

import net.jbaez.tepuy.module.Modules.ExactlyVersion;
import net.jbaez.tepuy.module.Modules.GreaterThanEqualVersion;
import net.jbaez.tepuy.module.Modules.LessThanEqualVersion;
import net.jbaez.tepuy.module.Modules.RangeVersion;

public class ModulesTest 
{

  @Test
  public void test_exactlyVersion()
  {
    RequiredVersion version = Modules.parseRequiredVersion("1.2.3");
    Assertions.assertEquals(ExactlyVersion.class, version.getClass());
  }
  
  @Test
  public void test_greaterThanEqualVersion()
  {
    RequiredVersion version = Modules.parseRequiredVersion(">=1.2.3");
    Assertions.assertEquals(GreaterThanEqualVersion.class, version.getClass());
  }
  
  @Test
  public void test_lessThanEqualVersion()
  {
    RequiredVersion version = Modules.parseRequiredVersion("<=1.2.3");
    Assertions.assertEquals(LessThanEqualVersion.class, version.getClass());
  }
  
  @Test
  public void test_rangeVersion()
  {
    RequiredVersion version = Modules.parseRequiredVersion("1.2.3-1.3.3");
    Assertions.assertEquals(RangeVersion.class, version.getClass());
  }
}

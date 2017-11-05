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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionsTest {
  
  @Test
  public void test_createVersion()
  {
    Version version = Versions.parseVersion("0.0.0");
    Assertions.assertEquals("0.0.0", version.toString());
  }
  
  @Test
  public void test_compareTo()
  {
    Version startVersion = Versions.parseVersion("1.0.0"),
            endVersion = Versions.parseVersion("1.1.0");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) < 0;
    });
  }
  
  @Test
  public void test_compareTo_equals()
  {
    Version startVersion = Versions.parseVersion("1.0.0"),
            endVersion = Versions.parseVersion("1.0.0");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) == 0;
    });
  }
  
  @Test
  public void test_compareTo_2()
  {
    Version startVersion = Versions.parseVersion("1.0.0"),
            endVersion = Versions.parseVersion("1.0.1");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) < 0;
    });
  }
  
  @Test
  public void test_compareTo_3()
  {
    Version startVersion = Versions.parseVersion("1.0.0"),
            endVersion = Versions.parseVersion("2.0.0");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) < 0;
    });
  }
  
  @Test
  public void test_compareTo_4()
  {
    Version startVersion = Versions.parseVersion("2.0.0"),
            endVersion = Versions.parseVersion("1.0.0");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) > 0;
    });
  }
  
  @Test
  public void test_compareTo_5()
  {
    Version startVersion = Versions.parseVersion("2.0.0"),
            endVersion = Versions.parseVersion("2.1.0");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) < 0;
    });
  }
  
  @Test
  public void test_compareTo_6()
  {
    Version startVersion = Versions.parseVersion("2.0.0"),
            endVersion = Versions.parseVersion("2.0.1");
    
    Assertions.assertTrue(() -> {
     return  startVersion.compareTo(endVersion) < 0;
    });
  }
  
  @Test()
  public void test_createVersion_fail()
  {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Versions.parseVersion("0.a.0"));
  }
  
  @Test()
  public void test_createVersion_fail_2()
  {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Versions.parseVersion("1.0"));
  }
  
  @Test()
  public void test_createVersion_fail_3()
  {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> Versions.parseVersion("1.0.z"));
  }

}

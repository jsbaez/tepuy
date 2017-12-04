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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.jbaez.tepuy.module.util.JsonTestModule;
import net.jbaez.tepuy.version.Versions;

public class JsonModuleDescriptorTest {

  @Test
  public void test_module_parse()
  {
    PlatformModule module = new JsonTestModule("module-1");
    RequiredVersion plataform = module.getPlatform();
    
    Assertions.assertFalse(plataform.isValid(Versions.parseVersion("0.0.1")));
    Assertions.assertFalse(plataform.isValid(Versions.parseVersion("0.0.2")));
    Assertions.assertTrue(plataform.isValid(Versions.parseVersion("0.1.2")));
    Assertions.assertTrue(plataform.isValid(Versions.parseVersion("0.1.0")));
    
    List<Dependency> dependencies = module.getDependencies();
    Assertions.assertNotNull(dependencies);
    Assertions.assertEquals(2, dependencies.size());
    
    Dependency dependency0 = dependencies.get(0),
               dependency1 = dependencies.get(1);
    
    Assertions.assertEquals("module-b", dependency0.getModuleId());
    Assertions.assertEquals("module-a", dependency1.getModuleId());
    
    RequiredVersion required0 = dependency0.getVersion(),
                    required1 = dependency1.getVersion();
    
    
    Assertions.assertTrue(required1.isValid(Versions.parseVersion("0.0.1")));
    Assertions.assertFalse(required1.isValid(Versions.parseVersion("0.0.2")));
    
    Assertions.assertTrue(required0.isValid(Versions.parseVersion("0.0.1")));
    Assertions.assertTrue(required0.isValid(Versions.parseVersion("0.0.2")));
    Assertions.assertFalse(required0.isValid(Versions.parseVersion("0.0.3")));
  }
  
  @Test
  public void test_module_sin_plataforma()
  {
    PlatformModule module = new JsonTestModule("module-sin-plataforma");
    
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      module.getPlatform();
    });
  }
  
  @Test
  public void test_module_sin_json()
  {
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      new JsonTestModule("module-mal-json");
    });
  }
  
  @Test
  public void test_module_mal_versiones()
  {
    PlatformModule module = new JsonTestModule("module-mal-versiones");
    
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      module.getPlatform();
    });
    
    Assertions.assertThrows(IllegalArgumentException.class, ()->{
      module.getDependencies();
    });
  }
}

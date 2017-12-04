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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.jbaez.tepuy.module.Modules;
import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.module.RequiredVersion;
import net.jbaez.tepuy.module.context.HierarchicalContextLayout;
import net.jbaez.tepuy.module.context.SimpleMainContextSupplier;
import net.jbaez.tepuy.module.util.ModuleTestBuilder;
import net.jbaez.tepuy.version.Version;
import net.jbaez.tepuy.version.Versions;

public class DefaultModuleEngineTest 
{
  private Version platformVersion;
 
  private List<PlatformModule> modules = new ArrayList<>();
  
  private RepositoryModules repositoryModules;
  
  private DefaultModuleEngine defaultModuleEngine;
  
  private RequiredVersion requiredPlatformVersion,
                          otherRequiredPlatformVersion;
  
  @BeforeEach
  public void before()
  {
    platformVersion = Versions.parseVersion("0.0.1");
    
    requiredPlatformVersion = Modules.parseRequiredVersion(">=0.0.1");
    otherRequiredPlatformVersion = Modules.parseRequiredVersion(">=0.0.2");
    
    modules = new ArrayList<>();
    repositoryModules = new TestRepositoryModules(modules);
    
    defaultModuleEngine = new DefaultModuleEngine(
        platformVersion, new SimpleMainContextSupplier(), 
        repositoryModules, new HierarchicalContextLayout()
    );
  }

  @Test
  public void test_platform_not_valid()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule module = builder.moduleId("platform-not-valid")
        .platformVersion(otherRequiredPlatformVersion)
        .version("0.0.1")
        .build();
    
    modules.add(module);
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
  }
  
  @Test
  public void test_platform_not_valid_2()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule module = builder.moduleId("platform-valid")
        .platformVersion(requiredPlatformVersion)
        .version("0.0.1")
        .build();
    modules.add(module);
    
    module = builder.moduleId("platform-not-valid")
        .platformVersion(otherRequiredPlatformVersion)
        .version("0.0.1")
        .build();
    
    modules.add(module);
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
  }
  
   
  @Test
  public void test_module_duplicate()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule module = builder.moduleId("platform-valid")
        .platformVersion(requiredPlatformVersion)
        .version("0.0.1")
        .build();
    modules.add(module);
    
    module = builder.moduleId("platform-valid")
        .platformVersion(requiredPlatformVersion)
        .version("0.0.2")
        .build();
    modules.add(module);
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
  }
  
  @Test
  public void test_modules_cycles()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule moduleA = builder.moduleId("module-a")
        .platformVersion(requiredPlatformVersion)
        .addDependency("module-b", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleA);
    
    PlatformModule moduleB = builder.moduleId("module-b")
        .platformVersion(requiredPlatformVersion)
        .cleanDependencies()
        .addDependency("module-a", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleB);
    
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
    
  }
  
  @Test
  public void test_modules_cycles_2()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule moduleA = builder.moduleId("module-a")
        .platformVersion(requiredPlatformVersion)
        .addDependency("module-b", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleA);
    
    PlatformModule moduleB = builder.moduleId("module-b")
        .platformVersion(requiredPlatformVersion)
        .cleanDependencies()
        .addDependency("module-c", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleB);
    
    PlatformModule moduleC = builder.moduleId("module-b")
        .platformVersion(requiredPlatformVersion)
        .cleanDependencies()
        .addDependency("module-a", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleC);
    
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
  }
  
  @Test
  public void test_dependency_not_exist()
  {
    ModuleTestBuilder builder = ModuleTestBuilder.createInstance();
    PlatformModule moduleA = builder.moduleId("module-a")
        .platformVersion(requiredPlatformVersion)
        .addDependency("module-b", "0.0.1")
        .addDependency("module-not-exist", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleA);
    
    PlatformModule moduleB = builder.moduleId("module-b")
        .platformVersion(requiredPlatformVersion)
        .cleanDependencies()
        .addDependency("module-c", "0.0.1")
        .version("0.0.1")
        .build();
    modules.add(moduleB);
    
    Assertions.assertThrows(IllegalStateException.class, 
        () -> defaultModuleEngine.start());
  }

  private static class TestRepositoryModules implements RepositoryModules
  {

    private List<PlatformModule> modules;
    
    public TestRepositoryModules(List<PlatformModule> modules) 
    {
      this.modules = modules;
    }

    @Override
    public Set<PlatformModule> findAll() 
    {
      return new HashSet<>(modules);
    }

    @Override
    public Optional<PlatformModule> findOne(String moduleId, RequiredVersion version) throws IllegalStateException 
    {
      return modules.stream().filter(module -> {
        return module.getModuleId().equals(moduleId) && version.isValid(module.getVersion());
      }).findAny();
    }
    
  }
  
}

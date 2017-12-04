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

package net.jbaez.tepuy.module.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jbaez.tepuy.module.Dependency;
import net.jbaez.tepuy.module.Modules;
import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.module.RequiredVersion;
import net.jbaez.tepuy.module.context.SpringEmptyConfig;
import net.jbaez.tepuy.version.Version;
import net.jbaez.tepuy.version.Versions;

/**
 * <p> Clase de utilidad para crear
 * {@link PlatformModule} para test
 * @author Jesus Baez
 */
public class ModuleTestBuilder 
{

  private String moduleId;
  private Version version;
  private RequiredVersion platformVersion;
  private List<Dependency> dependencies;
  
  private ModuleTestBuilder()
  {
    
  }
  
  public static ModuleTestBuilder createInstance()
  {
    return new ModuleTestBuilder();
  }
  
  public ModuleTestBuilder version(String version)
  {
    this.version = Versions.parseVersion(version);
    return this;
  }
  
  public ModuleTestBuilder moduleId(String moduleId)
  {
    this.moduleId = moduleId;
    return this;
  }
  
  public ModuleTestBuilder platformVersion(String version)
  {
    this.platformVersion = Modules.parseRequiredVersion(version);
    return this;
  }
  
  public ModuleTestBuilder platformVersion(RequiredVersion version)
  {
    this.platformVersion = version;
    return this;
  }
  
  public ModuleTestBuilder addDependency(String moduleId, String version)
  {
    RequiredVersion requiredVersion = Modules.parseRequiredVersion(version);
    
    if(dependencies == null)
    {
      dependencies = new ArrayList<>();
    }
    
    dependencies.add(new TestDependency(moduleId, requiredVersion));
    return this;
  }
  
  public ModuleTestBuilder addDependency(PlatformModule module)
  {
    addDependency(module.getModuleId(), module.getVersion().toString());
    return this;
  }
  
  public ModuleTestBuilder cleanDependencies()
  {
    if(dependencies != null)
    {
      dependencies.clear();
    }
    
    return this;
  }
  
  public PlatformModule build()
  {
    List<Dependency> list = dependencies == null ? Collections.emptyList() : dependencies;
    return new TestModule(moduleId, version, platformVersion, list, null);
  }
  
  private static class TestModule implements PlatformModule
  {
    private String moduleId;
    private Version version;
    private Class<?> config;
    private RequiredVersion platformVersion;
    private List<Dependency> dependencies;

    public TestModule(String moduleId, Version version, RequiredVersion platformVersion,
        List<Dependency> dependencies, Class<?> config) {
      this.config = config;
      this.version = version;
      this.moduleId = moduleId;
      this.dependencies = dependencies;
      this.platformVersion = platformVersion;
    }

    @Override
    public String getModuleId() 
    {
      return moduleId;
    }

    @Override
    public Version getVersion() 
    {
      return version;
    }

    @Override
    public RequiredVersion getPlatform() 
    {
      return platformVersion;
    }

    @Override
    public List<Dependency> getDependencies() 
    {
      if(dependencies == null)
      {
        Collections.emptyList();
      }
      
      return dependencies;
    }

    @Override
    public Class<?> getConfigClass() 
    {
      return config == null ? SpringEmptyConfig.class : config;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
      result = prime * result + ((version == null) ? 0 : version.hashCode());
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
      TestModule other = (TestModule) obj;
      if (moduleId == null) {
        if (other.moduleId != null)
          return false;
      } else if (!moduleId.equals(other.moduleId))
        return false;
      if (version == null) {
        if (other.version != null)
          return false;
      } else if (!version.equals(other.version))
        return false;
      return true;
    }

  }
  
  private static class TestDependency implements Dependency
  {

    private String moduleId;
    private RequiredVersion version;

    public TestDependency(String moduleId, RequiredVersion version) {
      this.moduleId = moduleId;
      this.version = version;
    }

    @Override
    public String getModuleId() 
    {
      return moduleId;
    }

    @Override
    public RequiredVersion getVersion() 
    {
      return version;
    }
    
  }

}

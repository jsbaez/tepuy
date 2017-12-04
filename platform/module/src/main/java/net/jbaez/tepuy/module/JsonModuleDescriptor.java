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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.jbaez.tepuy.version.Version;
import net.jbaez.tepuy.version.Versions;

/**
 * <p> Implementacion de un {@link PlatformModule} que obtiene
 * la informacion del modulo de un archivo JSON ubicado en 
 * la carpeta META-INF
 * @author Jesus Baez
 */
public abstract class JsonModuleDescriptor implements PlatformModule 
{

  private static final String PROP_PLATFORM = "platform";
  private static final String PROP_DEPENDENCIES = "dependencies";
  private static final String PROP_VERSION = "version";
  
  private String moduleId;
  private Version version;
  private List<Dependency> dependencies;
  private RequiredVersion platformVersion;
  private WeakReference<JSONObject> jsonReference;
  
  public JsonModuleDescriptor(String moduleId)
  {
    this.moduleId = moduleId;
    JSONObject json = readJsonModuleDescriptor(moduleId);
    this.jsonReference = new WeakReference<>(json);
  }
  
  @Override
  public String getModuleId() 
  {
    return moduleId;
  }
  
  @Override
  public Version getVersion() {
    if(version != null)
    {
      return version;
    }
    
    String strVersion = readProperty(PROP_VERSION, String.class, null);
    if(strVersion == null)
    {
      throw new IllegalArgumentException("The version is mandatory");
    }
    
    version = Versions.parseVersion(strVersion);
    return version;
  }

  @Override
  public RequiredVersion getPlatform() 
  {
    if(platformVersion != null) 
    {
      return platformVersion;
    }
    
    String version = readProperty(PROP_PLATFORM, String.class, null);
    if(version == null)
    {
      throw new IllegalArgumentException("The platform version is mandatory");
    }
    
    platformVersion = Modules.parseRequiredVersion(version);
    return platformVersion;
  }

  @Override
  public List<Dependency> getDependencies() 
  {
    if(this.dependencies != null)
    {
      return this.dependencies;
    }
    
    final JSONObject jsonDependencies = readProperty(PROP_DEPENDENCIES, JSONObject.class, null);
    if(jsonDependencies == null)
    {
      return Collections.emptyList(); 
    }
    
    Iterator<?> keys = jsonDependencies.keySet().iterator();
    Object key = null, version = null;
    List<Dependency> dependencies = new ArrayList<>();
    while(keys.hasNext())
    {
      key = keys.next();
      version = key != null ? jsonDependencies.get(key) : null;
      
      if(moduleId == null || version == null)
      {
        continue;
      }
      
      dependencies.add(
          new DependencyImpl(key.toString(), Modules.parseRequiredVersion(version.toString()))
      );
    }
    
    this.dependencies = dependencies;
    return this.dependencies;
  }
  
  @Override
  public boolean equals(Object other) {
    boolean isModule = PlatformModule.class.isInstance(other);
    if(!isModule)
    {
      return false;
    }
    
    PlatformModule otherModule = PlatformModule.class.cast(other);
    return this.getModuleId().equals(otherModule.getModuleId()) &&
        this.getVersion().equals(otherModule.getVersion());
  }

  @Override
  public int hashCode() {
    return "TEPUY_MODULE".hashCode() + 
            getModuleId().hashCode() + 
            getVersion().hashCode();
  }

  protected InputStream readFile(String moduleId)
  {
    String file = createFileName(moduleId);
    ClassLoader loader = this.getClass().getClassLoader();
    return loader.getResourceAsStream(file);
  }
  
  protected JSONObject readJsonModuleDescriptor(String moduleId)
  {
    InputStream in = readFile(moduleId);
    if(in == null)
    {
      String message = "The description file for module ''{0}'' does not exist";
      message = MessageFormat.format(message, moduleId);
      throw new IllegalArgumentException(message);
    }
    
    try {
      return (JSONObject) new JSONParser().parse(new InputStreamReader(in));
    } catch (IOException | ParseException ex) {
      String message = "Check the file ''{0}''",
             fileName = createFileName(moduleId);
      
      message = MessageFormat.format(message, fileName);
      throw new IllegalArgumentException(message, ex);
    }
  }
  
  protected String createFileName(String moduleName)
  {
    String file = "META-INF/{0}.tm.json";
    return MessageFormat.format(file, this.moduleId);
  }
  
  private <T> T readProperty(String propertyPath, Class<T> type, T defaultValue)
  {
    String[] properties = propertyPath.split(Pattern.quote("."));
    JSONObject json = jsonReference.get();
    
    if(json == null)
    {
      json = readJsonModuleDescriptor(this.moduleId);
      this.jsonReference = new WeakReference<>(json);
    }
    
    Object value = json;
    for(int i = 0; i < properties.length && value != null; i++)
    {
      value = json.get(properties[i]);
    }
    
    if(value == null)
    {
      return defaultValue;
    }
    
    try {
      return type.cast(value);
    }catch (ClassCastException ex) {
      String message = "The property ''{0}'' is not type ''{1}'', check the ''{3}'' file",
             fileName = createFileName(moduleId);

      message = MessageFormat.format(message, propertyPath, type.getCanonicalName(), fileName);
      throw new IllegalArgumentException(message, ex);
    }
  }
  
  private static class DependencyImpl implements Dependency
  {
    private String moduleId;
    private RequiredVersion version;
    
    public DependencyImpl(String moduleId, RequiredVersion version) {
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

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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * <p> Implementacion de un {@link TepuyModule} que obtiene
 * la informacion del modulo de un archivo JSON ubicado en 
 * la carpeta META-INF
 * @author Jesus Baez
 */
public abstract class JsonModuleDescriptor implements TepuyModule 
{

  private static final String PROP_PLATFORM = "platform";
  private static final String PROP_DEPENDENCIES = "dependencies";
  
  private String moduleId;
  private JSONObject json;
  private List<Dependency> dependencies;
  private RequiredVersion plataformVersion;
  
  public JsonModuleDescriptor(String moduleId)
  {
    this.moduleId = moduleId;
    InputStream in = readFile(moduleId);
    
    try {
      this.json = (JSONObject) new JSONParser().parse(new InputStreamReader(in));
    } catch (IOException | ParseException ex) {
      String message = "Check the file ''{0}''";
      message = MessageFormat.format(message, createFileName(moduleId));
      throw new IllegalArgumentException(message, ex);
    }
  }
  
  @Override
  public String getModuleId() 
  {
    return moduleId;
  }

  @Override
  public RequiredVersion getPlatform() 
  {
    if(plataformVersion != null) 
    {
      return plataformVersion;
    }
    
    String version = readProperty(json, PROP_PLATFORM, String.class, null);
    if(version == null)
    {
      throw new IllegalArgumentException("The platform version is mandatory");
    }
    
    plataformVersion = Modules.parseRequiredVersion(version);
    return plataformVersion;
  }

  @Override
  public List<Dependency> getDependencies() 
  {
    if(this.dependencies != null)
    {
      return this.dependencies;
    }
    
    final JSONObject jsonDependencies = readProperty(json, PROP_DEPENDENCIES, JSONObject.class, null);
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
  
  protected InputStream readFile(String moduleName)
  {
    String file = createFileName(moduleName);
    ClassLoader loader = this.getClass().getClassLoader();
    return loader.getResourceAsStream(file);
  }
  
  protected String createFileName(String moduleName)
  {
    String file = "META-INF/{0}.tm.json";
    return MessageFormat.format(file, this.moduleId);
  }
  
  protected <T> T readProperty(JSONObject json, String propertyPath, Class<T> type, T defaultValue)
  {
    String[] properties = propertyPath.split(Pattern.quote("."));
    
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

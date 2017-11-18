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

package net.jbaez.tepuy.module.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.jbaez.tepuy.module.engine.ClassLoaderSupplier;

/**
 * <p> Implementacion que crea un {@link ClassLoader} con todos
 * los archivos <code>jar</code> encontrados en la carpeta indicada
 * en el constructor
 * @author Jesus Baez
 */
public class FolderClassLoader implements ClassLoaderSupplier {

  private Path path;
  private ClassLoader parent;
  
  public FolderClassLoader(Path path, ClassLoader parent) 
  {
    this.path = path;
    this.parent = parent;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ClassLoader get() 
  {
    boolean isValid = isValidPath(this.path);
    if(!isValid)
    {
      throw new IllegalArgumentException(
          "Verify that the path is from a folder and has read permissions"
      );
    }
    
    URL[] urlJars = findJar(this.path);
    return new URLClassLoader(urlJars, parent);
  }
  
  protected boolean isValidPath(Path path)
  {
    File folder = path.toFile();
    return folder.isDirectory() && folder.canRead();
  }
  
  protected URL[] findJar(Path path)
  {
    File folder = path.toFile();
    File[] jars = folder.listFiles((File file, String name) -> {
      return file.isFile() && name.endsWith(".jar");
    });

    return Arrays.asList(jars).stream().map(file -> {
      try {
        return file.toURI().toURL();
      } catch (MalformedURLException exception) {
        throw new IllegalArgumentException(exception);
      }
    }).collect(Collectors.toList()).toArray(new URL[]{});
  }

}

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

package net.jbaez.tepuy.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * <p> Clase de inicio de la plataforma
 * @author Jesus Baez
 */
public class Main {

  /**
   * <p> Propiedad de sistema con la la ubicacion
   * del archivo de configuracion
   */
  public static final String PROP_CONF_DIR = "tepuy.config.dir";
  /**
   * <p> Propiedad de sistema con la la ubicacion
   * de la carpeta de modulos
   */
  public static final String PROP_MODULES_DIR = "tepuy.modules.dir";
  
  /**
   * <p> Nombre del archivo de configuracion
   */
  public static final String VALUE_CONF_FILE_NAME = "tepuy-config.properties";
  /**
   * <p> Nombre del archivo de bloqueo de la aplicacion
   */
  public static final String VALUE_BLOCK_FILE_NAME = "tepuy-server.lock";
  
  private static final String COMMAND_START = "start";
  private static final String COMMAND_STOP  = "stop";
  private static final String COMMAND_HELP  = "help";
  
  private static final int EXIT_CODE_OK = 0;
  private static final int EXIT_CODE_START_PROPERTIES = -100;
  private static final int EXIT_CODE_LOCK_EXIST = -200;
  
  public static void main(String[] args) 
  {
    String command = COMMAND_HELP;
    if (args.length == 1)
    {
      command = args[0];
    }

    switch (command) 
    {
      case COMMAND_START:
          start();
          break;
      case COMMAND_STOP:
          stop();
          break;
      default:
          help();
    }
  }
  
  private static void start()
  {
    try {
      validateSystemProperties();
    }catch(IllegalStateException ex) {
      System.err.println(ex.getMessage());
      System.exit(EXIT_CODE_START_PROPERTIES);
    }
    
    try {
      validateLockServer();
    }catch(IllegalStateException ex) {
      System.err.println(ex.getMessage());
      System.exit(EXIT_CODE_LOCK_EXIST);
    }
    
    //TODO Iniciar server
    
    try {
      createLockFile();
    }catch(IllegalStateException ex) {
      System.err.println(ex.getMessage());
      System.exit(EXIT_CODE_START_PROPERTIES);
    }
  }
  
  private static void stop()
  {
    
  }
  
  private static void help() 
  {
    String helpMessage = getMessage("tepuy.app.main.help");
    System.out.println(helpMessage);
    System.exit(EXIT_CODE_OK);
  }
  
  private static void validateLockServer() throws IllegalStateException
  {
    String value = System.getProperty(PROP_CONF_DIR);
    Path path = Paths.get(value, VALUE_BLOCK_FILE_NAME);
    File file = path.toFile();
    if(file.exists())
    {
      String message = getMessage("tepuy.app.main.lock.file.exist");
      throw new IllegalStateException(message);
    }
  }
  
  private static void validateSystemProperties() throws IllegalStateException
  {
    String value = System.getProperty(PROP_CONF_DIR);
    if(value == null)
    {
      String message = getMessage("tepuy.app.main.property.not.found", PROP_CONF_DIR);
      throw new IllegalStateException(message);
    }
    
    Path path = Paths.get(value);
    File file = path.toFile();
    if(!file.isDirectory() || !file.canWrite())
    {
      String message = getMessage("tepuy.app.main.dir.not.valid", file.getAbsolutePath());
      throw new IllegalStateException(message);
    }
    
    path = Paths.get(value, VALUE_CONF_FILE_NAME);
    file = path.toFile();
    if(!file.exists() || !file.canRead())
    {
      String message = getMessage("tepuy.app.main.file.not.valid", file.getAbsolutePath());
      throw new IllegalStateException(message);
    }
    
    value = System.getProperty(PROP_MODULES_DIR);
    if(value == null)
    {
      String message = getMessage("tepuy.app.main.property.not.found", PROP_MODULES_DIR);
      throw new IllegalStateException(message);
    }
    
    path = Paths.get(value);
    file = path.toFile();
    if(!file.isDirectory() || !file.canRead())
    {
      String message = getMessage("tepuy.app.main.dir.not.valid", file.getAbsolutePath());
      throw new IllegalStateException(message);
    }
  }
  
  private static void createLockFile() throws IllegalStateException
  {
    final long PID = ProcessHandle.current().pid();
    Iterable<String> lines = Arrays.asList(String.valueOf(PID));
    
    String value = System.getProperty(PROP_CONF_DIR);
    Path path = Paths.get(value, VALUE_BLOCK_FILE_NAME);
    
    try {
      path = Files.write(path, lines);
    }catch (IOException e) {
      String message = getMessage("tepuy.app.main.dir.not.valid", value);
      throw new IllegalStateException(message);
    }
    
    path.toFile().deleteOnExit();
  }
  
  private static String getMessage(String message, Object...  args)
  {
    ResourceBundle messages = ResourceBundle.getBundle("MainResourceBundle");
    String result = messages.getString(message);
    if(result == null)
    {
      return result;
    }
    
    if(args != null && args.length > 0)
    {
      result = MessageFormat.format(result, args);
    }
    
    return result;
  }

}

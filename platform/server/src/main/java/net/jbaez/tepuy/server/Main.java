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
  public static final String PROP_CONF_FILE = "tepuy.config.file";
  /**
   * <p> Propiedad de sistema con la la ubicacion
   * de la carpeta de modulos
   */
  public static final String PROP_MODULES_DIR = "tepuy.modules.dir";
  
  private static final String COMMAND_START = "start";
  private static final String COMMAND_STOP  = "stop";
  private static final String COMMAND_HELP  = "help";
  
  private static final int EXIT_CODE_OK = 0;
  
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
    
  }
  
  private static void stop()
  {
    
  }
  
  private static void help() 
  {
    ResourceBundle messages = ResourceBundle.getBundle("MainResourceBundle");
    String helpMessage = messages.getString("tepuy.app.main.help");
    System.out.println(helpMessage);
    System.exit(EXIT_CODE_OK);
  }

}

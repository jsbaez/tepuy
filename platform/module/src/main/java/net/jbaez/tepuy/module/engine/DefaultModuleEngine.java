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

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import net.jbaez.tepuy.module.Dependency;
import net.jbaez.tepuy.module.PlatformModule;
import net.jbaez.tepuy.version.Version;

/**
 * <p> Implementacion por defecto del motor 
 * de modulos
 * @author Jesus Baez
 */
public class DefaultModuleEngine implements ModuleEngine {

  private MainContextSupplier mainContextSupplier;
  private RepositoryModules repositoryModules;
  private ContextLayout contextLayout;
  private Version platformVersion;
  
  private boolean initiated;
  private AbstractApplicationContext mainContext;
  
  public DefaultModuleEngine(Version platformVersion, 
      MainContextSupplier mainContextSupplier, 
      RepositoryModules repositoryModule, 
      ContextLayout contextLayout) {
    this.mainContextSupplier = mainContextSupplier;
    this.repositoryModules = repositoryModule;
    this.platformVersion = platformVersion;
    this.contextLayout = contextLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start() {
    if(this.initiated)
    {
      throw new IllegalStateException("The engine is already started");
    }
    
    Set<PlatformModule> modules = repositoryModules.findAll();
    validateModules(modules);
    validateGraph(modules);
    
    this.mainContext = mainContextSupplier.get();
    configureMainContext(mainContext);
    
    configureModules(modules, mainContext);
    this.initiated = true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() {
    if(this.initiated)
    {
      throw new IllegalStateException("The engine is not started");
    }
    
    this.mainContext.close();
  }
  
  protected void configureModules(Set<PlatformModule> modules, ApplicationContext context)
  {
    this.contextLayout.initialize(context);
    modules.forEach(module -> this.contextLayout.addModule(module));
  }
  
  protected void configureMainContext(AbstractApplicationContext ctx)
  {
    //
  }
  
  protected void validateModules(Set<PlatformModule> modules)
  {
    //Platform not valid
    List<PlatformModule> platformNotValids = modules.stream().filter(module -> {
      return !module.getPlatform().isValid(platformVersion);
    }).collect(Collectors.toList());
    
    if(!platformNotValids.isEmpty())
    {
      List<String> modulesIds = platformNotValids.stream().map(module -> {
        return module.getModuleId();
      }).collect(Collectors.toList());
      
      String message = "The ''{0}'' modules require a platform version not compatible with ''{1}''";
      message = MessageFormat.format(message, modulesIds.toString(), platformVersion.toString());
      throw new IllegalStateException(message);
    }
    
    //Duplicated modules
    Map<String, Long> modulesCount = modules.stream().collect(
        Collectors.groupingBy(m -> m.getModuleId(), Collectors.counting())
    );
    List<String> modulesIds = modulesCount.entrySet().stream().filter(e -> {
      return e.getValue() > 1L;
    }).map(e -> e.getKey()).collect(Collectors.toList());
    if(!modulesIds.isEmpty())
    {
      String message = "The ''{0}'' modules are duplicated check the modules folder";
      message = MessageFormat.format(message, modulesIds.toString());
      throw new IllegalStateException(message);
    }
   }
  
  protected void validateGraph(Set<PlatformModule> modules)
  {
    Graph<PlatformModule, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

    //Add dependencies
    Iterator<PlatformModule> iterator = modules.iterator();
    while(iterator.hasNext())
    {
      addDependencies(graph, iterator.next());
    }
    
    CycleDetector<PlatformModule, DefaultEdge> detector = new CycleDetector<>(graph);
    Set<PlatformModule> cycles = detector.findCycles();
    if(cycles != null && !cycles.isEmpty())
    {
      String message = "Cyclic dependencies have been found with these modules ''{0}''";
      message = MessageFormat.format(message, cycles.toString());
      throw new IllegalStateException(message);
    }
  }
  
  private void addDependencies(Graph<PlatformModule, DefaultEdge> graph, PlatformModule module)
  {
    Iterator<Dependency> dependencies = module.getDependencies().iterator();
    Optional<PlatformModule> optionalModule = null;
    Dependency dependency = null;

    while(dependencies.hasNext())
    {
      dependency = dependencies.next();
      optionalModule = repositoryModules.findOne(
            dependency.getModuleId(), 
            dependency.getVersion()
      );
      
      if(!optionalModule.isPresent())
      {
        String message = "Module ''{0}'' in its version ''{1}'' is necessary";
        message = MessageFormat.format(message, 
            dependency.getModuleId(), 
            dependency.getVersion()
        );
        throw new IllegalStateException(message);
      }
      
      boolean containNode = graph.containsVertex(module);
      if(!containNode)
      {
        graph.addVertex(module);
      }
      
      containNode = graph.containsVertex(optionalModule.get());
      if(!containNode)
      {
        graph.addVertex(optionalModule.get());
      }
      
      boolean existVertex = graph.containsEdge(module, optionalModule.get());
      if(!existVertex)
      {
        graph.addEdge(module, optionalModule.get());
        addDependencies(graph, optionalModule.get());
      }
    }
  }
  
}

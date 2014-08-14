package gov.va.med.lom.javaUtils.classloader;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

/*
 * PluginClassLoader is not an actual ClassLoader, but serves a role
 * of preloading "plugin" classes into the JVM, so that the Plugins
 * can register themselves with whatever "plugin manager" they use.
 */
public class PluginClassLoader {

  /*
   * Interface to allow interested clients to be notified each
   * time a new plugin class is loaded into the JVM.
   */
  public static interface Listener {
    public void pluginLoaded(String pluginName);
    public void exception(Exception ex);
  }

  // Private data
  private URLClassLoader urlClassLoader;

  public PluginClassLoader(String dir) {
    this(dir, new Listener() {
      public void pluginLoaded(String pluginName) { }
      public void exception(Exception ex) { }
    });
  }

  public PluginClassLoader(String dir, Listener listener) {
    File file = new File(dir);
    reload(file, listener);
  }

  public PluginClassLoader(File dir) {
    this(dir, new Listener() {
      public void pluginLoaded(String pluginName) { }
      public void exception(Exception ex) { }
    });
  }

  public PluginClassLoader(File dir, Listener listener) {
    reload(dir, listener);
  }

  /*
   * Reload the plugins; note that the old URLClassLoader held
   * internally is released, so if the plugin classes loaded
   * earlier aren't in use within the app, they'll get GC'ed.
   *
   * However, if an instance of an earlier-loaded
   * plugin class is still in existence, it will remain an
   * entirely separate and distinct type from the type loaded
   * in on this plass, even if the .class files are identical!
   * This is because classes loaded into two separate (non-
   * parentally-related) ClassLoaders are considered separate
   * and unrelated types, even if their contents are identical.
   */
  public void reload(String dir, Listener listener) {
    reload(new File(dir), listener);
  }

  /*
   * Reload the plugins; note that the old URLClassLoader held
   * internally is released, so if the plugin classes loaded
   * earlier aren't in use within the app, they'll get GC'ed.
   *
   * However, if an instance of an earlier-loaded
   * plugin class is still in existence, it will remain an
   * entirely separate and distinct type from the type loaded
   * in on this plass, even if the .class files are identical!
   * This is because classes loaded into two separate (non-
   * parentally-related) ClassLoaders are considered separate
   * and unrelated types, even if their contents are identical.
   */
  public void reload(File dir, Listener listener) {
    String[] contents = getPluginDirContents(dir);

    Vector urls = new Vector();
    Vector plugins = new Vector();
    for (int i=0; i<contents.length; i++) {
      try {
        File jarFile = new File(dir, contents[i]);
        
        Attributes attribs = new JarFile(jarFile).getManifest().getMainAttributes();

        if (attribs.getValue("Plugin-Class") != null) {
          String pluginClass = attribs.getValue("Plugin-Class");

          urls.add(jarFile.toURL());
          plugins.add(pluginClass.trim());
          // Need the trim(); getValue() has the
          // annoying habit of leaving a trailing
          // space on the end of the class, which will
          // cause the loadClass() to fail later.
        }
      }
      catch (IOException ioEx) {
        // Just continue; ignore the file and move on
      }
      catch (NullPointerException npEx) {
        // No manifest, perhaps?
      }
    }

    urlClassLoader = URLClassLoader.newInstance(convertUrlVectorToArray(urls),
                                                getClass().getClassLoader());

    // Preload each of the plugins, giving them the chance to
    // register (in their static initializer block) with whatever
    // "PluginManager" they choose to.
    for (int i=0; i<plugins.size(); i++) {
      String plugin = (String)plugins.elementAt(i);
      try {
        urlClassLoader.loadClass(plugin).newInstance();
        //urlClassLoader.loadClass(plugin);
        // For some strange reason, just calling
        // loadClass() *won't* actually load the class
        // into the VM; instead, we have to actually
        // *create* an instance of the Plugin class
        // before resolution actually takes place. This
        // would seem to be contrary to the JVM Spec.
        // If this ever changes, then we can comment out
        // the first urlClassLoader.loadClass() line and
        // uncomment the second; the first presumes that
        // the class *can* be instantiated, which is not
        // always a safe/good/acceptable assumption.

        listener.pluginLoaded(plugin);
      } catch (Exception ex) {
        listener.exception(ex);
      }
    }
  }

  /*
   * Releases the handle on the URLClassLoader used internally;
   * this will have the effect of allowing all the plugin classes,
   * if not referenced anywhere else within the application, to be
   * GC'ed the next time GC takes place.
   */
  public void unload() {
    urlClassLoader = null;
  }

  /*
   * Returns a String array of filenames in the directory which are
   * potential plugin files.
   */
  private String[] getPluginDirContents(File dir) {
    // Sanity check--does the directory exist?
    if ( (!dir.exists()) || (!dir.isDirectory()) ) {
      return new String[0];
    }

    String[] contents = dir.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        if (name.endsWith(".jar") || name.endsWith(".zip")) {
          return true;
        } else
          return false;
      }
    });
    return contents;
  }

  /*
   * Returns a String array of filenames in the directory which are
   * .class files.
   */
  private String[] getPluginDirClasses(File dir) {
    String[] contents = dir.list(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        if (name.endsWith(".class"))
            return true;
        else
            return false;
      }
    });
    return contents;
  }

  /*
   * Simple helper method to convert a Vector of URL objects into an
   * array of URL objects (required by URLClassLoader)
   */
  private URL[] convertUrlVectorToArray(Vector urls) {
    URL[] urlArray = new URL[urls.size()];
    for (int i=0; i<urlArray.length; i++) {
      urlArray[i] = (URL)urls.elementAt(i);
    }
    return urlArray;
  }

  /*
   * Test suite--just load whatever plugins happen to be in the
   * current directory.
   */
  public static void main(String[] args) throws Exception {
    PluginClassLoader pcl = new PluginClassLoader(".", new Listener() {
        public void pluginLoaded(String pluginName) {
          System.out.println(pluginName + " loaded.");
        }
        public void exception(Exception ex) {
          System.out.println("Exception:");
          ex.printStackTrace();
        }
    });
  }
}

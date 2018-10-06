# fusion-main

The fusion editor main.  Downloads and updates the main plugin.  Opens files from the command-line.
Launches the server process if necessary.

## TODOs

* NOW
  * Rename to FuseCode *DONE*
    * .fusion folder default name
   * Find/use a clojure arg-processing lib here and in fusion-boot
  * Read :fusion-port from config and pass to bootstrap plugin main
  * Detect if server is already running and pass files to open to existing server
  * process file(s) to open

* Process command-line arguments:  (find/use a clojure arg-processing lib)
  * Override .fusion folder
  * Override boot task
  * Override default port
  * Boot pass-through options
  * Respect Boot's offline mode flag and don't try to install/update

* Extract common namespaces / functions into a common library
  * (newboot "--command" "line" "--arguments") - factor classpath-isolated Boot into a library function
  * Config file processing
  * Files
  * OO (Should this really be a defprotocol? *DONE* (No--the tests use inheritence in the stub implementations)
  * Patterns

* Make a classloader for (newboot) capable of operating from memory rather than extracting _bootstrap.jar to a file.

* Figure out how to package as a plugin
  * Boot
  * Leiningen
  * SBT

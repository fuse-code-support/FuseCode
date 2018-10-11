# The FuseCode Launcher

Downloads and updates the bootstrap plugin.  Opens files from the command-line.
Launches the server process if necessary.

## TODOs

* NOW
  * Find/use a clojure arg-processing lib
    * here *DONE*
    * and in fusion-boot
  * Read default :fusion-port from config and pass to bootstrap plugin main
  * Detect if server is already running and pass files to open to existing server
  * process file(s) to open

* Process command-line arguments:
  * In config/create-or-read
    * :fusecode-dir *DONE*
    * :offline
  * In launcher/attach-open
    * :port / :host  *DONE*
    * :new-window
    * pass files to open to server
  * In launcher/start
    * Add relevent fusion.core options to :tasks for Boot
      * :port / :host
      * :new-window
      * :offline

  * Override boot task
  * Override default port
  * Boot pass-through options
  * Respect Boot's offline mode flag and don't try to install/update

* Extract common namespaces / functions into a common library
  * (newboot "--command" "line" "--arguments") - factor classpath-isolated Boot into a library function?
  * Config file processing
  * Files
  * Patterns

* Make a classloader for (newboot) capable of operating from memory rather than extracting _bootstrap.jar to a file.

* Figure out how to package as a plugin
  * Boot
  * Leiningen
  * SBT

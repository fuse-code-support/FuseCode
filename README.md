# The FuseCode Launcher

FuseCode is ideal for applications that need to be easily extended using a distributed asynchronous
team and that need to progressively and dynamically load themselves from servers.  In a sense,
FuseCode takes Google's notion of progressive web applications and extends that to the server
side as well.

Clearly, there are security implications around doing this that developers need to be aware
of, and FuseCode provides tools to manage that risk.  That said, FuseCode would normally
be used for applications that are either:

* Developer tools where the developer managers what plugins are loaded and takes responsibility
  for security (e.g.: programmers' tools).
* Programs intended to run exclusively behind the firewall or with explicitly-trusted users
  (e.g.: other software developers in the same organization.)

## This program

This is the main launcher for an entire FuseCode application.  It's compiled to an executable
binary that works on Unix-like systems out of the box as long as a Java 8 or later runtime
is present on the path.

Its purpose is to be small, fast, and static.  All real functionality is contained in the plugin
framework that it launches.  To that end, its concerns are:

* To check if another instance is already running and if so to pass its command-line arguments
  to the running instance to be handled there.  Or:
* To create the root runtime environment if it isn't already present and download or update the 
  Bootstrap plugin.  Plugins are contained in Git repositories and we use Git as the autoupdate
  engine.
* To launch the Bootstrap plugin in a classpath-isolated environment so that FuseCode's dependencies
  cannot conflict with any plugin's classpath.  By default:
  * We launch the Bootstrap plugin using Clojure's Boot build tool with fullly-automatic runtime
    code reloading enabled.  This is because FuseCode is designed to be evolved using itself at 
    runtime to the highest degree possible.
  * The default Bootstrap plugin starts a web server and provides a dynamic client and server-side
    programming environment, but in reality the Bootstrap plugin could do anything.

For more information on next steps, see the FuseCode Bootstrap project.




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

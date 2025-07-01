@echo off
start cmd /k "cd bin && start rmiregistry"
timeout /t 2
start cmd /k "cd bin && java -cp .;../lib/* server.LibraryServer"

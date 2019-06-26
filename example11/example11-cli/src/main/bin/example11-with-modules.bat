@echo off
java -p "%~dp0\lib" ${moduleName}/${mainClass} %*

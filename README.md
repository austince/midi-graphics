# Midi Graphics
For CS 537 at Stevens Institute of Technology.

Using a 2D pipeline to create something more.

## Building + Running
This is a **maven** project, but some helpful commands are written in the `Makefile`.

- `make` | `make compile` : default build
- `make` clean : clean up
- `make run` : runs the application with default settings

To run with the command line args:
- `mvn exec:java -Dexec.args="[ARGUMENTS]"`

## Application arguments

## Application commands
- Q : quit
- If using the keyboard: 


## Keyboard Support
The examples will run without a midi keyboard
though they are much less fun that way.  

The only currently supported keyboard is the _Akai MPK Mini_.  
 
## Kotlin
I used Kotlin for the Driver of the examples, but the main pieces are all Java.
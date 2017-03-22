# Midi Graphics
For CS 537 at Stevens Institute of Technology.

Using a 2D pipeline to create something more.

## Building + Running + Other Make commands
This is a **maven** project, but some helpful commands are written in the `Makefile`.

- `make` : default build
- `make clean` : clean up
- `make run` : runs the application with default settings
- `make doc` : generates javadocs into the `docs` directory

After `make`:  
- `java -jar target/midterm-1.0-jar-with-dependencies.jar [ARGUMENTS]`

## Application arguments
- run with the `-u` for USAGE
 
## Application key commands
- 1: Example 1 'Squares'
- 2: Example 2 'Rectangle'
- Q : quit
- c : toggle clip window
- If using the midi keyboard: 
	- Pad 4: quit
	- Dials 1 - 6 adjust the clip window 
	- Example 1:
		- Pad 5: Add a square

## Examples
Press '1' for the Square Example  
Press '2' for the Rectangle Example  

## Midi Keyboard Support
The examples will run without a midi keyboard
though they are much less fun that way.  

The only currently supported keyboard is the _Akai MPK Mini_.  
 
## Known Bugs
- Concurrent Modification of PolyList in PolyCanvas
- Startup sometimes fails. Need to use a `Runnable` and `SwingUtilities.invokeLater`

## Kotlin
I used Kotlin for the Driver of the examples, but the main pieces are all Java.
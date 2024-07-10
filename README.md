# Jude Gibson - 3D-Graphics Project

This is a simple project involved 2 alien models rendered in 3D using Java OpenGL (JOGL). To run the program, you must first run the two following commands in your terminal:

export PATH=/{PROJECT_PATH}/jogl25/lib:$PATH

Export CLASSPATH=.:/{PROJECT_PATH}/jogl25/jar/jogl-all.jar:/{PROJECT_PATH}/jogl25/jar/gluegen-rt.jar:$CLASSPATH

where {PROJECT_PATH} indicates the location of the project on your device. Once these commands have been executed, you can just execute:

java Aliens

Note that if this doesn't work you may just need to compile the code first using: 

javac Aliens.java
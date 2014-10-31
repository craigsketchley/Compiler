#!/bin/bash

#make the ./bin folder if it does not exist
if [ ! -d ./bin ]; then
    mkdir ./bin
fi

#compile the project if not already done
if [ ! -f ./bin/IntermediateCodeOptimiser.class ]; then
	echo "First use. Compiling..."
	javac -sourcepath ./src -d ./bin ./src/IntermediateCodeOptimiser.java

	#check main file was created
	if [ ! -f ./bin/IntermediateCodeOptimiser.class ]; then
		echo "Compile error, could not compile files into ./bin"
		echo "Please try compiling manually"
		return
	fi
	echo "Compiling complete!"
fi

#run the optimiser!
java -cp bin IntermediateCodeOptimiser $@
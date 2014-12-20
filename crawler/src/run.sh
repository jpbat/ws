echo "-*-*-*-*- IMDBCrawler -*-*-*-*-"

declare -i used
used=0

if [ "$1" == "all" ]
then
	echo "compiling..."
	javac -cp ../lib/*: *.java
	echo "Running... Grab a snickers!"
	java -cp ../lib/*: IMDBCrawler $2 $3
	used=1
fi

if [ "$1" == "compile" ]
then
	echo "compiling..."
	javac -cp ../lib/*: *.java
	used=1
fi

if [ "$1" == "run" ]
then
	echo "running..."
	java -cp ../lib/*: IMDBCrawler $2 $3
	used=1
fi

if [ "$1" == "clean" ]
then
	echo 'Cleaning dataset...'
	rm -rf ../dataset/*
	echo 'Cleaning outputs...'
	rm -rf ../output/*
	echo 'Cleaning logs...'
	rm -rf ../logs/*
	echo 'Cleaning compiled classes...'
	rm -rf *.class
	rm -rf ../bin/*
	used=1
fi

if [ "$1" == "fix" ]
then
	java -cp ../lib/*: DataFixer
	used=1
fi

if [ "$1" == "help" ] || [ $used = 0 ]
then
	echo 'all: compile and run'
	echo 'compile: only compile'
	echo 'run: run a previous compiled version'
	echo 'clean: remove the outputs, logs and compiled classes'
	echo 'fix: fix some details with the dataset'
	echo 'help: show this menu'
fi

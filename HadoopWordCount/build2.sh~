if [ ! -d classes2 ]; then
        mkdir classes2;
fi

# Compile WordCount
javac -classpath $HADOOP_HOME/hadoop-core-1.1.2.jar:$HADOOP_HOME/lib/commons-cli-1.2.jar -d ./classes2 WordCount2.java

# Create the Jar
jar -cvf wordcount2.jar -C ./classes2/ .
 
# Copy the jar file to the Hadoop distributions
cp wordcount2.jar $HADOOP_HOME/bin/ 


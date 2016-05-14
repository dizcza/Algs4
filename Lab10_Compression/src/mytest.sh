#!/usr/bin/env bash
export CLASSPATH="/home/dizcza/algs4/algs4.jar:$CLASSPATH"
javac *.java
for txt in shortest/*
do
    if [[ $txt == *.txt ]];
    then
        echo $txt:
        java -ea MoveToFront - < $txt | java -ea MoveToFront + | grep Exception
        echo
    fi
done
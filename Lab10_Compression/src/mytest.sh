#!/usr/bin/env bash
export CLASSPATH="/home/dizcza/algs4/algs4.jar:$CLASSPATH"
javac *.java
for txt in shortest/*
do
    if [[ $txt == *.txt ]]
    then
        java BurrowsWheeler - < $txt | java BurrowsWheeler + > temp.txt
        miss="$(diff $txt temp.txt)"
        if [[ "$miss" -ne "" ]]
        then
            echo $txt FAILED
        fi
    fi
done
rm temp.txt
echo DONE
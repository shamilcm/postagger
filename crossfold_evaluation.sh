#!/bin/bash

if [ "$#" -ne  1 ]; then
    echo  "Illegal number of parameters. Usage: ./crossfold_evaluation.sh /path/to/training_data"
else
    read lines filename <<< `wc -l $1`
    echo $lines
    n=10
    basedir=evaluation
    newlines=`expr $lines + $n - 1`
    chunksize=`expr $newlines / $n`
    split -l $chunksize $evaluation/$filename
    for letter in {a..j} ; do
        mv $basedir/a$letter $basedir/testfile_$letter
        join $basedir/* > $basedir/trainfile_l$letter
    done
    #java -cp build build_tagger $1 $2 $3
fi

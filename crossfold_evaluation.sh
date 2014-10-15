#!/bin/bash

train_file=data/sents.train
untagged_file=data/sents.untagged

if [ "$#" -ne  2 ]; then
    echo  "Illegal number of parameters. Usage: ./crossfold_evaluation.sh /path/to/training_data /path/to/untagged_data"
    echo "Using default files to 10-fold cross evaluate..."

else
    train_file=$1
    untagged_file=$2
fi

read lines filename <<< `wc -l $train_file`
echo $lines
n=10
basedir=evaluation
rm $basedir/*
newlines=`expr $lines + $n - 1`
chunksize=`expr $newlines / $n`
split -l $chunksize $train_file $basedir/
split -l $chunksize $untagged_file $basedir/testfile_
for letter in {a..j} ; do
    mv $basedir/a$letter $basedir/corrfile_a$letter
    cat $basedir/a* > $basedir/trainfile_a$letter
    cp $basedir/corrfile_a$letter $basedir/a$letter
done

for letter in {a..j} ; do
    ./run_tagger
    echo  "Illegal number of parameters. Usage: ./build_tagger.sh /path/to/training_data /path/to/dev_data /path/to/modelfile"
    echo  "Illegal number of parameters. Usage: ./run_tagger.sh /path/to/testing_data /path/to/model_file /path/to/output_file"
    echo  "Illegal number of parameters. Usage: ./evaluate_tagger.sh /path/to/taggedoutput_data /path/to/taggedcorrect_data /path/to/result_file"

done

rm $basedir/a*

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
n=10
basedir=evaluation
mkdir -p $basedir
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

rm $basedir/a*

echo "Running cross-fold validation (n=10). "
count=1
for letter in {a..j} ; do
    echo "Step $count"
    count=$((count+1))
    ./build_tagger.sh $basedir/trainfile_a$letter data/sents.devt $basedir/model_a$letter
    ./run_tagger.sh $basedir/testfile_a$letter $basedir/model_a$letter $basedir/outfile_a$letter
    ./evaluate_tagger.sh $basedir/outfile_a$letter $basedir/corrfile_a$letter $basedir/result_a$letter
    echo "Done."
done

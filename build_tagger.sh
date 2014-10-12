#!/bin/bash

if [ "$#" -ne  3 ]; then
    echo  "Illegal number of parameters. Usage: ./build_tagger.sh /path/to/training_data /path/to/dev_data /path/to/modelfile"
    echo "Using default files to train..."
    java -cp build build_tagger data/sents.train data/sents.devt model/model_file
else
    java -cp build build_tagger $1 $2 $3
fi

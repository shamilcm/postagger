#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo  "Illegal number of parameters. Usage: ./run_tagger.sh /path/to/testing_data /path/to/model_file /path/to/output_file"
    echo "Using default files for POS tagging..."
    java -cp build run_tagger data/sents.test model/model_file output/sents.out
else
    java -cp build run_tagger $1 $2 $3
fi

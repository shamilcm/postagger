#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo  "Illegal number of parameters. Usage: ./evaluate_tagger.sh /path/to/taggedoutput_data /path/to/taggedcorrect_data /path/to/result_file"
    echo "Using default files for evaluation..."
    mkdir -p output
	java -cp build evaluate_tagger output/sents.out data/sents.out output/result_file
else
    java -cp build evaluate_tagger $1 $2 $3
fi

#!/bin/bash

for file in $(ls gitgud_outputs)
do
    diff ./gitgud_outputs/$file ./rahul_outputs/$file
    if [[ ! $? == 0 ]]
    then
        echo $file differs
    fi
done

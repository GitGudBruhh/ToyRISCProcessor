#!/bin/bash


#Directory structure
# CS311_assignment_2/
# === bin/
# === src/
# === build.xml
# === assemble_rahul.sh
#
# supporting_files_custom
# === test_cases/
# === gitgud_outputs/ (already filled)
# === rahul_outputs/ (run this file to get it)
# === test_zip.py (not needed)
# === check_diff.sh

mkdir ../supporting_files_custom/rahul_outputs/
for file in $(ls ../supporting_files_custom/test_cases/)
do
	ant && for i in {1..220}; do printf =; done && echo = && ant make-jar && for i in {1..220}; do printf =; done && echo = && java -Xmx1g -jar jars/assembler.jar ../supporting_files_custom/test_cases/$file ../supporting_files_custom/rahul_outputs/$file.out
done

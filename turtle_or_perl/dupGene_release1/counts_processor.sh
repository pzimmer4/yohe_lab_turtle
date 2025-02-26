#!/bin/bash

# Define the directory where your count files are
COUNTS_DIR="."

# Define the directory where your results will be stored
RESULTS_DIR="../results_dupGene"

# Define the path to your dupGene.pl script
DUPGENE_SCRIPT="./dupGene.pl"

# Define the path to your tree file
TREE_FILE="perl_names_turtle.tre"

# List of gene names
GENE_NAMES=("OR1.3.7" "OR10" "OR11" "OR12" "OR14" "OR2.13" "OR4" "OR5.8.9" "OR51" "OR52" "OR55" "OR56" "OR6" "V1R" "V2R" "bird_gammaOR" "squamate_V2r")

# Loop through each gene name and run the script
for gene in "${GENE_NAMES[@]}"; do
  # Construct the input and output file paths
  INPUT_FILE="${COUNTS_DIR}/perl_${gene}_counts.tsv"
  OUTPUT_FILE="${RESULTS_DIR}/${gene}_dup_pre_output.txt"
  FINAL_OUTPUT="${RESULTS_DIR}/${gene}_dup_output.txt"

  # Run the dupGene.pl script with a timeout
  timeout 8s ${DUPGENE_SCRIPT} -s=${TREE_FILE} -p=${INPUT_FILE} > ${OUTPUT_FILE} 2>/dev/null

  # Check if the process timed out
  if [ $? -eq 124 ]; then
    echo "Timeout: dupGene.pl for ${gene} took longer than 8 seconds."
  else
    # Print a message to indicate the script has finished (or timed out) for the current gene
    echo "Processed (or timed out) ${gene} and saved output to ${OUTPUT_FILE}"
  fi

  # Run head on the output file and redirect to a new file
  head -n 5 "${OUTPUT_FILE}" > "${FINAL_OUTPUT}"

done

echo "All genes processed (or attempted)."

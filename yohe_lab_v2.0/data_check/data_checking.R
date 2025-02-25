#Data Checking       Paul Zimmerman         Generative AI Used
setwd('C:/Users/PALL/Desktop/Yohe_lab/')   #using a working directory closer to the main directory from now on


#setting up work environment 
library(ggplot2)
library(wesanderson)    #color palette
library(reshape2)
library(plyr)
library(dplyr)
library(geiger)
library(phytools)
library(tidyr)
library(ape)
library(ComplexHeatmap)
library(autoimage)
library(tidyverse)


ncbi_genomes <- read_tsv("./research_downloads/ncbi_genomes_check.tsv")
ncbi_names <- ncbi_genomes[, 3]

names(ncbi_names) <- gsub(" ", "_", names(ncbi_names))
ncbi_names[["Organism_Name"]] <- gsub(" ", "_", ncbi_names[["Organism_Name"]])

ncbi_names$Organism_Name <- stringr::str_extract(ncbi_names$Organism_Name, "[^_]+_[^_]+")    #gets rid of the subpopulation name for matching purposes
names(ncbi_names) <- "testudines_all_species" #changing column name just for merging purposes

testudines_all_species <- read.csv("./turtle_files/all_testudines_species.csv")    #created from all species on ITIS.gov under testudines
testudines_all_species <- colnames(testudines_all_species)  #creates a vector of all testudines species names, not sure what we'll be using yet
testudines_all_species.df <- data.frame(testudines_all_species)   #for name checking purposes

#merging to check what all is the same
merged_df <- merge(ncbi_names, testudines_all_species.df, by = "testudines_all_species") # Merge based on the common "id" column
matching_count <- nrow(merged_df)      # Number of rows in merged df = number of matches

#Now which ones don't match
not_in_df2 <- ncbi_names %>%
  anti_join(testudines_all_species.df, by = "testudines_all_species")

not_in_df1 <- testudines_all_species.df %>%
  anti_join(ncbi_names, by = "testudines_all_species")


#### SOLUTION: Added in Chelonoidis_abingdonii to testudines_all_species.csv



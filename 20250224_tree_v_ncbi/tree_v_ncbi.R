#Tree vs NCBI genomes check       Paul Zimmerman         Generative AI Used 2/24
setwd('C:/Users/PALL/Desktop/Yohe_lab/')   #using a working directory closer to the main directory from now on


#setting up work environment 
library(ggplot2)
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



###Loading in and preparing the ncbi genome names to see what matches with the tree data. After running
###Technically there are 90+ entries but several are repeats. Only 51 unique species names after processing. 54 before getting rid of subspecies.

# - #### 
#loading in latest ncbi genome table (2/24/2024)
testudines_all_species_ncbi <- read.delim("./turtle_files/ncbi_all_testudines_feb24.tsv", sep ="\t")

#replaces spaces with underscores
testudines_all_species_ncbi$Organism.Name <- gsub(" ", "_", testudines_all_species_ncbi$Organism.Name)   

#gets rid of subspecies name
testudines_all_species_ncbi$Organism.Name <- stringr::str_extract(testudines_all_species_ncbi$Organism.Name, "[^_]+_[^_]+") 

ncbi_names <- testudines_all_species_ncbi[,3]
unique_ncbi_names <- unique(ncbi_names)
unique_ncbi_names <- data.frame(unique_ncbi_names)  #turning it into a dataframe for merging purposes

# - ####
#loading in tree data
turtle.tree <- read.tree("./turtle_files/turtle_tree_feb_21.tre")
turtle.tree <- ladderize(turtle.tree)
turtle.tree <- multi2di(turtle.tree, random = TRUE)
turtle.tree$edge.length[turtle.tree$edge.length==0.0] <- 0.1     #resolves troublesome nodes at random; can check if succe
tree_names  <- data.frame(turtle.tree$tip.label)



###Drops us to 48 matching entries, the three missing entries from our tree are Macrochelys_suwanniensis, Actinemys_pallida, andTerrapene_triunguis
# - ####
#Processing and merging data to find missing entries
names(tree_names) <- "Organism.Name"
names(unique_ncbi_names) <- "Organism.Name"

merged_df <- merge(tree_names, unique_ncbi_names, by = "Organism.Name") # Merge based on the common "id" column
matching_count <- nrow(merged_df)      # Number of rows in merged df = number of matches


matching_count <- sum(unique_ncbi_names$Organism.Name %in% tree_names$Organism.Name)
#48 match

not_in_tree <- unique_ncbi_names %>%
  anti_join(tree_names, by = "Organism.Name")
#gives us 3 that are in ncbi but not in the tree

# - ####
#saving tsv for validation reasons and also making stuff for perl script
write_tsv(merged_df, "turtle_files/matching_Organism.Name.tsv") 


# - ####
#reloading tree and changing names to be six letters for example Caretta_caretta is carCar
turtle.tree <- read.tree("./turtle_files/turtle_tree_feb_21.tre")

transform_name <- function(name) {
  parts <- unlist(strsplit(name, "_"))
  if (length(parts) == 2) {
    genus <- substr(parts[1], 1, 3)
    species <- substr(parts[2], 1, 3)
    
    # Modify genus and species case
    genus <- tolower(substr(genus, 1, 1)) %>% paste0(substr(genus, 2, 3))
    species <- toupper(substr(species, 1, 1)) %>% paste0(tolower(substr(species, 2, 3)))
    
    return(paste0(genus, species))
  } else {
    return(NA) # Handle cases where the format is incorrect
  }
}

turtle.tree$tip.label <- sapply(turtle.tree$tip.label, transform_name)  #all names are now in genSpe format
turtle.tree$edge.length <- NULL


write.tree(turtle.tree, file = "turtle_or_perl/dupGene_release1/perl_names_turtle.tre") #writes genSpe format without branch lengths to my chosen directory



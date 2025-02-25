#Perl File Prep             Paul Zimmerman         Generative AI Used 2/24
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



# - ####
#Loading turtle tree and changing names to be six letters for example Caretta_caretta is carCar, also removing branch lengths
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











# - ####
###OR count and Tree processing and matching; 33 species left
or.data <- read.csv("./turtle_files/Dino_OR_Taxa_Corrected_6_25_25_edit.csv", header = TRUE, row.names = 1)
colnames(or.data)[1] <- "Organism.Name" 
or.data$Organism.Name <- stringr::str_extract(or.data$Organism.Name, "[^_]+_[^_]+")    #gets rid of the subpopulation name for matching purposes





turtle.tree <- read.tree("./turtle_files/turtle_tree_feb_21.tre") #loading this back in to match pre-sgenSpe
tip_labels.data <- data.frame(turtle.tree$tip.label)
colnames(tip_labels.data) <- "Organism.Name"
merged_turtle.data <- merge(tip_labels.data, or.data, by = "Organism.Name") # Merge based on the common "id" column; MERGE OF COUNT AND TREE





merged_turtle.data$Organism.Name <- sapply(merged_turtle.data$Organism.Name, transform_name)   #genSpe formatting, replacing Organism.Name column entries; full name still in rows


#subsetting tree
###this is the first input file for the perl script; it's the tree and hopefully formatted correctly
turtle.tree$tip.label <- sapply(turtle.tree$tip.label, transform_name)  #all names are now in genSpe format
turtle.tree$edge.length <- NULL
sub_turtle_tree <- keep.tip(turtle.tree, merged_turtle.data$Organism.Name) #subsetting only for the matching stuff between the tree and the OR counts. 33 of them

#Need to add internal node labels to tree
ntips <- length(sub_turtle_tree$tip.label)
nnodes <- Nnode(sub_turtle_tree)
internal_nodes <- (ntips + 1):(ntips + nnodes)
sub_turtle_tree$node.label <- paste("IN", 1:nnodes, sep = "")


write.tree(sub_turtle_tree, file = "turtle_or_perl/dupGene_release1/perl_names_turtle.tre") #writes genSpe format without branch lengths to my chosen directory

# - #### 
#prep for perl from the count data that has been subset by the turtle tree; making data long
add_sum_columns <- function(df) {
  col_names <- names(df)
  prefixes <- unique(gsub("_.*", "", col_names))
  
  for (prefix in prefixes) {
    matching_cols <- grep(paste0("^", prefix, "_"), col_names, value = TRUE)
    
    if (length(matching_cols) > 1) {
      # Check if all matching columns are numeric
      numeric_cols <- sapply(df[, matching_cols, drop = FALSE], is.numeric)
      if (all(numeric_cols)) {
        new_col_name <- paste0(prefix, "_BOTH")
        df[[new_col_name]] <- rowSums(df[, matching_cols, drop = FALSE], na.rm = TRUE)
      } else {
        warning(paste("Non-numeric columns found for prefix:", prefix, ". Skipping sum calculation."))
      }
    }
  }
  return(df)
}
merged_turtle.data <- add_sum_columns(merged_turtle.data) #Adds a new column named gene_BOTH for psuedogene and coding. We only need coding for perl tho



#creating the wide data necessary for the second input file
OR6_turtle.data <- data.frame(t(merged_turtle.data$OR6_CODING)) # Transpose the OR6_CODING column
colnames(OR6_turtle.data) <- merged_turtle.data$Organism.Name  
write_tsv(OR6_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR6_counts.tsv") #writes counts for OR6 coding for use in the perl script

# # - ####
# ###48 matching between tree and ncbi
# #loading in ncbi names for checking
# testudines_all_species_ncbi <- read.delim("./turtle_files/ncbi_all_testudines_feb24.tsv", sep ="\t")
# #replaces spaces with underscores
# testudines_all_species_ncbi$Organism.Name <- gsub(" ", "_", testudines_all_species_ncbi$Organism.Name)   
# 
# #gets rid of subspecies name
# testudines_all_species_ncbi$Organism.Name <- stringr::str_extract(testudines_all_species_ncbi$Organism.Name, "[^_]+_[^_]+") 
# 
# ncbi_names <- testudines_all_species_ncbi[,3]
# unique_ncbi_names <- unique(ncbi_names)
# unique_ncbi_names <- data.frame(unique_ncbi_names)  #turning it into a dataframe for merging purposes
# names(unique_ncbi_names) <- "Organism.Name"
# merged_ncbi_tree_df <- merge(tip_labels.data, unique_ncbi_names, by = "Organism.Name") # Merge based on the common "id" column

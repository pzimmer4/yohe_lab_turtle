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

transform_tree_name <- function(name) {
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

turtle.tree$tip.label <- sapply(turtle.tree$tip.label, transform_tree_name)  #all names are now in genSpe format











# - ####
###OR count and Tree processing and matching; 33 species left
or.data <- read.csv("./turtle_files/Dino_OR_Taxa_Corrected_6_25_25_edit.csv", header = TRUE, row.names = 1)
colnames(or.data)[1] <- "Organism.Name" 
or.data$Organism.Name <- stringr::str_extract(or.data$Organism.Name, "[^_]+_[^_]+")    #gets rid of the subpopulation name for matching purposes





turtle.tree <- read.tree("./turtle_files/turtle_tree_feb_21.tre") #loading this back in to match pre-sgenSpe
tip_labels.data <- data.frame(turtle.tree$tip.label)
colnames(tip_labels.data) <- "Organism.Name"
merged_turtle.data <- merge(tip_labels.data, or.data, by = "Organism.Name") # Merge based on the common "id" column; MERGE OF COUNT AND TREE

transform_genspe_name <- function(name) {
  parts <- unlist(strsplit(name, "_"))
  if (length(parts) == 2) {
    genus <- substr(parts[1], 1, 3)
    species <- substr(parts[2], 1, 3)
    
    # Modify genus and species case
    genus <- tolower(substr(genus, 1, 1)) %>% paste0(substr(genus, 2, 3))
    species <- tolower(substr(species, 1, 1)) %>% paste0(tolower(substr(species, 2, 3)))
    
    return(paste0(genus, species))
  } else {
    return(NA) # Handle cases where the format is incorrect
  }
}



merged_turtle.data$Organism.Name <- sapply(merged_turtle.data$Organism.Name, transform_genspe_name)   #genSpe formatting, replacing Organism.Name column entries; full name still in rows


#subsetting tree
###this is the first input file for the perl script; it's the tree and hopefully formatted correctly
turtle.tree$tip.label <- sapply(turtle.tree$tip.label, transform_genspe_name)  #all names are now in genSpe format
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



##creating the wide data necessary for the second input file

#OR6 need to get rid of genSpe format into genspe for use on other gene families
OR6_turtle.data <- data.frame(t(merged_turtle.data$OR6_CODING)) # Transpose the OR6_CODING column
colnames(OR6_turtle.data) <- merged_turtle.data$Organism.Name  
write_tsv(OR6_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR6_counts.tsv") #writes counts for OR6 coding for use in the perl script






### in script do ./dupGene.pl -s=perl_names_turtle.tre -p=perl_OR6_counts.tsv > ../results_dupGene/OR6_dup_pre_output.txt
###For right now the script runs indefinitely so must be stopped then use <head -n 5 ../results_dupGene/OR6_dup_pre_output.txt > ../results_dupGene/OR6_dup_output.txt>



# - ####
#code for all the other OR gene families
OR1.3.7_turtle.data <- data.frame(t(merged_turtle.data$OR1.3.7_CODING))
colnames(OR1.3.7_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR1.3.7_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR1.3.7_counts.tsv")

OR10_turtle.data <- data.frame(t(merged_turtle.data$OR10_CODING))
colnames(OR10_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR10_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR10_counts.tsv")

OR11_turtle.data <- data.frame(t(merged_turtle.data$OR11_CODING))
colnames(OR11_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR11_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR11_counts.tsv")

OR12_turtle.data <- data.frame(t(merged_turtle.data$OR12_CODING))
colnames(OR12_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR12_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR12_counts.tsv")

OR14_turtle.data <- data.frame(t(merged_turtle.data$OR14_CODING))
colnames(OR14_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR14_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR14_counts.tsv")

OR2.13_turtle.data <- data.frame(t(merged_turtle.data$OR2.13_CODING))
colnames(OR2.13_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR2.13_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR2.13_counts.tsv")

OR4_turtle.data <- data.frame(t(merged_turtle.data$OR4_CODING))
colnames(OR4_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR4_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR4_counts.tsv")

OR5.8.9_turtle.data <- data.frame(t(merged_turtle.data$OR5.8.9_CODING))
colnames(OR5.8.9_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR5.8.9_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR5.8.9_counts.tsv")

OR51_turtle.data <- data.frame(t(merged_turtle.data$OR51_CODING))
colnames(OR51_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR51_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR51_counts.tsv")

OR52_turtle.data <- data.frame(t(merged_turtle.data$OR52_CODING))
colnames(OR52_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR52_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR52_counts.tsv")

OR55_turtle.data <- data.frame(t(merged_turtle.data$OR55_CODING))
colnames(OR55_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR55_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR55_counts.tsv")

OR56_turtle.data <- data.frame(t(merged_turtle.data$OR56_CODING))
colnames(OR56_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(OR56_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_OR56_counts.tsv")



# - ####
#code for the non-OR gene families
V1R_turtle.data <- data.frame(t(merged_turtle.data$V1R_CODING))
colnames(V1R_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(V1R_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_V1R_counts.tsv")  #empty

merged_turtle.data$all_V2R_CODING <- rowSums(merged_turtle.data[, c("V2R116_proteinAlignment_CODING", "V2R1_CODING", "V2R26_CODING")], na.rm = TRUE) #extra code to make V2 families into one
V2R_turtle.data <- data.frame(t(merged_turtle.data$all_V2R_CODING))
colnames(V2R_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(V2R_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_V2R_counts.tsv")

bird_gammaOR_turtle.data <- data.frame(t(merged_turtle.data$bird_gammaOR_CODING))
colnames(bird_gammaOR_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(bird_gammaOR_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_bird_gammaOR_counts.tsv")

squamate_V2r_turtle.data <- data.frame(t(merged_turtle.data$squamate_V2r_CODING))
colnames(squamate_V2r_turtle.data) <- merged_turtle.data$Organism.Name
write_tsv(squamate_V2r_turtle.data, file = "turtle_or_perl/dupGene_release1/perl_squamate_V2r_counts.tsv")


# - ####
#Reading in results after using dupGene.pl with the goal of plotting distributions. (for a later date for now)
#after using input of perl_names_turtle.tre and perl_OR6_counts.tsv
OR6_dist <- read.table("turtle_or_perl/results_dupGene/OR6_dup_output.txt", sep="\t", header = TRUE)









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

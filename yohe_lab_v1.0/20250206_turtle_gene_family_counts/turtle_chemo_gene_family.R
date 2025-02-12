#Turtle Gene Family Counts  -  Paul Zimmerman  -  Began: 02/06/2025  -  Using Generative AI
setwd('C:/Users/PALL/Desktop/Yohe_lab/')   #using a working directory closer to the main directory from now on


#setting up packages for work environment        BiocManager::install("package")
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

#Reading in OR data and formatting if necessary
OR.data <- read.csv("./turtle_files/Dino_OR_Taxa_Corrected_6_25_25_edit.csv", header = TRUE, row.names = 1)   #first column is row names; must use row names
rownames(OR.data) <- gsub(".collapsed.fasta", "", rownames(OR.data))  #removing collapsed.fasta from the rownames
names(OR.data)[1] <- "Organism.Name"    #changes first column to match the testudines genome table from ncbi, not necessary anymore and code can be redone for original format


#Loading in csv of all testudines and making it a vector
testudines_all_species <- read.csv("./turtle_files/all_testudines_species.csv")    #created from all species on ITIS.gov under testudines
testudines_all_species <- colnames(testudines_all_species)  #creates a vector of all testudines species names, not sure what we'll be using yet
testudines_all_species.df <- data.frame(testudines_all_species)   #for name checking purposes



#filtering OR.data for turtle species
rownames(OR.data) <- OR.data$Organism.Name      #need rownames to match
turtle_OR <- OR.data[grep(paste0("^(", paste(testudines_all_species, collapse = "|"), ")(_|$)"), OR.data$Organism.Name), ]      #turtle OR data has all the gene families including V1R etc.
turtle_OR$Organism.Name <- stringr::str_extract(turtle_OR$Organism.Name, "[^_]+_[^_]+")    #gets rid of the subpopulation name for matching purposes
rownames(turtle_OR) <- turtle_OR$Organism.Name

#OR families
OR_class_1_families <- turtle_OR[, c("OR51_CODING", "OR51_PSEUDOGENE", "OR52_CODING", "OR52_PSEUDOGENE", "OR55_CODING", "OR55_PSEUDOGENE", "OR56_CODING", "OR56_PSEUDOGENE")]
OR_class_1_families$Organism.Name <- rownames(OR_class_1_families)
OR_class_2_families <- turtle_OR[, c("OR1.3.7_CODING", "OR1.3.7_PSEUDOGENE", "OR2.13_CODING", "OR2.13_PSEUDOGENE", "OR4_CODING", "OR4_PSEUDOGENE", 
                                     "OR5.8.9_CODING", "OR5.8.9_PSEUDOGENE","OR6_CODING", "OR6_PSEUDOGENE", "OR10_CODING", "OR10_PSEUDOGENE", "OR11_CODING",
                                     "OR11_PSEUDOGENE", "OR12_CODING", "OR12_PSEUDOGENE", "OR14_CODING", "OR14_PSEUDOGENE" )]
OR_class_2_families$Organism.Name <- rownames(OR_class_2_families)

#plotting for each family

par(las = 2,  # Rotate labels 90 degrees
    cex.axis = 0.55, # Smaller font size (adjust as needed)
    mar = c(7, 4, 4, 2) + 0.5) # Increased bottom margin (adjust as needed)

boxplot(OR_class_1_families[, 1:8], main= "OR Class 1 Gene Families", xlab = "gene family", ylab = "gene count")              #class 1 gene families across all testudines
boxplot(t(OR_class_1_families[, 1:8]), main= "OR Class 1 Gene Families", xlab = "species", ylab = "gene count")



boxplot(OR_class_2_families[, 1:18], main = "OR Class 2 Gene Families", xlab = "gene family", ylab = "gene count")       #class 2 gene families across all testudines
boxplot(t(OR_class_2_families[, 1:18]), main = "OR Class 2 Gene Families", xlab = "species", ylab = "gene count")


reset.par() # Reset to default values (good practice; autoimage package command


#Heatmap all families
turtle_OR_matrix <- as.matrix(turtle_OR[, 4:50])  # Convert to matrix
mode(turtle_OR_matrix) <- "numeric" # Force numeric type.

row_order_OR <- sort(rownames(turtle_OR_matrix))
col_order_OR <- sort(colnames(turtle_OR_matrix))

Heatmap(turtle_OR_matrix, 
        col = colorRampPalette(c("white", "blue"))(100),
        row_order = row_order_OR, 
        column_order = col_order_OR
)

#Heatmap for OR class 1
turtle_class_one_matrix <- as.matrix(OR_class_1_families[, 1:8])
mode(turtle_class_one_matrix) <- "numeric" # Force numeric type.

row_order_one <- sort(rownames(turtle_class_one_matrix))
col_order_one <- sort(colnames(turtle_class_one_matrix))

Heatmap(turtle_class_one_matrix, 
        col = colorRampPalette(c("white", "blue"))(100),
        row_order = row_order_one, 
        column_order = col_order_one
)


#Heatmap for OR class 2
turtle_class_two_matrix <- as.matrix(OR_class_2_families[, 1:18])
mode(turtle_class_two_matrix) <- "numeric" # Force numeric type.

row_order_two <- sort(rownames(turtle_class_two_matrix))
col_order_two <- sort(colnames(turtle_class_two_matrix))

Heatmap(turtle_class_two_matrix, 
        col = colorRampPalette(c("white", "blue"))(100),
        row_order = row_order_two, 
        column_order = col_order_two
)




#Converting to long data
turtle_OR_long <- turtle_OR %>%
  pivot_longer(cols = starts_with("Organism"),  # Columns to gather
               names_to = "Species",         # Name of the new 'Species' column
               values_to = "GeneCount")       # Name of the new 'GeneCount' column
###Actually we need to write code that will get the number of class 1 and class 2 genes for each species and make two columns for these.
###then we can use the numbers to make a stacked column like the one in that paper

ggplot(turtle_OR_long, aes(x = Species, y = GeneCount, fill = rownames(turtle_OR))) +
  geom_bar(stat = "identity", position = "stack") +  # Use geom_bar and position = "stack"
  labs(title = "Gene Family Counts by Species",
       x = "Species",
       y = "Gene Count",
       fill = "Gene Family") +
  theme_bw() +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

#z_one <- as.data.frame(cbind(OR_class_1_families$Organism.Name, OR_class_1_families$OR51_CODING))          #cbind combines columns of choice
#rownames(z_one) <- rownames(OR_class_1_families)  #even though we no longer have species column this will correctly name the species
#colnames(z_one) <- c("Species", "OR_51_Family_Gene_Count")     #names the columns; replaces v1 and v2 column names
#z_one$Species <- as.factor(z_one$Species)
#z_one <- type.convert(z_one)       #will try to guess what type each column should be and convert the column to that class
#p <- ggplot(z_one, aes(OR_51_Family_Gene_Count, Species))
#p
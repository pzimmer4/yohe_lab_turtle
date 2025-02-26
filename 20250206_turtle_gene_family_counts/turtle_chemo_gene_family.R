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
library(tidyverse)

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


V1R_families <- turtle_OR %>%
  select(starts_with("V1") & !ends_with("SUM"))

V2R_families <- turtle_OR %>%
  select(starts_with("V2") & !ends_with("SUM"))

bird_families <- turtle_OR %>%
  select(starts_with("bird") & !ends_with("SUM"))

squamate_V2_families <- turtle_OR %>%
  select(starts_with("squamate_V2") & !ends_with("SUM"))

#plotting for each group of families

par(las = 2,  # Rotate labels 90 degrees
    cex.axis = 0.55, # Smaller font size (adjust as needed)
    mar = c(7, 4, 4, 2) + 0.5) # Increased bottom margin (adjust as needed)

boxplot(OR_class_1_families[, 1:8], main= "OR Class 1 Gene Families", ylab = "gene count")              #class 1 gene families across all testudines
boxplot(t(OR_class_1_families[, 1:8]), main= "OR Class 1 Gene Families", ylab = "gene count")


boxplot(OR_class_2_families[, 1:18], main = "OR Class 2 Gene Families", ylab = "gene count")       #class 2 gene families across all testudines
boxplot(t(OR_class_2_families[, 1:18]), main = "OR Class 2 Gene Families", ylab = "gene count")

boxplot(V1R_families, main = "V1R Gene Families", ylab = "gene count")    
boxplot(t(V1R_families), main = "V1R Gene Families",  ylab = "gene count")

boxplot(V2R_families, main = "V2R Gene Families",  ylab = "gene count")    
boxplot(t(V2R_families), main = "V2R Gene Families",  ylab = "gene count")

boxplot(bird_families, main = "Bird Gene Families",  ylab = "gene count")    
boxplot(t(bird_families), main = "Bird Gene Families",  ylab = "gene count")

boxplot(squamate_V2_families, main = "Squamate V2R Gene Families",  ylab = "gene count")    
boxplot(t(squamate_V2_families), main = "Squamate V2R Families",  ylab = "gene count")



reset.par() # Reset to default values (good practice; autoimage package command


#Heatmap all families
turtle_OR_matrix <- as.matrix(turtle_OR[, 4:53])  # Convert to matrix
mode(turtle_OR_matrix) <- "numeric" # Force numeric type.

row_order_OR <- sort(rownames(turtle_OR_matrix))
col_order_OR <- sort(colnames(turtle_OR_matrix))

Heatmap(turtle_OR_matrix, 
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_OR, 
        column_order = col_order_OR,
        heatmap_legend_param = list(title = "All Gene Families")
)

#Heatmap for OR class 1
turtle_class_one_matrix <- as.matrix(OR_class_1_families[, 1:8])
mode(turtle_class_one_matrix) <- "numeric" # Force numeric type.

row_order_one <- sort(rownames(turtle_class_one_matrix))
col_order_one <- sort(colnames(turtle_class_one_matrix))

Heatmap(turtle_class_one_matrix, 
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_one, 
        column_order = col_order_one,
        heatmap_legend_param = list(title = "OR Class 1 Families")
)


#Heatmap for OR class 2
turtle_class_two_matrix <- as.matrix(OR_class_2_families[, 1:18])
mode(turtle_class_two_matrix) <- "numeric" # Force numeric type.

row_order_two <- sort(rownames(turtle_class_two_matrix))
col_order_two <- sort(colnames(turtle_class_two_matrix))

Heatmap(turtle_class_two_matrix, 
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_two, 
        column_order = col_order_two,
        heatmap_legend_param = list(title = "OR Class 2 Families")
)

#Heatmap for V1R
V1R_families_matrix <- as.matrix(V1R_families)
mode(V1R_families_matrix) <- "numeric" # Force numeric type.

row_order_vone <- sort(rownames(V1R_families_matrix))
col_order_vone <- sort(colnames(V1R_families_matrix))

Heatmap(V1R_families_matrix,
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_vone,
        column_order = col_order_vone,
        heatmap_legend_param = list(title = "V1R Families")
)

#Heatmap for V2R
V2R_families_matrix <- as.matrix(V2R_families)
mode(V2R_families_matrix) <- "numeric" # Force numeric type.

row_order_vtwo <- sort(rownames(V2R_families_matrix))
col_order_vtwo <- sort(colnames(V2R_families_matrix))

Heatmap(V2R_families_matrix,
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_vtwo,
        column_order = col_order_vtwo,
        heatmap_legend_param = list(title = "V2R Families")
)

#Heatmap for bird
bird_families_matrix <- as.matrix(bird_families)
mode(bird_families_matrix) <- "numeric"

row_order_bird <- sort(rownames(bird_families_matrix))
col_order_bird <- sort(colnames(bird_families_matrix))

Heatmap(bird_families_matrix,
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_bird,
        column_order = col_order_bird,
        heatmap_legend_param = list(title = "Bird Families")
)


#Heatmap for squamate V2R
squamate_V2_families_matrix <- as.matrix(squamate_V2_families)
mode(squamate_V2_families_matrix) <- "numeric"

row_order_squamate <- sort(rownames(squamate_V2_families_matrix))
col_order_squamate <- sort(colnames(squamate_V2_families_matrix))

Heatmap(squamate_V2_families_matrix,
        col = colorRampPalette(c("white", "deeppink"))(100),
        row_order = row_order_squamate,
        column_order = col_order_squamate,
        heatmap_legend_param = list(title = "Squamate V2r Families")
)



###Actually we need to write code that will get the number of class 1 and class 2 genes for each species and make two columns for these.
###then we can use the numbers to make a stacked column like the one in that paper
turtle_OR <- turtle_OR %>%          #MUST SUM ACROSS COLUMNS NOT ROWS
  group_by(Organism.Name) %>%  # Group by organism name (KEY STEP)
  mutate(
    OR_CLASS1_SUM = sum(c_across(colnames(OR_class_1_families[, 1:8]))), # Sum across columns
    OR_CLASS2_SUM = sum(c_across(colnames(OR_class_2_families[, 1:18]))), # Sum across columns
    V1_SUM = sum(c_across(starts_with("V1"))),  # Sum across columns
    V2_SUM = sum(c_across(starts_with("V2")))   # Sum across columns
  ) %>%
  ungroup()  # VERY IMPORTANT: Ungroup after the grouped operations

#below we are adding birds and squamates
turtle_OR <- turtle_OR %>%
  group_by(Organism.Name) %>%
  mutate(BIRD_SUM = sum(c_across(starts_with("bird")))) %>%  # Sum across bird columns
  ungroup()

turtle_OR <- turtle_OR %>%
  group_by(Organism.Name) %>%
  mutate(SQUAMATE_V2_SUM = sum(c_across(starts_with("squamate_V2")))) %>%  # Sum across squamate_V2 columns
  ungroup()



#stacked bar plot; have to make the data long first
turtle_OR_long <- turtle_OR %>%    #NOTE OR_Count and OR_Family are used to categorize the V1 and V2 as well. This is no problem
  pivot_longer(cols = c(starts_with("OR_CLASS"), "V1_SUM", "V2_SUM", "BIRD_SUM", "SQUAMATE_V2_SUM"),  # Include V1/V2 sums
               names_to = "OR_Family", 
               values_to = "OR_Count") %>%
  mutate(OR_Family = case_when(
    startsWith(OR_Family, "OR_CLASS") ~ gsub("_Sum", "", gsub("OR_", "", OR_Family)),
    startsWith(OR_Family, "V") ~ gsub("_SUM", "", OR_Family), # Clean up V1/V2 names
    startsWith(OR_Family, "BIRD") ~ gsub("_SUM", "", OR_Family), # Clean up BIRD names
    startsWith(OR_Family, "SQUAMATE") ~ gsub("_SUM", "", OR_Family), # Clean up SQUAMATE names
    TRUE ~ OR_Family # Default case
  ))# Clean up family names

ggplot(turtle_OR_long, aes(x = Organism.Name, y = OR_Count, fill = OR_Family)) +   #stacked bar plot of v1 and v2 and class 1/2 OR counts
  geom_col(position = "stack") +
  labs(title = "Stacked Barplot of Gene Counts by Family",
       x = "Organism Name",
       y = "Gene Count",
       fill = "Gene Family") +
  theme_bw() +
  theme(axis.text.x = element_text(angle = 45, hjust = 1))

ggplot(turtle_OR_long, aes(x = Organism.Name, y = OR_Count, fill = OR_Family)) +
  geom_col(position = "stack") +
  facet_wrap(~ OR_Family, scales = "free_y") +  # Facet by OR_Family
  labs(title = "Gene Counts by Family (faceted)", x = "Species", y = "Gene Count") + # ... your other labels
  theme_bw() +
  theme(axis.text.x = element_text(angle =55, hjust = 1))





#z_one <- as.data.frame(cbind(OR_class_1_families$Organism.Name, OR_class_1_families$OR51_CODING))          #cbind combines columns of choice
#rownames(z_one) <- rownames(OR_class_1_families)  #even though we no longer have species column this will correctly name the species
#colnames(z_one) <- c("Species", "OR_51_Family_Gene_Count")     #names the columns; replaces v1 and v2 column names
#z_one$Species <- as.factor(z_one$Species)
#z_one <- type.convert(z_one)       #will try to guess what type each column should be and convert the column to that class
#p <- ggplot(z_one, aes(OR_51_Family_Gene_Count, Species))
#p
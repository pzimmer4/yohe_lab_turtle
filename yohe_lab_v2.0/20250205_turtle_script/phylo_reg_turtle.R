#Phylogenetic Regression Turtle Chemosensation  -  Paul Zimmerman  -  Began: 02/05/2025   - Using Generative AI


setwd('C:/Users/PALL/Desktop/Yohe_lab/')

#setting up packages for work environment        BiocManager::install("package")
library(ggplot2)
library(wesanderson)    #color palette
library(reshape2)
library(plyr)
library(dplyr)
library(geiger)
library(phytools)
library(ape)

#Reading in OR data csv for all species
OR.data <- read.csv("./turtle_files/Dino_OR_Taxa_Corrected_6_25_25_edit.csv", header = TRUE, row.names = 1)   #first column is row names; must use row names
rownames(OR.data) <- gsub(".collapsed.fasta", "", rownames(OR.data))  #removing collapsed.fasta from the rownames
names(OR.data)[1] <- "Organism.Name"    #changes first column to match the testudines genome table from ncbi, not necessary anymore and code can be redone for original format

#Loading in csv of all testudines and making it a vector
testudines_all_species <- read.csv("./turtle_files/all_testudines_species.csv")    #created from all species on ITIS.gov under testudines
testudines_all_species <- colnames(testudines_all_species)  #creates a vector of all testudines species names, not sure what we'll be using yet


###Phylogenetic Tree In Following Code
turtle.tree <- read.tree("./turtle_files/tetrapod_tree_grafted_forTurtles.tre")
turtle.tree <- ladderize(turtle.tree)
turtle.tree <- multi2di(turtle.tree, random = TRUE)
turtle.tree$edge.length[turtle.tree$edge.length==0.0] <- 0.1     #resolves troublesome nodes at random; can check if successful w/ internal nodes = n-1

#filtering OR.data for turtle species
rownames(OR.data) <- OR.data$Organism.Name      #need rownames to match
turtle_OR <- OR.data[grep(paste0("^(", paste(testudines_all_species, collapse = "|"), ")(_|$)"), OR.data$Organism.Name), ]
turtle_OR$Organism.Name <- stringr::str_extract(turtle_OR$Organism.Name, "[^_]+_[^_]+")    #gets rid of the subpopulation name for matching purposes


#checking names and making them match across objects
tree_names  <- data.frame(turtle.tree$tip.label)  


turtle.tree <- treedata(turtle.tree, turtle_OR, sort = T, warning = T)$phy                 #matches the tree and the dataset. $phy grabs the phylogeny data. working with Testudines subsetted OR data, Turtle_OR. Also names fixed to work
turtle.tree.data <- treedata(turtle.tree, turtle_OR, sort = T, warning = T)$data     #gives the other half of the output of treedata()
turtle.tree.data <- as.data.frame(turtle.tree.data)   #always check trough data to see if things are spelled correctly and make sure you get the maximum entries
plot(turtle.tree)












#LEGACY STUFF from practice
#selecting for turtles
#turtles <- as.data.frame(OR.data[which(OR.data$Order.ry == "Testudines"),])    #subsetting for the order of testudines, colloquially turtles


#max_value_chemo <- rownames(turtles)[which.max(turtles$Chemo.Total)]     #gives which turtle species(rowname) has the most chemo receptors

#z <- as.data.frame(cbind(turtles$Family.lry, turtles$OR.gamma))          #cbind combines columns of choice
#rownames(z) <- rownames(turtles)  #even though we no longer have species column this will correctly name the species
#colnames(z) <- c("Family", "turtle.gamma")     #names the columns; replaces v1 and v2 column names
#z$Order <- as.factor(z$Order)
#z <- type.convert(z)       #will try to guess what type each column should be and convert


#Plotting stuff
#p <- ggplot(z, aes(Family, turtle.gamma))
#p +geom_boxplot() +coord_flip() +theme_bw()
#ggsave("turtle_family_gamma_example.pdf", h=10, w=12)   #will save the plot into the working directory everytime it's run



#Reading in another file of traits


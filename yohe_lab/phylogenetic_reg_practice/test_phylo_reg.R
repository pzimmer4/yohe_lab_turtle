#Phylogenetic Regression Practice


setwd('C:/Users/PALL/Desktop/Yohe Lab/Phylogenetic Regression/practice')

#setting up packages for work environment        BiocManager::install("package")
library(ggplot2)
library(wesanderson)    #color palette
library(reshape2)
library(plyr)
library(geiger)
library(phytools)
library(ape)


#Reading in csv
OR.data <- read.csv("chemo_counts.csv", header = TRUE, row.names = 1)   #first column is row names; must use row names



#Selecting for birds
birds <- as.data.frame(OR.data[which(OR.data$Class.lry == "Aves"),])       #taking the data and subsetting it for the entries where class.lry == "Aves" (birds); selects each row with Class.lry == "Aves"
##birds[,1] selects one column; birds[1,] selects one row
##if you subset this way without as.data.frame() you get the index numbers

max_value_chemo <- rownames(birds)[which.max(birds$Chemo.Total)]       #gives which bird species(rowname) has the most chemo receptors
birds$Species[which(birds$Chemo.Total == max(birds$Chemo.Total))]
### the FORMAT -> df$column[index_finding_statement]

z <- as.data.frame(cbind(birds$Order.ry, birds$OR.gamma))          #cbind combines columns of choice
rownames(z) <- rownames(birds)  #even though we no longer have species column this will correctly name the species
colnames(z) <- c("Order", "bird.gamma")     #names the columns; replaces v1 and v2 column names
z$Order <- as.factor(z$Order)
z <- type.convert(z)       #will try to guess what type each column should be and convert the column to that class


#Plotting stuff
p <- ggplot(z, aes(Order, bird.gamma))
p +geom_boxplot() +coord_flip() +theme_bw()
ggsave("bird_Order_gamma_example.pdf", h=10, w=12)   #will save the plot into the working directory everytime it's run



#Reading in another file of traits
bird.trait<-read.csv("41559_2019_1070_MOESM3_ESM.csv", header = T, row.names = 1)
#Merging dataframes
bird.trait$Species <- rownames(bird.trait)   #making new species column for merging
bird.trait.merge <- merge(bird.trait, OR.data)
rownames(bird.trait.merge) <- bird.trait.merge$Species

###Phylogenetic Tree In Following Code
bird.tree <- read.tree("bird_consensus.tre")        #built with different data set than our other data
bird.tree <- ladderize(bird.tree)
bird.tree <- multi2di(bird.tree, random = TRUE)
bird.tree$edge.length[bird.tree$edge.length==0.0] <- 0.1     #resolves troublesome nodes at random; can check if successful w/ internal nodes = n-1
bird.tree <- treedata(bird.tree, bird.trait.merge, sort = T, warning = T)$phy       
bird.trait.merge <- treedata(bird.tree, bird.trait.merge, sort = T, warning = T)$data
bird.trait.merge <- as.data.frame(bird.trait.merge)   #always check trough data to see if things are spelled correctly and make sure you get the maximum entries



###################
#Steps
#Split the data into male and female
#split each set into training and testing data
#Build a model on each data
#For testing get at least 10 posts from a user
#Do a test on each post 
#For each post ascertain if it's silhouette is more of male or female
#If about 70% of the total posts tested favours a gender
#Then conclude the user belongs to the gender
####################
install.packages("wordcloud")
install.packages("SnowballC")
install.packages("arules")
install.packages("arulesViz")
install.packages("tm")
install.packages("rJava")
install.packages("RWeka")
install.packages("RWekajars")
install.packages("wordnet")
install.packages("RTextTools")
library(arulesViz)
library(class)
library(tm)
library(wordcloud)
library(SnowballC)
library(arules)
library(rpart)
library(RWeka)
library(rJava)
library(RWekajars)
library(wordnet)
library(RTextTools)

setwd("~/Github/Gender-Mining/Text Mining")
#stats = read.csv("stats.csv",header = TRUE)
#stats2 = read.csv("stat.csv",header = TRUE)
posts = read.csv("posts.csv",header = TRUE)
posts2 = read.csv("posts2.csv",header = TRUE)
post_data = rbind(posts,posts2)

#################################################
Cleaning the data
################################################
#post_data$post = as.character(post_data$post)
#post_data$id = as.character(post_data$id)
post_data$post[post_data$post==''] = NA
#View(stats)
post_data = na.omit(post_data)
#View(post_data)

#########################################################
#Analysing Data                                         #
#########################################################
#summary(stats)
#plot(stats$no_posts ~ stats$gender)

post_data$gender = factor(post_data$gender,levels = c("male","female"),labels = c("Male","Female"))
#stats$gender = factor(stats$gender,levels = c("male","female"),labels = c("Male","Female"))
#male_data = as.vector(post_data$post[post_data$gender=='Male'])
male_data = post_data$post[post_data$gender=='Male']
#summary(male_data)
#female_data = post_data$post[post_data$gender=='Female']
male_train = data.frame(male_data[1:round(0.75 * length(male_data))/4])
#female_train = data.frame(female_data[1:round(0.75 * length(female_data))])
#female_test = data.frame(female_data[(round(0.75 * length(female_data))+1):length(female_data)])

myCorpus <- Corpus(DataframeSource(male_train))
myCorpus <- tm_map(myCorpus, content_transformer(tolower))
myStopwords <- c(stopwords('english'),stopwords('french'),"http")
myCorpus <- tm_map(myCorpus,removeWords, myStopwords)
myCorpus <- tm_map(myCorpus, content_transformer(removeNumbers))
tdm <- TermDocumentMatrix(myCorpus, control = list(removePunctuation = TRUE, stopwords = TRUE,removeNumbers=TRUE))

male_test = data.frame(male_data[(round(0.75 * length(male_data))+1):length(male_data)])
myCorpus <- Corpus(DataframeSource(male_test))
myCorpus <- tm_map(myCorpus, content_transformer(tolower))
myStopwords <- c(stopwords('english'),stopwords('french'),"http")
myCorpus <- tm_map(myCorpus,removeWords, myStopwords)
myCorpus <- tm_map(myCorpus, content_transformer(removeNumbers))
testMat <- TermDocumentMatrix(myCorpus, control = list(removePunctuation = TRUE, stopwords = TRUE,removeNumbers=TRUE))

rm(posts2)
rm(posts)
rm(post_data)
rm(male_data)
rm(male_test)
rm(male_train)
rm(myCorpus)
rm(myStopwords)



fTrain = findFreqTerms(tdm, lowfreq=10)
fTest = findFreqTerms(testMat, lowfreq=10)
cl <- as.character(c(1:length(fTrain)/4))
cl[1:length(fTrain)/4] <- 'Male'
train = cbind(fTrain,cl)
tcl <- as.character(c(1:length(fTest)/4))
tcl[1:length(fTest)/4] <- 'Male'
test = cbind(fTest,tcl)
knn_class <- knn(train,test,cl,k = 1,prob =FALSE)
ff = as.data.frame(test[1:round(nrow(test)/100)])
Train = ff$test[1:round(nrow(test)/100)];

hh = rpart(Train ~.,data = ff)
hh = ctree(Train ~.,data = ff)
rpart.plot(hh)
install.packages("party")
install.packages("rpart.plot")
library(party)
library(rpart.plot)
# which words are associated with "r"?
tt = findAssocs(tdm, 'female', 0.30)
wordcloud(dd)

plot(post_data$post_type ~ post_data$id)
scatter.smooth(x = post_data$post)
View(stats)
stats$id = as.character(stats$id)
plot(stats$no_posts)
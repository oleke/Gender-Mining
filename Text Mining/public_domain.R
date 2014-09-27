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
library(tm)
library(wordcloud)
library(SnowballC)
library(arules)
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
#stats$about <- NULL
#stats$religion <- NULL
#stats$political_view <- NULL
#stats$bio = as.character(stats$bio)
#stats$id = as.character(stats$id)
#stats$quotes = as.character(stats$quotes)
#stats$relationship_status = as.character(stats$relationship_status)
#stats$bio[stats$bio=='NA'] = NA
#stats$quotes[stats$quotes=='NA'] = NA
#stats$relationship_status[stats$relationship_status=='NA'] = NA

post_data$post = as.character(post_data$post)
post_data$id = as.character(post_data$id)
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
summary(post_data)
male_data = as.vector(post_data$post[post_data$gender=='Male'])
summary(male_data)
female_data = post_data$post[post_data$gender=='Female']
male_train = data.frame(male_data[1:round(0.75 * length(male_data))])
male_test = data.frame(male_data[(round(0.75 * length(male_data))+1):length(male_data)])
female_train = data.frame(female_data[1:round(0.75 * length(female_data))])
female_test = data.frame(female_data[(round(0.75 * length(female_data))+1):length(female_data)])

myCorpus <- Corpus(VectorSource(male_train))
myCorpus <- tm_map(myCorpus, tolower)
# remove punctuation
myCorpus <- tm_map(myCorpus, removePunctuation)
# remove numbers
myCorpus <- tm_map(myCorpus, removeNumbers)
# remove stopwords
# keep "r" by removing it from stopwords
myStopwords <- c(stopwords('english'),stopwords('french'),"http")
idx <- which(myStopwords == "r")
myStopwords <- myStopwords[-idx]
myCorpus <- tm_map(myCorpus, removeWords, myStopwords)
dictCorpus <- myCorpus
# stem words in a text document with the snowball stemmers,
# which requires packages Snowball, RWeka, rJava, RWekajars
myCorpus <- tm_map(myCorpus, stemDocument)
# inspect the first three ``documents"
inspect(myCorpus[1:3])

head(myCorpus)
# stem completion
ff = stemCompletion(myCorpus,dictionary = dictCorpus)
myCorpus <- tm_map(myCorpus, stemCompletion,dictionary=dictCorpus)
myC = wordStem(dictCorpus,language = "english")
myDtm <- TermDocumentMatrix(myC, control = list(minWordLength =3))
myDtm <- TermDocumentMatrix(myCorpus, control = list(minWordLength =3))
inspect(myDtm[266:270,31:40])
matrix <- create_matrix(male_train, stemWords=TRUE, removeStopwords=TRUE, minWordLength=2)
colnames(matrix)
findFreqTerms(myDtm, lowfreq=10)
# which words are associated with "r"?
findAssocs(myDtm, 'r', 0.30)
# which words are associated with "mining"?
# Here "miners" is used instead of "mining",
# because the latter is stemmed and then completed to "miners". :-(
findAssocs(myDtm, 'miners', 0.30)
m <- as.matrix(myDtm)
# calculate the frequency of words
v <- sort(rowSums(m), decreasing=TRUE)
myNames <- names(v)
k <- which(names(v)=="miners")
myNames[k] <- "mining"
d <- data.frame(word=myNames, freq=v)
wordcloud(d$word, d$freq, min.freq=3)
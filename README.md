# Ad Hoc Table Retrieval
The main purpose of this project, is to retrieve data/info of Ad-Hoc tables using the concepts welearn in class, furthermore, usage of API, libraries and data-sets as Lucene, StanfordNLP, Glove andmore, to tokenize, index and retrieve data as good as possible.

OVERVIEW

•Lucene: Lucene was the base API for the project, using Lucene data was indexed, retrievedand ranked. Queries were built using Lucene and searched through the indexed data.

•StanfordNLP: Using this API, queries were tokenized, lemmatised and stop words werecleaned.

•Glove: Using a Java version of Glove, helped enrich the queries for other relevant wordsusing the glove-50 data-set that was converted to .bin file for space and efficiency.

The Project is using data from: https://github.com/iai-group/www2018-table

Default path for tables in project is: "C:\\WP_tables\\tables_redi2_1"

There are two main that can be run:

1) In AdHocTester: which searches for the queries that are saved in queries.txt

2) In FrameMain: a very simple UI may include bugs, all runs on the same thread which will freeze the UI (needs an MVVM redesign).


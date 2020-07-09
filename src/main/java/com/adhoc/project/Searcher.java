package com.adhoc.project;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher {

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;
    EnrichQuery enrichQuery;
    CleanSentencesService cleanSentencesService;

    public Searcher(String indexDirectoryPath)
            throws IOException {
        if(indexDirectoryPath == null || indexDirectoryPath.isEmpty()) {
            indexDirectoryPath = Consts.indexDir;
        }
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath).toPath());
        IndexReader reader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(reader);
        indexSearcher.setSimilarity(new LMDirichletSimilarity((float) 1));
        queryParser = new QueryParser(LuceneConstants.CONTENTS, new EnglishAnalyzer());
    }

    public void init() throws IOException {
        enrichQuery = new EnrichQuery();
        cleanSentencesService = new CleanSentencesService();
        cleanSentencesService.init();
        enrichQuery.init();
    }

    public TopDocs search(String searchQuery)
            throws Exception {
        List<String> allQueryWords = new ArrayList<>();
        String cleanText = cleanSentencesService.removeStopWordsFromText(searchQuery);
        List<List<String>> cleanSearchResults = cleanSentencesService.lemmatizationTokens(cleanText);
        String[] queryWords =  searchQuery.split(" ");
        for (List<String> words: cleanSearchResults) {
            for(String word : words) {
                List<String> listOfSimilarWords = enrichQuery.FindSimilarWords(word, 3);
                allQueryWords.add(word);
                allQueryWords.addAll(listOfSimilarWords);
            }
        }
        searchQuery = String.join(" ", allQueryWords);

        try {
            query = queryParser.parse(searchQuery);
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc)
            throws CorruptIndexException, IOException {
        return indexSearcher.doc(scoreDoc.doc);
    }
}

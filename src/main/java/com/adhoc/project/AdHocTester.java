package com.adhoc.project;


import org.apache.lucene.search.ScoreDoc;

import java.util.HashMap;

public class AdHocTester {

    public static void main(String[] args) throws Exception {
//        Searcher searcher;
//        try {
//            Dictionary<Integer, String> idToQuery = FileHelper.readQueriesReturnDict(null);
//
//            searcher = new Searcher(Consts.indexDir);
////            CreateIndexService.createIndex(null, null);
//            EnrichQuery enrichQuery = new EnrichQuery();
//            enrichQuery.init();
//            List<String> listOfSimilarWords = enrichQuery.FindSimilarWords("king", 3);
//            CleanSentencesService cleanSentencesService = new CleanSentencesService();
//            cleanSentencesService.init();
//            String cleanText = cleanSentencesService.removeStopWordsFromText(query);
//            List<List<String>> cleanSearchResults = cleanSentencesService.lemmatizationTokens(cleanText);
//            searcher.search(query); // TODO change to clean query
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Searcher searcher;
        searcher = new Searcher(null);
        searcher.init();
        HashMap<Integer, String> dict = FileHelper.readQueriesReturnDict(null);
        for(Integer key : dict.keySet()) {
            ScoreDoc[] scoreDocs = searcher.search(dict.get(key)).scoreDocs;
            for(ScoreDoc scoreDoc: scoreDocs) {
                searcher.getDocument(scoreDoc);
            }
        }

    }

//
//    public void search(String searchQuery) throws IOException {
//
//        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexDir))));
//        searcher.setSimilarity(new LMDirichletSimilarity((float) 1));
//        long startTime = System.currentTimeMillis();
//        TopDocs hits = null;
//        TermQuery query = new TermQuery(new Term("contents", searchQuery));
//        hits = searcher.search(query, 20);
//
//        long endTime = System.currentTimeMillis();
//        System.out.println(hits.totalHits +
//                " documents found. Time :" + (endTime - startTime));
//        for(ScoreDoc scoreDoc : hits.scoreDocs) {
//            Document doc = searcher.doc(scoreDoc.shardIndex); //TODO check if correct ID
//            System.out.println("Table Title: "
//                    + doc.get(LuceneConstants.PAGE_TITLE));
//        System.out.println("Table Content "
//                    + doc.get(LuceneConstants.CONTENTS));
//        }
//    }
}

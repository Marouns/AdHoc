package com.adhoc.project;


import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdHocTester {

    public static void main(String[] args) throws Exception {

        String teamName = "MarounStandard";
        String indexDirectoryPath = null;
        String targetDirectoryPath = null;
        List<String> scoreList = new ArrayList<String>();


        if(args.length > 0) {
            indexDirectoryPath = args[0];
        }
        if(args.length > 1) {
            targetDirectoryPath = args[1];
        }
        if(indexDirectoryPath == null || indexDirectoryPath.isEmpty()) {
            indexDirectoryPath = Consts.indexDir;
        }
        if(targetDirectoryPath == null || targetDirectoryPath.isEmpty()) {
            targetDirectoryPath = Consts.dataDir;
        }

//        CreateIndexService.createIndex(targetDirectoryPath, indexDirectoryPath);
        Searcher searcher = new Searcher(null);
        searcher.init();
        HashMap<Integer, String> dict = FileHelper.readQueriesReturnDict(null);
        for(Integer key : dict.keySet()) {
            int scoreCounter = 1;
            ScoreDoc[] scoreDocs = searcher.search(dict.get(key)).scoreDocs;
            for(ScoreDoc scoreDoc: scoreDocs) {

                Document doc = searcher.getDocument(scoreDoc);
                String docData = key + "\t" + "Q0" +"\t" + doc.get(LuceneConstants.TABLE_ID) +"\t" + scoreCounter + "\t" + scoreDoc.score + "\t" + teamName + "\n";
                scoreList.add(docData);
                scoreCounter++;
            }
        }
        FileHelper.outputToFile(scoreList);
    }

}

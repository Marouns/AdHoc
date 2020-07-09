package com.adhoc.project;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        //this directory will contain the indexes
        Directory indexDirectory =
                FSDirectory.open(new File(indexDirectoryPath).toPath());

        EnglishAnalyzer englishAnalyzer = new EnglishAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(englishAnalyzer);

        //create the indexer
        writer = new IndexWriter(indexDirectory, config);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(JSONObject jsonObject, String key, CleanSentencesService cleanSentencesService) throws Exception {
        Document document = new Document();

        //index file contents
        JSONArray getDataObject = (JSONArray) jsonObject.get(TableKeysConstants.Data);
        JSONArray columnTitles = (JSONArray) jsonObject.get(TableKeysConstants.COLUMN_TITLES);
        String caption = (String) jsonObject.get(TableKeysConstants.CAPTION);
        String pageTitle = (String) jsonObject.get(TableKeysConstants.PAGE_TITLE);
        List<String> allSentences = new ArrayList<>();

        for(Object data : getDataObject) {
            String cleanStopWords = cleanSentencesService.removeStopWordsFromText(data.toString());
//            List<List<String>> cleanStopWordsAndLemma = cleanSentencesService.lemmatizationTokens(cleanStopWords);
//            for(List<String> words : cleanStopWordsAndLemma) {
            allSentences.add(cleanStopWords);
//            }
        }

        String flatAllSentences = String.join(" ", allSentences);
        TextField pageTitleField = new TextField(LuceneConstants.PAGE_TITLE, pageTitle, Field.Store.YES);
        TextField contentField = new TextField(LuceneConstants.CONTENTS, flatAllSentences,Field.Store.YES);
        TextField tableTitlesField = new TextField(TableKeysConstants.COLUMN_TITLES, columnTitles.toString(),Field.Store.YES);
        TextField tableIdField = new TextField(LuceneConstants.TABLE_ID, key ,Field.Store.YES);
        TextField tableCaptionField = new TextField(LuceneConstants.CAPTION, caption, Field.Store.YES);


        document.add(contentField);
        document.add(pageTitleField);
        document.add(tableIdField);
        document.add(tableCaptionField);
        document.add(tableTitlesField);

        return document;
    }

    private void indexFile(File file, CleanSentencesService cleanSentencesService) throws Exception {
        JSONParser jsonParser = new JSONParser();
        FileReader fileReader = new FileReader(file);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

        Iterator<String> keys = jsonObject.keySet().iterator();

        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {

                Document document = getDocument((JSONObject) jsonObject.get(key), key, cleanSentencesService);
                writer.addDocument(document);
            }
        }

        System.out.println("Indexing "+file.getCanonicalPath());

    }

    public int createIndex(String dataDirPath, FileFilter filter)
            throws Exception {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();
        CleanSentencesService cleanSentencesService = new CleanSentencesService();
        cleanSentencesService.init();
        for (File file : files) {
            if(!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && filter.accept(file)
            ){
                try {
                    indexFile(file, cleanSentencesService);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return writer.numRamDocs();
    }
}
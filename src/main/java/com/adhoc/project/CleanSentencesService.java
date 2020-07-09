package com.adhoc.project;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CleanSentencesService {

    public Properties props;
    public StanfordCoreNLP pipeline;
    public CleanSentencesService(){

    }

    public void init(){
        props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    public String removeStopWordsFromText(String text) {

        String[] stopWords = Consts.StopWords;
        String stopWordsPattern = String.join("|", stopWords);
        Pattern pattern = Pattern.compile("\\b(?:" + stopWordsPattern + ")\\b\\s*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll("");
        return text;
    }

    public List<CoreSentence> extractSentences(String text) throws Exception {

        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);
        return doc.sentences();
    }

    public List<List<String>> lemmatizationTokens(String text) throws Exception {

        List<CoreSentence> sentences = extractSentences(text);
        List<List<String>> doc = new ArrayList<>();
        for (CoreSentence sent : sentences) {
            doc.add(sent.lemmas());
        }
        return doc;
    }
}

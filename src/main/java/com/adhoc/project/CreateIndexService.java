package com.adhoc.project;

import java.io.IOException;

public class CreateIndexService {


    public static void createIndex(String dataDir, String targetDir) throws Exception {

        Indexer indexer;
        if(dataDir == null) {
            dataDir = Consts.dataDir;
        }

        if(targetDir == null) {
            targetDir = Consts.indexDir;
        }

        indexer = new Indexer(targetDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }
}

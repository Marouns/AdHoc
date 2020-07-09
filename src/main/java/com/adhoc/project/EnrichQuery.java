package com.adhoc.project;

import de.jungblut.datastructure.KDTree;
import de.jungblut.distance.CosineDistance;
import de.jungblut.glove.GloveRandomAccessReader;
import de.jungblut.glove.impl.*;
import de.jungblut.glove.util.StringVectorPair;
import de.jungblut.math.DoubleVector;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class EnrichQuery {

    public static String BinaryFileName = "glove.6B.50d-bin";
    public static String GloveFileName = System.getProperty("user.dir") + "/glove.6B.50d.txt";
    public GloveRandomAccessReader db;
    GloveRandomAccessReader reader;

    public static void createBinaryFromText() throws IOException {

    }

    public void init() throws IOException {
        File file = new File(BinaryFileName);
        if (!file.exists()) {

            GloveTextReader reader = new GloveTextReader();
            Stream<StringVectorPair> stream = reader.stream(Paths.get(GloveFileName));
            GloveBinaryWriter writer = new GloveBinaryWriter();
            writer.writeStream(stream, Paths.get(BinaryFileName));
        }
        Path path = new File(BinaryFileName).toPath();
        db = new GloveBinaryRandomAccessReader(path);
    }

    public DoubleVector getWordVector(String word) throws IOException {
        return db.get(word);
    }

    public List<String> FindSimilarWords(String word, int k) throws IOException {

        List<String> similarWords = new ArrayList<>();
        GloveRandomAccessReader reader = new CachedGloveBinaryRandomAccessReader(
                new GloveBinaryRandomAccessReader(Paths.get(BinaryFileName)), 100l);
        final KDTree<String> tree = new KDTree<>();

        try (Stream<StringVectorPair> stream = new GloveBinaryReader().stream(Paths.get(BinaryFileName))) {
            stream.forEach((pair) -> {
                tree.add(pair.vector, pair.value);
            });

        }

        System.out.println("Balancing the KD tree...");
        tree.balanceBySort();

        System.out
                .println("Finished, input your word to find its nearest neighbours");


        DoubleVector v = reader.get(word);
        if (v == null) {
            System.err.println("doesn't exist");
        } else {
            System.out.print("Searching....");
            long start = System.currentTimeMillis();
            List<KDTree.VectorDistanceTuple<String>> nearestNeighbours = tree
                    .getNearestNeighbours(v, k);

            // sort and remove the one that we searched for
            Collections.sort(nearestNeighbours, Collections.reverseOrder());
            // the best hit is usually the same item with distance 0
            if (nearestNeighbours.get(0).getValue().equals(word)) {
                nearestNeighbours.remove(0);
            }

            System.out.println("done. Took " + (System.currentTimeMillis() - start)
                    + " millis.");
            for (KDTree.VectorDistanceTuple<String> tuple : nearestNeighbours) {
                System.out.println(tuple.getValue() + "\t" + tuple.getDistance());
                similarWords.add(tuple.getValue());
            }
            System.out.println();
        }
        return similarWords;
    }
}

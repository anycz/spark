package com.meow;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local[*]", "applicationName");
        JavaRDD<String> blRDD = sc.textFile("src/main/resources/data/boite aux lettres.csv")
                // Suppression de la ligne de header
                .filter(line -> !line.startsWith("CO_MUP"))
                .cache();
        blRDD.count();

        Map<String, Integer> blByCityMap = blRDD
                .map(line -> line.split(";"))
                .mapToPair(fields -> new Tuple2<>(fields[4], 1))
                .reduceByKey((x, y) -> x + y)
                .sortByKey()
                .collectAsMap();

        blByCityMap
                .entrySet()
                .stream()
                .sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
                .forEach(entry -> System.out.println(entry.getKey() + " : " + entry.getValue()));
    }
}

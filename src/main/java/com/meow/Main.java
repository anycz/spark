package com.meow;

import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local", "cats");
        sc.textFile("src/main/resources/data/cats.csv")
                .filter(line -> !line.startsWith("geom"))
                .map(line -> line.split(";"))
                .mapToPair(fields -> new Tuple2<>(fields[4], 1))
                .reduceByKey((x, y) -> x + y)
                .sortByKey()
                .foreach(t -> System.out.println(t._1 + " : " + t._2));
    }
}

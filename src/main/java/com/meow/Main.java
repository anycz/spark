package com.meow;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;


public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local[*]", "applicationName");
        SQLContext sqlContext = new SQLContext(sc);
        DataFrame df = sqlContext.read().json("src/main/resources/data/arbres.json");
        df.show();
        df.printSchema();
        df.select("fields.nom_commun", "fields.famille").show();
    }
}

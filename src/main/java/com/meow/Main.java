package com.meow;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local[*]", "applicationName");
        SQLContext sqlContext = new SQLContext(sc);
        JavaRDD<Person> personRdd = sc.textFile("src/main/resources/data/persons.txt")
                .map(line -> {
                    String[] colums = line.split(",");
                    return new Person(colums[0], Integer.parseInt(colums[1]));
                });
        DataFrame df = sqlContext.createDataFrame(personRdd, Person.class);
        df.registerTempTable("people");

        DataFrame teenagers = sqlContext.sql("SELECT name FROM people WHERE age >= 13 AND age <= 19");
        List<String> teens = teenagers.javaRDD()
                .map(row -> "Name : " + row.getString(0))
                .collect();
        teens.stream().forEach(System.out::println);
    }
}

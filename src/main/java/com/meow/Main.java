package com.meow;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local[*]", "applicationName");
        SQLContext sqlContext = new SQLContext(sc);

        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("name", DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("age", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);

        JavaRDD<Row> personRdd = sc.textFile("src/main/resources/data/persons.txt")
                .map(line -> {
                    String[] colums = line.split(",");
                    return RowFactory.create(colums[0], colums[1]);
                });

        DataFrame df = sqlContext.createDataFrame(personRdd, schema);
        df.registerTempTable("people");

        DataFrame teenagers = sqlContext.sql("SELECT name FROM people WHERE age >= 21");
        List<String> teens = teenagers.javaRDD()
                .map(row -> "Name : " + row.getString(0))
                .collect();
        teens.stream().forEach(System.out::println);
    }
}

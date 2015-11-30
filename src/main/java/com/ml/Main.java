package com.ml;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        JavaSparkContext sc = new JavaSparkContext("local", "ml");
        String path = "src/main/resources/data/ml/ml.txt";
        JavaRDD<LabeledPoint> data = sc.textFile(path)
                .map(line -> line.split(","))
                .map(line -> Arrays.asList(line).stream().mapToDouble(Double::parseDouble).toArray())
                .map(fields -> new LabeledPoint(fields[fields.length - 1], new DenseVector(Arrays.copyOfRange(fields, 0, fields.length - 1))));
        data.foreach(e -> System.out.println(e.label() + " ; " + e.features()));
        LogisticRegressionModel model = LogisticRegressionWithSGD.train(data.rdd(), 1);
        JavaRDD<Tuple2<Double, Double>> preds = data.map(p -> new Tuple2<>(p.label(), model.predict(p.features())));
        preds.foreach(p -> {
            System.out.println(p._1 + ": " + p._2);
        });
        System.out.println(preds.count());
        System.out.println(preds.filter(p -> !p._1.equals(p._2)).count());
        System.out.println(data.count());
        double err = preds.filter(p -> !p._1.equals(p._2)).count() / (double) data.count();
        System.out.println("Error: " + err);
    }
}

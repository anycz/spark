package com.ml;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.mllib.feature.StandardScaler;
import org.apache.spark.mllib.feature.StandardScalerModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import scala.Tuple2;

import java.util.Arrays;

public class Wine {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Linear Regression Example").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load and parse the data
        String path = "src/main/resources/data/ml/wine.txt";
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<LabeledPoint> parsedData = data.map(
            new Function<String, LabeledPoint>() {
                public LabeledPoint call(String line) {
                    String[] parts = line.split(";");
                    double[] features = Arrays.stream(parts).mapToDouble(Double::parseDouble).toArray();
                    return new LabeledPoint(Double.parseDouble(parts[parts.length-1]), Vectors.dense(Arrays.copyOf(features, 11)));
                }
            }
        ).cache();

        // Building the model
        LinearRegressionWithSGD regression = new LinearRegressionWithSGD(0.001, 1000, 1.0);
        regression.setIntercept(true);
        LinearRegressionModel model = regression.run(parsedData.rdd());

        // Test the model
        System.out.println(model.predict(Vectors.dense(7.4,0.7,0,1.9,0.076,11,34,0.9978,3.51,0.56,9.4))); // 5
        System.out.println(model.predict(Vectors.dense(6.9,0.765,0.02,2.3,0.063,35,63,0.9975,3.57,0.78,9.9))); // 5
        System.out.println(model.predict(Vectors.dense(11.5,0.54,0.71,4.4,0.124,6,15,0.9984,3.01,0.83,11.8))); // 7
        System.out.println(model.predict(Vectors.dense(5.6,0.85,0.05,1.4,0.045,12,88,0.9924,3.56,0.82,12.9))); // 8
        System.out.println(model.predict(Vectors.dense(11.6,0.58,0.66,2.2,0.074,10,47,1.0008,3.25,0.57,9))); // 3
    }
}

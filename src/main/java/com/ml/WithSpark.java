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

public class WithSpark {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Linear Regression Example").setMaster("local[1]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load and parse the data
        String path = "src/main/resources/data/ml/employees2.txt";
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<LabeledPoint> parsedData = data.map(
            new Function<String, LabeledPoint>() {
                public LabeledPoint call(String line) {
                    String[] parts = line.split(",");
                    return new LabeledPoint(Double.parseDouble(parts[1]), Vectors.dense(Double.parseDouble(parts[0])));
                }
            }
        ).cache();

        // Building the model
        LinearRegressionWithSGD regression = new LinearRegressionWithSGD();
        regression.setIntercept(true);
        regression.optimizer().setStepSize(0.0000000000000001);
        LinearRegressionModel model = regression.run(parsedData.rdd());

        // Save and load model
        // model.save(sc.sc(), "myModelPath");
        // LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(), "myModelPath");
        System.out.println(model.predict(Vectors.dense(30)));
        System.out.println(model.predict(Vectors.dense(1000)));
        System.out.println(model.predict(Vectors.dense(10000)));
        System.out.println(model.predict(Vectors.dense(30000)));
    }
}

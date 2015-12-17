package com.ml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Employees {
    public static void main(String[] args) {
        Data data = Data.loadData();
        double theta0 = 0;
        double theta1 = 0;
        double previousTheta0;
        double previousTheta1;
        double learningRate = 0.0000000000001;
        double m = data.getData().size();
        do {
            double sum = 0;
            for(Map.Entry<Integer, Integer> elem: data.getData().entrySet()) {
                sum += theta0 + theta1*elem.getKey() - elem.getValue();
            }
            double tmpTheta0 = (theta0 - (learningRate * (1 / m) * sum));
            sum = 0;
            for(Map.Entry<Integer, Integer> elem: data.getData().entrySet()) {
                sum += (theta0 + theta1*elem.getKey() - elem.getValue()) * elem.getKey();
            }
            double tmpTheta1 = (theta1 - (learningRate * (1 / m) * sum));
            previousTheta0 = theta0;
            previousTheta1 = theta1;
            theta0 = tmpTheta0;
            theta1 = tmpTheta1;
            // System.out.println("theta0="+theta0);
            // System.out.println("theta1="+theta1);
        } while(Math.abs(theta0 - previousTheta0) > learningRate && Math.abs(theta1 - previousTheta1) > learningRate);
        System.out.println("theta0="+theta0);
        System.out.println("theta1="+theta1);
        int feature1 = 5000;
        int feature2 = 40000;
        double predict1 = theta0+feature1*theta1;
        double predict2 = theta0+feature2*theta1;
        System.out.println(feature1 + " -> " + predict1);
        System.out.println(feature2 + " -> " + predict2);
    }

    static class Data {
        private final int min;
        private final int max;
        private final Map<Integer, Integer> data;

        private Data(int min, int max, Map<Integer, Integer> data) {
            this.min = min;
            this.max = max;
            this.data = data;
        }

        public static Data loadData() {
            BufferedReader in = new BufferedReader(new InputStreamReader(Data.class.getResourceAsStream("/data/ml/employees.txt")));
            Integer[] minMax = new Integer[] {null, null};
            Map<Integer, Integer> map = new HashMap<>();
            in.lines().forEach(e -> {
                String[] split = e.split(",");
                int employeesNumber = Integer.parseInt(split[0]);
                int projectNumber = Integer.parseInt(split[1]);
                if(minMax[0] == null || minMax[0] > projectNumber) {
                    minMax[0] = projectNumber;
                }
                if(minMax[1] == null || minMax[1] < projectNumber) {
                    minMax[1] = projectNumber;
                }
                map.put(employeesNumber, projectNumber);
            });
            return new Data(minMax[0], minMax[1], map);
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public Map<Integer, Integer> getData() {
            return data;
        }
    }
}

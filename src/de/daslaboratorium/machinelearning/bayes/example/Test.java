package de.daslaboratorium.machinelearning.bayes.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classifier;

public class Test {

	private static void forTrain(String path, String fileName,
			Classifier<String, String> bayes) {
		File file = new File(path + fileName);
		BufferedReader reader = null;
		String[] words = null;
		String type = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			int count = 0;
			// 一次读入一行，直到读入null为文件结束
			while ((temp = reader.readLine()) != null) {
				words = temp.split("\t")[0].split(" ");
				type = temp.split("\t")[1];
				bayes.learn(type, Arrays.asList(words));
				count++;
				if (count % 500 == 0) {
					System.out.println("now dealed " + count + " lines ");
				}
//				if (count == 20000) {
//					break;
//				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	private static void dealTrain(String path, String toTrainFile,
			String resultFile, Classifier<String, String> bayes) {

		BufferedReader reader = null;
		FileWriter writer = null;
		try {
			File file = new File(path + toTrainFile);

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(path + resultFile);

			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			String type = null;
			// 一次读入一行，直到读入null为文件结束
			while ((temp = reader.readLine()) != null) {
				// type = getClassification(temp.split(" "));
				type = bayes.classify(Arrays.asList(temp.split(" ")))
						.getCategory();
				writer.write(temp + "\t" + type);
				writer.write("\n");
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// if (args.length != 1) {
		// System.out.println("ERROR,please input the right file path");
		// return;
		// }
		// String inputPath = args[0];
		String inputPath = "/Users/jiangzewei/testdata/";
		String trainFile = "for_train.txt";
		String toTrainFile = "to_train.txt";
		String resultFile = "trained.txt";

		Classifier<String, String> bayes = new BayesClassifier<String, String>();
		
		bayes.reset();

		long start = System.currentTimeMillis();
		forTrain(inputPath, trainFile, bayes);
		System.out.print("take ");
		System.out.print(System.currentTimeMillis() - start);
		System.out.println(" mm");

		start = System.currentTimeMillis();
		dealTrain(inputPath, toTrainFile, resultFile, bayes);
		System.out.print("take ");
		System.out.print(System.currentTimeMillis() - start);
		System.out.println(" mm");

	}

}

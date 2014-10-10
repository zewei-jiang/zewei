package de.daslaboratorium.machinelearning.bayes.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

import net.sf.json.JSONObject;
import de.daslaboratorium.machinelearning.classifier.BayesClassifier;
import de.daslaboratorium.machinelearning.classifier.Classifier;

public class Classfier {
	
	private static Dictionary<String, Integer> stringToDic(String str){
		Dictionary<String, Integer> res = new Hashtable<String, Integer>();
		
		JSONObject json = JSONObject.fromObject(str);
		
		Set<String> set = json.keySet();
		for (String key : set) {
			res.put(key, json.getInt(key));
		}
		return res;
	}
	
	private static Dictionary<String, Dictionary<String, Integer>> stringToDicDic(String str){
		Dictionary<String, Dictionary<String, Integer>> res = new Hashtable<String, Dictionary<String, Integer>>();
		Dictionary<String, Integer> val = null;
		
		JSONObject json = JSONObject.fromObject(str);
		Set<String> set = json.keySet();
		
		JSONObject valJson = null;
		Set<String> valSet = null;
		for (String key : set) {
			val = new Hashtable<String, Integer>();
			valJson = json.getJSONObject(key);
			valSet = valJson.keySet();
			for (String valKey : valSet) {
				val.put(valKey, valJson.getInt(valKey));
			}
			res.put(key, val);
		}
		
		return res;
	}
	
	private static void forTrain(String path, String fileName,
			Classifier<String, String> bayes) {
		File file = new File(path + fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String temp = null;
			String type = null;
			// 一次读入一行，直到读入null为文件结束
			while ((temp = reader.readLine()) != null) {
				type = temp.split("\t")[0];
				if ("ca".equals(type)) {
					bayes.setTotalCategoryCount(stringToDic(temp.split("\t")[1]));
				} else if ("fe".equals(type)) {
					bayes.setTotalFeatureCount(stringToDic(temp.split("\t")[1]));
				} else if ("per".equals(type)) {
					bayes.setGeatureCountPerCategory(stringToDicDic(temp.split("\t")[1]));
				}
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
		
		String inputPath = "/Users/jiangzewei/testdata/";
		String trainJson = "trained.json";
		String toTrainFile = "to_train.txt";
		String resultFile = "sepTrained.txt";

		Classifier<String, String> bayes = new BayesClassifier<String, String>();

		long start = System.currentTimeMillis();
		forTrain(inputPath, trainJson, bayes);
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

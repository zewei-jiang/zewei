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

public class Train {

	
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
	
	private static JSONObject dicToJson(Dictionary<String ,Integer> dic) {
		
		if (dic == null) {
			return  null;
		}
		
		JSONObject json = new JSONObject();
		Set<String> set = ( (Hashtable<String, Integer>) dic).keySet();
		for (String key : set) {
			json.put(key, dic.get(key));
		}
		return json;
	}

	private static JSONObject dicDicToJson(Dictionary<String, Dictionary<String, Integer>> dic) {
		
		if (dic == null) {
			return  null;
		}
		
		JSONObject json = new JSONObject();
		Set<String> set = ( (Hashtable< String, Dictionary<String, Integer>>) dic).keySet();
		JSONObject valJson = null;
		for (String key : set) {
			valJson = new JSONObject();
			Set<String> valSet = ( (Hashtable< String, Integer>) dic.get(key)).keySet();
			for (String valKey : valSet) {
				valJson.put(valKey, dic.get(key).get(valKey)); 
			}
			json.put(key, valJson);
		}
		return json;
	}
	
	private static void writeResult(String path, String fileName,
			Classifier<String, String> bayes) {
		
		FileWriter writer = null;
		try {

			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(path + fileName);
			
			Dictionary<String, Integer> cateDic = bayes.getTotalCategoryCount();
			JSONObject cateJson = dicToJson(cateDic);
			Dictionary<String, Integer> featDic = bayes.getTotalFeatureCount();
			JSONObject featJson = dicToJson(featDic);
			Dictionary<String, Dictionary<String, Integer>> featPerDic = bayes.getFeatureCountPerCategory();
			JSONObject featPerJson = dicDicToJson(featPerDic);
			
			writer.write("ca\t");
			writer.write(cateJson.toString());
			writer.write("\n");
			writer.write("fe\t");
			writer.write(featJson.toString());
			writer.write("\n");
			writer.write("per\t");
			writer.write(featPerJson.toString());
			writer.write("\n");

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
		String trainFile = "for_train.txt";
		String resultFile = "trained.json";

		Classifier<String, String> bayes = new BayesClassifier<String, String>();

		forTrain(inputPath, trainFile, bayes);
		
		writeResult(inputPath, resultFile, bayes);
	}

}

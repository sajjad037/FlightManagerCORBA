package com.concordia.dist.asg1.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.concordia.dist.asg1.Models.Flight;
import com.concordia.dist.asg1.Models.Passenger;
import com.concordia.dist.asg1.Models.ServersList;
import com.concordia.dist.asg1.StaticContent.StaticContent;
import com.google.gson.Gson;

/**
 * This class is for saving a data
 * 
 * @author SajjadAshrafCan
 *
 */
public class FileStorage {

	/**
	 * this method saves servers Configurations
	 * 
	 * @param serversConfiguration
	 * @return
	 */
	public String saveConfigFile(ServersList serverConfig) {
		String fileContent = getJsonFromObject(serverConfig);
		String filePath = (new File(StaticContent.BASE_PATH + "App.Config")).getPath();
		// String filePath = (new
		// File("C:/FlightManagerWebService/App.Config")).getPath();
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(fileContent);
			fileWriter.flush();
			fileWriter.close();
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR : " + e.getMessage();
		}
	}

	/**
	 * this method opens up an Object from file
	 * 
	 * @param new_file
	 * @return
	 */
	public ServersList LoadServerConfigurations() {

		String fileContent = "";
		try {
			fileContent = new String(
					Files.readAllBytes(Paths.get((new File(StaticContent.BASE_PATH + "App.Config")).getPath())));
			// fileContent = new String(Files.readAllBytes(Paths.get((new
			// File("C:/FlightManagerWebService/App.Config")).getPath())));
			return (ServersList) getObjectFromJson(fileContent, ServersList.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean SaveBookingData(HashMap<String, ArrayList<Passenger>> passengerData, String fileName)
			 {

		try {
			File fileOne = new File(StaticContent.BASE_PATH + "Data/" + fileName);
			FileOutputStream fos = new FileOutputStream(fileOne);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(passengerData);
			oos.flush();
			oos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public HashMap<String, ArrayList<Passenger>> ReadBookingData(String fileName) {

		try {
			File file = new File(StaticContent.BASE_PATH + "Data/" + fileName);
			if(file.exists())
			{
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);

				@SuppressWarnings("unchecked")
				HashMap<String, ArrayList<Passenger>> passengerData = (HashMap<String, ArrayList<Passenger>>) ois
						.readObject();

				ois.close();
				fis.close();
				return passengerData;
			}
			else
			{
				return new HashMap<String, ArrayList<Passenger>>();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, ArrayList<Passenger>>();
		}
	}

	public boolean SaveFlightData(CopyOnWriteArrayList<Flight> flightsData, String fileName)  {

		try {
			File fileOne = new File(StaticContent.BASE_PATH + "Data/" + fileName);
			FileOutputStream fos = new FileOutputStream(fileOne);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(flightsData);
			oos.flush();
			oos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public CopyOnWriteArrayList<Flight> ReadFlightData(String fileName) {

		try {
			File file = new File(StaticContent.BASE_PATH + "Data/" + fileName);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis);

				@SuppressWarnings("unchecked")
				CopyOnWriteArrayList<Flight> flightsData = (CopyOnWriteArrayList<Flight>) ois.readObject();

				ois.close();
				fis.close();
				return flightsData;
			}
			else
				return new CopyOnWriteArrayList<Flight>();
		} catch (Exception e) {
			e.printStackTrace();
			return new CopyOnWriteArrayList<Flight>();
		}
	}

	/**
	 * this method gets json from object
	 * 
	 * @param new_object
	 *            new object
	 * @return json converts gson to json and returns it
	 */
	public String getJsonFromObject(Object new_object) {
		Gson gson = new Gson();
		return gson.toJson(new_object);
	}

	/**
	 * this methods gets object from a json
	 * 
	 * @param new_jsonString
	 *            json string object
	 * @param new_class
	 *            new clas
	 * @return object object from json
	 */
	public Object getObjectFromJson(String new_jsonString, Class<?> new_class) {
		Gson gson = new Gson();
		return gson.fromJson(new_jsonString, new_class);
	}

	/**
	 * read all data From File
	 * 
	 * @param path
	 * @param encoding
	 * @return
	 */
	public String readFromFile(String path, Charset encoding) {
		try {
			if (new File(path).exists()) {
				byte[] encoded = Files.readAllBytes(Paths.get(path));
				return new String(encoded, encoding);
			} else {
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * Save whole string to file.
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	public boolean saveToFile(String path, String content) {
		try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
			out.print(content);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove Character From Start or Left side
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromStrartorLeft(String InputString, String Characters) {
		return InputString.replaceAll("^\\" + Characters + "+", "");
	}

	/**
	 * Remove Character From End or Right
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromEndorRight(String InputString, String Characters) {
		return InputString.replaceAll("\\" + Characters + "+$", "");
	}

	/**
	 * Remove Character From Both End
	 * 
	 * @param InputString
	 * @param Characters
	 * @return
	 */
	public String RemoveCharacterFromBothEnd(String InputString, String Characters) {
		return InputString.replaceAll("^\\" + Characters + "+|\\" + Characters + "+$", "");
	}

}

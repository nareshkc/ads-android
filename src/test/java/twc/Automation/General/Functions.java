package twc.Automation.General;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.ComparisonFailure;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import twc.Automation.Driver.Drivers;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
//import twc.Automation.RetryAnalyzer.RetryAnalyzer;
import twc.Automation.RetryAnalyzer.Retry;

public class Functions extends Drivers{
	static Map<String, String>  pubads_call_results=null;
	public static String AdzoneValue=null;
	public static String AdzoneValueVideo=null;
	public static String Validation="Ad_iu";
	public static String alert="breaking";
	 public static SoftAssert softAssert = new SoftAssert();
	//Verify Animated Branded Background ad presented //naresh
			public static void Verify_Animated_BB() throws Exception{
				Thread.sleep(2000);
				try{
					if(Ad.findElementById("com.weather.Weather:id/background_ad_clickable").isDisplayed()){
						System.out.println("Animated BB ad present");
					}
				}catch(Exception e){
					System.out.println("Animated BB ad not presented");
					Assert.fail("Animated BB ad not presented");
				}
			}
	

	//Verify Saved addresslist  and select one Address   //by naresh
	public static void verifySavedAddressList_SelectOne(String AddressName) throws Exception{
			
			logStep("Select Locations From Location Manager ");
			logStep("Tap On Manage Location");
			logStep("Choose Locations Examples: 1) NewYork 2) Atlanta 3) Chicago");
			
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();
				/* --- Start For Android Device --- */
				if(Cap == 2){
				String[][] addressdata = read_excel_data.exceldataread("AddressPage");
				
				WebDriverWait wait4 = new WebDriverWait(Ad, 40);
				wait4.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
				
				//Root Location Element
				Ad.findElementById(addressdata[4][Cap]).click();
				
				WebDriverWait wait5 = new WebDriverWait(Ad, 20);
				wait5.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[6][Cap])));
				
				//List Location Element
				@SuppressWarnings("unchecked")
				List<MobileElement> loclist = Ad.findElements(By.id(addressdata[6][Cap]));
				
				int loc_size = loclist.size() -1;
				
				String loc_length = Integer.toString(loc_size);
				
				System.out.println("Total Saved Address List :::::" + loc_length);
				
				Thread.sleep(2000);
				
				System.out.println("Start Select Address List");
				
				String firsteleXpath = addressdata[5][Cap];
				String[] parts = firsteleXpath.split("Count");
				/* --- Start For Loop For Location Click --- */
				int addcount = 1;
				for(int i=2;i<= loclist.size();i++){
					
					String element = null;
					
					
					try {
						
						element = parts[0]+i+parts[1];
						
								   
						MobileElement ele = (MobileElement) Ad.findElementByXPath(element);
						Thread.sleep(2000);
						System.out.println("For This Location ====>"+ele.getText());
						 if(ele.getText().contains((AddressName))){
						
						WebDriverWait wait9 = new WebDriverWait(Ad, 20);
						wait9.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
						
						Ad.findElementByXPath(element).click();
						
//						WebDriverWait wait10 = new WebDriverWait(Ad, 20);
//						wait10.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
//						
//						Ad.findElementById(addressdata[4][Cap]).click();
						addcount=i;
						break;
						 }else{
							 System.out.println("Verifying wanted address in the list :-"+ i );
						 }
					} catch (Exception e) {
						logStep("Locations Are Not Found From Location List");
						System.out.println(element+" is not found in the location list");
					}
				}/* --- End For Loop For Location Click --- */
				
				Thread.sleep(8000);
//				
//				WebDriverWait wait12 = new WebDriverWait(Ad, 10);
//				wait12.until(ExpectedConditions.presenceOfElementLocated(By.xpath(parts[0]+addcount+parts[1])));
//				
//				Ad.findElementByXPath(parts[0]+addcount+parts[1]).click();
			}/* --- End For Android Device --- */
			System.out.println("End Select Address List");
		}

	
	public static void validate_API_Call_With_PubAds_Call(String excel_sheet_name) throws Exception{
		
		String apicall_results=null;
		String pubadscall_results=null;
		
		Map<String, String> api_call_results = read_API_Call_Data(excel_sheet_name);
		Map<String, String> pubads_call_results = read_Pub_Ad_Call_Data(excel_sheet_name);
		//System.out.println(api_call_results);
		//System.out.println(pubads_call_results);
		if(api_call_results.keySet().size() == 1){
			
			logStep("Verify Lotame Call Should Be Made With http://ad.crwdcntrl.net");

			for (String key : api_call_results.keySet()) {
				//System.out.println("key: " + key + " value: " + api_call_results.get(key));
				apicall_results = api_call_results.get(key).toString().replace("[", "").replace("]", "");
				//System.out.println(apicall_results);
			}
			for (String pubkey : pubads_call_results.keySet()) {
				//System.out.println("key: " + pubkey + " value: " + pubads_call_results.get(pubkey));
				pubadscall_results = pubads_call_results.get(pubkey).toString().replace("[", "").replace("]", "");
				//System.out.println(pubadscall_results);
			}
			
			logStep("Verify Every PubAd Call SG Keyword Values Should Match With ID Values From Lotame API Response");
			logStep("SG Keyword values Are Matched With Lotame API Response");
			String[] pubadsresults = pubadscall_results.split(",");
			for(int i=0;i<pubadsresults.length;i++){
				if(apicall_results.contains(pubadsresults[i])){
					System.out.println("Matched With "+ pubadscall_results +" :::: " + pubadsresults[i]);
				}
				else{
					logStep("SG Keyword values Are Not Matched With Lotame API Response");
					System.out.println("Does Not Matched With "+ pubadscall_results +" :::: " + pubadsresults[i]);
				}
			}

		}
		else{
			logStep("Verify Factual API Call Should Made With Url https://location.wfxtriggers.com");
			logStep("Verify Every PubAd Call FAUD Keyword Values Should Match With Group Values From Factual API Response");
			logStep("Verify Every PubAd Call FGEO Keyword Values Should Match With Index Values From Factual API Response");
			logStep("Faud And Fgeo Keyword Values Are Matched With Factual API Response");
			for (String key : api_call_results.keySet()) {
				//System.out.println("key: " + key + " value: " + api_call_results.get(key));
				apicall_results = api_call_results.get(key).toString().replace("[", "").replace("]", "");
				//ystem.out.println(apicall_results);
			}
			for (String pubkey : pubads_call_results.keySet()) {
				//System.out.println("key: " + pubkey + " value: " + pubads_call_results.get(pubkey));
				pubadscall_results = pubads_call_results.get(pubkey).toString().replace("[", "").replace("]", "");
				//System.out.println(pubadscall_results);
				
				String[] pubadsresults = pubadscall_results.split(",");
				//////////////////////////////////////////
				
				for(int i=0;i<pubadsresults.length;i++){
					if(!pubads_call_results.get(pubkey).equals("nl")){
						if(api_call_results.get(pubkey).contains(pubadsresults[i])){
							System.out.println("Matched With "+ pubads_call_results.get(pubkey) +" :::: " + pubadsresults[i]);
							Retry.maxRetryCount=0;
						}
						else{
							System.out.println("Does Not Matched With "+ pubads_call_results.get(pubkey) +" :::: " + pubadsresults[i]);
							Assert.fail("Does Not Matched With "+ pubads_call_results.get(pubkey) +" :::: " + pubadsresults[i]);
						}
					}
					else{
						logStep("Getting nl Value For "+pubkey+" From PubAd Call");
						System.out.println("Getting nl value for "+pubkey+" from pubads call");
						Retry.maxRetryCount=0;
					}
				}
			}
		}
	}
	
	public static void validate_Wfxtg_Value_With_Pubads_Call(String excel_sheet_name) throws Exception{
		
		logStep("Every PubAd Call WFXTG Keyword Values Should  Matche With WeatherFx API Call Response");
		//CharlesFunctions.ExportSession();
		String apicall_results=null;
		String pubadscall_results=null;
		Thread.sleep(4000);
		//Map<String, String> api_call_results = 
		Map<String, String> wfxtg_val = get_Wfxtg_values(excel_sheet_name);
		Map<String, String> pubads_call_results = read_Pub_Ad_Call_Data(excel_sheet_name);
		pubadscall_results = pubads_call_results.get("wfxtg");
		apicall_results = wfxtg_val.get("current").toString().replace("[", "").replace("]", "");
		System.out.println("Wfxtgcall_results"+apicall_results);
		System.out.println("pubadscall_results"+pubadscall_results);
		if(apicall_results.contains(pubadscall_results)){
			System.out.println("Matched With "+ pubadscall_results +" :::: " + apicall_results);
			logStep("WFXTG Keyword Values Matched");
			Retry.maxRetryCount=0;
		}
		else{
			logStep("WFXTG Keyword Values Not Matched");
			System.out.println("Does Not Matched With "+ pubadscall_results +" :::: " + apicall_results);
			Assert.fail("Does Not Matched With "+ pubadscall_results +" :::: " + apicall_results);
		}
	}
	public static Map<String , String>  read_API_Call_Data(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		Map<String , String> expected_map_results = new HashMap<String, String>();
		ArrayList<String> expected_Values_List = new ArrayList<String>();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		String validateValues = exceldata[11][Cap];
		String[] validate_Values = validateValues.split(",");
		
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		try {
			String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[2][Cap]));
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[3][Cap]));
			
			String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[4][Cap])+7,required_info.indexOf(exceldata[5][Cap]));
			String expectedValues = expected_data.toString();
			
			if(validate_Values.length == 1){
			
			if(expected_data.toString().contains(exceldata[11][Cap])){
				
				String expecteddata = expected_data.substring(expected_data.indexOf("[")+1,expected_data.indexOf("]")-1);
				System.out.println("Expected Data ::"+expecteddata);
				
				String[] expecteddata_into_arrays = expecteddata.split("},");
				String[] expectedValue = null;
				for(String dataKeys:expecteddata_into_arrays)
				{
					
					expectedValue =dataKeys.split(",");
					
					for(String ExpectedValuesKey:expectedValue)
					{
						if(ExpectedValuesKey.contains(exceldata[12][Cap]))
						{
							String replaceWith = ExpectedValuesKey.toString().replace("{", "").trim();
							
							String[] contentkey = replaceWith.toString().split(",");
							String expected_key = contentkey[0].replaceAll("^\"|\"$","");
							String[] contentvalue = expected_key.split(":");
							String expected_results =contentvalue[1].replaceFirst("^\"|\"$","");
							expected_Values_List.add(expected_results);
							if(expected_key.contains(""))
							{
								Assert.assertNotNull(expected_key);
							}
						}
					}
				}
			}
			expected_map_results.put(exceldata[12][Cap], expected_Values_List.toString());
			}
			else{
				
				String validateSecondValues = exceldata[12][Cap];
				String[] validate_Second_Values = validateSecondValues.split(",");
				List<String> fgeo_res = new ArrayList<String>();
				List<String> faud_res = new ArrayList<String>();
				
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(expectedValues);
				JSONObject jsonObject = (JSONObject) obj;
				
				JSONArray fgeoval = (JSONArray) jsonObject.get(validate_Values[0]);
				for(int i=0;i< fgeoval.size();i++){
					
					JSONObject filter = (JSONObject) fgeoval.get(i);
					if(filter.containsKey(validate_Second_Values[0])){
						fgeo_res.add(filter.get(validate_Second_Values[0]).toString());
					}
				}
				
				JSONArray faudval = (JSONArray) jsonObject.get(validate_Values[1]);
				for(int i=0;i< faudval.size();i++){
					
					JSONObject filter = (JSONObject) faudval.get(i);
					if(filter.containsKey(validate_Second_Values[1])){
						faud_res.add(filter.get(validate_Second_Values[1]).toString());
					}
				}
				
				expected_map_results.put("fgeo", fgeo_res.toString());
				expected_map_results.put("faud", faud_res.toString());
			}
		} catch (Exception e) {
			System.out.println(exceldata[1][Cap] +" Call Not Generated");
			Assert.fail(exceldata[1][Cap] +" Call Not Generated");
		}
		
		return expected_map_results;
	}
	
	public static Map<String , String> read_Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		Map<String , String> expected_results = new HashMap<String, String>();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		String validateValues = exceldata[16][Cap];
		String[] validate_Values = validateValues.split(",");
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[17][Cap]));
		String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[7][Cap]));
		
		required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
		required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
		required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");
		
		required_info = required_info.substring(required_info.indexOf(exceldata[14][Cap]),required_info.indexOf(exceldata[15][Cap]));
		
		
		String pubad_cust_params_data = required_info.toString();
		
		String[] pubadvalue = pubad_cust_params_data.split(exceldata[13][Cap]);
		
			for(String pubadkey:pubadvalue){
				
				String[] key = pubadkey.split("=");
				
				for(int i=0;i<validate_Values.length;i++){	
					
					if(key[0].equals(validate_Values[i])){
						expected_results.put(validate_Values[i], key[1].toString());
					}
				}
			}
		return expected_results;
	}
	
	public static void clean_App_Launch(String excel_sheet_name) throws Exception{
		
		logStep("Scroll Down To End Of The App And Verifying That Up To Feed_5 Ad Calls Made");
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		String feedVal=exceldata[3][Cap].toString().trim();

		System.out.println("Feeds Val are :"+feedVal.trim());

		int feedcount=Integer.parseInt(feedVal);
		
		List<String> pubads_not_match = new ArrayList<String>();
		
		boolean isExceptionOccered = false;
		
		for(int Feed=0;Feed<=feedcount;Feed++){
			
			String pubadcal;

			if(Feed==0){
			
				try {
					pubadcal = sb.toString().substring(sb.toString().lastIndexOf(exceldata[1][Cap]));
					if(pubadcal.toString().contains(exceldata[1][Cap])){
						System.out.println("BB Ad Call Generated");
						Retry.maxRetryCount=0;
						  //pubads_call_results=Functions.read_Ad_Calls_Data("SlotName");
						String Read_API_Call_Data=null;
						String Cust_params=null;
						
						//String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[2][Cap]));	
						String required_info = pubadcal.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[5][Cap]));
						required_info = required_info.substring(required_info.indexOf(exceldata[6][Cap]),required_info.indexOf(exceldata[7][Cap]));
						/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
						required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
						required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
						//required_info= required_info.toString().replaceAll("%2F", "/");
						System.out.println("required_info is :"+required_info);
						required_info= required_info.toString().replaceAll("%2F", "/");
						System.out.println("iu value is  ::: "  + required_info);	
						String required_info1 = pubadcal.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[8][Cap]));
						required_info1 = pubadcal.substring(required_info1.indexOf(exceldata[9][Cap]),required_info1.indexOf(exceldata[10][Cap]));
						System.out.println(required_info1);
						required_info1= Read_API_Call_Data.toString().replaceAll("US%26", "");
						System.out.println(required_info1);
						required_info1= required_info1.toString().replaceAll("%3D", "=");
						System.out.println(required_info1);

					}
				} catch (Exception e) {
					pubads_not_match.add(Integer.toString(Feed));
					isExceptionOccered=true;
				}
			}
			else
			{
				String feedcall = exceldata[2][Cap]+Feed;
				try {
					pubadcal = sb.toString().substring(sb.toString().lastIndexOf(exceldata[2][Cap]+Feed));
					//System.out.println(pubadcal);
					if(pubadcal.toString().contains(feedcall)){
						System.out.println("Feed_"+Feed +" Ad Call Generated");
						Retry.maxRetryCount=0;
						//Functions.read_Ad_Calls_Data("SlotName");
						String Read_API_Call_Data=null;
						String Cust_params=null;
						
						//String required_info = pubadcal.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[2][Cap]));	
						String required_info = pubadcal.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[5][Cap]));
						required_info = required_info.substring(required_info.indexOf(exceldata[6][Cap]),required_info.indexOf(exceldata[7][Cap]));
						/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
						required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
						required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
						//required_info= required_info.toString().replaceAll("%2F", "/");
						System.out.println("required_info is :"+required_info);
						required_info= required_info.toString().replaceAll("%2F", "/");
						System.out.println("iu value is  ::: "  + required_info);	
						String required_info1 = pubadcal.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[8][Cap]));
						required_info1 = required_info1.substring(required_info1.indexOf(exceldata[9][Cap]),required_info1.indexOf(exceldata[10][Cap]));
						System.out.println(required_info1);
						required_info1= required_info1.toString().replaceAll("US%26", "");
						System.out.println(required_info1);
						required_info1= required_info1.toString().replaceAll("%3D", "=");
						System.out.println(required_info1);
					}
				} catch (Exception e) {
					pubads_not_match.add(Integer.toString(Feed));
					isExceptionOccered=true;
				}
			}
		}
		for(int Feed=0;Feed<=feedcount;Feed++){
			if(isExceptionOccered){
				logStep("Feed_"+pubads_not_match + " Ad Call Not Generated");
				Assert.fail("Feed_"+pubads_not_match + " Ad Call Not Generated");
			}
		}
		
		
	}
	
	public static void bb_call_validation(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		Thread.sleep(4000);
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		try {
			String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[17][Cap]));
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[17][Cap]));
			
			String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[14][Cap]),required_info.indexOf(exceldata[15][Cap]));
			String expectedValues = expected_data.toString();
			
			System.out.println("BB Call Value is : "+expectedValues);
			
			if(excel_sheet_name.equalsIgnoreCase("PreRollVideo"))
			{
				if(expectedValues.contains(exceldata[17][Cap])){
					logStep("Video Ad Call Generated");
					System.out.println("Video ad call Value is : "+expectedValues);
					System.out.println("Video ad call generated");
					Retry.maxRetryCount=0;
				}
				
			}
			else if(excel_sheet_name.equalsIgnoreCase("Pulltorefresh"))
			{
				if(expectedValues.contains(exceldata[17][Cap])){
					logStep("BB Ad Call Generated");
					System.out.println("BB Call Value is : "+expectedValues);
					System.out.println("BB Call generated");
					Retry.maxRetryCount=0;
				}
				
			}
			else if(excel_sheet_name.equalsIgnoreCase("TestMode")){
				if(expectedValues.contains(exceldata[17][Cap])){
					logStep("BB Call Generated Successfully");
					System.out.println("BB Call Value is : "+expectedValues);
					System.out.println("BB Call generated");
					Retry.maxRetryCount=0;
				}
			}
		}catch (Exception e) {
			logStep("Ad call should be genrated");
			System.out.println(" Call not generated");
			Assert.fail(" Call not generated. ");
			
		}
	}
	
	
	//############
	
public static void bb_call_validation_Ski(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		Thread.sleep(4000);
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		
		MobileElement extendModule = (MobileElement) Ad.findElementById(exceldata[6][Cap]);
		
		if(exceldata[1][Cap].equalsIgnoreCase("Ski")){
			if(extendModule.isDisplayed() )
			{
				MobileElement extendModuleValidate = (MobileElement) Ad.findElementById(exceldata[4][Cap]);
				if(extendModule.getText().contains(exceldata[1][Cap]) )
				{
					System.out.println("On Extended "+exceldata[1][Cap]+" page");
					WebDriverWait wait1 = new WebDriverWait(Ad, 10);
					wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
					MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
					if (AdEle.isDisplayed()) {
						logStep("d is present on Extended"+exceldata[1][Cap]+" module");
						System.out.println("Ad is present on Extended page");
						
						Thread.sleep(2000);
											}
				}
			}	
			
			
			
		}
		
		try {
			
				if(sb.toString().indexOf(exceldata[13][Cap])>=0);
				{
					logStep("Ski Ad is present");
					System.out.println("Ski Ad is present");
					//RetryAnalyzer.count=0;
					if(sb.contains(exceldata[13][Cap]) && sb.contains(exceldata[13][Cap])&&sb.contains(exceldata[13][Cap])) {
						logStep("Ski Ad Call Generated");
						System.out.println("Ski Ad call generated");
						Retry.maxRetryCount=0;
					}
				}
		
		}catch (Exception e) {
				logStep("Ski Ad call should be genrated");
				System.out.println("Ski Ad Call not generated");
				Assert.fail(" Call not generated. ");
			
		}
	}
	
	
	//############
	
	@SuppressWarnings("unchecked")
	public static void beacons_validation(String excel_sheet_name) throws Exception{
		
		logStep("Verify BB Ad Presented On Home page ");
		if(excel_sheet_name.contains("ThirdpartyBecon")){
			logStep("Verify BB Ad Call Response Should Contains The Urls 1) CreativeId 2) ThirdPartyBeacon 3) ThirdPartySurvey ");
		}
		else{
			logStep("Verify BB Ad Call Response Should Contains The Urls 1) CreativeId 2) Video URL 3) FourthPartyBeacon ");
		}
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		Thread.sleep(4000);
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[2][Cap]));
		String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[2][Cap]));
		String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[2][Cap]),required_info.indexOf(exceldata[3][Cap]));
		String expectedValues = expected_data.toString();
		
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		String[] keypairs = expectedValues.split(exceldata[4][Cap]);
		
		try {
			for (String keyvalue : keypairs)
			{
			    String[] key_value = keyvalue.split(exceldata[5][Cap],2);
			    if(key_value[0].trim().equals(exceldata[8][Cap].trim())){
			    	if(!empty(key_value[1])){
			    		if(Read_API_Call_Data.contains("fourthPartyBeacon")){
			    		String fourthPartyBeaconURL = key_value[1].trim();
			    		
			    		String[] y = Read_API_Call_Data.toString().split("fourthPartyBeacon: ",2);
			    		
			    		String str = y[1];
			    		String findStr = fourthPartyBeaconURL;
			    		findStr=findStr.substring(findStr.indexOf("gampad"), findStr.length());
		    		
			    	
			    		
			    		//int index = 0;
			    		int count = 0;
			    		
			    		int p = 0;
						while(p != -1){
			    		    p = str.indexOf(findStr,p);
			    		    if(p != -1){
			    		        count ++;
			    		        p =p+findStr.length();
			    		    }
			    		}
			    		
			    		
			    		if(count>=2){
			    			logStep("FourthPartyBeacon call getting fired");
			    			System.out.println("FourthPartyBeacon call getting fired");
			    			Retry.maxRetryCount=0;
			    		}
			    		else{
			    			logStep("FourthPartyBeacon call not getting fired");	
			    			System.out.println("FourthPartyBeacon call not getting fired");
			    			Assert.fail("FourthPartyBeacon call not getting fired");
			    			
			    		}
			    	}
			    	
			    	}
			    	
			    }
			    map.put(key_value[0], key_value[1]);
			}
			
		
			if(!empty(map.get(exceldata[6][Cap])) && !empty(map.get(exceldata[7][Cap])) && !empty(map.get(exceldata[8][Cap]))){
				
				logStep("After Getting BB Ad Call,Verified ThirdPartyBeacon Values From BB Ad Call Response");
				logStep(exceldata[6][Cap]+" Value Is "+map.get(exceldata[6][Cap]));
				logStep(exceldata[7][Cap]+" Value Is "+map.get(exceldata[7][Cap]));
				logStep(exceldata[8][Cap]+" Value Is "+map.get(exceldata[8][Cap]));
				
				System.out.println(exceldata[6][Cap]+" Value is "+map.get(exceldata[6][Cap]));
				System.out.println(exceldata[7][Cap]+" Value is "+map.get(exceldata[7][Cap]));
				System.out.println(exceldata[8][Cap]+" Value is "+map.get(exceldata[8][Cap]));
				Retry.maxRetryCount=0;
			}
		} catch (Exception e) {
			logStep("After Getting BB Ad Call, Verified ThirdPartyBeacon Values Not Presented");
			System.out.println(exceldata[1][Cap] +" not available.");
			Assert.fail(exceldata[1][Cap] +" not available.");
		}
	}
	
	
	


	private static boolean empty(Object object) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static String get_pub_ad_call(int feed) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String expectedValues =null;
		String[][] exceldata = read_excel_data.exceldataread("AllFeeds");
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		for(int i=0;i<=10;i++){
		if(sb.toString().contains(exceldata[17][Cap]+feed)){
			
		String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[17][Cap]+feed));
		String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[7][Cap]));
		required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
		required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
		required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");
		
		String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[14][Cap]),required_info.indexOf(exceldata[15][Cap]));
		expectedValues = expected_data.toString();
		}
		}
		return expectedValues;
	}
	
	public static void validate_CXTG_values(String excel_sheet_name) throws Exception{
		
		logStep("Verify WeatherFX API Call Should Made In Charles");
		logStep("Verify CXTG Values From WeatherFX API Call Response");
		logStep("PubAd Call CXTG Keyword Values Are Matched With Weather FX API Call CXTG Values Based On Locations");
		
		Map<String, String> cxtg_res = get_wfxtriggers_call(excel_sheet_name);
		Map<String, String> pubad_res = null;
		List<String> cxtg_not_match = new ArrayList<String>();
		String finalval=null;
		boolean isExceptionOccered = false;
		Set<String> keys = cxtg_res.keySet();
		for (String key : keys) {
			pubad_res = get_pubad_call_by_zip(excel_sheet_name,"%26zip%3D"+key);
			finalval = cxtg_res.get(key).substring(1, cxtg_res.get(key).length() -1);
			System.out.println("CXTG Zip:::"+key+" CXTG Value :::"+finalval);
			System.out.println("Pub Zip:::"+pubad_res.get("zip")+" CXTG Value :::"+pubad_res.get("cxtg"));
			try {
				if(pubad_res.get("cxtg").equals("nl")){
					finalval=pubad_res.get("cxtg");
				Assert.assertEquals(pubad_res.get("cxtg"),finalval);
				Retry.maxRetryCount=0;
				break;
				}else
				{
					Assert.assertEquals(pubad_res.get("cxtg"),finalval);
					Retry.maxRetryCount=0;
					break;
				}

			} catch (ComparisonFailure e) {
				System.out.println(key + " Doesn't Match");
				cxtg_not_match.add(key);
				isExceptionOccered= true;
			}
			if(isExceptionOccered){
				logStep("WeatherFX Call Is Not Made.");
				logStep("Response From WeatherFX Should Be In JSON");
				System.out.println(cxtg_not_match);
				Assert.fail(cxtg_not_match + " are not matched");
			}
        }
	}
	
	
	

	public static Map<String, String> get_Wfxtg_values(String excel_sheet_name) throws Exception{
		
		Map<String , String> wfxtriggers_values = new HashMap<String, String>();
		String wxtgValues="";
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		try {
			String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[2][Cap]));
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[3][Cap]));
				
			String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[4][Cap])+7,required_info.indexOf(exceldata[5][Cap]));
			wxtgValues = expected_data.toString();
				
			JSONParser parser = new JSONParser();
			Object obj=null;
			try{
				obj = parser.parse(wxtgValues);
				}catch(Exception e){
					wxtgValues=wxtgValues.replace("]]", "");
					obj = parser.parse(wxtgValues);
				}
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject wfxtgval = (JSONObject) jsonObject.get("wfxtg");
			wfxtriggers_values.put("current", wfxtgval.get("current").toString());
		} catch (Exception e) {
			System.out.println(exceldata[1][Cap] +" Call Not Generated");
			Assert.fail(exceldata[1][Cap] +" Call Not Generated");
		}	
		
		return wfxtriggers_values;
			
	}
	public static Map<String, String> get_wfxtriggers_call(String excel_sheet_name) throws Exception{
		
		Map<String , String> wfxtriggers_values = new HashMap<String, String>();
		String wxtgValues="";
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		String jsonValues = exceldata[11][Cap];
		String[] json_Values = jsonValues.split(",");
		
		String validateValues = exceldata[16][Cap];
		String[] validate_Values = validateValues.split(",");
		
		/* --- Start JSON Parser for wfxtg Values --- */
		
			
			read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
			String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
			
			try {
				String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[2][Cap]));
				String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[3][Cap]));
				
				String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[4][Cap])+7,required_info.indexOf(exceldata[5][Cap]));
				wxtgValues = expected_data.toString();
				
				JSONParser parser = new JSONParser();
				Object obj=null;
				try{
				obj = parser.parse(wxtgValues);
				}catch(Exception e){
					wxtgValues=wxtgValues.replace("]]", "");
					obj = parser.parse(wxtgValues);
				}
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject wfxtgval = (JSONObject) jsonObject.get(json_Values[0]);
				JSONArray scatterSegsVal = (JSONArray) wfxtgval.get(json_Values[1]); 
				
				/* --- Start For Loop Main JSON Parser --- */
				for(int i=0;i< scatterSegsVal.size();i++){
					
					JSONObject zcsVal = (JSONObject) scatterSegsVal.get(i);
					/* --- Start Key Pair Contains ZCS --- */
					if(zcsVal.containsKey(exceldata[12][Cap])){
						JSONArray jsonArray = (JSONArray) zcsVal.get(exceldata[12][Cap]);
						/* --- Start ZCS contains multipul ZIP Values --- */
						for(int j=0;j< jsonArray.size();j++){
							JSONObject zipval = (JSONObject) jsonArray.get(j);
							/* --- Start Key Pair Contains ZIP --- */
							if(zipval.containsKey(validate_Values[0])){
								wfxtriggers_values.put(zipval.get(validate_Values[0]).toString(), zipval.get(validate_Values[1]).toString());
							}/* --- End Key Pair Contains ZIP --- */
							
						}/* --- End ZCS contains multipul ZIP Values --- */
						
					}/* --- End Key Pair Contains ZCS --- */

				}/* --- End For Loop Main JSON Parser --- */
			} catch (Exception e) {
				System.out.println(exceldata[1][Cap] +" Call Not Generated");
				Assert.fail(exceldata[1][Cap] +" Call Not Generated");
			}
			
		return wfxtriggers_values;
	}
	
	
		public static Map<String, String> get_pubad_call_by_zip(String excel_sheet_name,String Zip) throws Exception{
		
		Map<String , String> cxtg_values = new HashMap<String, String>();
		String cxtgValues="";
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		String validateValues = exceldata[16][Cap];
		String[] validate_Values = validateValues.split(",");
		/* --- Start JSON Parser for wfxtg Values --- */
		
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		
		try {
			
			String Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(Zip));
			String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(Zip));
			String expected_data = required_info.toString().substring(required_info.indexOf(Zip),required_info.indexOf(exceldata[15][Cap]));
			expected_data= expected_data.toString().replaceAll(exceldata[8][Cap], "=");
			expected_data= expected_data.toString().replaceAll(exceldata[9][Cap], "&");
			expected_data= expected_data.toString().replaceAll(exceldata[10][Cap], ",");
			expected_data= expected_data.toString().replaceAll("%22%7D%5D%7D", "");
			cxtgValues = expected_data.toString();
			
			String[] arrays = cxtgValues.split("&");
			for(String keys : arrays){
				if(keys.contains("=")){
					String[] key = keys.split("=");
					if(key[0].equals(validate_Values[0])){
						cxtg_values.put(key[0], key[1]);
					}
					if(key[0].equals(validate_Values[1])){
						cxtg_values.put(key[0], key[1]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Pub Ad Call Not Generated");
			Assert.fail("Pub Ad Call Not Generated");
		}
		return cxtg_values;
	}
	
	public static void verifySavedAddressList(int SelectAddress) throws Exception{
		
		logStep("Select Locations From Location Manager ");
		logStep("Tap On Manage Location");
		logStep("Choose Locations Examples: 1) NewYork 2) Atlanta 3) Chicago");
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
			/* --- Start For Android Device --- */
			if(Cap == 2){
			String[][] addressdata = read_excel_data.exceldataread("AddressPage");
			
			Thread.sleep(30000);

			WebDriverWait wait4 = new WebDriverWait(Ad, 60);
			wait4.until(ExpectedConditions.presenceOfElementLocated(By.name(addressdata[4][Cap])));
			
			//Root Location Element
			Ad.findElementByName(addressdata[4][Cap]).click();
			Thread.sleep(6000);
			Ad.hideKeyboard();
			WebDriverWait wait5 = new WebDriverWait(Ad, 40);
			wait5.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[6][Cap])));
			
			//List Location Element
			@SuppressWarnings("unchecked")
			List<MobileElement> loclist = Ad.findElements(By.id(addressdata[6][Cap]));
			
			int loc_size = loclist.size() -1;
			
			String loc_length = Integer.toString(loc_size);
			
			System.out.println("Total Saved Address List :::::" + loc_length);
			
			Thread.sleep(2000);
			
			System.out.println("Start Select Address List");
			
			String firsteleXpath = addressdata[5][Cap];
			String[] parts = firsteleXpath.split("Count");
			/* --- Start For Loop For Location Click --- */
			for(int i=1;i<= SelectAddress;i++){
				
				String element = null;
				
				try {
					
					element = parts[0]+i+parts[1];
							   
					MobileElement ele = (MobileElement) Ad.findElementByXPath(element);
					Thread.sleep(2000);
					System.out.println("For This Location ====>"+ele.getText());
					
					WebDriverWait wait9 = new WebDriverWait(Ad, 40);
					wait9.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
					
					Ad.findElementByXPath(element).click();
					
//					WebDriverWait wait10 = new WebDriverWait(Ad, 40);
//					wait10.until(ExpectedConditions.presenceOfElementLocated(By.name(addressdata[4][Cap])));
					Thread.sleep(6000);
					//Root Location Element
					if(i==SelectAddress){
						System.out.println("location selection end");
					}else{
					Ad.findElementByName(addressdata[4][Cap]).click();
					Thread.sleep(6000);
					Ad.hideKeyboard();
					}
					
				} catch (Exception e) {
					logStep("Locations Are Not Found From Location List");
					System.out.println(element+" is not found in the location list");
				}
			}/* --- End For Loop For Location Click --- */
			
//			Thread.sleep(8000);
//			
//			WebDriverWait wait12 = new WebDriverWait(Ad, 10);
//			wait12.until(ExpectedConditions.presenceOfElementLocated(By.xpath(parts[0]+1+parts[1])));
//			
//			Ad.findElementByXPath(parts[0]+1+parts[1]).click();
		}/* --- End For Android Device --- */
		System.out.println("End Select Address List");
	}
	
	public static void maplocal_bbcall_validation(String excel_sheet_name, String mode) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		Thread.sleep(5000);
		
		try {
			String pubadcal = sb.toString().substring(sb.toString().lastIndexOf(exceldata[1][Cap]));
		
			if(pubadcal.toString().contains(exceldata[1][Cap])){
				System.out.println("BB Ad Call Generated");
				if(mode.equals("severe2") || mode.equals("withalert")){
					System.out.println("BB Ad Call Should Not Been Generated");
					Assert.fail("BB Ad Call Should Not Been Generated");
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("BB Ad Call Not Generated");
		}
	}
public static Map<String , String> readAdzone__Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		Map<String , String> expected_results = new HashMap<String, String>();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		String validateValues = exceldata[16][Cap];
		String[] validate_Values = validateValues.split(",");
		
 		read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
		String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
		Thread.sleep(10000);
		String Read_API_Call_Data=null;
		try{
			//Validation="Ad_iu";
			if(Validation.equals("Ad_iu")){
		
		Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[17][Cap]));
			}else{
				Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[18][Cap]));
			}
		}catch(Exception e){
			//Validation="Video_iu";
			//Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[18][Cap]));
			System.out.println("Ad call is not presented");
		}
		String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[7][Cap]));
		
		required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
		required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
		required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");
		required_info= required_info.toString().replaceAll("%2F", "/");
		required_info = required_info.substring(required_info.indexOf(exceldata[14][Cap]),required_info.indexOf(exceldata[15][Cap]));
		System.out.println("required_info is :"+required_info);
		required_info= required_info.toString().replaceAll("iu=/7646/app_android_us/", "");
		required_info= required_info.toString().replaceAll("/","");
//		if(required_info.toString().equals("HOUSE_AD_BANNER")){
//			required_info="displaydetailsarticles";
//		}
		if(Validation.contains("Ad_iu"))
		{		AdzoneValue= required_info.toString();
		System.out.println("Adzone value"+ AdzoneValue);
		
		}else if( Validation.contains("Video_iu")){
			AdzoneValueVideo=required_info.toString();
			System.out.println("Adzone video value"+AdzoneValueVideo);
		}
		return expected_results;
	}
	
public static void news_article_call() throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	String[][] exceldata = read_excel_data.exceldataread("Adzone");
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(5000);
	String Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[20][Cap]));

}
public static void Drag_alerts_from_Notificationsbar() throws Exception
{
	MobileElement statusBar = (MobileElement) Ad
			.findElementById("android:id/statusBarBackground");
	System.out.println("Dragging from status bar");
	Thread.sleep(3000);
	MobileElement videoTile = (MobileElement) Ad
			.findElement(By
					.xpath("//*[@class='android.widget.TextView' and @text='Airlock']"));
	System.out.println("Dragging till " + videoTile.getText());

	TouchAction action = new TouchAction(Ad);
	Dimension dimensions = Ad.manage().window().getSize();
	Double startY = (double) dimensions.getHeight();
	Double startX = (double) dimensions.getWidth();
	System.out.println("StartX :" + startX + "startY" + startY);
	action.longPress(statusBar).moveTo(videoTile).release().perform();
	

}
public static Map<String , String> readPushalerts_breakingnews_Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	Map<String , String> expected_results = new HashMap<String, String>();
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	String validateValues = exceldata[16][Cap];
	String[] validate_Values = validateValues.split(",");
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(10000);
	String Read_API_Call_Data=null;
	String Cust_params=null;
	try{
	Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[40][Cap]));
		}
	catch(Exception e){		
		System.out.println("Ad call is not presented");
	 }
	
	//String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));	
	String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));
	required_info = required_info.substring(required_info.indexOf(exceldata[42][Cap]),required_info.indexOf(exceldata[43][Cap]));
	/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
	required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
	required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
	//required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("required_info is :"+required_info);
	required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("iu of breaking news alert  ::: "  + required_info);	
	String required_info1 = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[44][Cap]));
	required_info1 = required_info1.substring(required_info1.indexOf(exceldata[45][Cap]),required_info1.indexOf(exceldata[46][Cap]));
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("%3D", "=");
	required_info1= required_info1.toString().replaceAll("%26", "");
	System.out.println(required_info1);
		if(required_info1.contains("breaking")){
			System.out.println("alert param contains breking");
		}else
		{
			softAssert.fail("alert param does't contains breaking");
		}
		
	//}
	
	return expected_results;
}
public static Map<String , String> readPushalerts_lightingnews_Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	Map<String , String> expected_results = new HashMap<String, String>();
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	String validateValues = exceldata[16][Cap];
	String[] validate_Values = validateValues.split(",");
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(10000);
	String Read_API_Call_Data=null;
	String Cust_params=null;
	try{
	Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[40][Cap]));
		}
	catch(Exception e){		
		System.out.println("Ad call is not presented");
	 }
	//String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));	
	String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));
	required_info = required_info.substring(required_info.indexOf(exceldata[42][Cap]),required_info.indexOf(exceldata[43][Cap]));
	/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
	required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
	required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
	//required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("required_info is :"+required_info);
	required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("iu value is  ::: "  + required_info);	
	String required_info1 = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[44][Cap]));
	required_info1 = required_info1.substring(required_info1.indexOf(exceldata[45][Cap]),required_info1.indexOf(exceldata[46][Cap]));
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("%3D", "=");
	required_info1= required_info1.toString().replaceAll("%26", "");
    System.out.println(required_info1);
	if(required_info1.contains("lghtng")){
		System.out.println("alert param contains breking");
	}else
	{
		softAssert.fail("alert param does't contains breaking");
	}
	
	//}
	
	return expected_results;
}

public static Map<String , String> readPushalerts_realtimerain_Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	Map<String , String> expected_results = new HashMap<String, String>();
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	
	String validateValues = exceldata[16][Cap];
	String[] validate_Values = validateValues.split(",");	
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(10000);
	String Read_API_Call_Data=null;
	String Cust_params=null;
	try{	
		Thread.sleep(6000);
	Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[40][Cap]));
	Thread.sleep(6000);
	}
	catch(Exception e){		
		System.out.println("Ad call is not presented");
	 }
	Thread.sleep(6000);
	String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));	
	Thread.sleep(6000);
	required_info = required_info.substring(required_info.indexOf(exceldata[42][Cap]),required_info.indexOf(exceldata[43][Cap]));
	/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
	required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
	required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
	//required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("required_info is :"+required_info);
	required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("iu value is  ::: "  + required_info);	
	String required_info1 = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[44][Cap]));
	required_info1 = required_info1.substring(required_info1.indexOf(exceldata[45][Cap]),required_info1.indexOf(exceldata[46][Cap]));
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("%3D", "=");
	required_info1= required_info1.toString().replaceAll("%26", "");
	System.out.println(required_info1);
	if(required_info1.contains("rtrain")){
		System.out.println("alert param contains real time rain");
	}else
	{
		
		//Assert.fail("alert param does't contains breaking");
		softAssert.fail("alert param does't contains breaking");
		
	}
	System.out.println("/");
	
	/*int i=0;
	if(i==0){
		if(required_info1.contains("rtrain")){
			System.out.println("alert param contains real time rain");
		}else
		{
			softAssert.fail("alert param does't contains breaking");
			
		}
		i=i+1;
		System.out.println("i is :"+i);
	}
	if(i==1)
	{
		if(required_info1.contains("breaking")){
			System.out.println("alert param contains breking");
		}else
		{
			softAssert.fail("alert param does't contains breaking");
		}
		i=i+1;
		System.out.println("i is :"+i);
	}
	if(i==2)
	{
		if(required_info1.contains("lghtng")){
			System.out.println("alert param contains breking");
		}else
		{
			softAssert.fail("alert param does't contains breaking");
		}
		i=i+1;
		System.out.println("i is :"+i);
		
	}
	if(i==3)
	{
		if(required_info1.contains("severe")){
			System.out.println("alert param contains severe");
		}else
		{
			softAssert.fail("alert param does't contains breaking");
			
		}	
		i=i+1;
		System.out.println("i is :"+i);
	}*/
	
	
	//}
	
	return expected_results;
}
public static Map<String , String> readPushalerts_severe_Pub_Ad_Call_Data(String excel_sheet_name) throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	Map<String , String> expected_results = new HashMap<String, String>();
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	
	String validateValues = exceldata[16][Cap];
	String[] validate_Values = validateValues.split(",");	
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(10000);
	String Read_API_Call_Data=null;
	String Cust_params=null;
	try{	
	Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[40][Cap]));
	}
	catch(Exception e){		
		System.out.println("Ad call is not presented");
	 }
	//String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));	
	String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[41][Cap]));
	required_info = required_info.substring(required_info.indexOf(exceldata[42][Cap]),required_info.indexOf(exceldata[43][Cap]));
	/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
	required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
	required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
	//required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("required_info is :"+required_info);
	required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("iu value is  ::: "  + required_info);	
	String required_info1 = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[44][Cap]));
	required_info1 = required_info1.substring(required_info1.indexOf(exceldata[45][Cap]),required_info1.indexOf(exceldata[46][Cap]));
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("%3D", "=");
	required_info1= required_info1.toString().replaceAll("%26", "");
	System.out.println(required_info1);
	if(required_info1.contains("severe")){
		System.out.println("alert param contains severe");
	}else
	{
		softAssert.fail("alert param does't contains breaking");
		
	}
	
	//}
	
	return expected_results;
}

public static Map<String , String> read_Ad_Calls_Data(String excel_sheet_name) throws Exception{
	
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	Map<String , String> expected_results = new HashMap<String, String>();
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	
	String validateValues = exceldata[9][Cap];
	String[] validate_Values = validateValues.split(",");	
	read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
	String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
	Thread.sleep(10000);
	String Read_API_Call_Data=null;
	String Cust_params=null;
	try{	
	Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[1][Cap]));
	}
	catch(Exception e){		
		System.out.println("Ad call is not presented");
	 }
	//String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[2][Cap]));	
	String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[2][Cap]));
	required_info = required_info.substring(required_info.indexOf(exceldata[3][Cap]),required_info.indexOf(exceldata[4][Cap]));
	/*required_info= required_info.toString().replaceAll(exceldata[8][Cap], "=");
	required_info= required_info.toString().replaceAll(exceldata[9][Cap], "&");
	required_info= required_info.toString().replaceAll(exceldata[10][Cap], ",");*/
	//required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("required_info is :"+required_info);
	required_info= required_info.toString().replaceAll("%2F", "/");
	System.out.println("iu value is  ::: "  + required_info);	
	String required_info1 = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[5][Cap]));
	required_info1 = required_info1.substring(required_info1.indexOf(exceldata[6][Cap]),required_info1.indexOf(exceldata[7][Cap]));
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("US%26", "");
	System.out.println(required_info1);
	required_info1= required_info1.toString().replaceAll("%3D", "=");
	System.out.println(required_info1);

	
	
	//}
	
	return expected_results;
}

public static Map<String , String>  readI_Pushalerts_Variable_Data(String excel_sheet_name) throws Exception{
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	
	Map<String , String> expected_map_results = new HashMap<String, String>();
	ArrayList<String> expected_Values_List = new ArrayList<String>();
	
	String[][] exceldata = read_excel_data.exceldataread("pushalerts_Variables");
	return expected_map_results;
}

}
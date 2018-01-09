package twc.Automation.HandleWithApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import twc.Automation.Driver.Drivers;
import twc.Automation.General.DeviceStatus;
import twc.Automation.HandleWithAppium.AppiumFunctions;
import twc.Automation.HandleWithCharles.CharlesFunctions;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.ReadDataFromFile.read_xml_data_into_buffer;
import twc.Automation.RetryAnalyzer.Retry;


public class AppFunctions extends Drivers{
	
	
	static int startY;
	static int endY;
	
	
	public static void verify_subModule(String excel_sheet_name, int no) throws Exception{
		MobileElement module2;
		
		List<String> tab = new ArrayList<String>(5);
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
//
//	
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name); 
//		String [] healthModule={"Allergy", "ColdFlu"};
//		String [] healthModule={"ColdFlu"};
//		for(int i = 0; i<healthModule.length; i++){
		
			  if(no==0){
					AppiumFunctions.LaunchAppWithFullReset();
					Thread.sleep(5000);
					AppFunctions.scrollInToView(excel_sheet_name);
			  }
				
				CharlesFunctions.startSessionBrowserData();
				AppFunctions.clickOnModule(excel_sheet_name);
				Thread.sleep(3000);
				
				if(tab.size()>0){
					tab.clear();
				}
				if(excel_sheet_name.equalsIgnoreCase("Cold & Flu"))
				{

					tab.add("Cold");
					tab.add("Flu");
				}
				else if(excel_sheet_name.equalsIgnoreCase("Allergy"))
				{

					tab.add("Breathing");
					tab.add("Pollen");
					tab.add("Mold");
				}
				else if(excel_sheet_name.equalsIgnoreCase("GoRun"))
				{

					tab.add("Today");
					tab.add("Tomorrow");
					tab.add("This Week");
				}
			
			
				for(int i = 0; i<tab.size(); i++){
					
					module2 = (MobileElement)Ad.findElement(By.xpath("//android.widget.TextView[@text='"+tab.get(i)+"']"));	//tab[i]   
					//MobileElement module2 = (MobileElement)Ad.findElement(By.xpath("//*[@class='android.support.v7.app.ActionBar$Tab' and @index='1']"));	
					//MobileElement module2 = (MobileElement)Ad.findElement(By.xpath("//*[@class='android.widget.TextView' and @text='Flu']"));	
		
					System.out.println("is tab selected"+module2.isSelected());
					System.out.println("attribute of text"+module2.getAttribute("text"));
					System.out.println("text"+module2.getText());
					System.out.println("is enabled"+module2.isEnabled());
					
					if(i==0 && module2.getAttribute("text").equalsIgnoreCase(tab.get(0)) && module2.isEnabled()){
						System.out.println("default tab---->"+tab.get(i)+"-------> is selected first");
					}
					else if(!module2.isSelected())
					{
						module2.click();
						Thread.sleep(3000);
					}
					
//					if(module2.isSelected()){
//						
//					}
//					else
//					{
//						
//					}
					
					
					CharlesFunctions.ExportSessions();
					Thread.sleep(3000);
					AppFunctions.verifyAdPresentOnExtendedPage(excel_sheet_name);
				
				
					//String [] callAdsAllergy={"spotLightAd", "details"};
					//for(int k = 0; k<callAdsAllergy.length; k++){
					//AppFunctions.verifyAdCall(healthModule[i], callAdsAllergy[k]);	
					AppFunctions.verifyAdCall(excel_sheet_name, "spotLightAd");	
					
					
					if(!tab.get(i).equalsIgnoreCase("Breathing") && !excel_sheet_name.equalsIgnoreCase("GoRun")){
						CharlesFunctions.startSessionBrowserData();
						AppFunctions.Swipe();
						AppFunctions.Swipe();
						AppFunctions.Swipe();
						Thread.sleep(3000);
						CharlesFunctions.ExportSessions();
						Thread.sleep(5000);
					}
					else
					{
						AppFunctions.Swipe();
						AppFunctions.Swipe();
						AppFunctions.Swipe();
						Thread.sleep(5000);
					}
					
					
					
					
					AppFunctions.verifyAdPresentOnExtendedPage(excel_sheet_name);
					Thread.sleep(3000);
					
					if(excel_sheet_name.equalsIgnoreCase("GoRun")){
						AppFunctions.verifyAdCall(excel_sheet_name, "largeAds");
					}
					else
					{
						AppFunctions.verifyAdCall(excel_sheet_name, "details");
					}
					
					CharlesFunctions.startSessionBrowserData();
					
					
					if((tab.size()-1)==i){
						Ad.findElementByClassName(exceldata[9][Cap]).click();
					}	
					System.out.println("================= "+excel_sheet_name+" Test Case End =========================");
						
		}

}	
	
	
	public static void scrollInToView(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           

		int swipe = Integer.parseInt(exceldata[2][Cap].trim());
		System.out.println("swipe count"+swipe);
		Ad.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		
		
		MobileElement module;
		int MAX_SWIPES = 12;
		//int MAX_SWIPES = swipe;
		for (int i = 0; i < MAX_SWIPES; i++) {

			try {
				module = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				if(module.getText().equalsIgnoreCase(exceldata[1][Cap])){
					System.out.println(exceldata[1][Cap] + " Module Is Present On Page");
					logStep(exceldata[1][Cap] + " Module Is Present On Page");
					//Swipe();
					break;
				}
			} catch (Exception e) {
				System.out.println(exceldata[1][Cap] + " module is not present and so scrolling down");
				Swipe();
				Thread.sleep(2000);
			}
		}
	}
	
	
	
	public static void clickOnModule(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           

		
		try {
			MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
			module2.click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppFunctions.Swipe();
			MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
			module2.click();
		} 
	
	}
	
	
	
	
	
	
	public static void verifyAdPresentOnExtendedPage(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");  
		MobileElement extendModuleValidate = (MobileElement) Ad.findElementById(exceldata[4][Cap]);
		if(extendModuleValidate.isDisplayed() )
		{
			System.out.println("On Extended "+exceldata[1][Cap]+" page");
			WebDriverWait wait1 = new WebDriverWait(Ad, 10);
			wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
			MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
			if (AdEle.isDisplayed()) {
				logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
				System.out.println("Ad is present on Extended page");
				Retry.maxRetryCount=0;
				Thread.sleep(2000);
				
			}
		}
	}
	
	
//	

	
	
// public static void verifyCall(String excel_sheet_name, String skiCallName) throws Exception{
//		 
//		 String skiResortsCallURL = null;
//		 String skiResortsCallParam = null ;
//     	
//     	DeviceStatus device_status = new DeviceStatus();
// 		int Cap = device_status.Device_Status();
// 		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
// 		
// 		if(skiCallName.equalsIgnoreCase("skiContentAd")){
// 			skiResortsCallURL = exceldata[14][Cap];
// 			skiResortsCallParam = exceldata[13][Cap];
// 	     System.out.println("=====Checking for ski Content Ad call====");
// 		
// 		}else if(skiCallName.equalsIgnoreCase("skiSpotlightAd")){
// 			skiResortsCallURL = exceldata[33][Cap];
// 	 	    skiResortsCallParam = exceldata[34][Cap];
// 	 	    System.out.println("====Checking for ski Spotlight Ad call====");
// 		}else if(skiCallName.equalsIgnoreCase("skiLargeAd")){
// 		
// 			skiResortsCallURL = exceldata[35][Cap];
// 	 	    skiResortsCallParam = exceldata[36][Cap];
// 	 	    System.out.println("====Checking for ski LargeAd call====");
// 		}
// 		
// 		
// 		
//			read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
//			String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
//			
//			try {
//				String Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[15][Cap]));
//				String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[16][Cap]));
//				
//				String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[17][Cap])+(exceldata[17][Cap].length()),required_info.indexOf(exceldata[18][Cap]));
//				String callData = expected_data.toString();
//				
//				
//				String regex = null;
//				// [0-9]+
//				//String regex = "\\d+";
//				skiResortsCallURL="gampad/ads?riv="+regex+"&_activity_context=";
//				
//				
//				for(int i = 0; i<10; i++){
//					
//						try {
//								if(callData.contains(skiResortsCallURL)){
//									   
//										if(callData.contains(skiResortsCallParam) ){
//											System.out.println("Call is generated on "+exceldata[1][Cap]+" Page====");
//											logStep("Call is generated on "+exceldata[1][Cap]+" Page");
//											Retry.maxRetryCount=0;
//										}
//										else
//										{
//											System.out.println("Call is not Generated on "+exceldata[1][Cap]);
//											logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
//											Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
//											
//										}
//								}
////								else{
////	
////										System.out.println("Call is Not Generated on "+exceldata[1][Cap]);
////										logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
////										Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
////								}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							//e.printStackTrace();
//						}
//				}
//				
//				
//					
//				
//
//			} catch (Exception e) {
//				System.out.println( "Call is not Generated on "+exceldata[1][Cap]);
//				logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
//				Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
//			}		
//     		
//     }
	
	
	
	
	
	 public static void verifyAdCall(String excel_sheet_name, String callName) throws Exception{
		 int i;
		 MobileElement module = null;
		 boolean breathingTabPresent = false;
		 String Read_API_Call_Data= null;
		 String resortsCallURL = null;
		 String resortsCallParam = null;
		 String resortsCallURL_spotlight=null;
		 String resortsCallParam_spotlight=null; 
		 String resortsCallURL_details=null;
		 String resortsCallParam_details=null;
		 String resortsCallURL_largeAds=null;
		 String resortsCallParam_largeAds=null;
		 
		 
     	DeviceStatus device_status = new DeviceStatus();
 		int Cap = device_status.Device_Status();
 		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
 		
 		if(callName.equalsIgnoreCase("contentAd")){
 			resortsCallURL = exceldata[14][Cap];
 			resortsCallParam = exceldata[13][Cap];
 		
 	     System.out.println("=====Checking for Content Ad call====");
 		
 		}else if(callName.equalsIgnoreCase("spotlightAd")){
 			resortsCallURL = exceldata[33][Cap];
 	 	    resortsCallParam = exceldata[34][Cap];
 	 	    resortsCallURL_spotlight = exceldata[33][Cap];
	 	    resortsCallParam_spotlight = exceldata[34][Cap];
	 	  
 	 	    System.out.println("====Checking for Spotlight Ad call====");
 		}else if(callName.equalsIgnoreCase("largeAds")){
 			resortsCallURL = exceldata[35][Cap];
 	 	    resortsCallParam = exceldata[36][Cap];
 	 	    resortsCallURL_largeAds = exceldata[14][Cap];
 	 	    resortsCallParam_largeAds = exceldata[37][Cap];
	 	   System.out.println("====Checking for largeAds call====");
 		}
	 	   
	 	  else if(callName.equalsIgnoreCase("weatherAd")){
	 			resortsCallURL = exceldata[35][Cap];
	 	 	    resortsCallParam = exceldata[36][Cap];
	 	 	    resortsCallURL_largeAds = exceldata[14][Cap];
	 	 	    resortsCallParam_largeAds = exceldata[37][Cap];
		 	   System.out.println("====Checking for largeAds call====");
	 	  
 		}else if(callName.equalsIgnoreCase("details")){
 			resortsCallURL = exceldata[14][Cap];
 	 	    resortsCallParam = exceldata[37][Cap];
 	 	    resortsCallURL_details = exceldata[14][Cap];
	 	    resortsCallParam_details = exceldata[37][Cap];
 	 	    System.out.println("====Checking for details call====");
 		}
 		
 		
 		
 		    Thread.sleep(3000);
			read_xml_data_into_buffer xml_data_into_buffer = new read_xml_data_into_buffer();
			String sb = xml_data_into_buffer.read_xml_file_into_buffer_string();
			Thread.sleep(7000);
			try {
				System.out.println("in try block");
				if(!callName.equalsIgnoreCase("details") && !callName.equalsIgnoreCase("largeAds")){
					System.out.println("first index");
					Read_API_Call_Data = sb.toString().substring(sb.toString().indexOf(exceldata[15][Cap]));
				}
				else
				{	
					System.out.println("getting lastindex of [CDATA[GET /gampad");
					Read_API_Call_Data = sb.toString().substring(sb.toString().lastIndexOf(exceldata[15][Cap]));
					
				}
				String required_info = Read_API_Call_Data.toString().substring(Read_API_Call_Data.toString().indexOf(exceldata[16][Cap]));
				System.out.println("capturing the expected call data");
				String expected_data = required_info.toString().substring(required_info.indexOf(exceldata[17][Cap])+(exceldata[17][Cap].length()),required_info.indexOf(exceldata[18][Cap]));
				String callData = expected_data.toString();
				System.out.println("expected call data is captured");
				
				String regex = null;
				
				
				int regex1 = 1;
				String resortsCallURL1 = "gampad/ads?riv="+regex1+"&_activity_context=";
				
				int regex2 = 2;
				String resortsCallURL2 = "gampad/ads?riv="+regex2+"&_activity_context=";
				
				int regex3 = 5;
				String resortsCallURL3 = "gampad/ads?riv="+regex3+"&_activity_context=";
				
				int regex4 = 6;
				String resortsCallURL4 = "gampad/ads?riv="+regex4+"&_activity_context=";
				
				int regex5 = 15;
				String resortsCallURL5 = "gampad/ads?riv="+regex5+"&_activity_context=";
				
				if(excel_sheet_name.equalsIgnoreCase("allergy")){
					System.out.println("Allergy module------>finding Breathing element");
					module = (MobileElement)Ad.findElement(By.xpath("//android.widget.TextView[@text='Breathing']"));
				}
				
				
				
				//to handle the Breathing issue
				if(excel_sheet_name.equalsIgnoreCase("allergy")&& module.isSelected()||excel_sheet_name.equalsIgnoreCase("goRun")){
					System.out.println("Allergy module------>Breathing is selected");
					if(callData.contains(resortsCallURL1)||callData.contains(resortsCallURL2)||callData.contains(resortsCallURL3)||callData.contains(resortsCallURL4)){
						
						if(callData.contains(exceldata[39][Cap])){
							System.out.println("Allergy module------>Breathing call contains ui");	
										if(callData.contains(resortsCallParam) ){
											System.out.println("Cust Param is found for Breathing");
										}else {
										
												
												if(callName.equalsIgnoreCase("spotlightAd")){
														
														if(!excel_sheet_name.equalsIgnoreCase("GoRun")){
															System.out.println("=====Checking for Content Ad call====");
															resortsCallParam_details = exceldata[37][Cap];
												 			if(callData.contains(resortsCallParam_details) ){
																System.out.println("Cust Param is found for Breathing");
												 			}
												 		}else {
												 			System.out.println("=====Checking for largeAdscall====");
												 			resortsCallParam_largeAds = exceldata[37][Cap];
												 			if(callData.contains(resortsCallParam_largeAds) ){		
																System.out.println("Cust Param is found for Breathing");
												 			}
												 		}
												}else if(callName.equalsIgnoreCase("details")||callName.equalsIgnoreCase("largeAds")){
												 	    System.out.println("=====Checking for spotlightAd call====");
												 	    resortsCallParam_spotlight = exceldata[34][Cap];
												 	    if(callData.contains(resortsCallParam_spotlight) ){
												 	    	System.out.println("Cust Param is found");
												 	    }
												}
			 
							           }
					}
				}
					
				
				}else{
				
				
					if(callData.contains(resortsCallURL1)||callData.contains(resortsCallURL2)||callData.contains(resortsCallURL3)||callData.contains(resortsCallURL4)||callData.contains(resortsCallURL5)){
						    
							if(callData.contains(exceldata[39][Cap])){
									if(callData.contains(resortsCallParam)){
										System.out.println("Cust Param is found for the call on  "+exceldata[1][Cap]+" Page====");
										logStep("ust Param is found for the call on "+exceldata[1][Cap]+" Page");
										Retry.maxRetryCount=0;
									}
									else
									{
										System.out.println("Cust Param is not found for the call on "+exceldata[1][Cap]);
										logStep("Cust Param is not found for the call on "+exceldata[1][Cap]+" Page");
										Assert.fail("Cust Param is not found for the call on "+exceldata[1][Cap]);
										
									}
							}
					}
					else
					{
				
							System.out.println("Call is Not Generated on "+exceldata[1][Cap]);
							logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
							Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
					}
				}
				
				
			} catch (Exception e) {
				System.out.println( "Calllllll is not Generated on "+exceldata[1][Cap]);
				logStep("Call is not generated on "+exceldata[1][Cap]+" Page");
				Assert.fail("Call is not Generated on "+exceldata[1][Cap]);
			}		
     		
     }



	public static void  clickOnBackArrow(String excel_sheet_name) throws Exception{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  
		Ad.findElementByClassName(exceldata[10][Cap]).click();//click on back arrow
		
	}
	
	
	public static void Swipe_Up(){
		Dimension dimensions = Ad.manage().window().getSize();
		Double startY1 = dimensions.getHeight() * 0.7;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/6);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		System.out.println("endY  - "+endY);
		System.out.println("startY  - "+startY);
		Ad.swipe(0, endY, 0, startY,2000);
		//Ad.swipe(50, 10, 50, 98,2000);
	}
	
	
	
	//SwipeUp based on counter  //by naresh
	public static void SwipeUp_Conter(int Counter) throws Exception{

		int swipeup = Counter;

		for(int i=1;i<=swipeup ;i++){
			//Thread.sleep(2000);
			//Swipe();
			try{
			if(Ad.findElementById("com.weather.Weather:id/temperature").isDisplayed()){
				//System.out.println("Watson ad presented");
				Swipe_Up();
				Swipe_Up();
				break;
			}
			}catch (Exception e){
				Swipe_Up();
				Swipe_Up();
				Swipe_Up();
				Swipe_Up();
				Swipe_Up();
				Swipe_Up();
				Swipe_Up();
			}
			
			
			//Thread.sleep(2000);
		}
	}
	
	public static void SwipeUp_Counter(int Counter) throws Exception{

		int swipeup = Counter;

		for(int i=1;i<=swipeup ;i++){
			
			Swipe();
			Thread.sleep(5000);
			//Swipe();
		}
		}
		
	public static void SwipeUp_Counter_hourly_submodules() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=7 ;i++){
			
			Swipe();
			
			
			Boolean b=verifyElement(By.id("com.weather.Weather:id/hourly_more"));
			if(b==true)
			{
				Ad.findElementById("com.weather.Weather:id/hourly_more").click();
				AppiumFunctions.Check_submodules_Hourly_ad();
				Ad.findElementByClassName("android.widget.ImageButton").click();
				Thread.sleep(5000);
			
				
				break;
			}
			else
			{
				System.out.println("Module is not present scroll down");
			}

				
			
		}
		}
	public static void SwipeUp_Counter_Daily_submodule() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=7 ;i++){
			
			Swipe();
			
			Boolean b=verifyElement(By.id("com.weather.Weather:id/daily_more"));
			if(b==true)
			{
				Ad.findElementById("com.weather.Weather:id/daily_more").click();
				AppiumFunctions.Check_submodules_Daily_ad();
				Ad.findElementByClassName("android.widget.ImageButton").click();
				Thread.sleep(5000);				
				break;
			}
			else
			{
				System.out.println("Module is not present scroll down");
			}
			
			
		}
		}
	
	public static void SwipeUp_Counter_Maps_submodule() throws Exception{

		//int swipeup = Counter;

		for(int i=1;i<=7 ;i++){
			
			Swipe();
			
			Boolean b=verifyElement(By.id("com.weather.Weather:id/map_module_title"));
	
			if(b==true)
			{
				try
				{
				Ad.findElementById("com.weather.Weather:id/map_module_thumbnail").click();
				}
				catch(Exception e)
				{
					Ad.findElementById("com.weather.Weather:id/map_module_more").click();
				}
				AppiumFunctions.Check_submodules_Maps_ad();
				//Ad.findElementByClassName("android.widget.ImageButton").click();
				Thread.sleep(5000);				
				break;
			}
			else
			{
				System.out.println("Module is not present scroll down");
			}

				
			
		}
		}


	
	//Swipe based on counter  //by naresh
	public static void Swipe_Conter(int Counter) throws Exception{

		int swipe = Counter;

		for(int i=1;i<=swipe ;i++){
			//Thread.sleep(2000);
			//Swipe();
			System.out.println("swiping for watson ad");
			try{
				if(Ad.findElementByName("Name any course, dish, or ingredient").isDisplayed()){
					System.out.println("Watson ad presented");
					break;
					//if(driver.findElement(By.xpath(""))!= null){
				}else if(driver.findElements(By.xpath("//android.view.View[@content-desc='Try again']")).size() != 0){
					System.out.println("clicking on try again");
					driver.findElement(By.xpath("//android.view.View[@content-desc='Try again']")).click();
				}
				
			}catch (Exception e){
				Swipe();
				Swipe();
				System.out.println("watson ad not present");
			}
			
			
			//Thread.sleep(2000);
		}
	}
	

	//Search with Watson
		public static void search_with_watson_ad(String SeachText) throws Exception{
			
				
			
			Ad.findElementByName("Name any course, dish, or ingredient").click();
			//Ad.findElementByName("Name any course, dish, or ingredient").clear();
			
			Thread.sleep(2000);
			Ad.findElementByName("Name any course, dish, or ingredient").sendKeys(SeachText);
			Thread.sleep(2000);
			Ad.navigate().back();
			Thread.sleep(3000);//changed from 1 to 3 by ravi
			Ad.findElementByAccessibilityId("Enter").click();
			Thread.sleep(15000);
			String SearchedText = Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]").getAttribute("name");
														 //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]
														 //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[9]
//			System.out.println("SS Text is : "+Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]").getAttribute("name"));
//			System.out.println("Searched Text is :"+SearchedText );
				if(Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[8]").getAttribute("name").contains(SeachText.toString())){
					System.out.println("Seached text is matched");
				}else
				{
					Assert.fail("Searched Text : "+ SeachText +" not matched");
				}
			
			AppFunctions.Swipe_Conter(3);
			
			
		}
	public static void Pull_To_Refresh(String excel_sheet_name) throws Exception{
		logStep("On CC Screen Do A Pull To Refresh");
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		System.out.println("Pull the screen to REFRESH is Start");
		
		WebDriverWait wait = new WebDriverWait(Ad, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id(exceldata[1][Cap])));

		//Temperature element
		MobileElement temp = (MobileElement) Ad.findElementById(exceldata[1][Cap]);
		System.out.println("Temp : "+temp.getText());

		//HILO element
		MobileElement hilo = (MobileElement) Ad.findElementById(exceldata[18][Cap]);
		System.out.println("hilo : "+hilo.getText());
		TouchAction action = new TouchAction(Ad);
		action.longPress(temp).moveTo(hilo).release().perform();
		Thread.sleep(3000);
		System.out.println("Pull the screen to REFRESH is done");
	}
	
	public static void Kill_Launch_App() throws Exception{

		Ad.closeApp();
		Ad.launchApp();
		try{
			if((Ad.findElementByName("Allow")).isDisplayed()){
				Ad.findElementByName("Allow").click();
			}
		}catch(Exception e){
			System.out.println("Location already set");
		}
		
		
		//######
		try{
				if((Ad.findElementByName("OK")).isDisplayed()){
					Ad.findElementByName("OK").click();
				}else if((Ad.findElementByName("Continue")).isDisplayed()){
					Ad.findElementByName("Continue").click();
				}else if((Ad.findElementByName("Allow")).isDisplayed()){
					Ad.findElementByName("Allow").click();
				}
			}catch(Exception e){

			}
		Thread.sleep(9000);

	}
	
	public static void Swipe(){
		Dimension dimensions = Ad.manage().window().getSize();//throwing exception
		Double startY1 = dimensions.getHeight() * 0.8;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/40);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		Ad.swipe(0, startY, 0, endY,2000);
		
	}
	public static void Swipe_Breakignews(){
		Dimension dimensions = Ad.manage().window().getSize();//throwing exception
		Double startY1 = dimensions.getHeight() * 0.4;  
		startY = startY1.intValue();
		Double endY1 = (double) (dimensions.getHeight()/40);  //  dimensions.getHeight()  0.2;  == 512.0
		endY = endY1.intValue();
		Ad.swipe(0, startY, 0, endY,2000);
		
	}
	
	
	
//	public static void verify_adpresent_onextended_page(String excel_sheet_name) throws Exception{
//		
//		DeviceStatus device_status = new DeviceStatus();
//		int Cap = device_status.Device_Status();
//		
//		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
//		
//		System.out.println("Searching for "+exceldata[1][Cap]+" module");
//		
//		logStep("Searching For "+exceldata[1][Cap]+" Module");
//
//		int swipe = Integer.parseInt(exceldata[2][Cap]);
//		
//		for(int i=1;i<=swipe ;i++){
//			Thread.sleep(2000);
//			Swipe();
//			Thread.sleep(2000);
//		}
//		
//		int MAX_SWIPES = 5;
//		
//		for (int i = 0; i < MAX_SWIPES; i++) {
//
//			MobileElement module = null;
//
//			try {
//				
//				WebDriverWait wait0 = new WebDriverWait(Ad, 10);
//				wait0.until(ExpectedConditions.visibilityOf(Ad.findElementById(exceldata[5][Cap])));
//				module = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
//				
//				
//			} catch (Exception e) {
//				// System.out.println(e);
//			}
//		
//			if (module != null && module.isDisplayed()) {
//				
//				
//				
//				if(excel_sheet_name.equals("Map")){
//					
//					System.out.println( exceldata[1][Cap] + " module is present on page");
//					logStep(exceldata[1][Cap] + " Module Is Present On Page");
//					Ad.findElementById(exceldata[5][Cap]).click();
//					
//					MobileElement extendModule = (MobileElement) Ad.findElementByClassName(exceldata[6][Cap]);
//					if(extendModule.isDisplayed())
//					{
//						System.out.println("On Extended "+exceldata[1][Cap]+" page");
//					}
//				}
//				else if(excel_sheet_name.equals("News")){
//					
//					Ad.findElementById(exceldata[5][Cap]).click();
//					Thread.sleep(1000);
//					String extendModuleText = Ad.findElementById(exceldata[6][Cap]).getText();
//					
//					if(!extendModuleText.equals("News") && excel_sheet_name.equals("News")){
//						Thread.sleep(2000);
//						Ad.findElementByClassName(exceldata[10][Cap]).click();
//						Thread.sleep(2000);
//						Swipe();
//						Thread.sleep(2000);
//						
//						System.out.println( exceldata[1][Cap] + " module is present on page");
//						logStep(exceldata[1][Cap] + " Module Is Present On Page");
//						
//						Ad.findElementById(exceldata[5][Cap]).click();
//						String extendNewModuleText = Ad.findElementById(exceldata[6][Cap]).getText();
//						
//						System.out.println("Text : "+extendNewModuleText);
//						
//						if(extendNewModuleText.contains(exceldata[1][Cap]))
//						{
//							System.out.println("On Extended "+exceldata[1][Cap]+" page");
//							logStep("On Extended "+exceldata[1][Cap]+" Page");
//						}
//					}
//					else{
//						
//						System.out.println( exceldata[1][Cap] + " module is present on page");
//						logStep(exceldata[1][Cap] + " Module Is Present On Page");
//						
//						System.out.println("Text : "+extendModuleText);
//						
//						if(extendModuleText.contains(exceldata[1][Cap]))
//						{
//							System.out.println("On Extended "+exceldata[1][Cap]+" page");
//							logStep("On Extended "+exceldata[1][Cap]+" Page");
//						}
//					}
//				}
//				else{
//					
//					System.out.println( exceldata[1][Cap] + " module is present on page");
//					logStep(exceldata[1][Cap] + " Module Is Present On Page");
//					Ad.findElementById(exceldata[5][Cap]).click();
//					
//					String extendModuleText = Ad.findElementById(exceldata[6][Cap]).getText();
//					System.out.println("Text : "+extendModuleText);
//					
//					if(extendModuleText.contains(exceldata[1][Cap]))
//					{
//						System.out.println("On Extended "+exceldata[1][Cap]+" page");
//						logStep("On Extended "+exceldata[1][Cap]+" Page");
//					}
//				}
//				
//
//				MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]);
//				
//				WebDriverWait wait1 = new WebDriverWait(Ad, 10);
//				wait1.until(ExpectedConditions.visibilityOf(AdEle));
//
//				if (AdEle.isDisplayed()) {
//					logStep("Ad Presented In Extended "+exceldata[1][Cap]+" Page");
//					System.out.println("Ad is present on Extended page");
//					Thread.sleep(2000);
//					Ad.findElementByClassName(exceldata[10][Cap]).click();
//					break;
//				}
//			} else {
//				System.out.println(exceldata[1][Cap] + " module is not present and scrolling down");
//				Swipe();
//			}
//		}
//	}
	
	
	//module2 = (MobileElement)Ad.findElement(By.xpath("//android.widget.FrameLayout[@resource-id='com.weather.Weather:id/video_player_thumbnail_extra' and @NAF='true']"));	
	
	
	public static void verify_adpresent_onextended_page(String excel_sheet_name) throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//hourly/daily/map/news
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);  


		System.out.println("Searching for "+exceldata[1][Cap]+" module"); 
		logStep("Searching For "+exceldata[1][Cap]+" Module");           

		int swipe = Integer.parseInt(exceldata[2][Cap]);
		Ad.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);//causing exception
		MobileElement module;
		int MAX_SWIPES = 12;
		 for (int i = 0; i < MAX_SWIPES; i++) {


		try{
				module = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				System.out.println("text"+" "+module.getText());
	

				if(module.getText().equalsIgnoreCase(exceldata[1][Cap])){
					logStep(exceldata[1][Cap] + " Module Is Present On Page"); 
					try {
						MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
						
						module2.click();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						Swipe();
						MobileElement module2 = (MobileElement)Ad.findElement(By.id(exceldata[11][Cap]));	
						module2.click();
						Thread.sleep(6000);
					} 
						
							MobileElement extendModule = (MobileElement) Ad.findElementById(exceldata[6][Cap]);
							System.out.println(extendModule.getText());
							if(exceldata[1][Cap].equalsIgnoreCase("Radar & Mapsffasds")){
									if(extendModule.isDisplayed() )
									{
										MobileElement extendModuleValidate = (MobileElement) Ad.findElementById(exceldata[4][Cap]);
										if(extendModuleValidate.isDisplayed() )
										{
											System.out.println("On Extended "+exceldata[1][Cap]+" page");
											WebDriverWait wait1 = new WebDriverWait(Ad, 10);
											wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
											MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
											if (AdEle.isDisplayed()) {
												logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
												System.out.println("Ad is present on Extended page");
												Retry.maxRetryCount=0;
												Thread.sleep(2000);
												Ad.findElementByClassName(exceldata[10][Cap]).click();//click on back arrow
												break;
											}
										}
									}	
							}	
							
							
							
							else {
								if(extendModule.isDisplayed()  )
								{
									System.out.println("On Extended "+exceldata[1][Cap]+" page");
									WebDriverWait wait1 = new WebDriverWait(Ad, 60);
									//Comment by aswin fro taking latest ad element
									//wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[7][Cap])));
									//MobileElement AdEle =(MobileElement) Ad.findElementById(exceldata[7][Cap]); 
									try{
									wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(exceldata[7][Cap])));
										//wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[13][Cap])));
									
									}
									catch(Exception ae)
									{
										wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(exceldata[8][Cap])));
										//wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id(exceldata[13][Cap])));
									}
									MobileElement AdEle =(MobileElement) Ad.findElement(By.xpath(exceldata[7][Cap]));
									if (AdEle.isDisplayed()) {
										logStep("Ad is Present on Extended "+exceldata[1][Cap]+" Page");
										System.out.println("Ad is present on Extended page");
										Retry.maxRetryCount=0;
										Thread.sleep(2000);
										Ad.findElementByClassName(exceldata[10][Cap]).click();//click on back arrow
										break;
									}
								}
							}
						
					
				}	
		} catch (Exception e) {
			//System.out.println(e);
			System.out.println(exceldata[1][Cap] + " module is not present and so scrolling down");
			 Swipe();	
		}
	}//for loop end

}
	
	
	
	
	public static void CleanLaunch_launch(String excel_sheet_name) throws Exception
	{
		logStep("Verify Ad Calls On App Launch");
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		for(int i=1;i<=13 ;i++){
			Thread.sleep(2000);
			Swipe();
			Thread.sleep(2000);
		}
		
		int MAX_SWIPES = 5;
		
		for (int j = 0; j < MAX_SWIPES; j++) {

			MobileElement module = null;

			try {
				
				WebDriverWait wait0 = new WebDriverWait(Ad, 10);
				wait0.until(ExpectedConditions.visibilityOf(Ad.findElementByXPath(exceldata[1][Cap])));
				module = (MobileElement) Ad.findElementByXPath(exceldata[1][Cap]);
			} catch (Exception e) {
				// System.out.println(e);
			}
		

			if (module!=null && module.isDisplayed()) {
				 System.out.println("Last module is present");
				 Swipe();
				 break;
			} 
			else {
				logStep("Ad Calls Not Presented On App Launch");
				System.out.println("Last module is NOT present,scrolling down");
				Swipe();
			}
		}
	}
	
public static void Change_to_Test_Mode(String excel_sheet_name) throws Exception{
		
		logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
		logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");
		

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		WebDriverWait wait = new WebDriverWait(Ad, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
		
		MobileElement menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
		menu.click();
		System.out.println("clicking on Menu option");
		Ad.findElementByName(exceldata[5][Cap]).click(); 
		System.out.println("clicking on settings option");
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);//about this app
		aboutThisAPP.click();
		System.out.println("clicking on about this app option");
		System.out.println("tapping continously to get test mode option");	
			for (int i=1; i<=8; i++){
				Ad.findElementById(exceldata[18][Cap]).click();
			}
				Ad.findElementByName(exceldata[19][Cap]).click();
				System.out.println("clicking on test mode settings");	
				Ad.findElementByName(exceldata[20][Cap]).click();
				System.out.println("clicking on ads option");	
				Ad.findElementByName(exceldata[16][Cap]).click();
				System.out.println("clicking on test option");	
				logStep("changed Ads As Production To Test");
				System.out.println("changed to Test Mode");
				Thread.sleep(1000);
				logStep("Kill And Relaunch The App");
				//				Ad.closeApp();
				//				System.out.println("Closed the app");
								//Ad.launchApp();
				//AppiumFunctions.Kill_launch();
//				logStep("Kill And Relaunch The App");
//				Ad.closeApp();
////				System.out.println("Closed the app");
//				Ad.launchApp();
				//System.out.println("launching the app");
				//AppiumFunctions.LaunchApp();
				
				try{
					if((Ad.findElementByName("Allow")).isDisplayed()){
						Ad.findElementByName("Allow").click();
					}
				}catch(Exception e){
					System.out.println("Location already set");
				}
				
				//System.out.println("launching the app");
	}
	
	
public static void Change_to_Test_Mode2(String excel_sheet_name) throws Exception{
		
		logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
		logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");
		

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
		
		WebDriverWait wait = new WebDriverWait(Ad, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
		
		MobileElement menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
		menu.click();
		System.out.println("clicking on Menu option");
		Ad.findElementByName(exceldata[5][Cap]).click(); 
		System.out.println("clicking on settings option");
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);//about this app
		aboutThisAPP.click();
		System.out.println("clicking on about this app option");
		System.out.println("tapping continously to get test mode option");	
			for (int i=1; i<=8; i++){
				Ad.findElementById(exceldata[18][Cap]).click();
			}
				Ad.findElementByName(exceldata[19][Cap]).click();
				System.out.println("clicking on test mode settings");	
				Ad.findElementByName(exceldata[20][Cap]).click();
				System.out.println("clicking on ads option");	
				Ad.findElementByName(exceldata[16][Cap]).click();
				System.out.println("clicking on test option");	
				logStep("changed Ads As Production To Test");
				System.out.println("changed to Test Mode");
				Thread.sleep(1000);
				//logStep("Kill And Relaunch The App");
//				Ad.closeApp();
//				System.out.println("Closed the app");
//				Ad.launchApp();
				//System.out.println("launching the app");
				//AppiumFunctions.LaunchApp();
				
//				try{
//					if((Ad.findElementByName("Allow")).isDisplayed()){
//						Ad.findElementByName("Allow").click();
//					}
//				}catch(Exception e){
//					System.out.println("Location already set");
//				}
//				
//				System.out.println("launching the app");
	}


public static void goToTestModeSettings(String excel_sheet_name) throws Exception{
	
	logStep("Make Ads As Test From Test Mode Settings In Order To Get BB Ad Call");
	logStep("TestMode Settings: 1) Click On Menu Button 2) Click On Settings 3) Click On About This App 4) Click 10 Times On App Version 5) TestMode Setting Enabled 6) Click On TestMode Settings 7) Click On Ads");
	

	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	
	WebDriverWait wait = new WebDriverWait(Ad, 60);
	wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
	
	MobileElement menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
	menu.click();
	System.out.println("clicking on Menu option");
	Ad.findElementByName(exceldata[5][Cap]).click(); 
	System.out.println("clicking on settings option");
	MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName(exceldata[6][Cap]);//about this app
	aboutThisAPP.click();
	System.out.println("clicking on about this app option");
	System.out.println("tapping continously to get test mode option");	
//		for (int i=1; i<=8; i++){
//			Ad.findElementById(exceldata[18][Cap]).click();
//		}
			Ad.findElementByName(exceldata[19][Cap]).click();
			System.out.println("clicking on test mode settings");	
			
}


	public static void verifyBBCallLocationFromListInTestMode(String excel_sheet_name) throws Exception{
		
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		
		String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
			/* --- Start For Android Device --- */
			if(Cap == 2){
				String[][] addressdata = read_excel_data.exceldataread("AddressPage");
				Thread.sleep(20000);

				WebDriverWait wait4 = new WebDriverWait(Ad, 40);
				wait4.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
				
				//Root Location Element
				Ad.findElementById(addressdata[4][Cap]).click();
				
				Thread.sleep(2000);

				WebDriverWait wait5 = new WebDriverWait(Ad, 40);
				wait5.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[6][Cap])));
				
				//List Location Element
				@SuppressWarnings("unchecked")
				List<MobileElement> loclist = Ad.findElements(By.id(addressdata[6][Cap]));
				
				String firsteleXpath = addressdata[5][Cap];
				String[] parts = firsteleXpath.split("Count");
				
				String expectedLocation = null;
				/* --- Start For Loop For Location Click --- */
				for(int i=2;i<= loclist.size();i++){
					
					String element = null;
					
					try {
						
						element = parts[0]+i+parts[1];
								   
						MobileElement ele = (MobileElement) Ad.findElementByXPath(element);
						System.out.println("For This Location ====>"+ele.getText());
						String location = ele.getText();
						
						
						if(excel_sheet_name.equals("TestMode")){
							expectedLocation = exceldata[21][Cap];
						}
						else{
							expectedLocation = exceldata[9][Cap];
						}
						
						if(location.contains(expectedLocation)){
							logStep("TestLocation "+expectedLocation+" Is Presented");
							logStep("Successfully Added The Test Location "+expectedLocation+" In Order To Get The BB Ad Call");

							System.out.println("Location "+location);
							
							WebDriverWait wait12 = new WebDriverWait(Ad, 40);
							wait12.until(ExpectedConditions.presenceOfElementLocated(By.xpath(parts[0]+1+parts[1])));
							Ad.findElementByXPath(element).click();
							Thread.sleep(2000);
							break;
						}
						else
						{
							
						WebDriverWait wait9 = new WebDriverWait(Ad, 40);
						wait9.until(ExpectedConditions.presenceOfElementLocated(By.xpath(element)));
						
						Ad.findElementByXPath(element).click();
						Thread.sleep(2000);
						
						WebDriverWait wait10 = new WebDriverWait(Ad, 40);
						wait10.until(ExpectedConditions.presenceOfElementLocated(By.id(addressdata[4][Cap])));
						
						Ad.findElementById(addressdata[4][Cap]).click();
						Thread.sleep(5000);
						}
					} catch (Exception e) {
						logStep(expectedLocation+" Is Not Found From Location List. So Need To Set The Location For Test Mode");
						System.out.println(expectedLocation+" is not found in the location list. So need to set the Location for Test Mode");
					}
				}
		}/* --- End For Android Device --- */
	}
	
	public static void verify_Vedio_Module_Click_On_Forecast_Video(String excel_sheet_name) throws Exception{
		
	System.out.println("Searching for Video module");
	logStep("Navigating For Video Module");

	Thread.sleep(5000);
	DeviceStatus device_status = new DeviceStatus();
	int Cap = device_status.Device_Status();
	
	String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
	int swipe = Integer.parseInt(exceldata[2][Cap]);
	
	for(int i=1;i<=swipe ;i++){
		Swipe();
		Thread.sleep(1000);
	}
	
	int MAX_SWIPES = 5;
	
		for (int i = 0; i<MAX_SWIPES; i++) {
	
			MobileElement video = null;
	
			try {
				WebDriverWait wait0 = new WebDriverWait(Ad, 10);
				wait0.until(ExpectedConditions.visibilityOf(Ad.findElementById(exceldata[5][Cap])));
				video = (MobileElement) Ad.findElementById(exceldata[5][Cap]);
				logStep("ForeCast Video Module Is Presented");

			} catch (Exception e) {
				// System.out.println("Exception message :: "+e);	
			}
	
			if(video!=null && video.isDisplayed())
			{  
				System.out.println("Video module is present ");
				try{
				Ad.findElementById(exceldata[5][Cap]).click();
				}
				catch(Exception e)
				{
					Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]").click();
				}
				logStep("Tap On ForeCast Video Module ");
				logStep("Pre-Roll Video Will play");
				Thread.sleep(2000);
				Ad.findElementByClassName(exceldata[6][Cap]).click();
				Thread.sleep(2000);
				break;
			}else
			{
	           System.out.println("Video module is NOT present and scrolling down");
			   Swipe();
			}
		}
	}

	public static void compareBuildVersion() throws Exception{
		
		String build_ver = null;
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(Ad, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.ImageButton")));
		
		MobileElement menu = (MobileElement) Ad.findElement(By.className("android.widget.ImageButton"));
		menu.click();
		
		Ad.findElementByName("Settings").click();
		
		MobileElement aboutThisAPP = (MobileElement) Ad.findElementByName("About this app");
		aboutThisAPP.click();
		
		String BuildVersion = Ad.findElementById("com.weather.Weather:id/about_version").getText();
		System.out.println("Build Version is : " + BuildVersion);
		//String BuildVersion="6.11.0 690110560 (8a0deba release)";
		BuildVersion = BuildVersion.trim();
		String[] ver = BuildVersion.split(" ");
		
		System.out.println("Present Build version : "+ver[3]);
		
		String Build = properties.getProperty("BuildToDownload");
		
		if(Build.contains("Beta")){
			build_ver = properties.getProperty("AndroidFlagship_BetaBuild");
			
		}
		else{
			build_ver = properties.getProperty("AndroidFlagship_AlphaBuild");
		}
		
		
		if(ver[3].equals(build_ver)){
				System.out.println("New Build Installed");
		}
		else{
			System.out.println("New Build Not Yet Installed");
			System.exit(1);
		}
		
	}
	public static void resetApp(){
		Ad.resetApp();
	}
	 public static void goToWelcome(String excel_sheet_name) throws Exception{
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();
			
			String[][] exceldata = read_excel_data.exceldataread(excel_sheet_name);
			
			WebDriverWait wait = new WebDriverWait(Ad, 60);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className(exceldata[2][Cap])));//settings button
			System.out.println("clicking on Menu option");
			MobileElement menu = (MobileElement) Ad.findElement(By.className(exceldata[2][Cap]));
			Thread.sleep(3000);
			menu.click();
	}
	 public static void LaunchApp_for_feedcalls() throws Exception
		{
			logStep("Verify Ad Calls On App Launch");
			DeviceStatus device_status = new DeviceStatus();
			int Cap = device_status.Device_Status();
			
			
			
			for(int i=1;i<=10 ;i++){
				Thread.sleep(2000);
				Swipe();
				Thread.sleep(2000);
			}
		}
	 public static void Clickon_Real_Timerainalert_Notifiaction() throws Exception 
	 {
		 List<WebElement> Notificationslist = Ad.findElementsById("android:id/title");

			for(int i=0;i<=Notificationslist.size();i++){
				System.out.println("Notification : "+Notificationslist.get(i).getText());
				if(Notificationslist.get(i).getText().contains("Real-Time Rain")){
					
					Notificationslist.get(i).click();
				
					Thread.sleep(6000);
					break;
					
				}
				 
	 }
	 
}
	 
	 
	 
	 public static void Clickon_Lightning_Notifiaction() throws Exception
	 {
		 List<WebElement> Notificationslist = Ad.findElementsById("android:id/title");

			for(int i=0;i<=Notificationslist.size();i++){
				System.out.println("Notification : "+Notificationslist.get(i).getText());
				if(Notificationslist.get(i).getText().contains("Lightning")){				
					Notificationslist.get(i).click();
					Thread.sleep(6000);
					break;
					
				}
				 
	 }
	 
}
	 
	 
	 public static void Clickon_Breakingnews_Notifiaction() throws Exception
	 {
		 List<WebElement> Notificationslist = Ad.findElementsById("android:id/title");

			for(int i=0;i<=Notificationslist.size();i++){
				System.out.println("Notification : "+Notificationslist.get(i).getText());
				if(Notificationslist.get(i).getText().contains("Breaking")){				
					Notificationslist.get(i).click();			
					Thread.sleep(6000);
					break;
					
				}
				
				 
	 }
			
	 }			
			public static void Clickon_Severe_Notifiaction() throws Exception
			 {
				 List<WebElement> Notificationslist = Ad.findElementsById("android:id/title");

					for(int i=0;i<=Notificationslist.size();i++){
						System.out.println("Notification : "+Notificationslist.get(i).getText());
						if(Notificationslist.get(i).getText().contains("Severe")){							
							Notificationslist.get(i).click();			
							Thread.sleep(6000);
							break;
							
						}
						
						 
			 }
					
					
	 
}
			
			
			
			
			public static void SwipeUp_Counter_lifestyle_submodule() throws Exception{

				//int swipeup = Counter;

				for(int i=1;i<=7 ;i++){
					
					Swipe();
					
					Boolean b=verifyElement(By.name("Health & Activities"));
					if(b==true)
					{
						Ad.findElementById("com.weather.Weather:id/combo_item_name").click();
						//AppiumFunctions.Check_Lifestyle_Module_ad();
						//Ad.findElementByClassName("android.widget.ImageButton").click();
						Thread.sleep(5000);				
						break;
					}
					else
					{
						System.out.println("Module is not present scroll down");
					}

						
					
				}
				}
		
			public static Boolean verifyElement(By by) {
				try {
					// Get the element using the Unique identifier of the element
					Ad.findElement(by);
				} catch (NoSuchElementException e) {
					// Return false if element is not found
					return false;
				} catch (Exception e) {
					return false;
				}
				// Return true if element is found
				return true;
			}
			public static void clickGoRun() throws Exception
			{
				try{
					Ad.findElementByName("Run Times").click();
					Thread.sleep(3000);
				}
				catch(Exception e)
				{
					Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[3]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]").click();
					Thread.sleep(3000);
				}

			}


}
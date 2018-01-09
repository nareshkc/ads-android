package twc.Automation.HandleWithAppium;

import io.appium.java_client.android.AndroidDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import ANDROID_ARMS.ANDROID_ARMS.FeedAdVAlidationonUI;
import twc.Automation.Driver.Drivers;
import twc.Automation.ReadDataFromFile.read_excel_data;
import twc.Automation.General.DeviceStatus;
import twc.Automation.HandleWithCharles.CharlesFunctions;

public class AppiumFunctions extends Drivers{
	public static String homelocation;
	public static SoftAssert softAssert = new SoftAssert();
	public static File folder=null;
	public static String ScreenShot=System.getProperty("user.dir")+"/Screenshots";
	public static void killADB() throws IOException{
		String[] command ={"/usr/bin/killall","-KILL","adb"};
		Runtime.getRuntime().exec(command); 

		String[] command1 ={"/usr/bin/killall","-KILL","-9 adb"}; 
		Runtime.getRuntime().exec(command1);
	}

	public static void UnInstallApp() throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] paths = read_excel_data.exceldataread("Paths");
		String adbPath = paths[15][Cap];

		//System.out.println("adbPath "+ adbPath);

		System.out.println("Uninstall the APP and Installing");

		String[] uninstall ={"/bin/bash", "-c",  adbPath+" shell pm uninstall com.weather.Weather"};
		Runtime.getRuntime().exec(uninstall);
		System.out.println("Uninstall completed");
		Thread.sleep(5000);
	}

	public static void AppiumServerStop() throws InterruptedException{

		String[] command ={"/usr/bin/killall","-KILL","node"};  

		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			System.out.println("Appium Server Not Yet Killed At This Time");
		}  
		Thread.sleep(5000);
	}

	public static void clearTWCLogs() throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] paths = read_excel_data.exceldataread("Paths");
		System.out.println("Clear Logcat Logs for TWC App");	
		String[] clearLogcatdata ={"/bin/bash", "-c",  paths[15][Cap]+" logcat -c"};
		Runtime.getRuntime().exec(clearLogcatdata);	
		Thread.sleep(4000);
	}

	public static void AppiumServerStart() throws InterruptedException{

		CommandLine command = new CommandLine("/usr/local/bin/node");
		command.addArgument("/Applications/Appium.app/Contents/Resources/app/node_modules/appium/build/lib/main.js", false);
//		CommandLine command = new CommandLine("/Applications/Appium.app/Contents/Resources/node/bin/node");
//		command.addArgument("/Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js", false);
		
		
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument("4723");	
		command.addArgument("--no-reset", false);
		command.addArgument("--log-level", false);
		command.addArgument("error");

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			Thread.sleep(5000);
		} catch (ExecuteException e) {
			System.out.println("Appium Server Not Yet Started");
		} catch (IOException e) {
			System.out.println("Appium Server Not Yet Started");
		}
	}

	public static void ReLaunchApp() throws Exception{

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		clearTWCLogs();

		String[][] paths = read_excel_data.exceldataread("Paths");
		String adbPath = paths[15][Cap];

		String[] str ={"/bin/bash", "-c", adbPath+" shell pm disable com.weather.Weather"};
		Runtime.getRuntime().exec(str);
		Thread.sleep(2000);

		String[] str1 ={"/bin/bash", "-c", adbPath+" shell pm enable com.weather.Weather"};
		Runtime.getRuntime().exec(str1);

		Ad.closeApp();
		Ad.launchApp();
	}

	public static void clearCache() throws Exception{
		Thread.sleep(3000);
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		String[][] paths = read_excel_data.exceldataread("Paths");
		String adbPath = paths[15][Cap];

		String[] str ={"/bin/bash", "-c", adbPath+" shell pm clear com.weather.Weather"};
		Runtime.getRuntime().exec(str);
		System.out.println("Cleared The App Data Successfully");
		Thread.sleep(2000);

	}

	@SuppressWarnings("rawtypes")
	public static void LaunchApp() throws InterruptedException, IOException{

		killADB();
		AppiumServerStop();
		AppiumServerStart();

		logStep("Launch The App");
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		try {

			String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
			DesiredCapabilities capabilities = new DesiredCapabilities();

			/* --- Start Android Device Capabilities --- */
			if(Cap == 2){
				capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
				capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
				capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
				capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
				capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
				capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
				capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
				capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
				Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
			}
			/* ---End Android Device Capabilities --- */
			Ad.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

			try{
				if((Ad.findElementByName("OK")).isDisplayed()){
					Ad.findElementByName("OK").click();
				}else if((Ad.findElementByName("Continue")).isDisplayed()){
					Ad.findElementByName("Continue").click();
				}else if((Ad.findElementByName("Allow")).isDisplayed()){
					Ad.findElementByName("Allow").click();
					Thread.sleep(5000);
					homelocation=Ad.findElementById("com.weather.Weather:id/title_text_view").getText();
				}
			}catch(Exception e){

			}

			System.out.println("Capabilities have been launched");
			Thread.sleep(5000);
		} catch (Exception e) {
			System.out.println("Unable To Launch The Appium Capabilities");
		}
	}

	@SuppressWarnings("rawtypes")
	public static void LaunchAppWithFullReset() throws Exception{

		killADB();
		AppiumServerStop();
		AppiumServerStart();

		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();

		//try {

			String[][] capabilitydata = read_excel_data.exceldataread("Capabilities");
			DesiredCapabilities capabilities = new DesiredCapabilities();

			/* --- Start Android Device Capabilities --- */
			if(Cap == 2){
				capabilities.setCapability(capabilitydata[1][0], capabilitydata[1][Cap]);
				capabilities.setCapability(capabilitydata[2][0], capabilitydata[2][Cap]); 
				capabilities.setCapability(capabilitydata[3][0], capabilitydata[3][Cap]);
				capabilities.setCapability(capabilitydata[7][0], capabilitydata[7][Cap]); 
				capabilities.setCapability(capabilitydata[9][0], capabilitydata[9][Cap]);
				capabilities.setCapability(capabilitydata[10][0],capabilitydata[10][Cap]);
				capabilities.setCapability(capabilitydata[12][0],capabilitydata[12][Cap]);
				capabilities.setCapability("appActivity","com.weather.Weather.app.SplashScreenActivity");
				System.out.println("App : "+capabilitydata[10][Cap]);
				capabilities.setCapability(capabilitydata[13][0],capabilitydata[13][Cap]);
				capabilities.setCapability(capabilitydata[14][0],capabilitydata[14][Cap]);
				//Thread.sleep(50000);
				Ad = new AndroidDriver(new URL(capabilitydata[15][Cap]), capabilities);
				Thread.sleep(50000);
			}
			/* ---End Android Device Capabilities --- */
			Ad.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			//####added ths to handle allow button
			Thread.sleep(20000);
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
			System.out.println("Capabilities have been launched  with fullreset ");
			Thread.sleep(5000);
		//} catch (Exception e) 
		//{
			System.out.println("Unable To Launch The Appium Capabilities");
		//}
	}
	//Kill and relaunch the app
	public static void Kill_launch() throws Exception{
		try{
			Thread.sleep(5000);
			Ad.closeApp();
			Thread.sleep(5000);
			Ad.launchApp();		
			Thread.sleep(5000);
			After_launch();
		}catch(Exception e){

		}
	}

	//Handle popups after launch the app
	public static void After_launch(){
		try{
			if((Ad.findElementByName("Allow")).isDisplayed()){
				Thread.sleep(3000);
				Ad.findElementByName("Allow").click();
			}
		}catch(Exception e){
			System.out.println("Location already set");
		}
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

		try{
			Thread.sleep(3000);

			if(Ad.findElementByName("Please Search For A Location").isDisplayed()){
				System.out.println("Address not found, Entering Manually");
				Ad.findElementByName("Search").click();
				Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.view.ViewGroup[1]/android.support.v7.widget.LinearLayoutCompat[1]").sendKeys("30339");
				Thread.sleep(8000);
				Ad.tap(1, 380, 265, 1000);
				Thread.sleep(5000);

			}

		}catch(Exception e){

		}

	}
	public static void Check_feed1_ad() throws Exception
	{ 	
		WebElement feedad=null;
		try{
			System.out.println("Checking for feed1 ad");
			//WebElement feedad= Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
			//feedad=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[1]/android.view.View[1]");
			//Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]"));
			//feedad=	Ad.findElementByXPath(" //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]");
			feedad=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);
			if(feedad.isDisplayed())
			{
				System.out.println("feed1 ad present");
				//System.out.println("feed1 ad present and size is"+feedad.getSize());

				//screenShot("feed1");
				ScreenShot("feed_1","Passed");
				System.out.println("took the feed1 screen shot");

			}		   
		}
		catch(Exception e)
		{
			//screenShot_fail("feed1");
			ScreenShot("feed_1","Failed");
			System.out.println("took the failed feed1 screen shot");
			Assert.fail("feed1 ad not present");
			
		}
	}	
	public static void gettingfeed1_locators()
	{
		String allelements=Ad.getPageSource();
		System.out.println("Pagesource"+ allelements);
		String pack = Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]")).getAttribute("package");
		System.out.println("Attribule value package name  is:" + pack);
		//		   String attribue_name=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]")).getAttribute("text");
		//		   System.out.println("Attribule value name is:" + attribue_name);
		//		   String attribue_Accessibilityid=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]")).getAttribute("content-desc");
		//		   System.out.println("Attribule value is: Accessibilityid" + attribue_Accessibilityid);
	}

	public static void Check_feed2_ad() throws Exception
	{
		WebElement feedad=null;
		try{
			System.out.println("Checking for feed2 ad");
			Thread.sleep(5000);
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]"));
			feedad=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);
			if(feedad.isDisplayed())
			{
				System.out.println("feed2 ad present");
				System.out.println("feed2 ad present and size is"+feedad.getSize());
				//screenShot("feed2");
				ScreenShot("feed_2","Passed");
				System.out.println("took the feed2 screenshot");
			}		    
		}
		catch(Exception e)
		{
			//screenShot_fail("feed2");
			ScreenShot("feed_2","Failed");
			System.out.println("took the failed feed2 screen shot");
			Assert.fail("feed2 ad not present");
			
		}
	}
	public static void Check_feed3_ad() throws Exception
	{
		WebElement feedad=null;
		try{
			System.out.println("Checking for feed3 ad");

			//WebElement feedad=Ad.findElementById("com.weather.Weather:id/ad_view_holder");
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]"));
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]"));
			//feedad=	Ad.findElementByXPath(" //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]");
			feedad=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);			
			if(feedad.isDisplayed())
			{
				System.out.println("feed3 ad present");
				System.out.println("feed3 ad present and size is"+feedad.getSize());
				ScreenShot("feed_3","Passed");
				System.out.println("took the feed3 screenshot");
			}		    

		}
		catch(Exception e)
		{
			ScreenShot("feed_3","Failed");
			System.out.println("took the failed feed3 screen shot");
			Assert.fail("feed3 ad not present");
				
		}
	}
	public static void Check_feed4_ad() throws Exception
	{ 
		WebElement feedad=null;
		try{
			System.out.println("Checking for feed4 ad");
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
			//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]")); 
			feedad=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);
			if(feedad.isDisplayed())
			{
				System.out.println("feed4 ad present");
				//System.out.println("fee4 ad present and size is"+feedad.getSize());
				ScreenShot("feed_4","Passed");
				System.out.println("took the feed4 screenshot");
			}		    		  	
		}
		catch(Exception e)
		{
			ScreenShot("feed_4","Failed");
			System.out.println("took the failed feed4 screen shot");
			Assert.fail("feed4 ad not present");
		}
	}

	public static void Check_bb_ad()
	{ try{
		Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[2]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]")).isDisplayed();
		Thread.sleep(2000);
		System.out.println("bb ad present");
	}
	catch(Exception e)
	{
		System.out.println("bb ad not present");
	}
	}

	public static void screenShot(String screeenshotName)
	{
		String separator = File.separator;
		String filePath = System.getProperty("user.dir")+separator + "screenshots"+ separator+FeedAdVAlidationonUI.timeStamp + separator + screeenshotName +"_ pass" +".jpeg";

		// Take screenshot
		File sourceFile = ((TakesScreenshot) Ad).getScreenshotAs(OutputType.FILE);

		try {
			// Store screenshot
			FileUtils.copyFile(sourceFile, new File(filePath));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	public static void Check_submodules_Hourly_ad() throws Exception
	{ 
		try
		{
			System.out.println("Checking for Hourly Submodule  ad");
			Thread.sleep(5000);
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[4]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]"));
			//WebElement submodulead =Ad.findElementById("com.weather.Weather:id/ad_view_holder");
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[4]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.view.View[1]"));
			//WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]");
			WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[2]/android.widget.FrameLayout[1]");

			Thread.sleep(2000);
			if(submodulead.isDisplayed())
			{
				System.out.println("Hourly submodule ad present");
				System.out.println("Hourly submodule ad present ad present and size is"+submodulead.getSize());
				ScreenShot("Hourly ad","Passed");
				System.out.println("took the submoduleAd_Hourly screenshot");
			}		    

		}
		catch(Exception e)
		{
			ScreenShot("Hourly ad","Failed");
			System.out.println("took the failed submoduleAd_Hourly screenshot");
			Assert.fail("Hourly submodule ad is not  present");
		}
	}
	public static void Check_submodules_Daily_ad() throws Exception
	{ 
		try{
			System.out.println("Checking for Daily Submodule  ad");
			Thread.sleep(5000);
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]"));
			//WebElement submodulead = Ad.findElementById("com.weather.Weather:id/ad_view_holder");
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]"));
			//WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]");
			WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);
			if(submodulead.isDisplayed())
			{
				System.out.println("Daily submodule ad present");
				System.out.println("Daily submodule ad present ad present and size is"+submodulead.getSize());
				ScreenShot("Daily ad","Passed");
				System.out.println("took the submoduleAd_Daily screenshot");
			}		    
		}
		catch(Exception e)
		{
			/*softAssert.fail("Daily submodule ad is not  present");
			screenShot_fail("submoduleAd_Daily");
			System.out.println("took the failed submoduleAd_Daily screenshot");*/
			ScreenShot("Daily ad","Failed");
			System.out.println("took the failed submoduleAd_Daily screenshot");
			Assert.fail("Daily submodule ad is not  present");
		}
	}
	public static void Check_submodules_Maps_ad() throws Exception
	{ 
		try{
			System.out.println("Checking for Maps Submodule  ad");
			Thread.sleep(5000);
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[4]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]"));
			//WebElement submodulead  =Ad.findElementById("com.weather.Weather:id/ad_view_holder");
			//WebElement submodulead=Ad.findElement(By.xpath("/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"));
			//WebElement submodulead=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ViewSwitcher[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]"));			
			//WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]");
			WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);

			if(submodulead.isDisplayed())
			{
				System.out.println("Maps submodule ad present");
				System.out.println("Maps submodule ad present ad present and size is"+submodulead.getSize());
				ScreenShot("Maps ad","Passed");
				System.out.println("took the submoduleAd_Maps screenshot");
			}		    
		}
		catch(Exception e)
		{
			
			ScreenShot("Maps ad","Passed");
			System.out.println("took the failed submoduleAd_Maps screenshot");
			Assert.fail("Maps submodule ad is not  present");
		}
	}

	public static void selectlocation() throws Exception
	{
		DeviceStatus device_status = new DeviceStatus();
		int Cap = device_status.Device_Status();
		String[][] excel=read_excel_data.exceldataread("Zipcode");
		String homelocation;
		//Ad.findElement(By.id("com.weather.Weather:id/txt_location_name")).sendKeys(excel[0][Cap]);
		if(excel[1][1].contains("null"))
		{
			homelocation=Ad.findElementById("com.weather.Weather:id/txt_location_name").getAttribute("name");
			//				Ad.findElement(By.id("com.weather.Weather:id/txt_location_name")).sendKeys(homelocation);
			//				Thread.sleep(5000);
			
		
			System.out.println("App Launched with Home loation::" +homelocation);
		}
		else
		{
			Ad.findElement(By.id("com.weather.Weather:id/search")).click();
			Thread.sleep(2000);
			Ad.findElement(By.id("com.weather.Weather:id/txt_location_name")).sendKeys(excel[1][1]);
			System.out.println(excel[1][1]);
			Thread.sleep(2000);
			Ad.findElement(By.id("com.weather.Weather:id/search_item_container")).click();
			Thread.sleep(2000);
			System.out.println("App Launched with" +excel[1][1]+ "loation");
			Thread.sleep(2000);
		}

	}
	
	
	//read Build folder details from Build Folder
		public static void CheckBuildFolder(File folder) throws Exception {
			//readExcelValues.excelValues("Smoke","Paths");

			String Foldername = ScreenShot+"/"+CharlesFunctions.ver;
			folder = new File(Foldername);


			for(int i =0;i<=25;i++){
				if(i==0){
					folder = new File(Foldername);
				}else{
					folder = new File(Foldername+"_"+i);
				}
				if(folder.exists()){
					System.out.println("Build Number is exist "+CharlesFunctions.ver);
					System.out.println("Folder is :"+folder.toString());

				}else{
					if(i==0){
						System.out.println("Build Number is "+CharlesFunctions.ver);
						CharlesFunctions.ver=CharlesFunctions.ver;
						break;
					}else{
						System.out.println("Build Number is "+CharlesFunctions.ver+"_"+i);
						CharlesFunctions.ver=CharlesFunctions.ver+"_"+i;
						break;
					}
				}
			}

		}
	public static void ScreenShot(String Adtype,String ScreenType) throws Exception{
		ScreenShot = System.getProperty("user.dir")+"/Screenshots";
		if(ScreenType.equals("Passed")){

			File Screenshot = ((TakesScreenshot)Ad).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Screenshot, new File(ScreenShot+"/"+CharlesFunctions.ver+"/ScreenShot_"+ScreenType+" "+Adtype+".png"));
		}else{
			File Screenshot = ((TakesScreenshot)Ad).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Screenshot, new File(ScreenShot+"/"+CharlesFunctions.ver+"/ScreenShot_"+ScreenType+" "+Adtype+".png"));
			FileUtils.copyFile(Screenshot, new File(ScreenShot+"/Failed/ScreenShot_"+ScreenType+" "+Adtype+".png"));

		}
	}

	public static void screenShot_fail(String screeenshotName)
	{
		//String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime());
		String separator = File.separator;
		//String filePath = System.getProperty("user.dir")+separator+"screenshots"+separator+FeedAdVAlidationonUI.timeStamp + separator + screeenshotName +"_Fail"+ ".jpeg";
		String filePath = System.getProperty("user.dir")+separator+"screenshots"+separator+"Failed"+separator+FeedAdVAlidationonUI.timeStamp + separator + screeenshotName +"_Fail"+ ".jpeg";
		//Screenshots/Failed/*.jpeg
		// Take screenshot
		File sourceFile = ((TakesScreenshot) Ad).getScreenshotAs(OutputType.FILE);

		try {
			// Store screenshot
			FileUtils.copyFile(sourceFile, new File(filePath));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public static void Check_Allergy_spotAd() throws Exception
	{ 
		try
		{
		System.out.println("Checking for Allergy module spotlight ad");
		Thread.sleep(5000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("Allergy Module spotlight ad present");
	    	System.out.println("Allergy Module spotlight ad present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("Allergy Spotlight ad","Passed");
	    	System.out.println("took the Allergy module spotlight ad screenshot");
	    }		    		  
	
	}
		catch(Exception e)
		{
			
			ScreenShot("Allergy Spotlight ad","Failed");
			System.out.println("took the failed Allergy Module spotlight ad screen shot");
			Assert.fail("Allergy Module spotlight ad not present");
		}
	}
	
	public static void Check_Cold_Flu_spotAd() throws Exception
	{ 
		try
		{
		System.out.println("Checking for Cold&Flu module spot light ad");
		Thread.sleep(5000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("Cold&Flu Module spot light present");
	    	System.out.println("Cold&Flu Module spot light present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("Cold & Flu Spotlight ad","Passed");
	    	System.out.println("took the Cold&Flu module spot light screenshot");
	    }		    		  
	
	}
		catch(Exception e)
		{
			
			ScreenShot("Cold & Flu Spotlight ad","Failed");
			System.out.println("took the failed Cold&Flu Module spot light ad screen shot");
			Assert.fail("Cold&Flu Module spot light ad not present");
		}
	}
	
	public static void Check_Allergy_BigbannerAd() throws Exception
	{ 
		try
		{
		System.out.println("Checking for Allergy module BigBanner ad");
		Thread.sleep(5000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("Allergy Module BigBanner ad present");
	    	System.out.println("Allergy Module BigBanner ad present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("Allergy Module BigBanner ad","Passed");
	    	System.out.println("took the Allergy Module BigBanner adscreenshot");
	    }		    		  
	
	}
		catch(Exception e)
		{
			ScreenShot("Allergy Module BigBanner ad","Failed");
			
			System.out.println("took the failed Allergy Module BigBanner ad");
			Assert.fail("Allergy Module BigBanner adnot present");
		}
	}
	
	public static void Check_Cold_Flu_BigbannertAd() throws Exception
	{ 
		try
		{
		System.out.println("Checking for Cold_Flu module BigBanner ad");
		Thread.sleep(9000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("Cold_Flu Module BigBanner ad present");
	    	System.out.println("Cold_Flu Module BigBanner ad present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("Cold_Flu Module BigBanner ad","Passed");
	    	System.out.println("took the Cold_Flu Module BigBanner ad screenshot");
	    }		    		  
	
	}
		catch(Exception e)
		{
			ScreenShot("Cold_Flu Module BigBanner ad","Failed");
			//screenShot_fail("Cold_Flu Module BigBanner ad");
			System.out.println("took the failed Cold_Flu Module BigBanner ad");
			Assert.fail("Cold_Flu Module BigBanner ad not present");
		}
	}
	public static void Clickon_Back_Button() throws Exception 
	{
		try{
		Ad.findElementByClassName("android.widget.ImageButton").click();
		Thread.sleep(3000);
		}
		catch(Exception e )
		{
			Ad.findElementByClassName("android.widget.ImageButton").click();
			Thread.sleep(3000);
		}
	}
	
	//Delete Screenshots session xml files
	public static void delete_screenshots() throws Exception{
		
		ScreenShot = System.getProperty("user.dir")+"/screenshots";

		String downloadPath = ScreenShot+"/Failed";
		//String Screenshots = readExcelValues.data[16][Cap];

		File index = new File(downloadPath);
		//File Screenindex= new File(Screenshots);

		if (!index.exists()) {
			System.out.println("Specified ScreenShot folder is not exist and creating the same folder now");
			index.mkdir();
		} else {
			System.out.println("Specified ScreenShot folder is exist and deleting the same folder");
			FileUtils.cleanDirectory(index);
			System.out.println("Deleted all the files in the specified ScreenShot folder");
		}

	}
	
	
	public static void Check_Lifestyle_Module_ad() throws Exception
	{ 
		try{
			System.out.println("Checking for Lifesyle module  ad");
			Thread.sleep(5000);
			WebElement submodulead=	Ad.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]");
			Thread.sleep(2000);

			if(submodulead.isDisplayed())
			{
				System.out.println(" Lifesyle module ad present");
				//System.out.println("Maps submodule ad present ad present and size is"+submodulead.getSize());
				ScreenShot("Lifesyle module ad","Passed");
				System.out.println("took the Lifesyle module ad screenshot");
			}		    
		}
		catch(Exception e)
		{
			
			ScreenShot("Lifesyle module ad","Passed");
			System.out.println("took the failed Lifesyle module ad screenshot");
			Assert.fail("Lifesyle module ad is not  present");
		}
	}
	public static void clickallergy()
	{
	 Ad.findElementByName("Allergy").click();
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
	
	public static void Check_GoRun_spotAd() throws Exception
	{ try
		{
		System.out.println("Checking for GoRun module spotlight ad");
		Thread.sleep(5000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("GoRun Module spotlight ad present");
	    	System.out.println("GoRun Module spotlight ad present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("GoRun Spotlight ad","Passed");
	    	System.out.println("took the GoRun module spotlight ad screenshot");
	    }		    		  	
	}catch(Exception e)
		{
          	ScreenShot("GoRun Spotlight ad","Failed");
			System.out.println("took the failed GoRun Module spotlight ad screen shot");
			Assert.fail("GoRun Module spotlight ad not present");
		}
	}
	
	public static void Check_GoRun_BigbannerAd() throws Exception
	{ try
		{
		System.out.println("Checking for GoRun module BigBanner ad");
 		Thread.sleep(5000);
		//WebElement feedad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ListView[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]"));
		WebElement AllergyModfriestad=Ad.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]/android.widget.LinearLayout[1]/android.widget.ListView[1]/android.widget.FrameLayout[2]"));
		Thread.sleep(2000);
	    if(AllergyModfriestad.isDisplayed())
	    {
	    	System.out.println("GoRun Module BigBanner ad present");
	    	System.out.println("GoRun Module BigBanner ad present and size is"+AllergyModfriestad.getSize());
	    	ScreenShot("GoRun Module BigBanner ad","Passed");
	    	System.out.println("took the GoRun Module BigBanner adscreenshot");
	    }		    		  	
	}
	catch(Exception e)
	{		
		ScreenShot("GoRun Module BigBanner ad","Failed");
		System.out.println("took the failed GoRun Module BigBanner ad");
		Assert.fail("GoRun Module BigBanner adnot present");
		}
	}	
}

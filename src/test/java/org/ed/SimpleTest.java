package org.ed;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class SimpleTest {
    public static void main(String[] args) throws IOException {
    	System.setProperty("webdriver.gecko.driver","/usr/local/Cellar/geckodriver/0.26.0/bin/geckodriver");
    	
    	// --------- proxy setup ----------
    	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    	BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start(0);
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT );
        proxy.newHar("google.com");
        
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        // ------------------------------
    	
        @SuppressWarnings("deprecation")
    	WebDriver driver = new FirefoxDriver(capabilities);

        driver.get("https://google.com");
        Har har = proxy.getHar();
        
        String homeDir = System.getProperty("user.home");
        har.writeTo(new File(homeDir + "/Downloads/google.har"));

        System.out.println(driver.getTitle());
       
        driver.close();
       
    }

}
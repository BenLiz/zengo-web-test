import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

public class TestWeb {
    public static void testQRcode(WebElement driver) throws IOException, NotFoundException {
        String imgURLText = driver.getAttribute("src");
        URL imgURL = new URL(imgURLText);
        HttpURLConnection connection = (HttpURLConnection) imgURL
                .openConnection();
        connection.setRequestProperty(
                "User-Agent",
                "Chrome/100.0.4896.88");
        BufferedImage image = ImageIO.read(connection.getInputStream());
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(image);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        Result result = new MultiFormatReader().decode(binaryBitmap);
        String qrTxt = result.getText();
        System.out.println("QR URL IS: " + qrTxt);
    }

    public static void main(String[] args) throws InterruptedException, NotFoundException, IOException {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.zengo.com/");
        String freeBitcoinUrl = "https://zengo.com/free-bitcoin/";
        WebElement homePage = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("page")));
        WebElement freeBitcoin = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("menu-item-6043")));

        if (homePage.isDisplayed()) {
            System.out.println("Home Page Is Loaded");
        } else {
            System.out.println("Home Page Failed To Load");
            driver.quit();
        }
        freeBitcoin.click();
        String url = driver.getCurrentUrl();
        if (url.equalsIgnoreCase(freeBitcoinUrl)) {
            System.out.println("You Were Redirected To Free Bitcoin!");
        } else {
            System.out.println("You Were Not Redirected");
        }
        WebElement download = driver.findElement(By.xpath("//*[@class='cretive-button-text']"));
        download.click();
        WebElement qrCode = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@alt='qr code']"))));
        if (qrCode.isDisplayed()){
            testQRcode(qrCode);
            System.out.println("QR Popup Is Displayed!");
        }
        else {
            System.out.println("QR Popup Is Not Displayed");
            driver.quit();
        }
        Thread.sleep(5000);
        driver.quit();
    }
}

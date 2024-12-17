import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;

public class ParametrizedSwoopTests {

    @BeforeTest
    public void setup() {
        open("https://swoop.ge");
        cookies();

    }

    @DataProvider(name = "offerSelectors")
    public Iterator<Object[]> offerSelectors() {
            return Stream.generate(() -> {
                        ElementsCollection offers = $$("a.group.flex.flex-col.gap-3.cursor-pointer.max-tablet\\:gap-2");
                        if (!offers.isEmpty()) {
                            return new Object[]{offers.get(0)};
                        }
                        return null; // Stop generating null values
                    })
                    .filter(data -> data != null) // Filter out null values
                    .limit(10)
                    .iterator();

    }

    private void cookies() {
        try {
            $("#__next > div.w-full.mx-auto.overflow-x-clip.max-laptop\\:bg-white.laptop\\:bg-white > div.border-1.fixed.bottom-20.laptop\\:bottom-5.left-1\\/2.z-50.min-w-\\[320px\\].-translate-x-1\\/2.flex-col.items-center.justify-center.gap-3.rounded-xl.border-gray-10.bg-white.p-6.shadow-md.desktop\\:min-w-\\[725px\\].desktop\\:flex-row.flex > button").should(exist, Duration.ofSeconds(10)).shouldBe(visible, Duration.ofSeconds(10)).click();
        } catch (Exception ignore) {
        }
    }

    @Test(dataProvider = "offerSelectors")
    public void checkSaleValuesTest(SelenideElement offerSelector) {

        $("a[href='/category/110/sporti/']").shouldBe(visible, Duration.ofSeconds(10)).click();


        String fullPriceText = offerSelector.$("h4.line-through").getText().replace("₾", "").trim();
        String discountedPriceText = offerSelector.$("h4.font-tbcx-bold").getText().replace("₾", "").trim();
        String discountText = offerSelector.$("p.font-tbcx-bold").getText().replaceAll("[^\\d.]", "").trim();


        double fullPrice = Double.parseDouble(fullPriceText);
        double discountedPrice = Double.parseDouble(discountedPriceText);
        double discountPercentage = Double.parseDouble(discountText);

        int expectedDiscountedPrice = (int) Math.rint(fullPrice * (100.00 - discountPercentage) / 100);


        assertEquals(expectedDiscountedPrice, discountedPrice);

    }
//
//    @Test(dataProvider = "offerSelectors")
//    public void validateCartBehavior(SelenideElement offerSelector) {
//
//       open("https://swoop.ge/category/110/sporti/");
//        offerSelector.shouldBe(clickable, Duration.ofSeconds(10)).click();
//        $("button[data-testid='secondary-button']")
//                .scrollIntoView(true)
//                .shouldBe(clickable, Duration.ofSeconds(10))
//                .click();
//
//
//        $("#__next > div.w-full.mx-auto.overflow-x-clip.max-laptop\\:bg-white.laptop\\:bg-white > div.sticky.bg-white.top-0.z-50.transition-all.duration-300 > div > div > div.flex.justify-start.items-start.hidden.laptop\\:flex.gap-5.items-center.w-full > div.flex.justify-start.items-start.gap-3.items-center.hidden.tablet\\:flex > div.flex.justify-start.items-start.items-center > a").shouldBe(clickable, Duration.ofSeconds(10)).click();
//
//        SelenideElement quantityElement = $("#__next > div.w-full.mx-auto.overflow-x-clip.max-laptop\\:bg-white.laptop\\:bg-white > div.max-w-\\[1240px\\].w-\\[92\\%\\].mx-auto > div.flex.justify-start.items-start.laptop\\:justify-between.flex-col.laptop\\:flex-row.mt-8.mb-20.laptop\\:gap-10.desktop\\:gap-20.gap-6 > div.flex.flex-col.laptop\\:w-\\[760px\\].w-full > div.flex.justify-start.items-start.gap-6.flex-col");
//
//        // Assert that the quantity is correct
//        quantityElement.shouldHave(Condition.text("1"));
//
//    }
}

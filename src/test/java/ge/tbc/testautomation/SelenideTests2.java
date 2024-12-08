package ge.tbc.testautomation;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ge.tbc.testautomation.data.Constants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

public class SelenideTests2 {


    @Test
    public void validateDemosDesign() {
        goTo(TELERIK_URL);
        //web elements tests
        cookies();

        $$(WEB_SELECTOR).forEach(card -> {

            SelenideElement hoverImg = card.$(HOVER_IMG);
            if (hoverImg.isDisplayed()) {
                hoverImg.hover();

                SelenideElement overlay = hoverImg.$(OVERLAY);
                overlay.shouldBe(visible);
                overlay.shouldHave(cssValue(PROPERTY_NAME, EXPECTED_COLOR));
                if (card.getText().contains(KENDO_TXT)) {
                    overlay.$$("a").findBy(text(EXPECTED_TXT)).shouldBe(visible);
                }

            }
        });

        //   $$(WEB_SELECTOR).stream().filter(card -> card.$(HOVER_IMG).isDisplayed()).forEach(displayedCards -> {
        //
        //
        //            displayedCards.hover();
        //            System.out.println(displayedCards.getText()+"\n");
        //
        //
        //            SelenideElement overlay = displayedCards.$(OVERLAY);
        //            overlay.shouldBe(visible);
        //            overlay.shouldHave(cssValue(PROPERTY_NAME, EXPECTED_COLOR));
        //
        //            if (displayedCards.getText().contains(KENDO_TXT)) {
        //                overlay.$$("a").findBy(text(EXPECTED_TXT)).shouldBe(visible);
        //            }
        //
        //        });  i tried this but not working (mostly)


        //Desktop element test

        $$(DESKTOP_SELECTOR).stream().filter(card -> card.isDisplayed() && card.$(MICROSOFT).exists()).forEach(filteredCard -> filteredCard.$(MICROSOFT).shouldBe(visible));


        $$(MOBILE_SELECTOR).stream().filter(card -> card.isDisplayed() && card.getText().contains(TELERIK_UI_FOR_XAMARIN)).forEach(filteredCard -> {
            filteredCard.$(MICROSOFT).shouldBe(visible);
            filteredCard.$(APPLE_STORE).shouldBe(visible);
            filteredCard.$(GOOGLE_PLAY).shouldBe(visible);
        });


        $$(SECTION_LINKS).forEach(links -> links.shouldBe(visible));

        ElementsCollection sectionLinks = $$(SECTION_LINKS);
        ElementsCollection sectionElements = $$(SECTION_SELECTOR).filterBy(attribute("data-sf-element", "Row"));

        sectionLinks.shouldHave(size(sectionElements.size()));

        int size = sectionElements.size();
        for (int i = 0; i < size; i++) {
            SelenideElement elementLink = sectionLinks.get(i);
            SelenideElement elementSection = sectionElements.get(i);

            elementSection.scrollIntoView(true);

            boolean ans = Objects.requireNonNull(elementLink.getAttribute("class")).contains(("active"));
            Assert.assertTrue(ans);

        }


        for (int i = 0; i < size; i++) {
            SelenideElement elementLink = sectionLinks.get(i);
            SelenideElement elementSection = sectionElements.get(i);
            elementLink.hover();
            elementLink.click();
            elementSection.shouldBe(visible);
        }


    }

    @Test
    public void validateOrderMechanics() {
        goTo(TELERIK_URL);
        $(PRICING).hover().click();
        try {
            cookies();
        } catch (Exception ignored) {
        }
        $(BUY_BUTTON).hover().click();
        $(DONT_LOGIN).hover().click();
        double unitPrice = Double.parseDouble($$(".e2e-price-per-license").first().getText().replace(",", "").replace("$", ""));
        assertEquals(unitPrice,1499.00); //price is correct



        int quantity = Integer.parseInt($$(".k-input-value-text").first().getText());

        double expectedTotal = quantity == 1 ? unitPrice : quantity < 6 ? quantity * (unitPrice * 0.95) : quantity * unitPrice * .9;
        double actualTotal = Double.parseDouble($("td[data-label='Subtotal'] >div >div:nth-child(2)").getText().replace(",", "").replace("$", ""));

        Assert.assertEquals(actualTotal, expectedTotal);

    }

    @Test
    public void chainedLocatorsTest() {
        goTo(DEMOQA);

        ElementsCollection books = $$(BOOKS).filterBy(text(AUTHOR)).filterBy(text(JAVASCRIPT));

        books.forEach(book -> {
            SelenideElement img = book.$("img");
            assertNotNull(img.getAttribute("src"));
            assertFalse(img.getAttribute("src").isEmpty());
        });
    }

    @Test
    public void softAssertTest() {
        goTo(DEMOQA);
        SoftAssert softAssert = new SoftAssert();

        ElementsCollection books = $$(BOOKS).filterBy(text(AUTHOR)).filterBy(text(JAVASCRIPT));

        softAssert.assertNotEquals(books.size(), 10);

        String firstBookTitle = $(FIRST_BOOK).$$("div.rt-td").get(1).$("a").getText();

        assert firstBookTitle.equals(FIRST_TITLE);

        softAssert.assertAll();
    }

    private void goTo(String URL) {
        open(URL);
//        Configuration.holdBrowserOpen = true;
//        cookies();
    }

    private void cookies() {
        try {
            $(COOKIES_ACCEPT).should(exist, Duration.ofSeconds(10)).shouldBe(visible, Duration.ofSeconds(10)).click();
        } catch (Exception ignore) {
        }
    }


}
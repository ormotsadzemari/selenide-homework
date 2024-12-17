import com.codeborne.selenide.Condition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class AlternativeParametrizations {

    @DataProvider(name = "formData")
    public Object[][] formData() {
        return new Object[][]{
                {"John", "Doe", "john.doe@gmail.com", "Male", "1234567890"},
                {"Jane", "Smith", "jane.smith@gmail.com", "Female", "0987654321"},
                {"Regina", "Palange", "regina.palange@gmail.com", "Female", "2229993338"}
        };
    }

    @Test(dataProvider = "formData")
    public void formTests(String firstName, String lastName, String email, String gender, String mobileNumber) {
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
        $("#userEmail").setValue(email);

        $x("//label[text()='" + gender + "']")
                .scrollIntoView(true)
                .shouldBe(Condition.clickable, Duration.ofSeconds(10))
                .click();
        $("#userNumber").setValue(mobileNumber);

        $("#submit").scrollIntoView(true).shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldBe(Condition.enabled, Duration.ofSeconds(10))
                .click();
        $("#example-modal-sizes-title-lg").shouldBe(Condition.visible);
        $("td", 1).shouldHave(Condition.text(firstName + " " + lastName));

    }

    @Test
    @Parameters({"firstName", "lastName", "gender", "mobileNumber"})
    public void formTest(
            @Optional("John") String firstName,
            @Optional("Doe") String lastName,
            @Optional("Male") String gender,
            @Optional("1234567890") String mobileNumber) {
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);

        $x("//label[text()='" + gender + "']")
                .scrollIntoView(true)
                .shouldBe(Condition.clickable, Duration.ofSeconds(10))
                .click();

        $("#userNumber").setValue(mobileNumber);

        $("#submit").scrollIntoView(true).click();

        $("#example-modal-sizes-title-lg").shouldBe(Condition.visible);
        $("td", 1).shouldHave(Condition.text(firstName + " " + lastName));

    }
}



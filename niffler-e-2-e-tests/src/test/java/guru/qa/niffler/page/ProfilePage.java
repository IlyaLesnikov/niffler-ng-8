package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement archivedCheckbox = $("[class*='PrivateSwitchBase-input']");
    private final ElementsCollection categories = $$("[class*='MuiBox-root css-1lekzkb']");
    private final ElementsCollection button = $$("button");

    public ProfilePage archivedCheckboxSubmit() {
        archivedCheckbox.click();

        return this;
    }

    public ProfilePage shouldArchivedCategoryDisplayed(String categoryName) {
        categories.find(text(categoryName))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage archivedCategorySubmit(String categoryName) {
        categories.find(text(categoryName))
                .shouldBe(visible)
                .click();

        return this;
    }

    public ProfilePage unarchivedCategorySubmit(String categoryName) {
        Objects.requireNonNull(categories.stream().filter(element -> element.text().equals(categoryName))
                        .findFirst()
                        .orElse(null))
                .$("[data-testid='UnarchiveOutlinedIcon']").shouldBe(visible)
                .click();

        return this;
    }

    public ProfilePage buttonSubmit(String buttonName) {
        button.stream().filter(element -> element.text().equals(buttonName))
                .findFirst()
                .orElseThrow()
                .click();

        return this;
    }

    public void shouldArchiveCategoryButtonDisplayed(String categoryName) {
        categories.stream().filter(element -> element.text().equals(categoryName))
                .findFirst()
                .orElseThrow()
                .$("[data-testid='UnarchiveOutlinedIcon']")
                .shouldBe(visible);
    }
}

package de.mensa_uds.android.data;

public class Meal {

    private String category;
    private String title;
    private String description;
    private String priceStudent;
    private String priceWorker;
    private String priceGuest;
    private String color;

    private String[] allergens;
    private SpecialString[] components;
    private String[] dishes;
    private String[] supplements;

    public Meal() {

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriceStudent() {
        return priceStudent;
    }

    public void setPriceStudent(String priceStudent) {
        this.priceStudent = priceStudent;
    }

    public String getPriceWorker() {
        return priceWorker;
    }

    public void setPriceWorker(String priceWorker) {
        this.priceWorker = priceWorker;
    }

    public String getPriceGuest() {
        return priceGuest;
    }

    public void setPriceGuest(String priceGuest) {
        this.priceGuest = priceGuest;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SpecialString[] getComponents() {
        return components;
    }

    public void setComponents(SpecialString[] components) {
        this.components = components;
    }

    public String[] getDishes() {
        return dishes;
    }

    public void setDishes(String[] dishes) {
        this.dishes = dishes;
    }

    public String[] getSupplements() {
        return supplements;
    }

    public void setSupplements(String[] supplements) {
        this.supplements = supplements;
    }

    public String[] getAllergens() {
        return allergens;
    }

    public void setAllergens(String[] allergens) {
        this.allergens = allergens;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


}

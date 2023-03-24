package praktikum.model;

public class IngredientsArrayModel implements Model{
    private String[] ingredients;

    public IngredientsArrayModel(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}

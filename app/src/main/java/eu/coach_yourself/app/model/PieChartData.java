package eu.coach_yourself.app.model;

public class PieChartData {
    private String category_id="";
    private String category_color="";
    private float category_percentage=0;
    private String category_name="";

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_color() {
        return category_color;
    }

    public void setCategory_color(String category_color) {
        this.category_color = category_color;
    }

    public float getCategory_percentage() {
        return category_percentage;
    }

    public void setCategory_percentage(float category_percentage) {
        this.category_percentage = category_percentage;
    }
}

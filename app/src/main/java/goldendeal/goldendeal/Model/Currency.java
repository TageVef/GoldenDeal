package goldendeal.goldendeal.Model;

public class Currency {
    private Long value;
    private String title;
    private boolean imageEconomy;

    public Currency() {
    }

    public Currency(Long value, String title, boolean imageEconomy) {
        this.value = value;
        this.title = title;
        this.imageEconomy = imageEconomy;
    }

    public Long getValue() {
        return value;
    }

    public String printValue(){
        return Long.toString(value);
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isImageEconomy() {
        return imageEconomy;
    }

    public void setImageEconomy(boolean imageEconomy) {
        this.imageEconomy = imageEconomy;
    }
}

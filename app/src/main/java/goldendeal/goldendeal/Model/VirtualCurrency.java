package goldendeal.goldendeal.Model;

public class VirtualCurrency {
    private Long value;
    private String title;
    private boolean imageEconomy;
    private Long maxValue;

    public VirtualCurrency() {
    }

    public VirtualCurrency(Long value, Long maxValue) {
        this.value = value;
        this.maxValue = maxValue;
    }

    public VirtualCurrency(Long value, String title, boolean imageEconomy, Long maxValue) {
        this.value = value;
        this.title = title;
        this.imageEconomy = imageEconomy;
        this.maxValue = maxValue;
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

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }
}

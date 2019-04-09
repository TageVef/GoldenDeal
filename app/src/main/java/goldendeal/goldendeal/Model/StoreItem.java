package goldendeal.goldendeal.Model;

public class StoreItem {
    private String title;
    private String description;
    private String imageURL;
    private VirtualCurrency currency;

    public StoreItem() {

    }

    public StoreItem(String title, String description, VirtualCurrency currency) {
        this.title = title;
        this.description = description;
        this.currency = currency;
    }

    public StoreItem(String title, String description, String imageURL, VirtualCurrency currency) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.currency = currency;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {return imageURL;}

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public VirtualCurrency getCurrency() {return currency;}

    public void setCurrency(VirtualCurrency currency) {
        this.currency = currency;
    }
}

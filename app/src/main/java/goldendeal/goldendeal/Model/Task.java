package goldendeal.goldendeal.Model;

public class Task {

    private String title;
    private String description;
    private long rewardValue;
    private String rewardTitle;

    public Task() {
    }

    public Task(String title, String description, long rewardValue, String rewardTitle) {
        this.title = title;
        this.description = description;
        this.rewardValue = rewardValue;
        this.rewardTitle = rewardTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(long rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }
}

package goldendeal.goldendeal.Model;

public class Task {

    private String title;
    private String description;
    private String rewardValue;
    private String rewardTitle;
    private String taskOwner;

    public Task() {
    }

    public Task(String title, String description, String rewardValue, String rewardTitle, String taskOwner) {
        this.title = title;
        this.description = description;
        this.rewardValue = rewardValue;
        this.rewardTitle = rewardTitle;
        this.taskOwner = taskOwner;
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

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }
}

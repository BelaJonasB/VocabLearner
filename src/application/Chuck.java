package application;

public class Chuck {
    private String icon_url, id, url, value, created_at, updated_art;
    private final  String[]  categories;

    public Chuck(String icon_url, String id, String url, String value, String[] categories, String created_at, String updated_art) {
        this.icon_url = icon_url;
        this.id = id;
        this.url = url;
        this.value = value;
        this.categories = categories;
        this.created_at = created_at;
        this.updated_art = updated_art;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_art() {
        return updated_art;
    }

    public void setUpdated_art(String updated_art) {
        this.updated_art = updated_art;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package admin.Model;

public class Storage {

    private String service_id;
    private String name;
    private String quantity_unit;
    private double price;
    private String description;
    private String category;

    // Constructor đầy đủ
    public Storage(String service_id, String name, String quantity_unit,
            double price, String description, String category) {
        this.service_id = service_id;
        this.name = name;
        this.quantity_unit = quantity_unit;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    // Constructor chỉ có ID
    public Storage(String service_id) {
        this.service_id = service_id;
    }

    // ========== Getter ==========
    public String getService_id() {
        return service_id;
    }

    public String getName() {
        return name;
    }

    public String getQuantity_unit() {
        return quantity_unit;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    // ========== Setter ==========
    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity_unit(String quantity_unit) {
        this.quantity_unit = quantity_unit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

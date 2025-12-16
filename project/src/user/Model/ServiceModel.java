package user.Model;

import java.math.BigDecimal;

public class ServiceModel {
    private int serviceId;
    private String name;
    private String quantityUnit;
    private Double price;
    private String description;
    private String category;

    public ServiceModel(int serviceId, String name, String quantityUnit, Double price, String description,
            String category) {
        this.serviceId = serviceId;
        this.name = name;
        this.quantityUnit = quantityUnit;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + serviceId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }

    // Có thể thêm Setters nếu cần
}
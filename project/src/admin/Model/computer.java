package admin.Model;    

public class computer {
    private String id;
    private String name;
    private String ipadress;
    private String status;
    public computer(String id, String name, String ipadress, String status) {
        this.id = id;
        this.name = name;
        this.ipadress = ipadress;
        this.status = status;
    }
    
    public computer(String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIpadress() {
        return ipadress;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
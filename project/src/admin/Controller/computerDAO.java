package admin.Controller;

import java.util.ArrayList;
import admin.Model.computer;


public class computerDAO {
    public computerDAO() {
    }
    public ArrayList<computer> getAllComputer() {
        ArrayList<computer> computerList = new ArrayList<>();
        computerList.add(new computer("PC01", "Máy tính 01", "192.168.1.101", "Đang rảnh"));
        computerList.add(new computer("PC02", "Máy tính 02", "192.168.1.102", "Đã khóa"));
        computerList.add(new computer("PC03", "Máy tính 03", "192.168.1.103", "Đang hoạt động"));
        computerList.add(new computer("PC04", "Máy tính 04", "192.168.1.104", "Đang rảnh"));
        computerList.add(new computer("PC05", "Máy tính 05", "192.168.1.105", "Đang rảnh"));
        return computerList;
    }
}

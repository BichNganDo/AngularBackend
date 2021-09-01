package entity;

import java.util.List;

public class ListUsers {

    private int total;
    private List<User> allUsers;
    private int itemPerPage;

    public ListUsers() {
    }

    public ListUsers(int total, List<User> allUsers, int itemPerPage) {
        this.total = total;
        this.allUsers = allUsers;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}

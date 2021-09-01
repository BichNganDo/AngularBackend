package servlets;

import com.google.gson.Gson;
import common.APIResult;
import common.Config;
import entity.ListUsers;
import entity.User;
import helper.HttpHelper;
import helper.SecurityHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.UserModel;
import org.json.JSONObject;

public class APIUserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getuser": {
                int pageIndex = Integer.parseInt(request.getParameter("page_index"));
                int offset = (pageIndex - 1) * 10;
                List<User> allUser = UserModel.INSTANCE.getSliceUser(offset, 10);
                int totalUser = UserModel.INSTANCE.getTotalUser();

                ListUsers listUsers = new ListUsers();
                listUsers.setTotal(totalUser);
                listUsers.setAllUsers(allUser);
                listUsers.setItemPerPage(10);

                if (allUser.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listUsers);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "add": {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String level = request.getParameter("level");

                int addUser = UserModel.INSTANCE.addUser(username, password, level);

                if (addUser >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm user thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm user thất bại!");
                }
                break;
            }
            case "edit": {
                int id = Integer.parseInt(request.getParameter("id"));
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String level = request.getParameter("level");

                User userByID = UserModel.INSTANCE.getUserByID(id);
                if (userByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editUser = UserModel.INSTANCE.editUser(id, username, password, level);
                if (editUser >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa User thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa User thất bại!");
                }
                break;
            }

            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));
                int deleteUser = UserModel.INSTANCE.deleteUser(id);
                if (deleteUser >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa user thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa user thất bại!");
                }
                break;
            }

            case "login": {
                String username = request.getParameter("username");
                String password = SecurityHelper.getMD5Hash(request.getParameter("password"));
                boolean checkLogin = UserModel.INSTANCE.checkLogin(username, password);
                if (checkLogin) {
                    result.setErrorCode(0);
                    result.setMessage("Đăng nhập thành công!");
                    String genAuthenCookie = UserModel.INSTANCE.genAuthenCookie(username);
                    HttpHelper.setCookie(response, "authen", genAuthenCookie, 60000);
                } else {
                    result.setErrorCode(-3);
                    result.setMessage("Tên đăng nhập hoặc mật khẩu không đúng");
                }
                break;
            }
//            case "edit": {
//                int id = Integer.parseInt(request.getParameter("id"));
//                String name = request.getParameter("name");
//                String facebook = request.getParameter("facebook");
//                String year = request.getParameter("year");
//                String phone = request.getParameter("phone");
//
//                User userByID = UserModel.INSTANCE.getUserByID(id);
//                if (userByID.getId() == 0) {
//                    result.setErrorCode(-1);
//                    result.setMessage("Thất bại!");
//                    return;
//                }
//
//                int editUser = UserModel.INSTANCE.editUser(id, name, facebook, year, phone);
//                if (editUser >= 0) {
//                    result.setErrorCode(0);
//                    result.setMessage("Thành công!");
//                } else {
//                    result.setErrorCode(-1);
//                    result.setMessage("Thất bại!");
//                }
//                break;
//            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}

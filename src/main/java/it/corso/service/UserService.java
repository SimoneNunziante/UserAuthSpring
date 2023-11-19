package it.corso.service;
import java.util.List;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.corso.model.User;

public interface UserService
{
	ObjectNode userRegistration(User user);
	User getUserById(int id);
	ObjectNode updateUserData(User user);
	ObjectNode deleteUser(int id);
	List<User> getUsers();
	ObjectNode userLoginCheck(User user);
}
package it.corso.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.corso.dao.UserDao;
import it.corso.helper.ResponseManager;
import it.corso.helper.SecurityManager;
import it.corso.model.User;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private UserDao userDao;
	
	@Override
	public ObjectNode userRegistration(User user)
	{
		if(userDao.findByMail(user.getMail()) != null)
			return new ResponseManager(406, "Existing mail").getResponse();
		user.setPassword(SecurityManager.encode(user.getPassword()));
		userDao.save(user);
		return new ResponseManager(201, "User registrated").getResponse();
	}

	@Override
	public User getUserById(int id)
	{
		Optional<User> userOptional = userDao.findById(id);
		if(!userOptional.isPresent())
			return new User();
		User user = userOptional.get();
		user.setPassword(SecurityManager.decode(user.getPassword()));
		return user;
	}

	@Override
	public ObjectNode updateUserData(User user)
	{
		Optional<User> userOptional = userDao.findById(user.getId());
		if(!userOptional.isPresent())
			return new ResponseManager(404, "User not found").getResponse();
		User existing = userOptional.get();
		existing.setName(user.getName());
		existing.setLastname(user.getLastname());
		// existing.setMail(user.getMail());
		existing.setPassword(SecurityManager.encode(user.getPassword()));
		userDao.save(existing);
		return new ResponseManager(202, "User data updated").getResponse();
	}

	@Override
	public ObjectNode deleteUser(int id)
	{
		Optional<User> userOptional = userDao.findById(id);
		if(!userOptional.isPresent())
			return new ResponseManager(404, "User not found").getResponse();
		userDao.delete(userOptional.get());
		return new ResponseManager(202, "User profile removed").getResponse();
	}

	@Override
	public List<User> getUsers()
	{
		return (List<User>) userDao.findAll();
	}

	@Override
	public ObjectNode userLoginCheck(User user)
	{
		User existing = userDao.findByMailAndPassword(user.getMail(), SecurityManager.encode(user.getPassword()));
		if(existing == null)
			return new ResponseManager(401, "Not Authorized").getResponse();
		existing.setAuthToken(SecurityManager.generateToken(existing.getMail()));
		userDao.save(existing);
		return new ResponseManager(202, existing.getAuthToken()).getResponse();
	}
}
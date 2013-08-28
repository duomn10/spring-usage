package org.duomn.naive.web.service;

import java.util.List;

import org.duomn.naive.web.entity.UserModel;

public interface IUserService {
	
	int count();
	
	List<UserModel> listAll();
	
	UserModel get(int id);
	
	int save(UserModel userModel);
	
	void update(UserModel userModel);
	
	void delete(int id);
	
}

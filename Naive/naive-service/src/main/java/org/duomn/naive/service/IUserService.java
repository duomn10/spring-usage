package org.duomn.naive.service;

import java.util.List;

import org.duomn.naive.entity.UserModel;

public interface IUserService {
	
	int count();
	
	List<UserModel> listAll();
	
	UserModel get(int id);
	
	int save(UserModel userModel);
	
	void update(UserModel userModel);
	
	void delete(int id);
	
}

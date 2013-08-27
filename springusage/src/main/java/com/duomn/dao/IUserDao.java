package com.duomn.dao;

import java.util.List;

import com.duomn.entity.UserModel;

public interface IUserDao {

	int countAll();
	
	List<UserModel> listAll();
	
}

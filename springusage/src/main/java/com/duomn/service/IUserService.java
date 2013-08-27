package com.duomn.service;

import java.util.List;

import com.duomn.entity.UserModel;

public interface IUserService {
	
	int countUser();
	
	List<UserModel> listAll();
	
}

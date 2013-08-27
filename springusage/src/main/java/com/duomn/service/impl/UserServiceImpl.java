package com.duomn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duomn.dao.IUserDao;
import com.duomn.entity.UserModel;
import com.duomn.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;

	@Override
	public int count() {
		return userDao.countAll();
	}

	@Override
	public List<UserModel> listAll() {
		return userDao.listAll();
	}

	@Override
	public UserModel get(int id) {
		return userDao.get(id);
	}

	@Override
	public int save(UserModel userModel) {
		return userDao.save(userModel);
	}

	@Override
	public void update(UserModel userModel) {
		userDao.update(userModel);
	}

	@Override
	public void delete(int id) {
		userDao.delete(id);
	}
	
}

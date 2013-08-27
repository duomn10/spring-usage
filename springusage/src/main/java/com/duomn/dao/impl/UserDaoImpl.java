package com.duomn.dao.impl;

import org.springframework.stereotype.Repository;

import com.duomn.common.dao.impl.Hibernate4DaoImpl;
import com.duomn.dao.IUserDao;
import com.duomn.entity.UserModel;

@Repository
public class UserDaoImpl extends Hibernate4DaoImpl<UserModel, Integer> implements IUserDao {

	public UserDaoImpl() {
		super(UserModel.class);
	}
	
}

package org.duomn.naive.dao.impl;

import org.duomn.naive.common.dao.impl.Hibernate4DaoImpl;
import org.duomn.naive.dao.IUserDao;
import org.duomn.naive.entity.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends Hibernate4DaoImpl<UserModel, Integer> implements IUserDao {

	public UserDaoImpl() {
		super(UserModel.class);
	}
	
}

package org.duomn.naive.example.dao;

import org.duomn.naive.common.dao.impl.Hibernate4DaoImpl;
import org.duomn.naive.example.entity.UserExample;
import org.springframework.stereotype.Repository;

@Repository
public class UserExampleDaoImpl extends Hibernate4DaoImpl<UserExample, Integer> implements IUserExampleDao {

	public UserExampleDaoImpl() {
		super(UserExample.class);
	}
	
}

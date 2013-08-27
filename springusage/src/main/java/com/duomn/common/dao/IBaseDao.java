package com.duomn.common.dao;

import java.io.Serializable;
import java.util.List;

public interface IBaseDao<M, PK extends Serializable> {

	int countAll();
	
	M get(PK id);
	
	PK save(M model);
	
	void delete(PK id);
	
	void update(M model);
	
	List<M> listAll();
	
	List<M> listAll(int pn, int pageSize);
	
}

package org.duomn.naive.common.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.duomn.naive.common.dao.IBaseDao;
import org.mybatis.spring.support.SqlSessionDaoSupport;


public abstract class MyBatisDaoImpl<M, PK extends Serializable> extends SqlSessionDaoSupport implements IBaseDao<M, PK> {

	@Override
	public int countAll() {
		return getSqlSession().selectOne("select count(*) from tbl_user a");
	}

	@Override
	public M get(PK id) {
		// TODO 最后的结果转换
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		return getSqlSession().selectOne("selct", data);
	}

	@Override
	public PK save(M model) {
		// TODO
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(model.getClass().getSimpleName(), model);
		getSqlSession().insert("insert", data);
		return getSqlSession().selectOne("select pk");
	}

	@Override
	public void delete(PK id) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		getSqlSession().delete("delete", data);
		
	}

	@Override
	public void update(M model) {
		// TODO
//		Map<String, Object>
	}

	@Override
	public List<M> listAll() {
		// 结果转换
		return getSqlSession().selectList("selct");
	}

	@Override
	public List<M> listAll(int pn, int pageSize) {
		// 结果转换
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pn", pn);
		data.put("pageSize", pageSize);
		return getSqlSession().selectList("select", data);
	}

}

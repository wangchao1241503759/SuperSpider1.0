package cn.com.infcn.superspider.dao;

import org.springframework.stereotype.Repository;

import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.superspider.model.Task;

@Repository(value="taskDao")
public class TaskDao extends HibernateDao<Task, String>{

}

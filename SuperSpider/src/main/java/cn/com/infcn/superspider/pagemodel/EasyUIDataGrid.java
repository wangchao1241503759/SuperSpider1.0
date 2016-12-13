package cn.com.infcn.superspider.pagemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyUI DataGrid模型
 * 
 * @author infcn
 * @param <T>
 * 
 */
public class EasyUIDataGrid<T> implements java.io.Serializable {

	private static final long serialVersionUID = -5853568065264527789L;
	private Long total = 0L;
	private List<T> rows = new ArrayList<T>();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}

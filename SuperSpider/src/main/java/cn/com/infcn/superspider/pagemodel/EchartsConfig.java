/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年9月22日
 */
package cn.com.infcn.superspider.pagemodel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author lihf
 * @date 2016年9月22日
 */
public class EchartsConfig implements Serializable
{

	/**
	 * 
	 */
    private static final long serialVersionUID = -1685746866495695833L;
	private List<String> legendList;
	private List<String> xAxisList;
	private List<SeriesEntity> seriesList;
	private List<Map<String, Object>> list;
	/**
	 * @return the legendList
	 */
	public List<String> getLegendList()
	{
		return legendList;
	}
	/**
	 * @param legendList the legendList to set
	 */
	public void setLegendList(List<String> legendList)
	{
		this.legendList = legendList;
	}
	/**
	 * @return the xAxisList
	 */
	public List<String> getxAxisList()
	{
		return xAxisList;
	}
	/**
	 * @param xAxisList the xAxisList to set
	 */
	public void setxAxisList(List<String> xAxisList)
	{
		this.xAxisList = xAxisList;
	}
	/**
	 * @return the seriesList
	 */
	public List<SeriesEntity> getSeriesList()
	{
		return seriesList;
	}
	/**
	 * @param seriesList the seriesList to set
	 */
	public void setSeriesList(List<SeriesEntity> seriesList)
	{
		this.seriesList = seriesList;
	}
	/**
	 * @return the list
	 */
	public List<Map<String, Object>> getList()
	{
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<Map<String, Object>> list)
	{
		this.list = list;
	}
}

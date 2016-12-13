package cn.com.infcn.superspider.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import cn.com.infcn.superspider.model.PluginPointModel;
import cn.com.infcn.superspider.model.PointModel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.spider.Settings;
import com.justme.superspider.util.xml.BeanXMLUtil;
import com.justme.superspider.util.xml.XMLReader;
import com.justme.superspider.util.xml.XMLWriter;
import com.justme.superspider.xml.Extension;
import com.justme.superspider.xml.Extensions;
import com.justme.superspider.xml.Impl;
import com.justme.superspider.xml.Plugin;
import com.justme.superspider.xml.Plugins;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

public class XmlConfigUtil {
	
	public static Site toSite(String jsonStr)
	{
		return JSONObject.parseObject(jsonStr, Site.class);
	}
	public static void writeXml(String fileName,Site site)
	{
		try {
			File siteFolder = new File(Settings.website_xml_folder());
			if (!siteFolder.exists())
				throw new Exception("can not found WebSites folder -> " + siteFolder.getAbsolutePath());
			
			if (!siteFolder.isDirectory())
				throw new Exception("WebSites -> " + siteFolder.getAbsolutePath() + " must be folder !");
			//generate a site.xml file
			File file = new File(siteFolder.getAbsoluteFile()+File.separator+fileName+".xml");
			XMLWriter writer = BeanXMLUtil.getBeanXMLWriter(file, site);
			writer.setBeanName("site");
			writer.setClass("site", Site.class);
			writer.write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Plugins getPlugins(PluginPointModel pluginPointModel)
	{
		if(pluginPointModel.getPoints() != null && pluginPointModel.getPoints().size() > 0)
		{
			Plugins plugins = new Plugins();
			Plugin plugin = new Plugin();
			BeanUtils.copyProperties(pluginPointModel, plugin);
			
			Map<String,Extension> points = new HashMap<String, Extension>();
			Extensions extensions = new Extensions();
			for(PointModel point : pluginPointModel.getPoints())
			{
				Impl impl = new Impl();
				impl.setValue(point.getPointImpl());
				impl.setSort(point.getSort());
				if(points.get(point.getPointName()) != null)
				{
					points.get(point.getPointName()).getImpl().add(impl);
					continue;
				}else{
					Extension extension = new Extension();
					extension.setPoint(point.getPointName());
					List<Impl> impls = new ArrayList<Impl>();
					impls.add(impl);
					extension.setImpl(impls);
					points.put(point.getPointName(),extension);
					extensions.getExtension().add(extension);
				}
			}
			plugin.setExtensions(extensions);
			plugins.getPlugin().add(plugin);
			return plugins;
		}
		return null;
	}
	/**
	 * @param folderPath 站点路径;
	 * 加载所有配置文件;
	 * @throws Exception
	 */
	public static List<Site> loadConfigFiles(String folderPath) throws Exception{
		File siteFolder = new File(folderPath);
		if (!siteFolder.exists())
			throw new Exception("can not found Sites Folder -> " + siteFolder.getAbsolutePath());
		if (!siteFolder.isDirectory())
			throw new Exception("Sites -> " + siteFolder.getAbsolutePath() + " must be folder !");
		File[] files = siteFolder.listFiles();
		List<Site> sites = new ArrayList<Site>(files.length);
		for (File file : files){
			if (!file.exists())
				continue;
			if (!file.isFile())
				continue;
			if (!file.getName().endsWith(".xml"))
				continue;
			if(loadConfigFile(file) != null)
			sites.add(loadConfigFile(file));
		}
		return sites;
	}
	/**
	 * 加载所有配置文件;
	 * 配置文件中配置;
	 * @throws Exception
	 */
	public static List<Site> loadConfigFiles() throws Exception{
		
		return loadConfigFiles(Settings.website_xml_folder());
	}
	/**
	 * 加载单个配置文件;
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Site loadConfigFile(File file) throws Exception {
		if (!file.exists())
            return null;
        if (!file.isFile())
            return null;
        if (!file.getName().endsWith(".xml"))
            return null;
        XMLReader reader = BeanXMLUtil.getBeanXMLReader(file);
        reader.setBeanName("site");
        reader.setClass("site", Site.class);
        Site site = reader.readOne();
        if (site == null)
            throw new Exception("site xml file error -> " + file.getAbsolutePath());
        if (site.getId() == null || site.getId().trim().length() == 0)
			throw new Exception("site id required -> " + file.getAbsolutePath());
		if (site.getName() == null || site.getName().trim().length() == 0)
			throw new Exception("site name required -> " + file.getAbsolutePath());
		if (site.getTargets() == null || site.getTargets().getTarget().isEmpty())
			throw new Exception("site target required -> " + file.getAbsolutePath());
		List<Target> targets = site.getTargets().getTarget();
		if (targets == null || targets.isEmpty())
			throw new Exception("can not get any url target of site -> " + site.getName());
        return site;
	}
	
	public static void main(String[] args)throws Exception{
		File file = new File("d:/tianya_sample.xml");
		Site site = XmlConfigUtil.loadConfigFile(file);
		String siteJsonStr = JSON.toJSONString(site);
		System.out.println(siteJsonStr);
	}
}

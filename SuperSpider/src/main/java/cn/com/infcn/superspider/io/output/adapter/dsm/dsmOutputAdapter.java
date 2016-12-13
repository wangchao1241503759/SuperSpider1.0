package cn.com.infcn.superspider.io.output.adapter.dsm;

import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;

import com.alibaba.fastjson.JSONObject;

/*
 * dsm输出
 * 
 * @author WChao
 *
 */
public class dsmOutputAdapter extends OutputAdapter{
	
	/*protected static final Logger LOG = Logger.getLogger(dsmOutput.class);

	private ConcurrentLinkedQueue<JSONObject> queue = null;

	private int batchSize = 0;
	
	private IFCDSMDataSource dataSource;
	
	public IFCDSMService ifcdsmService;
	
	public IIFCDSMEngine dsmEngine;
	
	private String savetype;//1：保存文件到hdfs，2：保存结构化数据到mongo，3：保存文件和结构化数据

	public dsmOutput(JSONObject configuration, String jobName) {
		IFCDSMDataSource dataSource = new IFCDSMDataSource();
		dataSource.setDbname(configuration.getString("dbname"));
		dataSource.setTablename(configuration.getString("tableName"));
		dataSource.setDbtype(DBType.MONGO);
		dataSource.setHdfsip(configuration.getString("hdfsip"));
		dataSource.setHdfsport(configuration.getIntValue("hdfsport"));
		String hosts = configuration.getString("hosts");
		savetype = configuration.getString("savetype");
		IFCDSMHost host = null;
		Set<IFCDSMHost> hostset = new HashSet<IFCDSMHost>();
		for(String hoststr : hosts.split(",")){
			String[] hostarray = hoststr.split(":");
			host = new IFCDSMHost(hostarray[0], Integer.parseInt(hostarray[1]));
			hostset.add(host);
		}
		dataSource.setIFCDSMHosts(hostset);
		
		try {
			ifcdsmService = new IFCDSMService(dataSource);
			dsmEngine = ifcdsmService.getiIFCDSMEngine();
		} catch (IFCDSMException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(Doc doc){
		if (queue == null) {
			Map<String,Object> map = null;
			InputStream is = null;
			try {
				
				IFCDSMDataSource dataSource = new IFCDSMDataSource();
				//hdfs参数
				dataSource.setDbname("testfile2");
				dataSource.setTablename("file3");
				dataSource.setDbtype(DBType.MONGO);
				dataSource.setHdfsip("192.168.2.142");
				dataSource.setHdfsport(9000);
				String hosts = "192.168.2.142:27017,192.168.2.143:27017,192.168.2.144:27017,192.168.2.145:27017";
				IFCDSMHost host = null;
				Set<IFCDSMHost> hostset = new HashSet<IFCDSMHost>();
				for(String hoststr : hosts.split(",")){
					String[] hostarray = hoststr.split(":"); 
					host = new IFCDSMHost(hostarray[0], Integer.parseInt(hostarray[1]));
					hostset.add(host);
				}
				dataSource.setIFCDSMHosts(hostset);
				try {
					ifcdsmService = new IFCDSMService(dataSource);
					dsmEngine = ifcdsmService.getiIFCDSMEngine();
				} catch (IFCDSMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				if(doc instanceof FtpFile){
					LOG.debug("saving FtpFile to dsm.");
					FtpFile ftpFile = (FtpFile)doc;
					ftpFile.downloadToLocalTemporaryPath();
					
					map = new HashMap<String, Object>();
					map.put("fileName", ftpFile.getName());
					map.put("filePath", ftpFile.getFilePath());
					String fid = dsmEngine.add(map);
					
					File testFile = ftpFile.getLocalTemporaryFile();
					InputStream in = new BufferedInputStream(new FileInputStream(testFile));
					dsmEngine.upFile(in, fid, ftpFile.getName(), ftpFile.getSuffix());
					in.close();
					ftpFile.cleanTemporaryFile();
					LOG.debug("FtpFile saved to dsm.");
				} else if(doc instanceof BinaryFile){
					LOG.debug("save BinaryFile to dsm.");
					BinaryFile binaryFile = (BinaryFile)doc;
					String filePath = binaryFile.getFilePath();
					String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
					String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
					
					map = new HashMap<String, Object>();
					map.put("fileName", fileName);
					map.put("filePath", filePath);
					String fid = dsmEngine.add(map);
					
					File file = new File(filePath);
					dsmEngine.upFile(new FileInputStream(file), fid, fileName, suffix);
					LOG.debug("BinaryFile saved to dsm.");
				} 
				
				//存在要保存的结果信息
				if(doc.getResult() != null){
					LOG.debug("save doc.result to dsm.");
					dsmEngine.add(doc.getResult());
					LOG.debug("doc.result saved to dsm.");
				}
				
			} catch (IFCDSMException e) {
				LOG.error(e.getMessage(), e);
			} catch(Exception e){
				LOG.error(e.getMessage(), e);
			} finally{
				if(is != null)
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		} else {
			queue.add(doc.getResult());
			if (queue.size() >= batchSize) {
				flush();
			}
		}
		
	}

	@Override
	public synchronized void flush() {
		batchSave();
	}

	public synchronized void batchSave() {
		JSONObject job = null;
		if(queue != null){
			while ((job = queue.poll()) != null) {
				try {
					dsmEngine.add(job);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void shutdown() {
		flush();
	}

	public IFCDSMDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(IFCDSMDataSource dataSource) {
		this.dataSource = dataSource;
	}
*/
	public static void main(String[] args) {
		
		JSONObject conf = new JSONObject();
		conf.put("dbtype", "mongo");
		conf.put("dbname", "qingdao");
		conf.put("tableName", "qingdao");
		conf.put("hosts", "192.168.2.142:27017,192.168.2.143:27017,192.168.2.144:27017,192.168.2.145:27017");
		conf.put("hdfsip", "192.168.2.142");
		conf.put("hdfsport", "9000");

		/*dsmOutput output = new dsmOutput(conf, "test");
		FtpConfiguration configuration = new FtpConfiguration("192.168.2.106", 21, "infcn", "infcn", "/");
		FtpFile ftpFile = new FtpFile(configuration, "/Public/01_软件库/02_驱动程序/说明.txt");
		output.save(ftpFile);*/
	}

	@Override
	public void init(JSONObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(Doc doc) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown(boolean isNow) {
		// TODO Auto-generated method stub
		
	}
}

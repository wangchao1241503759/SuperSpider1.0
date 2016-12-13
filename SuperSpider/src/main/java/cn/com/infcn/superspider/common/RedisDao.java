package cn.com.infcn.superspider.common;

import java.io.IOException;

import org.redisson.Config;
import org.redisson.MasterSlaveServersConfig;
import org.redisson.Redisson;
import org.redisson.SentinelServersConfig;

public class RedisDao {

	private Redisson redisson;

	private String hosts;
	private String masterName;
	private int threads;

	public RedisDao(String host) throws IOException {
		redisson = createRedisson(host, 1, null);
	}

	public RedisDao(String host, int threads) throws IOException {
		redisson = createRedisson(host, threads, null);
	}

	public RedisDao(String host, int threads, String password) throws IOException {
		redisson = createRedisson(host, threads, password);
	}

	public RedisDao(String masterName, String sentinels) throws IOException {
		redisson = createSentinelRedisson(masterName, 1, null, sentinels);
	}

	public RedisDao(String masterName, String sentinels, int threads) throws IOException {
		redisson = createSentinelRedisson(masterName, threads, null, sentinels);
	}

	public RedisDao(String masterName, int threads, String password, String sentinels) throws IOException {
		redisson = createSentinelRedisson(masterName, threads, password, sentinels);
	}

	private Redisson createRedisson(String hosts, Integer threads, String password) {

		this.hosts = hosts;

		String[] splits = hosts.split("\\|");

		Config config = new Config();

		MasterSlaveServersConfig msConfig = config.useMasterSlaveConnection();

		msConfig.setMasterAddress(splits[0]);

		for (int i = 1; i < splits.length; i++) {
			msConfig.addSlaveAddress(splits[i]);
		}

		if (password != null) {
			msConfig.setPassword(password);
		}
		
		msConfig.setTimeout(60000) ;
		
		msConfig.setSlaveConnectionPoolSize(threads);
		msConfig.setMasterConnectionPoolSize(threads);
		return Redisson.create(config);
	}

	private Redisson createSentinelRedisson(String masterName, Integer threads, String password, String sentinels) {

		this.hosts = sentinels;
		this.masterName = masterName;
		this.threads = threads;

		Config config = new Config();

		SentinelServersConfig msConfig = config.useSentinelConnection();

		msConfig.setMasterName(masterName);
		msConfig.addSentinelAddress(sentinels.split("\\|"));
		if (!"".equals(password)) {
			msConfig.setPassword(password);
		}
		if (threads != null) {
			msConfig.setSlaveConnectionPoolSize(threads);
			msConfig.setMasterConnectionPoolSize(threads);
		}
		msConfig.setRetryInterval(1000);
		msConfig.setRetryAttempts(3);

		msConfig.setTimeout(60000) ;
		
		return Redisson.create(config);
	}

	/**
	 * 
	 * @return
	 */
	public Redisson getRedisson() {
		return redisson;
	}

	public String getHosts() {
		return hosts;
	}

	public int getThreads() {
		return threads;
	}

	public String getMasterName() {
		return masterName;
	}
}

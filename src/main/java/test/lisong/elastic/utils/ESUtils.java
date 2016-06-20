package test.lisong.elastic.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ESUtils {

	private static Client client;
	static {
		try {
			initClient("luyou_search", "localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @author 李嵩
	 * @param clusterName 集群名称
	 * @param hosts 集群节点 host:port,host2:port2 如果不提供port则使用9300
	 * @return
	 * @throws UnknownHostException 
	 * @date Jun 16, 2016
	 */
	public static void initClient(String clusterName, String hosts) throws UnknownHostException {
		Settings settings = Settings.settingsBuilder()
		        .put("cluster.name", clusterName).build();
		TransportClient _client = TransportClient.builder().settings(settings).build();
		String[] _hosts = hosts.split(",");
		for (String host : _hosts) {
			if (hosts != null) {
				String[] _host = host.split(":");
				String h = _host[0];
				Integer p = null;
				if (_host.length == 1) {
					p = 9300;
				} else {
					p = Integer.parseInt(_host[1]);
				}
				_client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(h), p));
			}
		}
		client = _client;
	}
	
	public static Client getClient() {
		return client;
	}
}

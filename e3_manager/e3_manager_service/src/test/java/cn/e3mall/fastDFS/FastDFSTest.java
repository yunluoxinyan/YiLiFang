package cn.e3mall.fastDFS;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {

	@Test
	public void uploadImage() throws Exception {
		ClientGlobal.init("D:/JavaEEworkspace/e3_manager/e3_manager_service/src/main/resources/conf/client.conf");
		TrackerClient trackerClient = new TrackerClient();
		TrackerServer trackerServer = trackerClient.getConnection();
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		String[] strings = storageClient.upload_file("D:/che.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
}

package de.uni_stuttgart.test.test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hyperic.sigar.SigarException;
import org.junit.Ignore;
import org.junit.Test;

import de.uni_stuttgart.monitoring.AverageLatencyProbe;
import de.uni_stuttgart.monitoring.FreeDiskSpaceProbe;
import de.uni_stuttgart.monitoring.IOLoadProbe;
import de.uni_stuttgart.monitoring.NFSStatusProbe;
import de.uni_stuttgart.monitoring.NetworkStateProbe;

public class SigarTest {
	String filePath = "/tmp/foo.txt";
	String fsRoot = "/";
	Properties properties;
	public SigarTest() throws IOException{
		properties = new Properties();
		String propFileName = "config.properties";
		FileInputStream in = new FileInputStream(propFileName);
		properties.load(in);
	}

	@Ignore
	@Test
	public void testFreeSpace() throws SigarException
	{
		FreeDiskSpaceProbe diskprobe = new FreeDiskSpaceProbe();
		diskprobe.getFreeDiskSpace(properties.getProperty("fs_root"));
	}
	@Ignore
	@Test
	public void testNetworkState() throws IOException, SigarException, InterruptedException{
		double averageRxRate;
		double averageTxRate;
		NetworkStateProbe networkState = new NetworkStateProbe();
		averageRxRate = networkState.getAverageRxRate();
		averageTxRate = networkState.getAverageTxRate();
		System.out.println("Average receive rate is "+averageRxRate/1024 + " kBytes/sec");
		System.out.println("Average transmit rate is "+averageTxRate/1024 + " kBytes/sec");
	}
	@Ignore
	@Test
	public void testAverageNumberOfReads() throws SigarException, InterruptedException
	{
		IOLoadProbe ioMonitor = new IOLoadProbe(properties.getProperty("fs_root"));
		ioMonitor.start();
		// measure read write requests during 30 seconds
		Thread.sleep(30000);
	}
	@Ignore
	@Test
	public void testAverageLatency() throws IOException{
		String ip = "google.com";
		int port = 80;
		AverageLatencyProbe latency = new AverageLatencyProbe();
		System.out.println("Latency to "+ip+": "+ latency.getAverageLatence(ip, port) + "ms");
	}
	@Ignore
	@Test
	public void testUsedBandwidth() throws SigarException, InterruptedException{
		NetworkStateProbe networkState = new NetworkStateProbe();
		System.out.println("Average used download bandwidth is "+networkState.getAverageUsedDownloadBandwidth(100) +"%");
		System.out.println("Average used upload bandwidth is "+networkState.getAverageUsedUploadBandwidth(100) +"%");
	}
	@Test
	public void testNFSStatus() throws SigarException, IOException {
		NFSStatusProbe nfsStatus = new NFSStatusProbe();
		System.out.println(nfsStatus.isNFSV3Available(properties.getProperty("nfs_mount_point")));
	}
	
	
}

///**
// * Write file to the disk asynchronously
// * @author raskin
// *
// */
//class Writer extends Thread{
//	
//	String filePath;
//	public Writer(String filePath){
//		this.filePath = filePath;
//	}
//	@Override
//	public void run(){
//		
//		try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		File file = new File(filePath);
//		try {
//			file.createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter(filePath,"UTF-8");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		writer.println("some random string");
//		writer.close();
//		System.out.println("File was written");
//	}
//	
//}
///**
// * Read file from the disk asynchronously
// * @author raskin
// *
// */
//class Reader extends Thread{
//	
//	String filePath;
//	public Reader(String filePath){
//		this.filePath = filePath;
//	}
//	@Override
//	public void run(){
//		int bytes = 0;
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		File file = new File(filePath);
//		BufferedReader reader = null;
//		try {
//			reader =new BufferedReader(new FileReader(file));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			bytes = reader.read();
//			System.out.println(reader.readLine());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("Number of bytes read:" + bytes);
//		//file.deleteOnExit();
//		//System.out.println("File deleted");
//	}
//}

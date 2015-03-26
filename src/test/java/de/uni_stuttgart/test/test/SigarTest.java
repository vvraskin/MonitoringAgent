package de.uni_stuttgart.test.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.hyperic.sigar.SigarException;
import org.junit.Ignore;
import org.junit.Test;

import de.uni_stuttgart.monitoring.AverageLatencyProbe;
import de.uni_stuttgart.monitoring.FreeDiskSpaceProbe;
import de.uni_stuttgart.monitoring.IOLoadProbe;
import de.uni_stuttgart.monitoring.NFSStatusProbe;
import de.uni_stuttgart.monitoring.NetworkStateProbe;
import de.uni_stuttgart.test.test.SigarTest.Queue;

public class SigarTest {
	Properties properties;
	protected static Queue queue;
	public SigarTest() throws IOException{
		properties = new Properties();
		String propFileName = "config.properties";
		FileInputStream in = new FileInputStream(propFileName);
		properties.load(in);
		queue = new Queue(properties.getProperty("io_test_directory")+"foo.txt");
	}

	//@Ignore
	@Test
	public void testFreeSpace() throws SigarException
	{
		FreeDiskSpaceProbe diskprobe = new FreeDiskSpaceProbe();
		System.out.println("Free disk space is: "+diskprobe.getFreeDiskSpace(properties.getProperty("fs_root")));
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
	//@Ignore
	@Test
	public void testAverageNumberOfReads() throws SigarException, InterruptedException
	{
		IOLoadProbe ioMonitor = new IOLoadProbe(properties.getProperty("fs_root"),Integer.valueOf(properties.getProperty("big_cycle")));
		MyReader reader = new MyReader(queue);
		MyWriter writer = new MyWriter(queue);
		ioMonitor.start();
		reader.start();
		writer.start();
		// measure read write requests during 30 seconds
		Thread.sleep(30000);
	}
	//@Ignore
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
	@Ignore
	@Test
	public void testNFSStatus() throws SigarException, IOException {
		NFSStatusProbe nfsStatus = new NFSStatusProbe();
		System.out.println(nfsStatus.isNFSV3Available(properties.getProperty("nfs_mount_point")));
	}
	/**
	 * Object of this class is needed for synchronization of reader/writer threads
	 * @author raskin
	 *
	 */
	class Queue{
		private int size = 0; 
		String fileName;
		public  Queue(String fileName){
			this.fileName = fileName;
		}
		public synchronized void write() throws InterruptedException{
			while(this.size > 0){
				wait();
			}
			// write file on disk
			this.writeOnDisk(fileName);
			size++;
			notifyAll();
		}
		public synchronized void read() throws InterruptedException, IOException{
			while(this.size==0){
				wait();
			}
			// read file from disk and delete
			this.readFromDiskAndDelete(fileName);
			size--;
			notifyAll();
		}
		public String getFilePath(){
			return this.fileName;
		}
		private void writeOnDisk(String filePath){
			File file = new File(filePath);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(filePath,"UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			writer.println("some random string");
			writer.close();
			System.out.println("File was written");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void readFromDiskAndDelete(String filePath) throws IOException{
			int bytes = 0;
			File file = new File(filePath);
			BufferedReader reader = null;
			try {
				reader =new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				
				bytes = reader.read();
				System.out.println(reader.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Number of bytes read:" + bytes);
			file.deleteOnExit();
			System.out.println("File deleted");
		}
		}
	}
	/**
	 * Write file to the disk asynchronously
	 * @author raskin
	 *
	 */
	class MyWriter extends Thread{
		

		public MyWriter(Queue queue){
		}
		@Override
		public void run(){
			while(true){
				try {
					SigarTest.queue.write();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
	}
	}
	/**
	 * Read file from the disk asynchronously
	 * @author raskin
	 *
	 */
	class MyReader extends Thread{
		
		public MyReader(Queue queue){
		}
		@Override
		public void run(){
			while(true){
				try {
					SigarTest.queue.read();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
	}
}
	
	

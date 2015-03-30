**Monitoring Agent- An [HLRS](https://www.hlrs.de/) monitoring service**
------------------------------------------------
The following agent is intended to monitor specific network metrics. The monitoring agent applies to both Windows and Linux machines. The code is developed in Java and use certain libraries like [Sigar](https://github.com/hyperic/sigar)$

Supported network metrics:


----------


 **Network Latency** ( miliseconds )

 - Calculates the amount of time for a package to reach its destination.
 - You can define the ip address, port number and the number of times you want to send the packets, in order to  calculate the latency.
 - config.properties values: PING_IP, PING_PORT, PING_LOOP
**Free Disk Space**( Mb )

 - Number of unallocated Mb on the host

**Average rate of transmitted/received bytes** ( kBytes/sec )
 

 - Avg. of packages transmitted or received per second.

**I/O load**

 - Number of reads & writes in Disk.
 
**Bandwidth** ( Mbit / sec )
 

 - Average rate of transmitted bytes / channel width

**NFS connection status** 
 - Check if mount point exist or not. Support versions 2 & 3. 


----------


**Getting started**

The code runs one Junit test for every available metric. These tests are declared inside the SigarTest.java at the test folder.
For any details regarding the Junit tests structure, check the SigarTest class.

**Requirements**

 - JDK 1.4 or higher
 - Eclipse Luna

**Libraries**

 - Sigar(Inside the lib folder)
 - Log4j(Inside the lib folder)


**Installation**

    git clone https://github.com/vvraskin/MonitoringAgent.git

**Eclipse Configuration**

 - Import the Maven project into your Eclipse workspace.
 - Add missing libraries to the project build path(log4j & sigar). Available in the lib folder of the project.
 - Add Junit to the build path. So you can execute the Junit tests.

**Bugs and Issues**

 - If Junit tests are run on Windows  hosts, please be sure to convert the properties file to dos compatible format, e.g. with unix2dos utility. Otherwise,  the file will not be read correctly.
 - For the NFS test, ensure to modify your mount point at the properties file.

**Communication**

Email: Pavel Skvortsov[skvortsov@hlrs.de] or Vadim Raskin[raskinvadim@gmail.com] 




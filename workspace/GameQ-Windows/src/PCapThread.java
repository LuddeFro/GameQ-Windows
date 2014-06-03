import java.util.Date;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JFlowMap;
import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JHeaderPool;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;


public class PCapThread implements Runnable {

	private PcapIf device;
	private StringBuilder errbuf;
	//private static final String filterString = "ip";
	private static final String filterString = "udp dst portrange 11235-11335 or tcp dst port 11031 or udp src portrange 27015-27030 or udp dst port 27005";
	public static int honQPack;
	public static int dotaQPack;
	public static int dotaCPack;
	public static int dota206Pack;
	public static int dota190Pack;
	public static int dota174Pack;
	public static int csgoGamePack;
	public static int csgoQPack;
	
	
	public PCapThread(PcapIf device, StringBuilder errbuf) {
		this.errbuf = errbuf;
		this.device = device;
	}
	
	public void run() {
		openStreams(device, errbuf);
	}
	
	private void openStreams(PcapIf device, StringBuilder errbuf) {
		int snaplen = 64 * 1024;           // Capture all packets, no trucation  
        int flags = Pcap.MODE_NON_PROMISCUOUS; // capture all packets  
        int timeout = 10 * 1000;           // 10 seconds in millis  
        Pcap pcap =  
            Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);  
  
        
        
        if (pcap == null) {  
            System.err.printf("Error while opening device for capture: "  
                + errbuf.toString());  
            return;  
        }  
        
          
        PcapBpfProgram filter = new PcapBpfProgram();
        String expression = filterString;
        int optimize = 0; // 1 means true, 0 means false
        int netmask = 0;
        
        int r = pcap.compile(filter, expression, optimize, netmask);
        if (r != Pcap.OK) {
          System.out.println("Filter error: " + pcap.getErr());
        }
        pcap.setFilter(filter);
        
        
        PcapPacketHandler<String> handler = new PcapPacketHandler<String>() {  
        	  
            public void nextPacket(PcapPacket packet, String user) {  
            	//System.out.println(packet.toString());
            	String packetString = packet.toString();
            	
            	
            	
            	
            	int i = packetString.indexOf("destination");
            	int dstPort = 0;
            	while(i >= 0) {
            	     i = packetString.indexOf("destination", i+1);
            	     String a = packetString.substring(i+14, i+19);
            	     if (a.contains(".")) {
            	    	 
            	     } else {
            	    	 a = a.replaceAll("[^0-9.]", "");
            	    	 if (!a.isEmpty()) {
            	    		 dstPort = Integer.parseInt(a);
            	    	 }
            	     }
            	}
            	System.out.println("DSTPORT: " + dstPort);
            	
            	i = packetString.indexOf("source");
            	int srcPort = 0;
            	while(i >= 0) {
            	     i = packetString.indexOf("source", i+1);
            	     String a = packetString.substring(i+9, i+14);
            	     if (a.contains(".")) {
            	    	 
            	     } else {
            	    	 a = a.replaceAll("[^0-9.]", "");
            	    	 if (!a.isEmpty()) {
            	    		 srcPort = Integer.parseInt(a);
            	    	 }
            	     }
            	}
            	System.out.println("SRCPORT: " + srcPort);
            	
            	
            	
            	
            	
            	
                JHeader last = getStaticLastHeader(packet); // static method call  
                System.out.printf("name=%s, descr=%s%n", last.getName(), last.getDescription());
                //name === protocol
                
                int len = packet.getCaptureHeader().wirelen();
                String protocol = last.getName();
                System.out.println(packet.getCaptureHeader());
                
                
                System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s sPort="+srcPort+" dPort="+dstPort+" protocol:"+protocol+"\n",  
                    new Date(packet.getCaptureHeader().timestampInMillis()),   
                    packet.getCaptureHeader().caplen(),  // Length actually captured
                    packet.getCaptureHeader().wirelen(), // Original length 
                    user                                 // User supplied object  
                    );  
                
                
                if (dstPort <= 11335 && dstPort >= 11235) {
                    PCapThread.honQPack++;
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && len <= 750 && len >= 600) {
                    //checks wirelength 600-750
                    
                    PCapThread.dotaQPack++;
                    
                }
                
                if (dstPort == 27005) {
                	PCapThread.dotaCPack++;
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && len == 174) {
                   
                    
                    PCapThread.dota174Pack++;
                    
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && len == 190) {
                    
                    if (PCapThread.dota174Pack > 0 || Main.timeHandler.dota174Buffer.bufferValue() > 0) {
                    	PCapThread.dota190Pack++;
                    	
                    }
                    
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && len == 206) {
                   
                    if ((PCapThread.dota174Pack > 0 || Main.timeHandler.dota174Buffer.bufferValue() > 0) && (PCapThread.dota190Pack > 0 || Main.timeHandler.dota190Buffer.bufferValue() > 0)) {
                        PCapThread.dota206Pack++;
                    	
                    }
                    
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && dstPort == 27005 && len == 60) {
                    
                    
                    PCapThread.csgoQPack++;
                    
                }
                
                if (srcPort >= 27015 && srcPort <= 27020 && dstPort == 27005 && len >= 100 && len <= 1200) {
                    
                    
                    PCapThread.csgoGamePack++;
                    
                }
                
                
                
            }  
        }; 
     
        int cnt = 0; // Capture packet count
        String out = "packet caught"; // Our custom object to send into the handler
        System.out.println("stream opening");
        pcap.loop(cnt, handler, out); // Each packet will be dispatched to the handler
        System.out.println("stream closing");
        pcap.close();
        System.out.println("stream closed");
        
	}
	
	public static JHeader getStaticLastHeader(JPacket packet) {  
	    return getStaticLastHeader(packet, false);   
	}  
	  
	public static JHeader getStaticLastHeader(JPacket packet, boolean payloadOk) {  
	    int last = packet.getHeaderCount() - 1;  
	  
	    if (!payloadOk && packet.getHeaderIdByIndex(last) == Payload.ID  
	        && last > 0) {  
	        last--; // We want the last header before payload  
	    }  
	  
	    final JHeader header =  
	        JHeaderPool.getDefault().getHeader(packet.getHeaderIdByIndex(last));  
	    packet.getHeaderByIndex(last, header);  
	  
	    return header;  
	}  
	
}

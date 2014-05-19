import java.util.Date;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JHeaderPool;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.Payload;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;


public class PCapThread implements Runnable {

	private PcapIf device;
	private StringBuilder errbuf;
	private static final String filterString = "ip";
	//private static final String filterString = "udp dst portrange 11235-11335 or tcp dst port 11031 or udp src portrange 27015-27030 or udp dst port 27005";
	
	
	
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
            	
            	/*if (packet.hasHeader(ip) == false){  
                    return;  
                }  
                if (packet.hasHeader(tcp)== false) {  
                    return;  
                } */
                JHeader last = getStaticLastHeader(packet); // static method call  
                System.out.printf("name=%s, descr=%s%n", last.getName(), last.getDescription());
                //name === protocol
                int i = 0;
                int sPort = 0;
                int dPort = 0;
                int len = packet.getCaptureHeader().wirelen();
                String protocol = last.getName();
                
                
                
                System.out.printf("Received packet at %s caplen=%-4d len=%-4d %s sPort="+sPort+" dPort="+dPort+" protocol:"+protocol+"\n",  
                    new Date(packet.getCaptureHeader().timestampInMillis()),   
                    packet.getCaptureHeader().caplen(),  // Length actually captured
                    packet.getCaptureHeader().wirelen(), // Original length 
                    user                                 // User supplied object  
                    );  
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

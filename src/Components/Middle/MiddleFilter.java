package Components.Middle;

import java.io.IOException;
import Framework.CommonFilterImpl;

public class MiddleFilter extends CommonFilterImpl{
    @Override
    public boolean specificComputationForFilter() throws IOException {
        int numOfBlank = 0;
        int idx = 0;
        byte[] buffer = new byte[64];
        boolean notCS = false;    
        int byte_read = 0;
        int startIdx = 0;
        
        while(true) {
            while(byte_read != '\n' && byte_read != -1) {
            	byte_read = in.read();
                if(byte_read == ' ') numOfBlank++;
                if(byte_read != -1) buffer[idx++] = (byte)byte_read;
               
                if(numOfBlank == 3) {
                	startIdx = idx;
                }
            }
            if(buffer[startIdx-2] != 'C' && buffer[startIdx-1] != 'S') {
            	notCS = true;
            }
            
            if(notCS) {
                for(int i = 0; i<idx; i++) 
                	out.write((char)buffer[i]);
                notCS = false;
            }
            if (byte_read == -1) return true;
            idx = 0;
            numOfBlank = 0;
            byte_read = '\0';
        }
    }  
}

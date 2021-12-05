package Components.AddFilter;

import java.io.IOException;

import Framework.CommonFilterImpl;

public class AddFilter extends CommonFilterImpl {

	@Override
	public boolean specificComputationForFilter() throws IOException {

		int numOfBlank = 0;
		int idx = 0;
		byte[] buffer = new byte[64];
		int byte_read = 0;
		int startIdx = 0;
		boolean course12345 = false;
		boolean course23456 = false;

		while (true) {
			while (byte_read != '\n' && byte_read != -1) {
				byte_read = in.read();
				if (byte_read == ' ')
					numOfBlank++;
				if (byte_read != -1) {
					buffer[idx++] = (byte) byte_read;
				}
				if (numOfBlank == 4) {
					//여기서 startIdx는 CS, EE와 같은 부서 다음을 의미
					startIdx = idx;
				}
			}
			//반복문을 통해 12345, 23456이 있는지 확인
			for (int i = startIdx - 5; i < idx; i = i + 6) {
				if ((char) buffer[i] == '1' && (char) buffer[i + 1] == '2' && (char) buffer[i + 2] == '3' && (char) buffer[i + 3] == '4' && (char) buffer[i + 4] == '5')
					course12345 = true;

				if((char) buffer[i] == '2' && (char) buffer[i + 1] == '3' && (char) buffer[i + 2] == '4' && (char) buffer[i + 3] == '5' && (char) buffer[i + 4] == '6')
					course23456 = true;
			}
			
			
			if (idx != 0) {
				idx=idx-2;
				//12345, 23456을 추가해주는 장소
				if (!course12345) {
					buffer[idx++] = ((byte) (' '));
					buffer[idx++] = ((byte) ('1'));
					buffer[idx++] = ((byte) ('2'));
					buffer[idx++] = ((byte) ('3'));
					buffer[idx++] = ((byte) ('4'));
					buffer[idx++] = ((byte) ('5'));
				}
				if (!course23456) {
					buffer[idx++] = ((byte) (' '));
					buffer[idx++] = ((byte) ('2'));
					buffer[idx++] = ((byte) ('3'));
					buffer[idx++] = ((byte) ('4'));
					buffer[idx++] = ((byte) ('5'));
					buffer[idx++] = ((byte) ('6'));
				}
				buffer[idx++] = ((byte)('\n'));
				course12345 = false;
				course23456 = false;
			}
			for (int i = 0; i < idx; i++) {
				out.write((char) buffer[i]);
			}
			if (byte_read == -1)
				return true;
			idx = 0;
			numOfBlank = 0;
			byte_read = '\0';

		}
	}
}
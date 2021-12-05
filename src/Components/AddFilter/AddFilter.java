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
		int startidx = 0;
		boolean remove17651 = false;
		boolean remove17652 = false;
		int check17651Idx = 0;
		int check17652Idx = 0;

		while (true) {
			while (byte_read != '\n' && byte_read != -1) {
				byte_read = in.read();
				if (byte_read == ' ')
					numOfBlank++;
				if (byte_read != -1) {
					buffer[idx++] = (byte) byte_read;
				}
				if (numOfBlank == 4) {
					startidx = idx;
				}
			}
			//17651, 17652가 있는지 확인 후 있다면 인덱스 저장
			for (int i = startidx - 5; i < idx; i = i + 6) {
				if ((char) buffer[i] == '1' && (char) buffer[i + 1] == '7' && (char) buffer[i + 2] == '6' && (char) buffer[i + 3] == '5' && (char) buffer[i + 4] == '1') {
					check17651Idx = i;
					remove17651 = true;
				}
				if ((char) buffer[i] == '1' && (char) buffer[i + 1] == '7' && (char) buffer[i + 2] == '6' && (char) buffer[i + 3] == '5' && (char) buffer[i + 4] == '2') {
					check17652Idx = i;
					remove17652 = true;
				}
			}

			if (idx != 0) {
				if (remove17651) {
					for (int i = check17651Idx; i < idx - 6; i++) {
						buffer[i] = buffer[i + 6];
					}
					idx = idx-6;
				}
				if (remove17652) {
					for (int i = check17652Idx; i < idx - 6; i++) {
						buffer[i] = buffer[i + 6];
					}
					idx = idx-6;
				}
				remove17651 = false;
				remove17652 = false;
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
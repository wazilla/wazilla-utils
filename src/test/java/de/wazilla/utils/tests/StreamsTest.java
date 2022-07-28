package de.wazilla.utils.tests;

import de.wazilla.utils.Streams;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamsTest {

	@Test
	void close_NullGiven_ShouldNotThrowException() {
		Streams.close(null);
	}
	
	@Test
	void close_StreamWhichThrowsExceptionOnClose_ShouldNotThrowEx() {
		TestInputStream stream = new TestInputStream();
		stream.setThrowExOnClose(true);
		Streams.close(stream);
	}

	@Test
	void close_StreamGiven_ShouldCloseStream() {
		TestInputStream stream = new TestInputStream();
		Streams.close(stream);
		assertTrue(stream.isClosed());
	}

	@Test
	void read_StreamGiven_ShouldReturnContentAsBytes() throws Exception {
		byte[] data = "Test".getBytes();
		TestInputStream stream = new TestInputStream();
		stream.setData(data);
		byte[] read = Streams.read(stream);
		assertArrayEquals(data, read);
	}
	
	private static class TestInputStream extends InputStream {

		private int pos;
		private byte[] data;
		private boolean closed;
		private boolean throwExOnClose;
		
		@Override
		public int read() throws IOException {
			if (data == null || pos >= data.length) {
				return -1;
			} else {
				return data[pos++];
			}
		}
		
		@Override
		public void close() throws IOException {
			this.closed = true;
			if (throwExOnClose) throw new IOException();
		}
		
		public void setData(byte[] data) {
			this.data = data;
			this.pos = 0;
		}

		public void setThrowExOnClose(boolean throwExOnClose) {
			this.throwExOnClose = throwExOnClose;
		}
		
		public boolean isClosed() {
			return closed;
		}
		
	}
	
}

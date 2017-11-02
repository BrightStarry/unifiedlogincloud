package com.zuma.logstoragenetty;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.zuma.dto.LogMessage;
import lombok.Cleanup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogStorageApplicationTests {

	/**
	 * kryo序列化测试
	 */
	@Test
	public void test() {
		Kryo kryo = new Kryo();

		LogMessage logMessage = LogMessage.builder()
				.content("aaa")
				.build();


		System.out.println(logMessage);

		@Cleanup
		ByteOutputStream byteOutputStream = new ByteOutputStream();
		@Cleanup
		Output output = new Output(byteOutputStream);
		kryo.writeObject(output, logMessage);
		output.close();
		byte[] buf = byteOutputStream.getBytes();
		System.out.println(buf.length);


		ByteInputStream byteInputStream = new ByteInputStream(buf,buf.length);
		@Cleanup
		Input input = new Input(byteInputStream);
		LogMessage result = kryo.readObject(input, LogMessage.class);
		System.out.println(result);
	}

}

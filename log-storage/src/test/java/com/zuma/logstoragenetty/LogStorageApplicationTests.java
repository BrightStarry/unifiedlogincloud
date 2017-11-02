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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
				.channel(9999)
				.content("aaaa")
				.date(new Date())
				.moduleName("bbbb")
				.build();
		System.out.println(logMessage);

		@Cleanup
		Output output = new Output(9999,99999);
		kryo.writeObject(output, logMessage);
		output.close();
		byte[] buf = output.toBytes();
		System.out.println(buf.length);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
		@Cleanup
		Input input = new Input(inputStream);
		LogMessage result = kryo.readObject(input, LogMessage.class);
		System.out.println(result);
	}

}

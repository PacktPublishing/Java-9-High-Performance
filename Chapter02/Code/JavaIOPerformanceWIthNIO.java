import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class JavaIOPerformanceWIthNIO {
	public static void main(String args[]) {

		JavaIOPerformanceWIthNIO iotest = new JavaIOPerformanceWIthNIO();

		try {
			FileOutputStream foStream = new FileOutputStream("/Users/MayurRamgir/fileoutputstream.txt");
			iotest.write(foStream);

			BufferedOutputStream boStream = new BufferedOutputStream(
					new FileOutputStream("/Users/MayurRamgir/bufferedoutputstream.txt"));
			iotest.write(boStream);

			Path nioFilePath = Paths.get("/Users/MayurRamgir/niotest.txt");
			OutputStream nioStream = Files.newOutputStream(nioFilePath);
			iotest.write(nioStream);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void write(OutputStream os) {

		long startTime = Instant.now().toEpochMilli();

		try {
			for (int i = 0; i < 99999999; i++) {
				os.write(1);
			}
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endTime = Instant.now().toEpochMilli();

		long totalTimeSpent = endTime - startTime;

		System.out.format("Program took %02d min, %02d sec, %02d millisec",
				TimeUnit.MILLISECONDS.toMinutes(totalTimeSpent),
				TimeUnit.MILLISECONDS.toSeconds(totalTimeSpent)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTimeSpent)),
				TimeUnit.MILLISECONDS.toMillis(totalTimeSpent)

		);
		System.out.println(" for " + os.toString());
	}
}

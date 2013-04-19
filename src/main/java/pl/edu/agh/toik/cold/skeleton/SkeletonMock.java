package pl.edu.agh.toik.cold.skeleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SkeletonMock {

	
	public static void main(String[] args) {
		File file = new File(args[0] + ".log");
		try {
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			while (true) {
				writer.write("Skeleton is listening on port : " + args[0] + "\n");
				writer.flush();
				Thread.sleep(5000);

			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
}

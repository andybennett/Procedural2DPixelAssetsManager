package ajb.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ajb.domain.Asset;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtils {

	public static void outputAsset(Asset asset) {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		String xml = xstream.toXML(asset);
		writeStringToFile(asset.getUuid() + ".xml", xml);
	}

	public static void writeStringToFile(String filename, String textToWriteToFile) {
		try {
			File file = new File(filename);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(textToWriteToFile);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package preprocessor;


import java.io.File;
import java.io.FileReader;

import org.apache.tools.ant.BuildException;
import de.enough.polish.preprocess.CustomPreprocessor;
import de.enough.polish.resources.Translation;
import de.enough.polish.resources.TranslationManager;
import de.enough.polish.util.StringList;

public class ResourceLocalization extends CustomPreprocessor {

	public ResourceLocalization() {
		super();
	}

	public void setCards(File file) {
		String line = "";
		int c;
		FileReader fr = new FileReader(file);
		TranslationManager tm = new Tr
		while ( c != -1 ) {
			while ( line.endsWith(";") ) {
				c = fr.read();
				line += (char) c;
			}
			int localeStart = line.indexOf( "Locale.get(\"");
			int localeEnd = line.indexOf(")");
			String argument = line.substring( localeStart + "Locale.get(\"".length() + 1, localeEnd - 1).trim();
			System.out.println("String argument:" + argument);
				
			if (replacePos == -1) {
				throw new BuildException(className + " at line " 
						+ (lines.getCurrentIndex() + 1) 
						+ ": Unable to process #date-directive: found no ${date} sequence in line [" 
						+ line + "." );
			}
		String result = argument.substring(0, replacePos )
		+ ( new Date().toString() ) 
		+ argument.substring( replacePos + "${date}".length() );
		lines.setCurrent(result);
	}
}

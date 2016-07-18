package generator;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

public interface Generator {

    public void generateFile() throws IOException, TemplateException;

    public void generateFile(Writer out) throws IOException, TemplateException;

    public void generateFile(String filePath) throws IOException, TemplateException;

}

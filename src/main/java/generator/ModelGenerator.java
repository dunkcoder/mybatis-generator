package generator;

import freemarker.FMTemplateFactory;
import model.GenTables;
import model.GenColumns;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import util.SystemInfo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelGenerator implements Generator {

//    private static Logger logger = LoggerFactory.getLogger(ModelGenerator.class);

    private GenTables genTables = null;

    public ModelGenerator(GenTables genTables){
        this.genTables = genTables;
    }

    public String getPackage(){
        return genTables.getModelPackage();
    }

    /**
     * 获取到需要import的class的字符串
     *
     * @return
     */
    public List<String> getImportList(){
        List<GenColumns> genColumnsList = genTables.getGenColumnsList();

        if(genColumnsList != null && genColumnsList.size() > 0){
            List<String> importList = new ArrayList<String>();

            String dateStr = null;
            String bigDecimalStr = null;
            for(GenColumns genColumns : genColumnsList){
                if("Date".equals(genColumns.getPropertyType()) && dateStr == null){
                    dateStr = "java.util.Date";
                    importList.add(dateStr);
                }else if("BigDecimal".equals(genColumns.getPropertyType()) && bigDecimalStr == null){
                    bigDecimalStr = "java.math.BigDecimal";
                    importList.add(bigDecimalStr);
                }
            }

            return importList;
        }

        return null;
    }

    public String getClassName(){
        return genTables.getClassName();
    }

    public List<GenColumns> getPropertyList(){
        return genTables.getGenColumnsList();
    }

    public void generateFile(Writer out) throws IOException, TemplateException {
        Template temp = FMTemplateFactory.getTemplate("model.ftl");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("package", getPackage());
        root.put("importList", getImportList());
        root.put("className", getClassName());
        root.put("tableComment", genTables.getTableComment());
        root.put("propertyList", getPropertyList());

        root.put("sysUsername", SystemInfo.getUsername());

        temp.process(root, out);
    }

    public void generateFile(String filePath) throws IOException, TemplateException {
        Writer out = new OutputStreamWriter(FileUtils.openOutputStream(new File(filePath)));
        generateFile(out);
        out.flush();
    }

    public void generateFile() throws IOException, TemplateException {
        generateFile(genTables.getModelPath());
    }

    public GenTables getGenTables() {
        return genTables;
    }

    public void setGenTables(GenTables genTables) {
        this.genTables = genTables;
    }

}

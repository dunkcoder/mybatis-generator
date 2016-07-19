package com.nozturn.mbg.generator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.nozturn.mbg.freemarker.FMTemplateFactory;
import com.nozturn.mbg.model.GenColumns;
import com.nozturn.mbg.model.GenTables;
import com.nozturn.mbg.util.SystemInfo;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MapperGenerator implements Generator {

//    private static Logger logger = LoggerFactory.getLogger(MapperGenerator.class);

    private GenTables genTables = null;

    public MapperGenerator(GenTables genTables){
        this.genTables = genTables;
    }

    private String getModelPath(){
        return genTables.getModelPackage() + "." + genTables.getClassName();
    }

    private String getPrimaryKeyType(){

        int primaryKeyCount = 0;
        GenColumns property = null;
        for(GenColumns genColumns : genTables.getGenColumnsList()){
            if(genColumns.getIsPrimaryKey() != null && genColumns.getIsPrimaryKey()){
                property = genColumns;
                primaryKeyCount++;
            }
        }

        if(primaryKeyCount > 1){
            return "Map";
        }

        return property.getPropertyType();
    }

    public void generateFile(Writer out) throws IOException, TemplateException {
        Template temp = FMTemplateFactory.getTemplate("mapper.ftl");

        Map<String, String> root = new HashMap<String, String>();
        root.put("package", genTables.getMapperPackage());
        root.put("modelPath", getModelPath());
        root.put("className", genTables.getClassName());
        root.put("tableName", genTables.getTableName());
        root.put("tableComment", genTables.getTableComment());
        root.put("primaryKeyType", getPrimaryKeyType());

        root.put("sysUsername", SystemInfo.getUsername());

        temp.process(root, out);
        out.flush();
    }

    public void generateFile(String filePath) throws IOException, TemplateException {
        Writer out = new OutputStreamWriter(FileUtils.openOutputStream(new File(filePath)));
        generateFile(out);
        out.flush();
    }

    public void generateFile() throws IOException, TemplateException {
        generateFile(genTables.getMapperPath());
    }

    public GenTables getGenTables() {
        return genTables;
    }

    public void setGenTables(GenTables genTables) {
        this.genTables = genTables;
    }
}

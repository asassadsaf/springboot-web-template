package com.fkp.template;

import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.ASTNodeAccess;
import org.apache.commons.io.FileUtils;
import org.apache.shardingsphere.sql.parser.api.ASTNode;
import org.apache.shardingsphere.sql.parser.api.SQLStatementVisitorEngine;
import org.apache.shardingsphere.sql.parser.api.parser.SQLParser;
import org.apache.shardingsphere.sql.parser.core.ParseASTNode;
import org.apache.shardingsphere.sql.parser.core.SQLParserFactory;
import org.apache.shardingsphere.sql.parser.opengauss.parser.OpenGaussLexer;
import org.apache.shardingsphere.sql.parser.opengauss.parser.OpenGaussParser;
import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.column.ColumnDefinitionSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.DataTypeLengthSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.DataTypeSegment;
import org.apache.shardingsphere.sql.parser.sql.common.segment.generic.table.TableNameSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.opengauss.ddl.OpenGaussCreateTableStatement;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author fengkunpeng
 * @version 1.0
 * @description
 * @date 2024/12/9 22:01
 */
public class SqlConvertTest {

    @Test
    @SneakyThrows
    void test(){
        SQLParser sqlParser = SQLParserFactory.newInstance(FileUtils.readFileToString(new File("C:\\Users\\fengkunpeng\\Desktop\\test.sql"), StandardCharsets.UTF_8), OpenGaussLexer.class, OpenGaussParser.class);
        ParseASTNode node = (ParseASTNode) sqlParser.parse();
        System.out.println(node.getRootNode());
        SQLStatementVisitorEngine statementVisitorEngine = new SQLStatementVisitorEngine("openGauss", true);
        OpenGaussCreateTableStatement visit = (OpenGaussCreateTableStatement) statementVisitorEngine.visit(node);
        TableNameSegment tableName = visit.getTable().getTableName();
        Collection<ColumnDefinitionSegment> columnDefinitions = visit.getColumnDefinitions();
        for (ColumnDefinitionSegment columnDefinition : columnDefinitions) {
            ColumnSegment columnName = columnDefinition.getColumnName();
            String qualifiedName = columnName.getQualifiedName();
            DataTypeSegment dataType = columnDefinition.getDataType();
            String dataTypeName = dataType.getDataTypeName();
            DataTypeLengthSegment dataLength = dataType.getDataLength();
            System.out.println(qualifiedName);
            System.out.println(dataTypeName);
            if(dataLength != null){
                int precision = dataLength.getPrecision();
                System.out.println(precision);
            }
        }
        System.out.println(tableName);
    }
}

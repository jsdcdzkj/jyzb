package com.jsdc.rfid.common.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 根据实体类名 生成controller service mapper dao 类
 * @Author libin
 * @create 2021/12/31 16:51
 */
public class Generators {
    static String workspacePath = "D:\\workSpaces\\svn\\检察院RFID固定资产";
    static String author = "zhangdequan";//作者
//    static String author = "thr";//作者

    static String modelPath = workspacePath + "\\rfid-model\\src\\main\\java\\com\\jsdc\\rfid\\model\\{className}.java";
    static String daoPath = workspacePath + "\\rfid-api\\src\\main\\java\\com\\jsdc\\rfid\\dao\\{className}Dao.java";
    static String mapperPath = workspacePath + "\\rfid-api\\src\\main\\java\\com\\jsdc\\rfid\\mapper\\{className}Mapper.java";
    static String servicePath = workspacePath + "\\rfid-api\\src\\main\\java\\com\\jsdc\\rfid\\service\\{className}Service.java";
    static String controllerPath = workspacePath + "\\rfid-web\\src\\main\\java\\com\\jsdc\\rfid\\controller\\{className}Controller.java";

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws Exception {
        //单独生成
        generator("CarryWhite");
        //批量生成
//        generator("AssetsManage","InventoryManagement","InventoryWarehousingMember"
//                ,"OperationRecord","WarehousingManagement","WarehousingPurchaseMember"
//        ); //换成自已要生成的类名
    }

    /**
     * 生成代码的主方法
     *
     * @param classNames
     * @throws Exception
     */
    private static void generator(String... classNames) throws Exception {

        for (String className : classNames) {
            //String className = "AssetsClGroup";//AssetsBackup  类名
            className = trimToEmpty(className);
            String classNameX = firstCharLower(className);//首字母小写

            Date currentTime = new Date();

            String[] objectName = new String[]{"dao", "mapper", "service", "controller"};

            for (String cName : objectName) {
                String template = getTemplate(cName);

                //=============================== start========================================
                String cStr = template.replace("{className}", className);
                cStr = cStr.replace("{classNameX}", classNameX);
                cStr = cStr.replace("{author}", author);
                cStr = cStr.replace("{createDate}", sdf.format(currentTime));
                if ("dao".equals(cName)) {
                    cStr = cStr.replace("{tableName}", getTable(className));
                }

                String path = getPath(cName);
                String tempPath = path.replace("{className}", className);


                FileUtils.writeStringToFile(new File(tempPath), cStr, "utf-8");
                //=============================== end========================================
            }
        }

    }


    /**
     * 获取类模板
     *
     * @param cName
     * @return
     */
    private static String getTemplate(String cName) {
        if ("dao".equals(cName)) {
            return daoTemplate;
        } else if ("service".equals(cName)) {
            return serviceTemplate;
        } else if ("controller".equals(cName)) {
            return controllerTemplate;
        } else if ("mapper".equals(cName)) {
            return mapperTemplate;
        } else {
            return null;
        }
    }

    /**
     * 获取类路径
     *
     * @param cName
     * @return
     */
    private static String getPath(String cName) {
        if ("dao".equals(cName)) {
            return daoPath;
        } else if ("service".equals(cName)) {
            return servicePath;
        } else if ("controller".equals(cName)) {
            return controllerPath;
        } else if ("mapper".equals(cName)) {
            return mapperPath;
        } else {
            return null;
        }
    }

    /**
     * 根据实体类获取表名
     *
     * @param className
     * @return
     * @throws IOException
     */
    private static String getTable(String className) throws IOException {
        String model = modelPath.replace("{className}", className);
        List<String> recodesList = FileUtils.readLines(new File(model), "utf-8");
        String tableName = "";
        for (String row : recodesList) {
            row = trimToEmpty(row);
            if (row.startsWith("@TableName")) {
                tableName = row.replace("@TableName(\"", "").replace("\")", "");
                break;
            }
        }
        return trimToEmpty(tableName);
    }

    //首字母转小写
    public static String firstCharLower(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    //首字母转大写
    public static String firstCharUpper(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * controller模板
     */
    static String controllerTemplate = "package com.jsdc.rfid.controller;\n" +
            "\n" +
            "import com.github.pagehelper.PageInfo;\n" +
            "import com.jsdc.core.base.BaseController;\n" +
            "import com.jsdc.rfid.model.{className};\n" +
            "import com.jsdc.rfid.service.{className}Service;\n" +
            "import com.jsdc.rfid.vo.ResultInfo;\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.stereotype.Controller;\n" +
            "import org.springframework.web.bind.annotation.RequestMapping;\n" +
            "import org.springframework.web.bind.annotation.RequestMethod;\n" +
            "import org.springframework.web.bind.annotation.RequestParam;\n" +
            "import org.springframework.web.bind.annotation.ResponseBody;\n" +
            "\n" +
            "/**\n" +
            " * @Author {author}\n" +
            " * @create {createDate}\n" +
            " */\n" +
            "@Controller\n" +
            "@RequestMapping(\"/{classNameX}\")\n" +
            "public class {className}Controller extends BaseController {\n" +
            "\n" +
            "    @Autowired\n" +
            "    private {className}Service {classNameX}Service;\n" +
            "\n" +
            "\n" +
            "    @RequestMapping(value = \"toList.do\", method = RequestMethod.POST)\n" +
            "    @ResponseBody\n" +
            "    public ResultInfo toList(@RequestParam(defaultValue = \"1\") Integer pageIndex, @RequestParam(defaultValue = \"10\") Integer pageSize, {className} beanParam) {\n" +
            "        PageInfo<{className}> page = {classNameX}Service.toList(pageIndex, pageSize, beanParam);\n" +
            "\n" +
            "        return ResultInfo.success(page);\n" +
            "    }\n" +
            "     /**\n" +
            "     *  ID查询\n" +
            "     */\n" +
            "    @RequestMapping(value = \"getById.do\",method = RequestMethod.POST)\n" +
            "    @ResponseBody\n" +
            "    public ResultInfo getById(Integer id){\n" +
            "        return {classNameX}Service.getById(id);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     *  添加\n" +
            "     */\n" +
            "    @RequestMapping(value = \"toAdd.do\",method = RequestMethod.POST)\n" +
            "    @ResponseBody\n" +
            "    public ResultInfo add{className}({className} {classNameX}){\n" +
            "\n" +
            "        return {classNameX}Service.add{className}({classNameX});\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     *  编辑\n" +
            "     */\n" +
            "    @RequestMapping(value = \"toEdit.do\",method = RequestMethod.POST)\n" +
            "    @ResponseBody\n" +
            "    public ResultInfo edit{className}({className} {classNameX}){\n" +
            "\n" +
            "        return {classNameX}Service.edit{className}({classNameX});\n" +
            "    }\n" +
            "}\n";

    /**
     * service模板
     */
    static String serviceTemplate = "package com.jsdc.rfid.service;\n" +
            "\n" +
            "import com.jsdc.core.base.BaseService;\n" +
            "import com.jsdc.rfid.dao.{className}Dao;\n" +
            "import com.jsdc.rfid.mapper.{className}Mapper;\n" +
            "import com.jsdc.rfid.model.{className};\n" +
            "import com.github.pagehelper.PageHelper;\n" +
            "import com.github.pagehelper.PageInfo;\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.stereotype.Service;\n" +
            "import org.springframework.transaction.annotation.Transactional;\n" +
            "import java.util.List;\n" +
            "\n" +
            "import java.util.Date;\n" +
            "import com.jsdc.rfid.vo.ResultInfo;\n" +
            "import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\n" +
            "\n" +
            "/**\n" +
            " * @Author {author}\n" +
            " * @create {createDate}\n" +
            " */\n" +
            "@Service\n" +
            "@Transactional\n" +
            "public class {className}Service extends BaseService<{className}Dao, {className}> {\n" +
            "\n" +
            "    @Autowired\n" +
            "    private {className}Mapper {classNameX}Mapper;\n" +
//            "    @Autowired\n" +
//            "    private {className}Dao {classNameX}Dao;\n" +
            "    @Autowired\n" +
            "    private SysUserService sysUserService;\n" +
            "\n" +
            "    public PageInfo<{className}> toList(Integer pageIndex, Integer pageSize, {className} beanParam) {\n" +
            "        PageHelper.startPage(pageIndex, pageSize);\n" +
            "\n" +
            "        List<{className}> {classNameX}Vos = {classNameX}Mapper.toList(beanParam);\n" +
            "\n" +
            "        PageInfo<{className}> page = new PageInfo<>({classNameX}Vos);\n" +
            "\n" +
            "        return page;\n" +
            "    }\n" +
            "\n" +
            "    public List<{className}> getList({className} beanParam) {\n" +
            "\n" +
            "        List<{className}> {classNameX}Vos = {classNameX}Mapper.toList(beanParam);\n" +
            "\n" +
            "        return {classNameX}Vos;\n" +
            "    }\n" +
            "    public ResultInfo getById(Integer id) {\n" +
            "        QueryWrapper<{className}> queryWrapper = new QueryWrapper<>();\n" +
            "        queryWrapper.eq(\"is_del\",0);\n" +
            "        queryWrapper.eq(\"id\",id);\n" +
            "        {className} {classNameX} = selectOne(queryWrapper);\n" +
            "        return ResultInfo.success({classNameX});\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     *  添加\n" +
            "     */\n" +
            "    public ResultInfo add{className}({className} bean) {\n" +
            "        // 删除状态\n" +
            "        bean.setIs_del(String.valueOf(0));\n" +
            "        // 创建时间\n" +
            "        bean.setCreate_time(new Date());\n" +
            "        // 创建者\n" +
            "        bean.setCreate_user(sysUserService.getUser().getId());\n" +
            "        insert(bean);\n" +
            "        return ResultInfo.success();\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     *  编辑\n" +
            "     */\n" +
            "    public ResultInfo edit{className}({className} bean) {\n" +
            "        // 修改者\n" +
            "        bean.setUpdate_user(sysUserService.getUser().getId());\n" +
            "        // 修改时间\n" +
            "        bean.setUpdate_time(new Date());\n" +
            "        updateById(bean);\n" +
            "        return ResultInfo.success();\n" +
            "    }" +
            "}\n";

    /**
     * mapper模板
     */
    static String mapperTemplate = "package com.jsdc.rfid.mapper;\n" +
            "\n" +
            "import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n" +
            "import com.jsdc.rfid.model.{className};\n" +
            "import org.apache.ibatis.annotations.Mapper;\n" +
            "import com.jsdc.rfid.dao.{className}Dao;\n" +
            "import org.apache.ibatis.annotations.SelectProvider;\n" +
            "import java.util.List;\n" +
            "\n" +
            "/**\n" +
            " * @Author {author}\n" +
            " * @create {createDate}\n" +
            " */\n" +
            "@Mapper\n" +
            "public interface {className}Mapper extends BaseMapper<{className}> {\n" +
            "\n" +
            "    @SelectProvider(method = \"toList\",type = {className}Dao.class)\n" +
            "    List<{className}> toList({className} beanParam);\n" +
            "}";

    /**
     * dao模板
     */
    static String daoTemplate = "package com.jsdc.rfid.dao;\n" +
            "\n" +
            "import com.jsdc.core.base.BaseDao;\n" +
            "import com.jsdc.rfid.model.{className};\n" +
            "import org.springframework.stereotype.Repository;\n" +
            "\n" +
            "/**\n" +
            " * @Author {author}\n" +
            " * @create {createDate}\n" +
            " */\n" +
            "@Repository\n" +
            "public class {className}Dao extends BaseDao<{className}> {\n" +
            "\n" +
            "    public String toList({className} beanParam){\n" +
            "        StringBuilder sql = new StringBuilder();\n" +
            "\n" +
            "        sql.append(\" SELECT * FROM {tableName}\");\n" +
            "        sql.append(\" where is_del = 0 \");\n" +
            "        return sql.toString();\n" +
            "    }\n" +
            "}\n";
}

package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * mvn批量处理工具，功能如下
 * <p>
 * 1. 批量上传指定目录下所有jar到私服
 * 2. 批量上传指定目录下所有jar到本地仓库
 *
 * @author fengshuonan
 * @date 2022/9/30 21:31
 */
@Getter
@Setter
public class MvnDeployUtil {

    /**
     * 所有需要被探测到的目录
     */
    private final List<File> directories = new ArrayList<>();

    /**
     * mvn命令的绝对路径
     * <p>
     * 例如：D:\apache-maven-3.5.4\bin\mvn.cmd
     */
    private String mvnExePath;

    /**
     * 仓库目录，包含需要被推送的压缩包的完整路径
     * <p>
     * 一般压缩包中包含了多个目录的jar
     * <p>
     * 例如：D:\tmp\needToDeploy
     */
    private String targetToDeployPath;

    /**
     * maven的settings文件配置路径
     * <p>
     * 例如：D:\apache-maven-3.5.4\conf\settings.xml
     */
    private String mvnSettingXmlPath;

    /**
     * 仓库的名称，推送到指定私服的仓库id
     * <p>
     * 例如：maven-host-company
     */
    private String repositoryId;

    /**
     * 仓库的url，远程私服的地址
     * <p>
     * 例如：http://192.168.1.2:8081/repository/maven-host-company/
     */
    private String repositoryUrl;

    public static void main(String[] args) {
        MvnDeployUtil mvnDeployUtil = new MvnDeployUtil();
        mvnDeployUtil.setTargetToDeployPath("D:\\tmp\\devops-plugins");
        mvnDeployUtil.setMvnExePath("D:\\apache-maven-3.5.4\\bin\\mvn.cmd");
        mvnDeployUtil.setMvnSettingXmlPath("D:\\apache-maven-3.5.4\\conf\\settings.xml");
        mvnDeployUtil.setRepositoryId("company-hosted");
        mvnDeployUtil.setRepositoryUrl("http://192.168.31.3:8081/repository/company-hosted/");
        mvnDeployUtil.beginDeploy();
    }

    /**
     * 探测pom文件的内容，是否packaging为pom类型
     *
     * @author fengshuonan
     * @date 2022/9/30 21:45
     */
    public static boolean packagingIsPomFlag(File pom) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(pom)));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().contains("<packaging>pom</packaging>")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtil.close(reader);
        }
        return false;
    }

    /**
     * 递归获取一个目录下的所有文件目录路径
     *
     * @author fengshuonan
     * @date 2022/9/30 21:49
     */
    private void getAllDirs(String fileDir) {

        // 先添加本目录
        directories.add(new File(fileDir));

        // 递归添加子目录
        File file = new File(fileDir);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File item : files) {
            if (item.isDirectory()) {
                directories.add(new File(item.getAbsolutePath()));
                getAllDirs(item.getAbsolutePath());
            }
        }
    }

    /**
     * 获取目录的类型，判断目录下是否同时有pom和jar文件，或者单纯有pom文件
     *
     * @author fengshuonan
     * @date 2022/9/30 21:49
     */
    private DirectoryType getDirectoryType(File directoryPath) {
        boolean pom = false;
        boolean jar = false;

        File[] files = directoryPath.listFiles();
        if (files == null) {
            return DirectoryType.NONE;
        }

        for (File file : files) {
            if (file.getName().endsWith(".pom")) {
                pom = true;
            } else if (file.getName().endsWith(".jar")) {
                jar = true;
            }
        }

        if (pom && !jar) {
            return DirectoryType.POM;
        } else if (jar && pom) {
            return DirectoryType.JAR_AND_POM;
        } else {
            return DirectoryType.NONE;
        }
    }

    /**
     * 对只有pom文件的目录，执行mvn deploy操作
     *
     * @author fengshuonan
     * @date 2022/9/30 21:55
     */
    private void doOnlyPom(File directory) {

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        File pom = null;
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(".pom")) {
                pom = file;
            }
        }

        String command = buildCommand(FileType.POM, null, pom);
        executeCommand(command);
    }

    /**
     * 对同时包含jar和pom文件的目录，执行mvn deploy操作
     *
     * @author fengshuonan
     * @date 2022/9/30 21:58
     */
    private void doJarAndPom(File directory) {

        File[] files = directory.listFiles();

        File pom = null;
        File jar = null;

        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith(".pom")) {
                    pom = file;
                } else if (name.endsWith(".jar")) {
                    jar = file;
                }
            }
            if (jar != null) {
                String command = buildCommand(FileType.JAR, jar, pom);
                executeCommand(command);
            }
        }
    }

    /**
     * 程序入口
     *
     * @author fengshuonan
     * @date 2022/9/30 21:58
     */
    public void beginDeploy() {

        //初始化，获取所有的目录存到list
        this.getAllDirs(targetToDeployPath);

        //遍历所有目录，并根据不同类型的目录，执行deploy
        for (File directory : directories) {
            DirectoryType directoryType = getDirectoryType(directory);
            if (directoryType.equals(DirectoryType.NONE)) {
                continue;
            } else if (directoryType.equals(DirectoryType.JAR_AND_POM)) {
                doJarAndPom(directory);
            } else if (directoryType.equals(DirectoryType.POM)) {
                doOnlyPom(directory);
            }
        }
    }

    /**
     * 执行真正的mvn命令
     *
     * @author fengshuonan
     * @date 2022/9/30 21:54
     */
    private void executeCommand(String command) {
        try {
            System.out.println("开始执行mvn命令：" + command);
            Process exec = Runtime.getRuntime().exec(command);
            System.out.println(IoUtil.read(exec.getInputStream(), true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造mvn deploy command命令
     *
     * @author fengshuonan
     * @date 2022/9/30 21:52
     */
    private String buildCommand(FileType fileType, File deployJar, File deployJarPom) {

        // 目录下单纯有pom文件，并且不是packaging=pom类型的，直接略过
        if (fileType.equals(FileType.POM)) {
            if (!packagingIsPomFlag(deployJarPom)) {
                return "";
            }
        }

        // 构建执行推送的命令
        String command = this.mvnExePath + " " +
                "-s " + this.mvnSettingXmlPath + " " +
                "install:install-file " +
                "-Durl=" + this.repositoryUrl + " " +
                "-DrepositoryId=" + this.repositoryId + " " +
                "-DgeneratePom=false ";

        // 获取packing
        String packing;
        if (fileType.equals(FileType.JAR)) {
            packing = "-Dpackaging=jar ";
        } else {
            packing = " ";
        }

        // 获取pomFile和file
        String pomFile = deployJarPom.getAbsolutePath();
        String file;
        if (fileType.equals(FileType.POM)) {
            file = deployJarPom.getAbsolutePath();
        } else {
            file = deployJar.getAbsolutePath();
        }

        command += packing;
        command += " -Dfile=" + file + " ";
        command += " -DpomFile=" + pomFile + " ";

        return command;
    }

    /**
     * 目录的类型
     * <p>
     * 判断目录下是否有可以上传的jar
     *
     * @author fengshuonan
     * @date 2022/9/30 21:33
     */
    private enum DirectoryType {
        /**
         * 目录下jar和pom都没有
         */
        NONE,

        /**
         * 目录下只有pom
         */
        POM,

        /**
         * 目录下同时有jar和pom文件
         */
        JAR_AND_POM
    }

    /**
     * 可以被mvn上传的文件类型
     *
     * @author fengshuonan
     * @date 2022/9/30 21:34
     */
    private enum FileType {

        /**
         * jar文件
         */
        JAR,

        /**
         * pom文件
         */
        POM
    }

}

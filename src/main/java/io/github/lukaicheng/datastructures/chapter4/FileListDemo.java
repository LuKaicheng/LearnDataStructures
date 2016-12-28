package io.github.lukaicheng.datastructures.chapter4;

import java.io.File;

/**
 * 
 * Chapter 4.1.2 树的遍历及应用 
 *  
 * 以下示例代码展示了简单的目录打印，遍历策略采用了先序遍历
 */
public class FileListDemo {

    public static void main(String[] args) {
        String path = "D:\\Document";
        int depth = 0;
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("Path not exist!");
            return;
        }
        printAll(file, depth);
        listAndSum(file, depth);
    }


    private static void printAll(File file, int depth) {
        String space = "";
        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                space += "\t";
            }
        }
        System.out.println(space + file.getName());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                printAll(childFile, depth + 1);
            }
        }
    }
    
    private static long listAndSum(File file, int depth) {
        long totalSize = 0;
        String space = "";
        if (depth > 0) {
            for (int i = 0; i < depth; i++) {
                space += "\t";
            }
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                totalSize += listAndSum(childFile, depth + 1);
            }
        } else {
            totalSize += file.length();
        }
        System.out.println(String.format("%s,%-4s", (space + file.getName()), totalSize));
        return totalSize;
    }

}

package com.ai.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    private String courseFilePath = new File("src\\main\\resources\\static\\courses\\").getAbsolutePath();
    private String profileFilePath = new File("src\\main\\resources\\static\\profile\\").getAbsolutePath();
    private String assignmentFilePath = new File("src\\main\\resources\\static\\assignment\\").getAbsolutePath();

    public void createFile(MultipartFile file, String folderName) throws IllegalStateException, IOException {
        file.transferTo(
                new File(courseFilePath + folderName + "\\" + file.getOriginalFilename()));
    }

    public String  createProfileFile(MultipartFile file, String loginId) throws IllegalStateException, IOException {
        var fileName =  "profile-".concat(loginId).concat(".").concat(file.getOriginalFilename().split("\\.")[1]);
        file.transferTo(
                new File( profileFilePath + "\\" + fileName));
        return fileName;
    }

    public String  createAssignmentFile(MultipartFile file, String foldername) throws IllegalStateException, IOException {
        var fileName =  String.valueOf(Math.random() * 100).concat(".").concat(file.getOriginalFilename());
        file.transferTo(
                new File( assignmentFilePath + "\\" + foldername + "\\" + fileName));
        return fileName;
    }

    public boolean deleteFile(String path) {
        var file = new File(path);
        return file.delete();
    }

    public void createFolderForModule(String courseName) {
        var theDir = new File(courseFilePath + "\\"+courseName);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        var videoFolder = new File(courseFilePath + "\\"+courseName + "\\videos");
        if (!videoFolder.exists()) {
            videoFolder.mkdirs();
        }
        var resourceFolder = new File(courseFilePath + "\\"+courseName + "\\resources");
        if (!resourceFolder.exists()) {
            resourceFolder.mkdirs();
        }
    }

    public void createFolderForAssignment(String folderName){
        var theDir = new File(assignmentFilePath + "\\"+folderName);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    public void renameFolder(String oldFolder, String newFolder) throws IOException {
        var data = Paths.get(courseFilePath, oldFolder);
        Files.move(data, data.resolveSibling(newFolder));
    }

    public void deleteFolder(String folderName) throws IOException {
        var folder = new File(courseFilePath.concat("\\").concat(folderName));
        FileUtils.deleteDirectory(folder);
    }

}

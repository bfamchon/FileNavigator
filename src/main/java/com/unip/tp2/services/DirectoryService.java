package com.unip.tp2.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baptiste on 01/06/17.
 * Hi
 */

@Service
public class DirectoryService {
    public static List<HashMap> getContent(HttpServletRequest request) throws FileNotFoundException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/directory", "");

        String pathToReturn = path.replace("users_workspace", "");

        String pathParentToReturn = pathToReturn;

        if (!pathToReturn.equals("/")) {
            pathParentToReturn += "../";
        }
        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {

            File[] resources = folder.listFiles();

            List<HashMap> toReturn = new ArrayList<>();


            if (resources != null) {
                HashMap<String, List> dirContent = new HashMap<>();
                HashMap<String, String> parent = new HashMap<>();
                List<HashMap> files = new ArrayList<>();

                for (File resource : resources) {
                    HashMap<String, String> infos = new HashMap<>();

                    if (resource.isDirectory()) {
                        infos.put("type", "directory");
                    } else {
                        infos.put("type", "file");
                    }
                    infos.put("path", pathToReturn + resource.getName());
                    infos.put("name", resource.getName());

                    files.add(infos);
                }
                dirContent.put("dirContent", files);

                parent.put("name", folder.getName());

                parent.put("type", "directory");

                parent.put("path", pathParentToReturn);

                toReturn.add(dirContent);
                toReturn.add(parent);
            }

            return toReturn;
        }
        throw new FileNotFoundException();
    }

    public static HashMap<String,String> createDirectory(HttpServletRequest request, String name) throws FileAlreadyExistsException, FileNotFoundException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/directory", "");
        String pathToReturn = path.replace("users_workspace", "");

        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {
            String formatedName = FilenameUtils.removeExtension(name);

            File newDir = new File(path + "/" + formatedName);
            if (!newDir.exists()) {
                newDir.mkdir();
                HashMap<String, String> infos = new HashMap<>();

                infos.put("type", "directory");
                infos.put("path", pathToReturn + newDir.getName());
                infos.put("name", newDir.getName());
                return infos;
            }
            throw new FileAlreadyExistsException("File " + newDir.getName() + "already exists !");
        }
        throw new FileNotFoundException("Error directory can't be found");
    }

    public static void changeName(HttpServletRequest request, String name) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/directory", "");

        File folder = new File(path);

        if (folder.exists() && folder.isDirectory()) {
            String formatedName = FilenameUtils.removeExtension(name);

            Path source = Paths.get(path);
            Files.move(source, source.resolveSibling(formatedName));
        }
    }

    public static HashMap<String, String> deleteDirectory(HttpServletRequest request) throws FileNotFoundException {

        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replaceFirst("/directory", "");

        File folder = new File(path);
        System.out.println(folder.getAbsolutePath());
        if ((folder.exists() && folder.isDirectory()) && folder.getAbsolutePath().contains("users_workspace")) {
            HashMap<String, String> map = new HashMap<>();
            deleteDir(folder);
            map.put("name", folder.getName());
            return map;
        }

        throw new FileNotFoundException();
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}

package com.unip.tp2.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by baptiste on 01/06/17.
 * Hi
 */

@Service
public class FileService {

    public static ResponseEntity<?> downloadFile(HttpServletRequest request) throws FileNotFoundException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/file","");

        File file = new File(path);

        if (file.exists() && !file.isDirectory()){
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-Disposition",String.format("attachment; filename=\"%s\"",
                    file.getName()));

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        throw new FileNotFoundException(file.getName());
    }

    public static HashMap<String,String> uploadFile(HttpServletRequest request, MultipartFile file) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/file","");
        String pathToReturn = path.replace("users_workspace", "");

        File f = new File(path);

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path + file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();

                HashMap<String, String> infos = new HashMap<>();

                infos.put("type", "file");
                infos.put("path", pathToReturn + file.getOriginalFilename());
                infos.put("name", file.getOriginalFilename());
                System.out.println("You have uploaded file");

                return infos;
            } catch (Exception e) {
                System.out.println("Failed to upload");
            }
        }
        throw new IOException("File is empty and can't be upload");
    }

    public static void changeName(HttpServletRequest request, String name) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replace("/file","");

        File file = new File(path);

        if (file.exists() && !file.isDirectory()){
            String formatedName = FilenameUtils.removeExtension(name);
            String[] filetype = file.getName().split("\\.");

            if (file.getName().contains(".")) {
                formatedName += "." + filetype[filetype.length-1];
            }

            Path source = Paths.get(path);
            Files.move(source, source.resolveSibling(formatedName));
        }
    }

    public static HashMap<String,String> deleteFile(HttpServletRequest request) throws FileNotFoundException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        path = "users_workspace" + path.replaceFirst("/file","");

        File file = new File(path);

        if (file.exists() && !file.isDirectory()){
            HashMap<String,String> map = new HashMap<>();
            file.delete();
            map.put("name", file.getName());
            return map;
        }

        throw new FileNotFoundException();
    }
}

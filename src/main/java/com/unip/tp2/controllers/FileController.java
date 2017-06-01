package com.unip.tp2.controllers;

import com.unip.tp2.services.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/file/**")
public class FileController {

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<?> get(HttpServletRequest request) throws IOException {
        System.out.println("/file/** -> get()");
        return FileService.downloadFile(request);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HashMap<String, String> post(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("/file/**&name='<something>' -> post()");
        return FileService.uploadFile(request,file);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void put(HttpServletRequest request, @RequestParam("name") String name) throws IOException {
        System.out.println("/file/**&name='<something>' -> put()");
        FileService.changeName(request,name);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public HashMap<String, String> delete(HttpServletRequest request) throws FileNotFoundException {
        System.out.println("/file/** -> delete()");
        return FileService.deleteFile(request);
    }
}

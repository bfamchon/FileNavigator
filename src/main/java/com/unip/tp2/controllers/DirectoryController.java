package com.unip.tp2.controllers;

import com.unip.tp2.services.DirectoryService;
import com.unip.tp2.services.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by baptiste on 14/05/17.
 * Hi
 */

@RestController
@RequestMapping(path = "/directory/**")
public class DirectoryController {

    @RequestMapping(method = RequestMethod.GET)
    public List<HashMap> get(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("/directory/** -> get()");
        return DirectoryService.getContent(request);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HashMap<String, String> post(HttpServletRequest request, @RequestParam("name") String name) throws IOException {
        System.out.println("/directory/**&name='<something>' -> post()");
        return DirectoryService.createDirectory(request,name);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void put(HttpServletRequest request, @RequestParam("name") String name) throws IOException {
        System.out.println("/directory/**&name='<something>' -> put()");
        DirectoryService.changeName(request,name);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public HashMap<String, String> delete(HttpServletRequest request) throws FileNotFoundException {
        System.out.println("/directory/** -> delete()");
        return DirectoryService.deleteDirectory(request);
    }
}


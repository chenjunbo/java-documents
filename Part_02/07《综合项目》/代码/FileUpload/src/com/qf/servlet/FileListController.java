package com.qf.servlet;

import com.qf.utils.DownLoadUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(name = "FileListController",value = "/fileList")
public class FileListController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");

        //集合，map，key=UUID   value=源文件名称
        HashMap<String,String> fileMap = new HashMap<>();
        String basePath = request.getServletContext().getRealPath("/WEB-INF/upload");
        DownLoadUtils.getFileList(new File(basePath),fileMap);

        request.setAttribute("fileMap",fileMap);
        request.getRequestDispatcher("/list.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ceva;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


/**
 *
 * @author Test
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class UploadServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part part = request.getPart("uploadFile");
        String fileName =  part.getSubmittedFileName();
        
        try (FileOutputStream fout = new FileOutputStream("c:/temp/uploadFolder/" + fileName)) {
            InputStream in = part.getInputStream();
            byte buffer[] = new byte[2048];
            int len = in.read(buffer);
            while (len > 0) {
                fout.write(buffer, 0, len);
                len = in.read(buffer);
            }
            in.close();
        }
        
        response.sendRedirect("uploadDemo.html");
    }
}

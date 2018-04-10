package org.flysky.coder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by hxuhao233 on 2018/4/8.
 */
@Controller
@RequestMapping("/source")
public class SourceController {

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request,@RequestParam("file") MultipartFile uploadFile) throws IOException {
        String basePath = request.getSession().getServletContext().getRealPath("/") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + "upload" + System.getProperty("file.separator");

        String originalFileName = uploadFile.getOriginalFilename();
        String fileType = originalFileName.substring(originalFileName.indexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + fileType;
        File targetFile = new File(basePath, newFileName);

        uploadFile.transferTo(targetFile);

        return "succeed";
    }

}

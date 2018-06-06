package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.entity.Source;
import org.flysky.coder.entity.User;
import org.flysky.coder.entity.wrapper.SourceWrapper;
import org.flysky.coder.service.ISourceService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.source.SourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 资源分享控制器
 * Created by hxuhao233 on 2018/4/8.
 */
@Controller
public class SourceController {

    @Autowired
    private ISourceService sourceService;

    /**
     * 创建资源
     * @param request
     * @param session
     * @param sourceInfo
     * @param uploadFile
     * @return
     * @throws IOException
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value="/source", method=RequestMethod.POST)
    @ResponseBody
    public Result createSource(HttpServletRequest request, HttpSession session, SourceInfo sourceInfo, @RequestParam(value = "file") MultipartFile uploadFile) throws IOException {
        ResultWrapper result = new ResultWrapper();
        if (sourceService.hasSourceName(sourceInfo.getName())){
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("该资源名已存在");
            return result;
        }

        User user = (User) session.getAttribute("user");
        String basePath = request.getSession().getServletContext().getRealPath("/") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + "upload" + System.getProperty("file.separator");
        LocalDateTime time = LocalDateTime.now();
        String newFileName = generateFileName(uploadFile);
        Source source = new Source();

        if (sourceInfo.getType() == Source.TYPE_IMG) {
            source.setName(newFileName);
        } else {
            source.setName(sourceInfo.getName());
        }
        source.setAddr(newFileName);
        source.setName(sourceInfo.getName());
        source.setCreatedAt(time);
        source.setType(sourceInfo.getType());
        source.setDescription(sourceInfo.getDescription());
        source.setUpdatedAt(time);
        source.setUserId(user.getId());
        source.setIsDeleted(false);
        sourceService.createSource(source, basePath, uploadFile);

        result.setCode(ResponseCode.SUCCEED);
        result.setPayload(source);
        return result;
    }

    /**
     * 修改资源
     * @param request
     * @param session
     * @param sourceId
     * @param sourceInfo
     * @param uploadFile
     * @return
     * @throws IOException
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value="/source/{sourceId}", method=RequestMethod.POST)
    @ResponseBody
    public Result modifySource(HttpServletRequest request, HttpSession session, @PathVariable(value = "sourceId")int sourceId, SourceInfo sourceInfo, @RequestParam(value = "file", required = false) MultipartFile uploadFile) throws IOException {
        ResultWrapper result = new ResultWrapper();

        Source source = sourceService.getSourceById(sourceId);
        if (source == null) {
            result.setCode(ResponseCode.NOT_FOUND);
            return result;
        } else if (source.getUserId().equals(((User)session.getAttribute("user")).getId())) {
            throw new UnauthorizedException();
        }

        if (sourceService.hasSourceName(sourceInfo.getName())){
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("该资源名已存在");
            return result;
        }

        String basePath = request.getSession().getServletContext().getRealPath("/") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + "upload" + System.getProperty("file.separator");
        LocalDateTime time = LocalDateTime.now();

        source.setName(sourceInfo.getName());
        if (uploadFile != null) {
            source.setAddr(generateFileName(uploadFile));
        }
        source.setDescription(sourceInfo.getDescription());
        source.setUpdatedAt(time);

        sourceService.modifySource(source, basePath, uploadFile);
        result.setCode(1);
        result.setPayload(source);
        return result;
    }

    /**
     * 删除资源
     * @param session
     * @param sourceId
     * @return
     */
    @ResponseBody
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/source/{sourceId}", method = RequestMethod.DELETE)
    public Result deleteSourceById(HttpSession session, @PathVariable(value = "sourceId") int sourceId){
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Source source = sourceService.getSourceById(sourceId);

        if (source == null){
            result.setCode(ResponseCode.NOT_FOUND);
            return result;
        }

        if (!source.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        }

        sourceService.deleteById(sourceId);

        result.setCode(ResponseCode.SUCCEED);
        return result;

    }


    /**
     * 搜索资源
     * @param info
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/source", method = RequestMethod.GET)
    public Result getSourceWrapperByInfo(@RequestParam(value = "info")String info, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper result = new ResultWrapper();
        PageInfo<SourceWrapper> sourceWrappers = sourceService.getSourceWrappersByInfo(info, pageNum, pageSize);

        if(sourceWrappers.getSize() <= 0) {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("没有找到相关资源");
        } else {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(sourceWrappers);
        }
        return result;
    }

    /**
     * 查看个人资源列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/mySources", method = RequestMethod.GET)
    public Result getSourceWrapperByUserId(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");
        PageInfo<SourceWrapper> sourceWrappers = sourceService.getSourceWrappersByUserId(user.getId(), pageNum, pageSize);

        if(sourceWrappers.getSize() <= 0) {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("你还没有上传资源");
        } else {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(sourceWrappers);
        }
        return result;
    }

    /**
     * 查看资源
     * @param sourceId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/source/{sourceId}", method = RequestMethod.GET)
    public Result getSourceWrapperById(@PathVariable(value = "sourceId")int sourceId){
        ResultWrapper result = new ResultWrapper();
        SourceWrapper sourceWrapper = sourceService.getSourceWrapperById(sourceId);

        if(sourceWrapper == null) {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该资源不存在");
        } else {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(sourceWrapper);
        }
        return result;
    }

    @RequiresRoles(value = "user")
    @RequestMapping("/source/download/{sourceId}")
    public String download(HttpServletRequest request, HttpServletResponse response ,@PathVariable(value = "sourceId")int sourceId){
        response.setContentType("text/html;charset=utf-8"); /*设定相应类型 编码*/
        Source source = sourceService.getSourceById(sourceId);
        if (source == null) {
            return null;
        }

        java.io.BufferedInputStream bis = null;//创建输入输出流
        java.io.BufferedOutputStream bos = null;

        String basePath = request.getSession().getServletContext().getRealPath("/") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + ".." + System.getProperty("file.separator") + "upload" + System.getProperty("file.separator");
        String downLoadPath = basePath + source.getAddr();
        try {
            long fileLength = new File(downLoadPath).length();//获取文件长度
            response.setContentType("application/x-msdownload;");//下面这三行是固定形式
            response.setHeader("Content-disposition", "attachment; filename=" + new String(source.getAddr().getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));//创建输入输出流实例
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];//创建字节缓冲大小
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();//关闭输入流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();//关闭输出流
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;

    }

    private String generateFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileType = originalFileName.substring(originalFileName.indexOf(".") + 1);    // 获取源文件后缀
        String newFileName = UUID.randomUUID().toString() + "." + fileType;
        return newFileName;
    }


}

package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Source;
import org.flysky.coder.entity.wrapper.SourceWrapper;
import org.flysky.coder.mapper.SourceMapper;
import org.flysky.coder.service.ISourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by hxuhao233 on 2018/4/20.
 */
@Service
public class SourceService implements ISourceService {

    @Autowired
    private SourceMapper sourceMapper;


    @Override
    public int createSource(Source source, String basePath, MultipartFile uploadFile) throws IOException {
        File targetFile = new File(basePath, source.getAddr());
        uploadFile.transferTo(targetFile);
        sourceMapper.insertSelective(source);
        return 1;
    }

    @Override
    public int modifySource(Source source, String basePath, MultipartFile uploadFile) throws IOException {
        if (uploadFile != null) {
            File targetFile = new File(basePath, source.getAddr());
            uploadFile.transferTo(targetFile);
        }
        sourceMapper.updateByPrimaryKeySelective(source);
        return 1;
    }

    @Override
    public boolean hasSourceName(String name) {
        return sourceMapper.hasSourceName(name);
    }

    @Override
    public Source getSourceById(int id) {
        return sourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public SourceWrapper getSourceWrapperById(int id) {
        return sourceMapper.getSourceWrapperById(id);
    }

    @Override
    public PageInfo<SourceWrapper> getSourceWrappersByInfo(String info, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(sourceMapper.getSourceWrappersByInfo(info));
    }

    @Override
    public PageInfo<SourceWrapper> getSourceWrappersByUserId(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(sourceMapper.getSourceWrappersByUserId(userId));
    }

    @Override
    public int deleteById(int id) {
        Source source = new Source();
        source.setId(id);
        source.setIsDeleted(true);
        return sourceMapper.updateByPrimaryKeySelective(source);
    }

}

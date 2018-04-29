package org.flysky.coder.service;

import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.Source;
import org.flysky.coder.entity.wrapper.SourceWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by hxuhao233 on 2018/4/20.
 */
public interface ISourceService {
    int createSource(Source source, String basePath, MultipartFile uploadFile) throws IOException;

    int modifySource(Source source, String basePath, MultipartFile uploadFile) throws IOException;

    boolean hasSourceName(String name);

    Source getSourceById(int id);

    SourceWrapper getSourceWrapperById(int id);

    PageInfo<SourceWrapper> getSourceWrappersByInfo(String info, int pageNum, int pageSize);

    PageInfo<SourceWrapper> getSourceWrappersByUserId(int userId, int pageNum, int pageSize);

    int deleteById(int id);
}

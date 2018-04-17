package org.flysky.coder.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.Column;
import org.flysky.coder.entity.User;
import org.flysky.coder.service.IArticleService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.article.ColumnInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
@RestController
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @RequiresRoles("user")
    @RequestMapping(value="/column", method = RequestMethod.POST)
    public Result createColumn(HttpSession session, @RequestBody ColumnInfo columnInfo){
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");

        Column column = new Column();
        column.setName(columnInfo.getName());
        column.setDescription(columnInfo.getDescription());
        column.setCreatedAt(time);
        column.setUpdatedAt(time);
        column.setIsDeleted(false);
        column.setUserId(user.getId());
        int code = articleService.createColumn(column);

        Result result = new Result(code);
        if (code == 1) {
            result.setInfo("创建成功");
        } else {
            result.setInfo("创建失败，名字重复");
        }
        return result;
    }

}

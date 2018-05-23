package org.flysky.coder.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.entity.*;
import org.flysky.coder.entity.wrapper.ArticleWrapper;
import org.flysky.coder.entity.wrapper.CommentWrapper;
import org.flysky.coder.service.IArticleService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.VoteInfo;
import org.flysky.coder.vo.article.ArticleInfo;
import org.flysky.coder.vo.article.ColumnInfo;
import org.flysky.coder.vo.article.CommentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
@RestController
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    /**
     * 创建专栏
     * @param session
     * @param columnInfo
     * @return
     */
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

        ResultWrapper result = new ResultWrapper();
        if (code == 1) {
            result.setCode(ResponseCode.SUCCEED);
            result.setInfo("创建成功");
            result.setPayload(column);
        } else {
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("创建失败，该专栏已存在");
        }
        return result;
    }

    /**
     * 修改专栏
     * @param session
     * @param columnId
     * @param columnInfo
     * @return
     */
    @RequiresRoles("user")
    @RequestMapping(value="/column/{columnId}", method = RequestMethod.PUT)
    public Result modifyColumn(HttpSession session, @PathVariable("columnId")int columnId, @RequestBody ColumnInfo columnInfo) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();

        Column column = articleService.getColumnById(columnId);
        if (column == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该专栏不存在");
        } else if (!column.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            boolean needCheckName;
            if (columnInfo.getName().equals(column.getName())) {
                needCheckName = false;
            }else {
                needCheckName = true;
                column.setName(columnInfo.getName());
            }
            column.setDescription(columnInfo.getDescription());
            column.setUpdatedAt(time);

            int code = articleService.modifyColumn(column, needCheckName);
            if (code == 1) {
                result.setCode(ResponseCode.SUCCEED);
                result.setInfo("修改成功");
            } else {
                result.setCode(ResponseCode.DUPLICATE_NAME);
                result.setInfo("修改失败，已存在此专栏");
            }
        }
        return result;
    }


    /**
     * 查看专栏
     * @param columnId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/column/{columnId}",method = RequestMethod.GET)
    public Result getColumn(@PathVariable(value = "columnId")int columnId) {
        Column column = articleService.getColumnWrapperById(columnId);
        ResultWrapper result = new ResultWrapper();
        if (column != null) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(column);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该专栏不存在");
        }
        return result;
    }

    /**
     * 删除专栏
     * @param session
     * @param columnId
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/column/{columnId}",method = RequestMethod.DELETE)
    public Result deleteColumn(HttpSession session, @PathVariable(value = "columnId")int columnId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Column column = articleService.getColumnById(columnId);
        if (column == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该专栏不存在");
        } else if (!column.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            articleService.deleteColumn(columnId);
            result.setCode(ResponseCode.SUCCEED);
        }

        return result;
    }

    /**
     * 查看专栏列表（个人）
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/column",method = RequestMethod.GET)
    public Result getColumn(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");
        PageInfo<Column> column = articleService.getColumnByUserId(user.getId(), pageNum, pageSize);

        if (column.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(column);
        } else {
            result.setInfo("你还没有专栏");
            result.setCode(ResponseCode.NOT_FOUND);
        }

        return result;
    }

    /**
     * 查看文章列表(某专栏下)
     * @param columnId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/column/{columnId}/articles", method = RequestMethod.GET)
    public Result getArticleByColumnId(@PathVariable(value = "columnId") int columnId, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        ResultWrapper result = new ResultWrapper();

        if (articleService.getColumnById(columnId)==null) {
            result.setCode(ResponseCode.PREV_OBJECT_NOT_FOUND);
            result.setInfo("该专栏不存在");
        }

        PageInfo<ArticleWrapper> articles = articleService.getArticleByColumnId(columnId, pageNum, pageSize);
        if (articles.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(articles);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该专栏下还没有文章");
        }
        return result;
    }

    /**
     * 查看个人文章列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/myArticles", method = RequestMethod.GET)
    public Result getArticleByUserId(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        User user = (User) session.getAttribute("user");
        ResultWrapper result = new ResultWrapper();

        PageInfo<ArticleWrapper> articles = articleService.getArticleWrappersByUserId(user.getId(), pageNum, pageSize);
        if (articles.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(articles);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("您还没有创建文章");
        }
        return result;
    }


    /**
     * 创建文章
     * @param session
     * @param articleInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/article",method = RequestMethod.POST)
    public Result createArticle(HttpSession session, @RequestBody ArticleInfo articleInfo) {
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");
        ResultWrapper result = new ResultWrapper();

        /*
        if (articleService.getColumnById(articleInfo.getColumnId()) == null){
            result.setCode(4);
            result.setInfo("创建文章失败，该专栏不存在");
            return result;
        }
        */

        Article article = new Article();
        article.setName(articleInfo.getName());
        article.setDescription(articleInfo.getDescription());
        article.setContent(articleInfo.getContent());
        article.setParseType(articleInfo.getParseType());
        article.setCreatedAt(time);
        article.setUpdatedAt(time);
        article.setIsDeleted(false);
        //article.setColumnId(articleInfo.getColumnId());
        article.setUserId(user.getId());
        int code = articleService.createArticle(article, articleInfo.getTags());

        if (code == 1) {
            result.setCode(ResponseCode.SUCCEED);
            result.setInfo("创建成功");
            result.setPayload(article);
        } else {
            result.setCode(ResponseCode.DUPLICATE_NAME);
            result.setInfo("创建失败，已存在该文章");
        }
        return result;
    }

    /**
     * 修改文章
     * @param session
     * @param articleInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.PUT)
    public Result modifyArticle(HttpSession session, @PathVariable(value = "articleId") int articleId, @RequestBody ArticleInfo articleInfo) {
        LocalDateTime time = LocalDateTime.now();
        ResultWrapper result = new ResultWrapper();
        User user = (User) session.getAttribute("user");

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("不存在此文章");
        } else if (!article.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            boolean needCheckName;
            if (articleInfo.getName().equals(article.getName())) {
                needCheckName = false;
            }else {
                needCheckName = true;
                article.setName(articleInfo.getName());
            }

            article.setContent(articleInfo.getContent());
            article.setParseType(articleInfo.getParseType());
            article.setDescription(articleInfo.getDescription());
            article.setUpdatedAt(time);

            ArticleWrapper articleWrapper = articleService.modifyArticle(article, needCheckName, articleInfo.getTags(), user, articleService.getColumnById(article.getColumnId()));
            if (articleWrapper == null) {
                result.setCode(ResponseCode.DUPLICATE_NAME);
                result.setInfo("修改失败，文章名字重复");
            } else {
                result.setCode(ResponseCode.SUCCEED);
                result.setInfo("修改成功");
            }
        }
        return result;
    }

    /**
     * 查看文章
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET)
    public Result getArticle(@PathVariable(value = "articleId")int articleId) {
        Article article = articleService.getArticleWrapperById(articleId);
        ResultWrapper result = new ResultWrapper();
        if (article != null) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(article);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("不存在此文章");
        }
        return result;
    }

    /**
     * 搜索文章
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/article", method = RequestMethod.GET)
    public Result getArticleByInfo(@RequestParam(value = "info")String info, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize) {
        PageInfo<ArticleWrapper> articles = articleService.getArticleWrapperByInfo(info, pageNum, pageSize);
        ResultWrapper result = new ResultWrapper();
        if (articles.getSize() > 0) {
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(articles);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("搜索不到");
        }
        return result;
    }

    /** 删除文章
     * @param session
     * @param articleId
     * @return
     */
    @RequiresRoles(value = "user")
    @ResponseBody
    @RequestMapping(value = "/article/{articleId}",method = RequestMethod.DELETE)
    public Result deleteArticle(HttpSession session, @PathVariable(value = "articleId")int articleId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Article article = articleService.getArticleWrapperById(articleId);
        if (article == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("不存在文章");
        } else if (!article.getUserId().equals(user.getId())) {
            throw new UnauthorizedException();
        } else {
            articleService.deleteArticle(articleId);
            result.setCode(ResponseCode.SUCCEED);
        }

        return result;
    }

    /**
     * 对文章评论
     * @param session
     * @param articleId
     * @param commentInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "article/{articleId}/comment")
    public Result createComment(HttpSession session, @PathVariable(value = "articleId") int articleId, @RequestBody CommentInfo commentInfo) {
        LocalDateTime time = LocalDateTime.now();
        User user = (User) session.getAttribute("user");
        Result result = new Result();

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            result.setCode(ResponseCode.PREV_OBJECT_NOT_FOUND);
            result.setInfo("评论失败，该文章不存在");
            return result;
        }

        Comment comment = new Comment();
        comment.setCommentedId(articleId);
        comment.setCommentedType(Comment.COMMENTED_TYPE_ARTICLE);
        comment.setContent(commentInfo.getComment());
        comment.setCreatedAt(time);
        comment.setUid(user.getId());
        articleService.createComment(comment);

        return new Result(1);
    }

    /**
     * 查看文章评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/article/{articleId}/comments", method = RequestMethod.GET)
    public Result getArticleComments(@PathVariable(value = "articleId")int articleId, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ResultWrapper result = new ResultWrapper();

        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            result.setCode(4);
            result.setInfo("该文章不存在");
            return result;
        }

        PageInfo<CommentWrapper> commentWrappers = articleService.getCommentWrapperByArticleId(articleId, pageNum, pageSize);
        if (commentWrappers.getSize() > 0){
            result.setCode(1);
            result.setPayload(commentWrappers);
        } else {
            result.setCode(2);
            result.setInfo("该文章暂无评论");
        }

        return result;
    }


    /**
     * 对文章点赞
     * @param session
     * @param articleId
     * @param voteInfo
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/article/{articleId}/vote", method = RequestMethod.POST)
    public Result voteArticle(HttpSession session, @PathVariable(value = "articleId") int articleId, @RequestBody VoteInfo voteInfo) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        Article article = articleService.getArticleById(articleId);
        if (article == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该文章不存在");
            return result;
        }

        if (articleService.getVoteArticle(user.getId(), articleId) != null) {
            result.setCode(ResponseCode.DUPLICATE_ACTION);
            result.setInfo("已经vote");
            return result;
        }

        UserVoteArticle userVoteArticle = new UserVoteArticle();
        userVoteArticle.setCreatedAt(time);
        userVoteArticle.setArticleId(articleId);
        userVoteArticle.setVoteType(voteInfo.getType());
        userVoteArticle.setUserId(user.getId());
        articleService.voteArticle(article, userVoteArticle);

        result.setCode(ResponseCode.SUCCEED);
        return result;

    }

    /**
     * 查看对文章的点赞状态
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/article/{articleId}/vote", method = RequestMethod.GET)
    public ResultWrapper getVoteStatusByArticleId(HttpSession session, @PathVariable(value = "articleId") int articleId) {
        ResultWrapper resultWrapper = new ResultWrapper();
        User user = (User) session.getAttribute("user");

        UserVoteArticle userVoteArticles = articleService.getVoteArticle(user.getId(), articleId);
        resultWrapper.setCode(ResponseCode.SUCCEED);
        resultWrapper.setPayload(userVoteArticles);
        return resultWrapper;
    }

    /**
     * 撤销对文章点赞
     * @param session
     * @param articleId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/article/{articleId}/vote", method = RequestMethod.DELETE)
    public Result undoVoteArticle(HttpSession session, @PathVariable(value = "articleId") int articleId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();
        Article article = articleService.getArticleById(articleId);
        if (article == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该文章不存在");
            return result;
        }

        if (articleService.getCollectArticle(user.getId(), articleId) == null) {
            result.setCode(ResponseCode.DUPLICATE_ACTION);
            result.setInfo("已经撤销");
            return result;
        }

        articleService.undoVoteArticle(user.getId(), article);

        result.setCode(ResponseCode.SUCCEED);
        return result;
    }


    /**
     * 获取收藏文章
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/collectedArticles", method = RequestMethod.GET)
    public Result getCollectArticle(HttpSession session,@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute("user");
        ResultWrapper resultWrapper = new ResultWrapper();

        PageInfo<ArticleWrapper> articleWrappers = articleService.getCollectedArticles(user.getId(), pageNum, pageSize);
        if (articleWrappers.getSize() > 0) {
            resultWrapper.setCode(ResponseCode.SUCCEED);
            resultWrapper.setPayload(articleWrappers);
        } else {
            resultWrapper.setCode(ResponseCode.NOT_FOUND);
        }
        return  resultWrapper;


    }


    /**
     * 收藏文章
     * @param session
     * @param articleId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/collectedArticle/{articleId}", method = RequestMethod.POST)
    public Result collectArticle(HttpSession session, @PathVariable(value = "articleId") int articleId) {
        User user = (User) session.getAttribute("user");
        LocalDateTime time = LocalDateTime.now();
        Result result = new Result();
        Article article = articleService.getArticleById(articleId);
        if (article == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该文章不存在");
            return result;
        }

        if (articleService.getCollectArticle(user.getId(), articleId) != null) {
            result.setCode(ResponseCode.DUPLICATE_ACTION);
            result.setInfo("已经收藏");
            return result;
        }

        UserCollectArticle userCollectArticle = new UserCollectArticle();
        userCollectArticle.setCreatedAt(time);
        userCollectArticle.setArticleId(articleId);
        userCollectArticle.setUserId(user.getId());
        articleService.collectArticle(article, userCollectArticle);
        result.setCode(ResponseCode.SUCCEED);
        return result;
    }

    /**
     * 查看对文章 收藏状态
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/collectArticle/{articleId}", method = RequestMethod.GET)
    public ResultWrapper getCollectedStatusByArticleId(HttpSession session, @PathVariable(value = "articleId") int articleId) {
        ResultWrapper resultWrapper = new ResultWrapper();
        User user = (User) session.getAttribute("user");

        UserCollectArticle userCollectArticle = articleService.getCollectArticle(user.getId(), articleId);
        resultWrapper.setPayload(userCollectArticle);
        resultWrapper.setCode(ResponseCode.SUCCEED);
        return resultWrapper;
    }


    /**
     * 撤销收藏文章
     * @param session
     * @param articleId
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/collectArticle/{articleId}", method = RequestMethod.DELETE)
    public Result undoCollectArticle(HttpSession session, @PathVariable(value = "articleId") int articleId) {
        User user = (User) session.getAttribute("user");
        Result result = new Result();
        Article article = articleService.getArticleById(articleId);
        if (article == null){
            result.setCode(ResponseCode.NOT_FOUND);
            result.setInfo("该文章不存在");
            return result;
        }

        if (articleService.getCollectArticle(user.getId(), articleId) == null) {
            result.setCode(ResponseCode.DUPLICATE_ACTION);
            result.setInfo("已经取消收藏");
            return result;
        }

        articleService.undoCollectArticle(user.getId(), article);

        result.setCode(ResponseCode.SUCCEED);
        return result;
    }

}

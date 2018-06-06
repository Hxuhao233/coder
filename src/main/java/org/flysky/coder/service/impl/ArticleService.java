package org.flysky.coder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.*;
import org.flysky.coder.entity.wrapper.ArticleWrapper;
import org.flysky.coder.entity.wrapper.ColumnWrapper;
import org.flysky.coder.entity.wrapper.CommentWrapper;
import org.flysky.coder.mapper.*;
import org.flysky.coder.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxuhao233 on 2018/4/17.
 */
@Service
public class ArticleService implements IArticleService {

    @Autowired
    private ColumnMapper columnMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserVoteArticleMapper userVoteArticleMapper;

    @Autowired
    private UserCollectArticleMapper userCollectArticleMapper;

    @Autowired
    private RedisCacheManager cacheManager;

    @Override
    public boolean hasColumnName(String name) {
        return columnMapper.hasColumnName(name);
    }

    @Override
    public int createColumn(Column column) {
        if (columnMapper.hasColumnName(column.getName())) {
            return 0;
        }

        return columnMapper.insertSelective(column);
    }

    @Override
    public int modifyColumn(Column column, boolean needCheckName) {
        if (needCheckName && columnMapper.hasColumnName(column.getName())) {
            return 0;
        }
        return columnMapper.updateByPrimaryKeySelective(column);
    }

    @Override
    public int deleteColumn(int columnId) {
        Column column = new Column();
        column.setId(columnId);
        column.setIsDeleted(true);
        return columnMapper.updateByPrimaryKeySelective(column);
    }

    @Override
    public Column getColumnById(int columnId) {
        return columnMapper.selectByPrimaryKey(columnId);
    }

    @Override
    public ColumnWrapper getColumnWrapperById(int columnId) {
        return columnMapper.getColumnWrapperById(columnId);
    }

    @Override
    public PageInfo<Column> getColumnByUserId(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(columnMapper.getColumnByUserId(userId));
    }

    @Override
    public boolean hasArticleName(String name) {
        return hasArticleName(name);
    }

    @Override
    public int createArticle(Article article, List<String> tags) {
        if (articleMapper.hasArticleName(article.getName())) {
            return 0;
        }

        int result = articleMapper.insertSelective(article);

        for (String tagName : tags) {
            Tag tag = tagMapper.getTagByTagNameAndType(tagName, Tag.TYPE_ARTICLE);
            if (tag == null) {
                tag = new Tag();
                tag.setName(tagName);
                tag.setType(Tag.TYPE_ARTICLE);
                tagMapper.insertSelective(tag);
            }

            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagid(tag.getId());
            articleTagMapper.insertSelective(articleTag);
        }

        return result;
    }

    @Override
    @CachePut(value = "article", key = "#article.id")
    public ArticleWrapper modifyArticle(Article article, boolean needCheckName, List<String> tags, User user, Column column) {

        if (needCheckName && articleMapper.hasArticleName(article.getName())) {
            return null;
        }
        int result = articleMapper.updateByPrimaryKeySelective(article);

        // 查找修改之前的文章标签
        List<ArticleTag> articleTags = articleTagMapper.getTagsByArticleId(article.getId());
        //List<Integer> deleteTagIdList = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            Tag tag = tagMapper.selectByPrimaryKey(articleTag.getTagid());
            int index = tags.indexOf(tag.getName());
            if (index == -1) {
                // 修改后没有的标签则删除关联
                articleTagMapper.deleteByPrimaryKey(articleTag.getId());
            } else {
                // 修改后还有的标签无需创建
                tags.remove(index);
            }
        }

        // 创建新增标签并建立关联
        for (String tagName : tags) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setType(Tag.TYPE_ARTICLE);
            tagMapper.insertSelective(tag);

            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagid(tag.getId());
            articleTagMapper.insertSelective(articleTag);
        }

        return ArticleWrapper.build(article, tags, user, null);
    }

    @Override
    @CacheEvict(value = "article" ,key = "#articleId")
    public int deleteArticle(int articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsDeleted(true);
        return articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    //@Cacheable(value = "article", key = "#article.id")
    public PageInfo<ArticleWrapper> getArticleWrapperByInfo(String info, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<ArticleWrapper> articleWrappers = articleMapper.getArticleWrapperByInfo(info);
        for (ArticleWrapper articleWrapper : articleWrappers) {
            List<ArticleTag> articleTags = articleTagMapper.getTagsByArticleId(articleWrapper.getId());
            List<String> tagNames = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                tagNames.add(tagMapper.selectByPrimaryKey(articleTag.getTagid()).getName());
            }
            articleWrapper.setTags(tagNames);
        }

        return new PageInfo<>(articleWrappers);
    }

    @Override
    //@Cacheable("article")
    public PageInfo<ArticleWrapper> getArticleByColumnId(int columnId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<ArticleWrapper> articleWrappers = articleMapper.getArticleWrapperByColumnId(columnId);
        for (ArticleWrapper articleWrapper : articleWrappers) {
            List<ArticleTag> articleTags = articleTagMapper.getTagsByArticleId(articleWrapper.getId());
            List<String> tagNames = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                tagNames.add(tagMapper.selectByPrimaryKey(articleTag.getTagid()).getName());
            }
            articleWrapper.setTags(tagNames);
        }

        return new PageInfo<>(articleWrappers);
    }

    @Override
    public Article getArticleById(int articleId) {
        return articleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    @Cacheable("article")
    public ArticleWrapper getArticleWrapperById(int articleId) {
        ArticleWrapper articleWrapper = articleMapper.getArticleWrapperById(articleId);
        if (articleWrapper == null) {
            return null;
        }
        List<ArticleTag> articleTags = articleTagMapper.getTagsByArticleId(articleId);
        List<String> tagNames = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tagNames.add(tagMapper.selectByPrimaryKey(articleTag.getTagid()).getName());
        }
        articleWrapper.setTags(tagNames);

        return articleWrapper;
    }

    @Override
    public int createComment(Comment comment) {
        return commentMapper.insertSelective(comment);
    }

    @Override
    public PageInfo<CommentWrapper> getCommentWrapperByArticleId(int articleId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(commentMapper.getCommentWrapperByArticleId(articleId, Comment.COMMENTED_TYPE_ARTICLE));
    }

    @Override
    public PageInfo<ArticleWrapper> getArticleWrappersByUserId(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<ArticleWrapper> articleWrappers = articleMapper.getArticleWrapperByUserId(userId);
        for (ArticleWrapper articleWrapper : articleWrappers) {
            List<ArticleTag> articleTags = articleTagMapper.getTagsByArticleId(articleWrapper.getId());
            List<String> tagNames = new ArrayList<>();
            for (ArticleTag articleTag : articleTags) {
                tagNames.add(tagMapper.selectByPrimaryKey(articleTag.getTagid()).getName());
            }

            articleWrapper.setTags(tagNames);
        }

        return new PageInfo<>(articleWrappers);
    }

    @Override
    public PageInfo<ArticleWrapper> getCollectedArticles(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(articleMapper.getCollectedArticleWrapperByUserId(userId));
    }

    @Override
    public int voteArticle(Article article, UserVoteArticle userVoteArticle) {

        if (userVoteArticle.getVoteType().equals(UserVoteArticle.TYPE_UP_VOTE)) {
            article.setVoteCount(article.getVoteCount() + 1);
        }
        articleMapper.updateByPrimaryKeySelective(article);

        // 更新缓存
        ArticleWrapper articleWrapper = cacheManager.getCache("article").get(article.getId(), ArticleWrapper.class);
        if (articleWrapper != null) {
            articleWrapper.setVoteCount(article.getVoteCount());
            cacheManager.getCache("article").put(articleWrapper.getId(), articleWrapper);
        }
        return userVoteArticleMapper.insertSelective(userVoteArticle);
    }

    @Override
    public int undoVoteArticle(int userId, Article article) {
        UserVoteArticle userVoteArticle = userVoteArticleMapper.getUserVoteArticleByUserIdAndArticleId(userId, article.getId());
        if (userVoteArticle == null) {
            return 0;
        }

        if (userVoteArticle.getVoteType().equals(UserVoteArticle.TYPE_UP_VOTE)) {
            if (article.getVoteCount() > 0) {
                article.setVoteCount(article.getVoteCount() - 1);
            }
        }
        articleMapper.updateByPrimaryKeySelective(article);

        ArticleWrapper articleWrapper = cacheManager.getCache("article").get(article.getId(), ArticleWrapper.class);
        if (articleWrapper != null) {
            articleWrapper.setVoteCount(article.getVoteCount());
            cacheManager.getCache("article").put(articleWrapper.getId(), articleWrapper);
        }

        return userVoteArticleMapper.deleteByPrimaryKey(userVoteArticle.getId());
    }

    @Override
    public UserVoteArticle getVoteArticle(int userId, int articleId){
        return userVoteArticleMapper.getUserVoteArticleByUserIdAndArticleId(userId, articleId);
    }

    @Override
    public int collectArticle(Article article, UserCollectArticle userCollectArticle) {
        article.setCollectCount(article.getCollectCount() + 1);
        articleMapper.updateByPrimaryKeySelective(article);

        ArticleWrapper articleWrapper = cacheManager.getCache("article").get(article.getId(), ArticleWrapper.class);
        if (articleWrapper != null) {
            articleWrapper.setCollectCount(article.getCollectCount());
            cacheManager.getCache("article").put(articleWrapper.getId(), articleWrapper);
        }

        return userCollectArticleMapper.insertSelective(userCollectArticle);
    }

    @Override
    public int undoCollectArticle(int userId, Article article) {
        UserCollectArticle userCollectArticle = userCollectArticleMapper.getUserCollectArticleByUserIdAndArticleId(userId, article.getId());
        if (userCollectArticle == null){
            return 0;
        }
        article.setCollectCount(article.getCollectCount() - 1);
        articleMapper.updateByPrimaryKeySelective(article);

        ArticleWrapper articleWrapper = cacheManager.getCache("article").get(article.getId(), ArticleWrapper.class);
        if (articleWrapper != null) {
            articleWrapper.setCollectCount(article.getCollectCount());
            cacheManager.getCache("article").put(articleWrapper.getId(), articleWrapper);
        }

        return userCollectArticleMapper.deleteByPrimaryKey(userCollectArticle.getId());
    }

    @Override
    public UserCollectArticle getCollectArticle(int userId, int articleId){
        return userCollectArticleMapper.getUserCollectArticleByUserIdAndArticleId(userId, articleId);
    }

}
